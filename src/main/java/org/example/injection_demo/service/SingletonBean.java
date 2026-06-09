package org.example.injection_demo.service;

import org.springframework.stereotype.Component;

@Component  // 默认就是 Singleton，不需要写 @Scope
public class SingletonBean {

    private int count = 0;

    public void addCount() {
        count++;
        System.out.println("SingletonBean count = " + count);
    }

    public int getCount() {
        return count;
    }
}