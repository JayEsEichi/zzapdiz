//package com.example.zzapdiz.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
//@Configuration
//public class MailConfig {
//
//    // host, username, password, port, ssl.trust
//
//    @Bean
//    public JavaMailSender javaMailService() {
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//
//        javaMailSender.setHost("smtp.naver.com");
//        javaMailSender.setUsername("네이버 SMTP 설정 이메일");
//        javaMailSender.setPassword("네이버 계정 비밀번호");
//        javaMailSender.setPort(465);
//        javaMailSender.setJavaMailProperties(getMailProperties());
//
//        return javaMailSender;
//    }
//
//    /** 메일을 보내기 위한 smtp 설정 **/
//    private Properties getMailProperties() {
//        Properties properties = new Properties();
//        properties.setProperty("mail.transport.protocol", "smtp");
//        properties.setProperty("mail.smtp.auth", "true");
//        properties.setProperty("mail.smtp.starttls.enable", "true");
//        properties.setProperty("mail.debug", "true");
//        properties.setProperty("mail.smtp.ssl.trust","smtp.naver.com");
//        properties.setProperty("mail.smtp.ssl.enable","true");
//
//        return properties;
//    }
//}