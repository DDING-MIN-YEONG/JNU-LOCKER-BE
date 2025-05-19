package com.jnulocker.auth.adapter.out;

import com.jnulocker.auth.application.port.out.ManagerEmailSender;
import com.jnulocker.auth.exception.SendEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class ManagerApprovalEmailSendeer implements ManagerEmailSender {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendManagerApprovedEmail(String email, String department) {
        try {
            MimeMessage message = setupEmailForm(email, department);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw SendEmailException.EXCEPTION;
        }
    }

    private MimeMessage setupEmailForm(String email, String department) throws MessagingException {
        String form = generateEmailForm(department);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setTo(email);
        helper.setSubject("[전남대학교 사물함 신청 서비스] 관리자 승인 완료 안내 메일");
        helper.setText(form, true);
        return message;
    }

    private String generateEmailForm(String department) {
        Context context = new Context();
        context.setVariable("department", department);
        return templateEngine.process("manager-approve", context);
    }
}
