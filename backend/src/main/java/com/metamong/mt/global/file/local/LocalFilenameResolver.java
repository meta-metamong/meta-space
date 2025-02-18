package com.metamong.mt.global.file.local;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.metamong.mt.global.file.AbstractFilenameResolver;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Spring Bean으로 등록된 FilenameResolver 구현체가 없을 경우 이 객체가
 * Spring Bean으로 등록된다.
 * <p>로컬, 테스트 환경에서는 이 객체를 사용한다.
 * 
 * @author pgd
 */
@Component
//@Profile("!prod")
@Slf4j
public class LocalFilenameResolver extends AbstractFilenameResolver {
    private static final String COMMON_FILE_PATH = "/resources/files/";
    
    private final String serverOrigin;
    private final String commonFileUrlPrefix;
    
    public LocalFilenameResolver(@Value("${server.port}") int serverPort, @Value("${server-origin}") String serverOrigin) {
        log.debug("server.port={}", serverPort);
        this.commonFileUrlPrefix = "https://www.metaspaces.shop" + COMMON_FILE_PATH;
        this.serverOrigin = serverOrigin;
    }

    @Override
    public String resolveFileUrl(String filename) {
        return this.commonFileUrlPrefix + filename;
    }
}
