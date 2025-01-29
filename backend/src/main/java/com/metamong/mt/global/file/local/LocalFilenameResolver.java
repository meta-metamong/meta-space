package com.metamong.mt.global.file.local;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.metamong.mt.global.file.AbstractFilenameResolver;

/**
 * <p>Spring Bean으로 등록된 FilenameResolver 구현체가 없을 경우 이 객체가
 * Spring Bean으로 등록된다.
 * <p>로컬, 테스트 환경에서는 이 객체를 사용한다.
 * 
 * @author pgd
 */
@Component
@Profile("!prod")
public class LocalFilenameResolver extends AbstractFilenameResolver {
    private static final String COMMON_FILE_PATH = "/resources/files/";
    
    private final String commonFileUrlPrefix;
    
    public LocalFilenameResolver(@Value("server.port") int serverPort) {
        this.commonFileUrlPrefix = "http://localhost:" + serverPort + COMMON_FILE_PATH;
    }

    @Override
    public String resolveFileUrl(String filename) {
        return this.commonFileUrlPrefix + filename;
    }
}
