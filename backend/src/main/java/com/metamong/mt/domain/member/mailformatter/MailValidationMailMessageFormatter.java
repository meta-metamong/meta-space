package com.metamong.mt.domain.member.mailformatter;

import org.springframework.stereotype.Component;

import com.metamong.mt.global.mail.MailMessageFormatter;
import com.metamong.mt.global.mail.MailType;

@Component
public class MailValidationMailMessageFormatter extends MailMessageFormatter {

    @Override
    protected String getMessageFormat() {
        return """
                인증 번호는 "{0}"입니다.
                """;
    }

    @Override
    public MailType getSupportedMailType() {
        return MailType.MAIL_VALIDATION;
    }
}
