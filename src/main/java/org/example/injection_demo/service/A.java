package org.example.injection_demo.service;

import org.example.injection_demo.annotation.MyAutowired;
import org.example.injection_demo.annotation.MyComponent;

@MyComponent
public class A {

    @MyAutowired
    private B b;

    public void doSomething() {
        System.out.println("A.doSomething() 被调用");
        System.out.println("  A 中的 B 实例 hashCode：" + b.hashCode());
        b.doSomething();
    }

    public String getInfo() {
        return "我是 A 实例：" + this.hashCode();
    }
}