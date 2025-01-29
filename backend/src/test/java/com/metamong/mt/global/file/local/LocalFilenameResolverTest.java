package com.metamong.mt.global.file.local;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.metamong.mt.global.file.FileType;

class LocalFilenameResolverTest {
    LocalFilenameResolver localFilenameResolver;
    
    @BeforeEach
    void beforeEach() {
        this.localFilenameResolver = new LocalFilenameResolver();
    }
    
    @Test
    @DisplayName("generateUuidFilename(String) - UUID가 생성되는지 확인")
    void generateUuidFilename_checkWhetherUuidGenerated() {
        // Given
        final String originalFilename = "anyujin.jpg";
        
        // When
        String result = this.localFilenameResolver.generateUuidFilename(originalFilename);
        
        // Then
        Pattern uuidFilePattern = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.jpg$");
        Matcher matcher = uuidFilePattern.matcher(result);
        assertThat(matcher.matches()).isTrue();
    }
    
    @Test
    @DisplayName("generateUuidFilename(String) - null을 인자로 전달할 경우 NullPointerException")
    void generateUuidFilename_NullPointerException() {
        String originalFilename = null;
        
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> this.localFilenameResolver.generateUuidFilename(originalFilename));
    }
    
    @Test
    @DisplayName("generateUuidFilename(String) - 확장자가 없는 filename의 경우")
    void generateUuidFilename_originalFilenameWithoutExtension() {
        // Given
        final String originalFilename = "Hello, World!";
        
        // When
        String result = this.localFilenameResolver.generateUuidFilename(originalFilename);
        
        // Then
        Pattern uuidPattern = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        Matcher matcher = uuidPattern.matcher(result);
        assertThat(matcher.matches()).isTrue();
    }
    
    static Stream<FileType> generateUuidFilename_fileType() {
        return Arrays.stream(FileType.values())
                .filter((v) -> v != FileType.NONE);
    }
    
    @ParameterizedTest
    @MethodSource
    @DisplayName("generateUuidFilename(FileType) - 성공")
    void generateUuidFilename_fileType(FileType fileType) {
        
        // When
        String result = this.localFilenameResolver.generateUuidFilename(fileType);
        
        // Then
        Pattern uuidPattern = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\" + fileType.extension() + "$");
        Matcher matcher = uuidPattern.matcher(result);
        assertThat(matcher.matches()).isTrue();
    }
    
    @Test
    @DisplayName("generateUuidFilename(FileType) - FileType.NONE")
    void generateUuidFilename_fileType_NONE() {
        // Given
        final FileType none = FileType.NONE;
        
        // When
        String result = this.localFilenameResolver.generateUuidFilename(none);
        
        // Then
        Pattern uuidPattern = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        Matcher matcher = uuidPattern.matcher(result);
        assertThat(matcher.matches()).isTrue();
    }
    
    @Test
    @DisplayName("generateUuidFilename(FileType) - null이 인자로 주어질 경우 NullPointerException")
    void generateUuidFilename_nullIsNotAllowed() {
        // Given
        final FileType fileType = null;
        
        // When
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> this.localFilenameResolver.generateUuidFilename(fileType));
    }
}
