package com.metamong.mt.global.mail;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 메일을 전송하지 않는 Mock Bean.
 * 실제로 메일을 전송하면 비용이 발생하므로 테스트 환경에서는
 * 메일을 보내는 척만하는 이 Mock 객체 사용.
 */
@Slf4j
public class MailAgentMock extends AbstractMailAgentBean {
    
    public MailAgentMock(List<MailMessageFormatter> formatters) {
        super(formatters);
    }

    @Override
    public void send(MailType mailType, String subject, String receiverEmail, Object... params) {
        MailMessageFormatter formatter = super.getMessageFormatter(mailType);
        log.info("formatter={}", formatter);
        log.info("mailType={}", mailType);
        log.info("subject={}", subject);
        log.info("receiver={}", receiverEmail);
        log.info("mail text={}", formatter.format(params));
    }
}
