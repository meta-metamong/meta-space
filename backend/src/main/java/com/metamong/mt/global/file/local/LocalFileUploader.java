package com.metamong.mt.global.file.local;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.metamong.mt.global.file.FileUploader;

/**
 * <p>Spring Bean으로 등록된 FileUploader 구현체가 없을 경우 이 객체가
 * Spring Bean으로 등록된다.
 * <p>로컬, 테스트 환경에서는 이 객체를 사용한다.
 * 
 * @author pgd
 */
@Component
@Profile("!prod")
public class LocalFileUploader implements FileUploader {

    @Override
    public String generateUploadUrl(String filename) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void storeFile(String filename, InputStream is) throws UnsupportedOperationException, IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void storeFile(String filename, byte[] data) throws UnsupportedOperationException, IOException {
        // TODO Auto-generated method stub
        
    }
}
