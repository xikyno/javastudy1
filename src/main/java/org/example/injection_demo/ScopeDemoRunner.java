package org.example.injection_demo;

import org.example.injection_demo.service.PrototypeBean;
import org.example.injection_demo.service.SingletonBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ScopeDemoRunner {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ScopeDemoRunner.class, args);

        System.out.println("\n========== 作用域对比实验 ==========\n");

        // ========== Singleton 测试 ==========
        System.out.println("--- Singleton 测试 ---");

        SingletonBean s1 = context.getBean(SingletonBean.class);
        SingletonBean s2 = context.getBean(SingletonBean.class);

        System.out.println("s1 和 s2 是同一个对象吗？" + (s1 == s2));

        s1.addCount();  // count = 1
        s2.addCount();  // count = 2（同一个对象，count 累加）

        System.out.println();

        // ========== Prototype 测试 ==========
        System.out.println("--- Prototype 测试 ---");

        PrototypeBean p1 = context.getBean(PrototypeBean.class);
        PrototypeBean p2 = context.getBean(PrototypeBean.class);

        System.out.println("p1 和 p2 是同一个对象吗？" + (p1 == p2));

        p1.addCount();  // count = 1
        p2.addCount();  // count = 1（不同对象，各自的 count）

        System.out.println("\n=====================================");
    }
}