package com.metamong.mt.global.file.local;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.metamong.mt.global.file.FileUploader;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Spring Bean으로 등록된 FileUploader 구현체가 없을 경우 이 객체가
 * Spring Bean으로 등록된다.
 * <p>로컬, 테스트 환경에서는 이 객체를 사용한다.
 * <p>BeanConfig 클래스에서 Spring Bean으로 등록된다.
 * 
 * @author pgd
 */
@Slf4j
public class LocalFileUploader implements FileUploader {
    private static final int BUFFER_SIZE = 1024;
    private static final String PATH = "resources/files/";
    
    private final String fileSystemPath;
    
    public LocalFileUploader(String rootPath) {
        this.fileSystemPath = rootPath + PATH;
    }
    
    @PostConstruct
    public void init() {
        File fileDir = new File(this.fileSystemPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }

    @Override
    public String generateUploadUrl(String filename) {
        return "http://localhost:8080/api/files/" + filename;
    }

    @Override
    public void storeFile(String filename, InputStream is) throws UnsupportedOperationException, IOException {
         try (BufferedInputStream bis = new BufferedInputStream(is);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(this.fileSystemPath + filename)))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int readSize;
            while ((readSize = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, readSize);
            }
            if (log.isTraceEnabled()) {
                log.trace("File has been stored: {}", this.fileSystemPath + filename);
            }
        }
    }

    @Override
    public void storeFile(String filename, byte[] data) throws UnsupportedOperationException, IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(this.fileSystemPath + filename)))) {
            bos.write(data);
        }
    }
}
