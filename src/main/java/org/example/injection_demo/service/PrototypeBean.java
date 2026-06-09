package org.example.injection_demo.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")  // 👈 关键：声明为多例模式
public class PrototypeBean {

    private int count = 0;

    public void addCount() {
        count++;
        System.out.println("PrototypeBean count = " + count);
    }

    public int getCount() {
        return count;
    }
}