package com.metamong.mt.domain.member.mailformatter;

import org.springframework.stereotype.Component;

import com.metamong.mt.global.mail.MailMessageFormatter;
import com.metamong.mt.global.mail.MailType;

@Component
public class PasswordResetLinkMailMessageFormatter extends MailMessageFormatter {

    @Override
    protected String getMessageFormat() {
        return """
                패스워드를 변경하시려면 다음 링크를 클릭하세요: {0}
                """;
    }

    @Override
    public MailType getSupportedMailType() {
        return MailType.PASSWORD_RESET_LINK;
    }
}
