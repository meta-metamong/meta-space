package com.metamong.mt.global.file.local;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.metamong.mt.global.file.AbstractFilenameResolver;
import com.metamong.mt.global.file.FileType;
import com.metamong.mt.global.file.FilenameResolver;

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

    @Override
    public String resolveFileUrl(String filename) {
        // TODO Auto-generated method stub
        return null;
    }
}
