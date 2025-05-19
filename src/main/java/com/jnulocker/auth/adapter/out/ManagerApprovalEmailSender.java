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
public class ManagerApprovalEmailSender implements ManagerEmailSender {

    private static final String EMAIL_SUBJECT = "[전남대학교 사물함 신청 서비스] 관리자 승인 결과 안내 메일";
    private static final String APPROVE_TEMPLATE = "manager-approve";
    private static final String REJECT_TEMPLATE = "manager-reject";
    private static final String UTF_8_ENCODING = "UTF-8";

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendManagerApprovedEmail(String email, String department) {
        sendEmail(email, department, APPROVE_TEMPLATE);
    }

    @Override
    public void sendManagerRejectedEmail(String email, String department) {
        sendEmail(email, department, REJECT_TEMPLATE);
    }

    private void sendEmail(String email, String department, String templateName) {
        try {
            MimeMessage message = setUpEmailForm(email, department, templateName);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw SendEmailException.EXCEPTION;
        }
    }

    private MimeMessage setUpEmailForm(String email, String department, String templateName)
            throws MessagingException {
        String form = generateEmailContent(department, templateName);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, UTF_8_ENCODING);

        helper.setTo(email);
        helper.setSubject(EMAIL_SUBJECT);
        helper.setText(form, true);
        return message;
    }

    private String generateEmailContent(String department, String templateName) {
        Context context = new Context();
        context.setVariable("department", department);
        return templateEngine.process(templateName, context);
    }
}
