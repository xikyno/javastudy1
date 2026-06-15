package org.example.injection_demo.container;

import org.example.injection_demo.annotation.MyComponent;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyIocContainer {

    private final Map<Class<?>, Object> beanMap = new HashMap<>();

    public MyIocContainer(String basePackage) {
        System.out.println("========== 开始初始化 MyIocContainer ==========");

        List<Class<?>> componentClasses = scanPackage(basePackage);
        System.out.println("扫描到 " + componentClasses.size() + " 个带 @MyComponent 的类");

        instantiateBeans(componentClasses);

        System.out.println("容器初始化完成，共管理 " + beanMap.size() + " 个 Bean");
        System.out.println("==============================================\n");
    }

    private void scanSourceDirectory(File directory, String packageName, List<Class<?>> componentClasses) {
        if (!directory.exists() || !directory.isDirectory()) return;

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanSourceDirectory(file, packageName + "." + file.getName(), componentClasses);
            } else if (file.getName().endsWith(".java")) {
                String className = packageName + "." + file.getName().replace(".java", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(MyComponent.class)) {
                        componentClasses.add(clazz);
                        System.out.println("发现 @MyComponent 类：" + clazz.getName());
                    }
                } catch (ClassNotFoundException e) {
                    // 如果类还没编译，跳过
                    System.out.println("类未编译：" + className);
                }
            }
        }
    }

    private List<Class<?>> scanPackage(String basePackage) {
        List<Class<?>> componentClasses = new ArrayList<>();
        String packagePath = basePackage.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // 获取 classes 目录的路径
        URL classesUrl = classLoader.getResource(packagePath);
        if (classesUrl != null) {
            File directory = new File(classesUrl.getFile());
            if (directory.exists()) {
                scanDirectory(directory, basePackage, componentClasses);
            }
        }

        // 如果 classes 目录没找到，尝试从文件系统直接扫描
        if (componentClasses.isEmpty()) {
            String projectPath = System.getProperty("user.dir");
            File sourceDir = new File(projectPath, "src/main/java/" + packagePath);
            if (sourceDir.exists()) {
                scanSourceDirectory(sourceDir, basePackage, componentClasses);
            }
        }

        return componentClasses;
    }

    private void scanDirectory(File directory, String packageName, List<Class<?>> componentClasses) {
        //System.out.println("进入 scanDirectory，目录：" + directory.getAbsolutePath());
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("目录不存在或不是目录");
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            System.out.println("files 为 null");
            return;
        }

        //System.out.println("找到 " + files.length + " 个文件/子目录");

        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("进入子目录：" + file.getName());
                scanDirectory(file, packageName + "." + file.getName(), componentClasses);
            } else if (file.getName().endsWith(".class")) {
                //System.out.println("找到 .class 文件：" + file.getName());
                String className = packageName + "." + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    //System.out.println("类加载成功：" + className);
                    if (clazz.isAnnotationPresent(MyComponent.class)) {
                        componentClasses.add(clazz);
                        //System.out.println("发现 @MyComponent 类：" + clazz.getName());
                    } else {
                        //System.out.println("类 " + className + " 没有 @MyComponent 注解");
                    }
                } catch (ClassNotFoundException e) {
                    //System.out.println("加载类失败：" + className);
                    e.printStackTrace();
                }
            }
        }
    }

    private void instantiateBeans(List<Class<?>> componentClasses) {
        for (Class<?> clazz : componentClasses) {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                beanMap.put(clazz, instance);
                System.out.println("实例化 Bean：" + clazz.getName() + "，实例 hashCode：" + instance.hashCode());
            } catch (Exception e) {
                System.out.println("实例化失败：" + clazz.getName());
                e.printStackTrace();
            }
        }
    }

    public <T> T getBean(Class<T> clazz) {
        Object bean = beanMap.get(clazz);
        if (bean == null) {
            throw new RuntimeException("找不到 Bean：" + clazz.getName());
        }
        return (T) bean;
    }

    public int getBeanCount() {
        return beanMap.size();
    }

    public void printAllBeans() {
        System.out.println("\n========== 当前容器中的 Bean ==========");
        for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()) {
            System.out.println("Key: " + entry.getKey().getSimpleName());
            System.out.println("Value: " + entry.getValue().getClass().getSimpleName() + "@" + entry.getValue().hashCode());
            System.out.println("---");
        }
        System.out.println("======================================\n");
    }
}