package org.example.injection_demo.service;

import org.springframework.stereotype.Component;

@Component
public class OrderServiceConstructor {

    private final EmailService emailService;

    public OrderServiceConstructor(EmailService emailService) {
        this.emailService = emailService;
        System.out.println("【构造器注入】OrderServiceConstructor 被创建");
        System.out.println("           传入的 EmailService 实例 hashCode：" + emailService.getInstanceHashCode());
    }

    public void placeOrder(String userEmail, String productName) {
        System.out.println("【构造器注入-订单服务】处理订单：" + productName);
        emailService.sendEmail(userEmail, "您的订单【" + productName + "】已创建成功！");
    }
}