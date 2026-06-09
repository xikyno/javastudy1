package org.example.injection_demo.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 生命周期演示 Bean
 *
 * 这个类实现了多个生命周期回调方法，用于观察 Spring 容器管理 Bean 的完整过程
 *
 * 执行顺序：
 * 1. 构造器
 * 2. @PostConstruct
 * 3. InitializingBean.afterPropertiesSet()
 * 4. 自定义 initMethod（需要在配置类中指定，这里先不加）
 * 5. 业务方法
 * 6. @PreDestroy（容器关闭时）
 * 7. DisposableBean.destroy()
 */
@Component
public class LifecycleDemo implements InitializingBean {

    private String name;

    /**
     * 1. 构造器
     * 执行时机：Spring 调用构造器创建对象
     * 此时依赖还没有注入（字段还是 null）
     */
    public LifecycleDemo() {
        System.out.println("【1】构造器：LifecycleDemo 对象被创建");
        System.out.println("    此时 name = " + name + "（还没赋值）");
    }

    /**
     * 2. @PostConstruct 注解的方法
     * 执行时机：构造器执行完后，依赖注入完成后
     * 注意：需要添加 javax.annotation-api 依赖
     */
    @PostConstruct
    public void postConstruct() {
        System.out.println("【2】@PostConstruct：依赖注入完成，执行初始化前的准备工作");
        this.name = "初始化名称";
        System.out.println("    此时 name 已被赋值为：" + name);
    }

    /**
     * 3. InitializingBean.afterPropertiesSet()
     * 执行时机：@PostConstruct 之后
     * 这是 Spring 提供的接口方式，和 @PostConstruct 类似
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("【3】InitializingBean.afterPropertiesSet()：所有属性设置完成");
        System.out.println("    可以在这里做一些额外的初始化工作");
    }

    /**
     * 业务方法
     * 执行时机：Bean 初始化完成之后，可以被调用
     */
    public void doSomething() {
        System.out.println("【业务方法】doSomething()：Bean 已经初始化完成，可以正常使用了");
        System.out.println("    当前 name = " + name);
    }

    /**
     * 4. @PreDestroy 注解的方法
     * 执行时机：Spring 容器关闭时，销毁 Bean 之前
     */
    @PreDestroy
    public void preDestroy() {
        System.out.println("【4】@PreDestroy：Spring 容器即将关闭，执行清理工作");
    }

    /**
     * 5. DisposableBean.destroy()
     * 执行时机：@PreDestroy 之后
     */
    // 不实现 DisposableBean 了，够用就行
}