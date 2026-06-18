package org.example.injection_demo.container;

import org.example.injection_demo.annotation.MyAutowired;
import org.example.injection_demo.annotation.MyComponent;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 手写 IoC 容器 —— 支持三级缓存解决循环依赖
 *
 * 三级缓存：
 * - 一级缓存（singletonObjects）：成品 Bean
 * - 二级缓存（earlySingletonObjects）：半成品 Bean（已实例化，未填充属性）
 * - 三级缓存（singletonFactories）：ObjectFactory，用于生成 Bean 的早期引用
 */
public class MyIocContainerWithCache {

    // ==================== 三级缓存 ====================

    // 一级缓存：成品 Bean（已经完整初始化）
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    // 二级缓存：半成品 Bean（已实例化，但属性还没填充）
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

    // 三级缓存：ObjectFactory（用于生成 Bean 的早期引用）
    private final Map<String, ObjectFactory<?>> singletonFactories = new ConcurrentHashMap<>();

    // 记录正在创建中的 Bean，用于检测循环依赖
    private final Set<String> singletonsCurrentlyInCreation = new HashSet<>();

    // Bean 定义缓存：类名 -> Class
    private final Map<String, Class<?>> beanClassMap = new HashMap<>();

    // ==================== 构造方法 ====================

    public MyIocContainerWithCache(String basePackage) {
        System.out.println("========== 开始初始化容器（支持循环依赖）==========");

        // 1. 扫描包，找到所有带 @MyComponent 的类
        scanPackage(basePackage);

        // 2. 遍历所有 Bean 定义，创建 Bean
        for (String beanName : beanClassMap.keySet()) {
            getBean(beanName);
        }

        System.out.println("容器初始化完成，共管理 " + singletonObjects.size() + " 个 Bean");
        System.out.println("==================================================\n");
    }

    // ==================== 核心方法：getBean ====================

    public Object getBean(String beanName) {
        // 1. 一级缓存有：直接返回
        Object singleton = singletonObjects.get(beanName);
        if (singleton != null) {
            return singleton;
        }

        // 2. 二级缓存有：说明是半成品，直接返回
        Object earlySingleton = earlySingletonObjects.get(beanName);
        if (earlySingleton != null) {
            return earlySingleton;
        }

        // 3. 检测循环依赖
        if (singletonsCurrentlyInCreation.contains(beanName)) {
            // 发生了循环依赖！从三级缓存获取工厂，生成早期引用
            ObjectFactory<?> factory = singletonFactories.get(beanName);
            if (factory != null) {
                Object object = factory.getObject();
                // 放入二级缓存（半成品）
                earlySingletonObjects.put(beanName, object);
                // 从三级缓存移除（工厂只用一次）
                singletonFactories.remove(beanName);
                return object;
            }
        }

        // 4. 正常创建流程
        return doCreateBean(beanName);
    }

    // ==================== 创建 Bean ====================

    private Object doCreateBean(String beanName) {
        System.out.println("开始创建 Bean：" + beanName);

        // 标记正在创建中（用于循环依赖检测）
        singletonsCurrentlyInCreation.add(beanName);

        // 获取 Bean 的 Class
        Class<?> clazz = beanClassMap.get(beanName);
        if (clazz == null) {
            throw new RuntimeException("找不到 Bean 定义：" + beanName);
        }

        try {
            // 1. 实例化（调用无参构造器）
            Object instance = clazz.getDeclaredConstructor().newInstance();
            System.out.println("  实例化完成：" + beanName);

            // 2. 提前暴露工厂（放入三级缓存）—— 这是解决循环依赖的关键！
            singletonFactories.put(beanName, new ObjectFactory<Object>() {
                @Override
                public Object getObject() {
                    // 返回实例本身（如果有 AOP 代理，可以在这里返回代理对象）
                    return instance;
                }
            });
            System.out.println("  三级缓存已存放：" + beanName + " 的工厂");

            // 3. 填充属性（依赖注入）
            populateBean(beanName, instance);

            // 4. 初始化完成：从三级缓存移除，放入一级缓存
            singletonObjects.put(beanName, instance);
            singletonFactories.remove(beanName);
            earlySingletonObjects.remove(beanName);
            singletonsCurrentlyInCreation.remove(beanName);

            System.out.println("  ✅ Bean 创建完成：" + beanName);
            return instance;

        } catch (Exception e) {
            // 创建失败，清理缓存
            singletonsCurrentlyInCreation.remove(beanName);
            singletonFactories.remove(beanName);
            throw new RuntimeException("创建 Bean 失败：" + beanName, e);
        }
    }

    // ==================== 依赖注入 ====================

    private void populateBean(String beanName, Object instance) {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(MyAutowired.class)) {
                String fieldName = field.getType().getSimpleName();
                String fieldBeanName = toLowerFirst(field.getType().getSimpleName());

                System.out.println("  注入依赖：" + beanName + " -> " + fieldName);

                // 递归获取依赖的 Bean（这里会触发循环依赖检测）
                Object dependency = getBean(fieldBeanName);

                // 暴力注入
                field.setAccessible(true);
                try {
                    field.set(instance, dependency);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("注入失败：" + beanName + "." + field.getName(), e);
                }
            }
        }
    }

    // ==================== 包扫描 ====================

    private void scanPackage(String basePackage) {
        String packagePath = basePackage.replace('.', '/');
        String projectPath = System.getProperty("user.dir");
        File classDir = new File(projectPath, "target/classes/" + packagePath);

        if (!classDir.exists()) {
            System.out.println("目录不存在：" + classDir.getAbsolutePath());
            return;
        }

        scanDirectory(classDir, basePackage);
    }

    private void scanDirectory(File dir, String packageName) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class") && !file.getName().contains("$")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(MyComponent.class)) {
                        String beanName = toLowerFirst(clazz.getSimpleName());
                        beanClassMap.put(beanName, clazz);
                        System.out.println("发现 Bean 定义：" + beanName + " -> " + clazz.getName());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ==================== 工具方法 ====================

    private String toLowerFirst(String name) {
        if (name == null || name.isEmpty()) return name;
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    public <T> T getBean(Class<T> clazz) {
        String beanName = toLowerFirst(clazz.getSimpleName());
        return (T) getBean(beanName);
    }

    public void printAllBeans() {
        System.out.println("\n========== 容器中的 Bean ==========");
        for (String name : singletonObjects.keySet()) {
            System.out.println("  " + name + " -> " + singletonObjects.get(name).getClass().getSimpleName());
        }
        System.out.println("====================================\n");
    }

    // ==================== ObjectFactory 接口 ====================

    @FunctionalInterface
    public interface ObjectFactory<T> {
        T getObject();
    }
}