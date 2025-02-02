package com.metamong.mt.testutil;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.metamong.mt.global.file.FileUploader;

import lombok.extern.slf4j.Slf4j;

/**
 * 테스트 환경에서만 등록되는 더미 파일 업로더
 */
@Component
@Primary
@Slf4j
public class MockFileUploader implements FileUploader {
    
    @Override
    public String generateUploadUrl(String filename) {
        log.info("generateUploadUrl(String {})", filename);
        return "http://localhost:8080/api/files/" + filename;
    }

    @Override
    public void storeFile(String filename, InputStream is) throws UnsupportedOperationException, IOException {
        log.info("storeFile(String {}, InputStream {})", filename, is);
    }

    @Override
    public void storeFile(String filename, byte[] data) throws UnsupportedOperationException, IOException {
        log.info("storeFile(String {}, byte[] {})", filename, data);
    }
}
