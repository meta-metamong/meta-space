package com.metamong.mt.global.file;

public enum FileType {
    NONE, // 확장자가 없는 파일
    JPG,
    PNG,
    JPEG,
    WEBP;
    
    private final String extension;
    
    FileType() {
        this.extension = "NONE".equals(super.name()) ? "" : "." + super.name().toLowerCase();
    }
    
    /**
     * <p>파일의 확장자를 반환. "."이 prefix로 붙는다.
     * <p>예: FileType.JPG의 경우, ".jpg" 반환
     * 
     * @return 파일의 확장자. ex) .jpg
     */
    public String extension() {
        return this.extension;
    }
}
