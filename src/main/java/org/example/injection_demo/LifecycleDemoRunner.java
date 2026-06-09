package org.example.injection_demo;

import org.example.injection_demo.service.LifecycleDemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 生命周期演示专用启动类
 *
 * 运行这个 main 方法，观察 Bean 的生命周期
 * 不影响原来的 InjectionDemoApplication
 */
@SpringBootApplication
public class LifecycleDemoRunner {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LifecycleDemoRunner.class, args);

        System.out.println("\n========== Spring 容器启动完成 ==========\n");

        LifecycleDemo demo = context.getBean(LifecycleDemo.class);
        demo.doSomething();

        System.out.println("\n========== 准备关闭 Spring 容器 ==========\n");

        context.close();

        System.out.println("\n========== Spring 容器已关闭 ==========");
    }
}