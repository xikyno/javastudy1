package org.example.injection_demo.service;

import org.example.injection_demo.annotation.MyAutowired;
import org.example.injection_demo.annotation.MyComponent;

@MyComponent
public class B {

    @MyAutowired
    private A a;

    public void doSomething() {
        System.out.println("B.doSomething() 被调用");
        System.out.println("  B 中的 A 实例 hashCode：" + a.hashCode());
    }

    public String getInfo() {
        return "我是 B 实例：" + this.hashCode();
    }
}