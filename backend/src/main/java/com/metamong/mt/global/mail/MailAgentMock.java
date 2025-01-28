package com.metamong.mt.global.mail;

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 메일을 전송하지 않는 Mock Bean.
 * 실제로 메일을 전송하면 비용이 발생하므로 테스트 환경에서는
 * 메일을 보내는 척만하는 이 Mock 객체 사용.
 */
@Component
@ConditionalOnMissingBean(MailAgent.class)
@Slf4j
public class MailAgentMock extends AbstractMailAgentBean {
    
    public MailAgentMock(List<MailMessageFormatter> formatters) {
        super(formatters);
    }

    @Override
    public void send(MailType mailType, Object... params) {
        MailMessageFormatter formatter = super.getMessageFormatter(mailType);
        log.info("Sent mail={}", formatter.format(params));
    }
}
