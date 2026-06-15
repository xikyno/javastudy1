package org.example.injection_demo.service;

import org.example.injection_demo.annotation.MyComponent;

@MyComponent
public class BService {

    public void doSomething() {
        System.out.println("BService.doSomething() 被调用");
    }

    public String getInfo() {
        return "我是 BService 实例：" + this.hashCode();
    }
}