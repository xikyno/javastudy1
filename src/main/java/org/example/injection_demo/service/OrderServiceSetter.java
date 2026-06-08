package org.example.injection_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceSetter {

    private EmailService emailService;

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
        System.out.println("【Setter注入】OrderServiceSetter 的 emailService 通过 setter 方法传入");
        System.out.println("           传入的 EmailService 实例 hashCode：" + emailService.getInstanceHashCode());
    }

    public void placeOrder(String userEmail, String productName) {
        System.out.println("【Setter注入-订单服务】处理订单：" + productName);
        if (emailService != null) {
            emailService.sendEmail(userEmail, "您的订单【" + productName + "】已创建成功！");
        } else {
            System.out.println("警告：EmailService 尚未注入");
        }
    }
}