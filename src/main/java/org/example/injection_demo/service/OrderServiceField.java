package org.example.injection_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceField {

    @Autowired
    private EmailService emailService;

    public void placeOrder(String userEmail, String productName) {
        System.out.println("【字段注入-订单服务】处理订单：" + productName);
        emailService.sendEmail(userEmail, "您的订单【" + productName + "】已创建成功！");
        System.out.println("           注入的 EmailService 实例 hashCode：" + emailService.getInstanceHashCode());
    }
}