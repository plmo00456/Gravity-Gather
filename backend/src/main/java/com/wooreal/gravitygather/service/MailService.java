package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.exception.ExceptionCode;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender emailSender;

    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(String toEmail,
        String title,
        String authCode) {
        String text = "<div class=\"container\" style=\"width: 600px;padding: 20px;\">\n"
            + "        <div class=\"content\">\n"
            + "            <h1>인증번호 이메일입니다</h1>\n"
            + "            <p>효율적인 커뮤니케이션을 위한 서비스, <b>끌림</b>에 가입하신것을 환영합니다!<br>아래의 인증번호를 입력하여 이메일 확인을 완료해주세요.</p>\n"
            + "            <div class=\"code\" style=\"background: #f0f0f0;padding: 15px;font-size: 50px;color: #3498db;margin: 30px 0;\">\n"
            + "                <b>" + authCode + "</b>\n"
            + "            </div>\n"
            + "\t\t\t<p>끌림을 어떻게 활용해야 할지 궁금하시다면?<br>아래의 도움말을 참고해보세요!</p>\n"
            + "\t\t\t<ul style=\"margin-top:30px\">\n"
            + "\t\t\t\t<li><a href=\"#\" style=\"margin-bottom: 10px;\">처음 끌림을 이용하시는 분을 위한 가이드</a></li>\n"
            + "\t\t\t\t<li><a href=\"#\" style=\"margin-bottom: 10px;\">일정을 관리하고 공유해보세요!</a></li>\n"
            + "\t\t\t\t<li><a href=\"#\">사람을 모아 미팅을 주선해보세요.</a></li>\n"
            + "\t\t\t</ul>\n"
            + "        </div>\n"
            + "    </div>";

        MimeMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            System.out.println("MailService.sendEmail exception occur toEmail: " + toEmail + ", " + "title: " + title + ", text: " + text);
            throw new BusinessLogicException(HttpStatus.valueOf(500), "이메일 전송 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
    }

    // 발신할 이메일 데이터 세팅
    private MimeMessage createEmailForm(String toEmail,
        String title,
        String text) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(text, true); // 두 번째 인자를 true로 설정하여 HTML 이메일임을 나타냅니다.
        } catch (MessagingException e) {
            System.out.println("MailService.createEmailForm exception occur toEmail: " + toEmail + ", " + "title: " + title + ", text: " + text);
            throw new BusinessLogicException(HttpStatus.valueOf(500), "이메일 생성 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }

        return message;
    }
}
