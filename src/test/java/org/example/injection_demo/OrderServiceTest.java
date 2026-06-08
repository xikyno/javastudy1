package org.example.injection_demo;

import org.example.injection_demo.service.OrderServiceConstructor;
import org.example.injection_demo.service.OrderServiceField;
import org.example.injection_demo.service.OrderServiceSetter;
import org.junit.jupiter.api.Test;

/**
 * 单元测试类
 *
 * 作用：测试三种注入方式的类是否正常工作
 *
 * 注意：这个测试类在 src/test/java 目录下，不是 src/main/java
 * 运行方式：点击类名或方法名左边的绿色三角
 */
public class OrderServiceTest {

    /**
     * 测试构造器注入
     *
     * 关键点：测试构造器注入的类时，直接 new 就行了！
     * 不需要启动 Spring 容器，不需要任何框架，就是纯 Java
     *
     * 这就是为什么推荐构造器注入：
     *   测试简单、快速、不依赖 Spring
     */
    @Test
    public void testConstructorInjection() {
        // 直接创建依赖对象
        MockEmailService mockEmail = new MockEmailService();

        // 直接 new，把 mock 传进去
        OrderServiceConstructor service = new OrderServiceConstructor(mockEmail);

        // 调用业务方法
        service.placeOrder("test@test.com", "测试商品");

        // 验证：邮件服务是否被调用了
        if (mockEmail.wasCalled) {
            System.out.println("✅ 构造器注入测试通过：邮件服务被正确调用");
        } else {
            System.out.println("❌ 构造器注入测试失败：邮件服务未被调用");
        }
    }

    /**
     * 测试 Setter 注入
     *
     * 测试方式：也是直接 new，然后手动调用 setter
     * 不需要启动 Spring，但也需要额外写 setter 调用
     */
    @Test
    public void testSetterInjection() {
        MockEmailService mockEmail = new MockEmailService();

        // 先 new 对象
        OrderServiceSetter service = new OrderServiceSetter();

        // 然后手动调用 setter 把依赖塞进去
        service.setEmailService(mockEmail);

        service.placeOrder("test@test.com", "测试商品");

        if (mockEmail.wasCalled) {
            System.out.println("✅ Setter注入测试通过：邮件服务被正确调用");
        } else {
            System.out.println("❌ Setter注入测试失败：邮件服务未被调用");
        }
    }

    /**
     * 测试字段注入
     *
     * 问题：字段注入的类，根本没法直接测试！
     * 因为 emailService 是 private 字段，没有构造器，没有 setter
     * 你没法把 mock 对象塞进去
     *
     * 这就是为什么字段注入不推荐：
     *   单元测试极其困难，必须启动 Spring 容器
     */
    @Test
    public void testFieldInjection() {
        // ❌ 这行代码会报错！因为 OrderServiceField 没有构造器
        // OrderServiceField service = new OrderServiceField();
        // service.placeOrder(...);  // 这里 emailService 是 null，会空指针异常

        System.out.println("⚠️ 字段注入无法直接测试！");
        System.out.println("   因为 emailService 字段是 private，没有构造器，没有 setter");
        System.out.println("   必须启动 Spring 容器才能注入，单元测试会变得很复杂");
        System.out.println("   这就是为什么 Spring 官方不推荐字段注入");
    }

    // ============================================================
    // Mock 类：假的邮件服务
    //
    // 作用：不真的发邮件，只记录"有没有被调用"
    // 这样测试时不会真的发邮件骚扰别人，又能验证逻辑是否正确
    // ============================================================
    private static class MockEmailService extends org.example.injection_demo.service.EmailService {
        boolean wasCalled = false;

        @Override
        public void sendEmail(String to, String content) {
            wasCalled = true;
            System.out.println("【Mock】假的发送邮件，没有真的发出去");
            System.out.println("       收件人：" + to);
            System.out.println("       内容：" + content);
        }

        @Override
        public int getInstanceHashCode() {
            return 999999;  // mock 对象返回一个固定值
        }
    }
}