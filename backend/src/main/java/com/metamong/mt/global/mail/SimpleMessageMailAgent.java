package com.metamong.mt.global.mail;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.metamong.mt.global.mail.exception.MailTransmissionException;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile("prod")
@Slf4j
public class SimpleMessageMailAgent extends AbstractMailAgentBean {
    private final MailSender mailSender;
    
    public SimpleMessageMailAgent(List<MailMessageFormatter> formatterBeans, MailSender mailSender) {
        super(formatterBeans);
        this.mailSender = mailSender;
    }

    @Override
    public void send(MailType mailType, String subject, String receiverEmail, Object... params) {
        MailMessageFormatter formatter = super.getMessageFormatter(mailType);
        String formatted = formatter.format(params);
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(receiverEmail);
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setText(formatted);
            this.mailSender.send(message);
        } catch (MailException e) {
            throw new MailTransmissionException(e);
        }
    }
}
