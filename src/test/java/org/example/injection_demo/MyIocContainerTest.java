package org.example.injection_demo;

import org.example.injection_demo.container.MyIocContainer;
import org.example.injection_demo.service.AService;
import org.example.injection_demo.service.BService;
import org.junit.jupiter.api.Test;

public class MyIocContainerTest {

    @Test
    public void testContainerCanScanAndInstantiate() {
        System.out.println("\n========== 测试1：容器扫描和实例化 ==========");
        MyIocContainer container = new MyIocContainer("org.example.injection_demo");
        System.out.println("容器中的 Bean 数量：" + container.getBeanCount());
        container.printAllBeans();
        /*System.out.println("当前工作目录：" + System.getProperty("user.dir"));
        java.net.URL url = Thread.currentThread().getContextClassLoader().getResource("org/example/injection_demo/service");
        System.out.println("URL: " + url);
        if (url != null) {
            System.out.println("路径: " + new java.io.File(url.getFile()).getAbsolutePath());
        }*/
    }

    @Test
    public void testGetBean() {
        System.out.println("\n========== 测试2：getBean 获取实例 ==========");
        MyIocContainer container = new MyIocContainer("org.example.injection_demo");

        AService aService = container.getBean(AService.class);
        System.out.println("获取到的 AService：" + aService.getInfo());
        aService.doSomething();

        BService bService = container.getBean(BService.class);
        System.out.println("获取到的 BService：" + bService.getInfo());
        bService.doSomething();
    }

    @Test
    public void testSingleton() {
        System.out.println("\n========== 测试3：验证单例效果 ==========");
        MyIocContainer container = new MyIocContainer("org.example.injection_demo");

        AService a1 = container.getBean(AService.class);
        AService a2 = container.getBean(AService.class);

        System.out.println("第一次获取 AService hashCode：" + a1.hashCode());
        System.out.println("第二次获取 AService hashCode：" + a2.hashCode());
        System.out.println("两次获取的是同一个对象吗？" + (a1 == a2));

        BService b1 = container.getBean(BService.class);
        BService b2 = container.getBean(BService.class);

        System.out.println("\n第一次获取 BService hashCode：" + b1.hashCode());
        System.out.println("第二次获取 BService hashCode：" + b2.hashCode());
        System.out.println("两次获取的是同一个对象吗？" + (b1 == b2));
    }

}
