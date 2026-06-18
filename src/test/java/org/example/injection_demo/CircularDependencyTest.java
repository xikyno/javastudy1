package org.example.injection_demo;

import org.example.injection_demo.container.MyIocContainerWithCache;
import org.example.injection_demo.service.A;
import org.example.injection_demo.service.B;

public class CircularDependencyTest {

    public static void main(String[] args) {
        System.out.println("=== 循环依赖测试 ===\n");

        // 创建容器
        MyIocContainerWithCache container = new MyIocContainerWithCache("org.example.injection_demo");

        // 获取 A
        A a = container.getBean(A.class);
        System.out.println("\n获取 A 成功：" + a.getInfo());

        // 获取 B
        B b = container.getBean(B.class);
        System.out.println("获取 B 成功：" + b.getInfo());

        System.out.println("\n=== 调用 A.doSomething() ===");
        a.doSomething();

        System.out.println("\n=== 调用 B.doSomething() ===");
        b.doSomething();

        // 验证单例
        A a2 = container.getBean(A.class);
        System.out.println("\n两次获取 A 是同一个对象吗？" + (a == a2));
        B b2 = container.getBean(B.class);
        System.out.println("两次获取 B 是同一个对象吗？" + (b == b2));

        container.printAllBeans();

        System.out.println("=== 测试完成 ===");
    }
}