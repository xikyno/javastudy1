package org.example.injection_demo;

import org.example.injection_demo.container.MyIocContainer;
import org.example.injection_demo.service.AService;

public class MainTest {

    public static void main(String[] args) {
        System.out.println("=== 直接测试 MyIocContainer ===");

        // 创建容器
        MyIocContainer container = new MyIocContainer("org.example.injection_demo");

        // 打印容器里的 Bean 数量
        System.out.println("Bean 数量: " + container.getBeanCount());

        // 尝试获取 AService
        try {
            AService a = container.getBean(AService.class);
            System.out.println("获取 AService 成功: " + a.getInfo());
            a.doSomething();
        } catch (Exception e) {
            System.out.println("获取 AService 失败: " + e.getMessage());
        }

        // 打印所有 Bean
        container.printAllBeans();
    }
}