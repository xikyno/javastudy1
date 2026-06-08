package org.example.injection_demo;

import org.example.injection_demo.service.OrderServiceConstructor;
import org.example.injection_demo.service.OrderServiceField;
import org.example.injection_demo.service.OrderServiceSetter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class InjectionDemoApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(InjectionDemoApplication.class, args);

        System.out.println("\n========== 三种注入方式对比测试 ==========\n");

        // 构造器注入
        OrderServiceConstructor constructorService = context.getBean(OrderServiceConstructor.class);
        constructorService.placeOrder("user1@test.com", "MacBook Pro");

        System.out.println("\n----------------------------------------\n");

        // Setter注入
        OrderServiceSetter setterService = context.getBean(OrderServiceSetter.class);
        setterService.placeOrder("user2@test.com", "iPhone 15");

        System.out.println("\n----------------------------------------\n");

        // 字段注入
        OrderServiceField fieldService = context.getBean(OrderServiceField.class);
        fieldService.placeOrder("user3@test.com", "AirPods");

        System.out.println("\n========================================\n");

        // 验证单例
        OrderServiceConstructor service1 = context.getBean(OrderServiceConstructor.class);
        OrderServiceConstructor service2 = context.getBean(OrderServiceConstructor.class);
        System.out.println("【验证单例】两次获取是同一个对象吗？" + (service1 == service2));
    }
}