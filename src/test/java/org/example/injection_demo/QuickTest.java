package org.example.injection_demo;

import org.example.injection_demo.annotation.MyComponent;
import org.example.injection_demo.service.CService;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuickTest {

    // 自定义容器
    static class SimpleContainer {
        private Map<Class<?>, Object> beans = new HashMap<>();

        public SimpleContainer(String basePackage) {
            try {
                // 扫描 target/classes 目录
                String packagePath = basePackage.replace('.', '/');
                String projectPath = System.getProperty("user.dir");
                File classDir = new File(projectPath, "target/classes/" + packagePath);

                if (classDir.exists()) {
                    scanAndInstantiate(classDir, basePackage);
                } else {
                    System.out.println("目录不存在：" + classDir.getAbsolutePath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void scanAndInstantiate(File dir, String packageName) throws Exception {
            File[] files = dir.listFiles();
            if (files == null) return;

            for (File file : files) {
                if (file.isDirectory()) {
                    scanAndInstantiate(file, packageName + "." + file.getName());
                } else if (file.getName().endsWith(".class") && !file.getName().contains("$")) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(MyComponent.class)) {
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        beans.put(clazz, instance);
                        System.out.println("发现并实例化：" + clazz.getSimpleName());
                    }
                }
            }
        }

        @SuppressWarnings("unchecked")
        public <T> T getBean(Class<T> clazz) {
            return (T) beans.get(clazz);
        }

        public int size() {
            return beans.size();
        }
    }

    // 测试用的 Bean
    @MyComponent
    static class TestService {
        public void sayHello() {
            System.out.println("TestService 说：你好！");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 快速测试 ===\n");

        // 创建容器，扫描当前包
        SimpleContainer container = new SimpleContainer("org.example.injection_demo");

        System.out.println("\n容器中的 Bean 数量：" + container.size());

        // 获取 TestService（本类内部定义的）
        TestService testService = container.getBean(TestService.class);
        if (testService != null) {
            testService.sayHello();
        } else {
            System.out.println("TestService 获取失败");
        }

        // 获取 CService（新建的）
        CService cService = container.getBean(CService.class);
        if (cService != null) {
            System.out.println("CService 获取成功：" + cService.getInfo());
            cService.doSomething();
        } else {
            System.out.println("CService 不在容器中");
        }

        System.out.println("\n=== 测试完成 ===");
    }
}