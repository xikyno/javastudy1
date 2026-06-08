package org.example.injection_demo.service;

import org.springframework.stereotype.Component;

@Component
public class EmailService {

    public void sendEmail(String to, String content) {
        System.out.println("【邮件服务】发送邮件给 " + to + "，内容：" + content);
    }

    public int getInstanceHashCode() {
        return this.hashCode();
    }
}