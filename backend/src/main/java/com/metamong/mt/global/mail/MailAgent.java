package com.metamong.mt.global.mail;

import com.metamong.mt.global.mail.exception.MailTransmissionException;

public interface MailAgent {
    
    /**
     * 메일 전송.
     * 
     * @param mailType 전송하려는 메일의 type
     * @param subject 메일 제목
     * @param receiverEmail 메일을 받는 사람의 이메일 주소
     * @param params 메시지 포맷에 정의한 파라미터
     * @throws MailTransmissionException 메일 전송에 실패할 경우
     */
    void send(MailType mailType, String subject, String receiverEmail, Object... params)
            throws MailTransmissionException;
}
