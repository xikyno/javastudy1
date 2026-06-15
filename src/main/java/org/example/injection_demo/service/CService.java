package org.example.injection_demo.service;

import org.example.injection_demo.annotation.MyComponent;

@MyComponent
public class CService {

    public void doSomething() {
        System.out.println("CService.doSomething() 被调用");
    }

    public String getInfo() {
        return "我是 CService 实例：" + this.hashCode();
    }
}