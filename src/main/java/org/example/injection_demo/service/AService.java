package org.example.injection_demo.service;

import org.example.injection_demo.annotation.MyComponent;

@MyComponent
public class AService {

    public void doSomething() {
        System.out.println("AService.doSomething() 被调用");
    }

    public String getInfo() {
        return "我是 AService 实例：" + this.hashCode();
    }
}