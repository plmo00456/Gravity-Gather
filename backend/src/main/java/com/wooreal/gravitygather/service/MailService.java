package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.exception.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender emailSender;

    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(String toEmail,
                          String title,
                          String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            System.out.println("MailService.sendEmail exception occur toEmail: " + toEmail + ", " + "title: " + title + ", text: " + text);
            throw new BusinessLogicException(HttpStatus.valueOf(500), "이메일 전송 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail,
                                              String title,
                                              String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}