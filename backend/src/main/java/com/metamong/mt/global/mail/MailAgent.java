package com.metamong.mt.global.mail;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailAgent {
    private final List<MailMessageFormatter> messageFormatters;
}
