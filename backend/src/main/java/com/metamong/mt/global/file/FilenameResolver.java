package com.metamong.mt.global.file;

public interface FilenameResolver {

    /**
     * <p>UUID로 파일명을 생성해 반환하는 메소드. 생성된 파일명에 기존 파일명의 확장자를 붙인다.
     * <p>예: <br>
     * originalFilename = "anyujin.jpg"일 경우, "8a463aa4-b1dc-4f27-9c3f-53b94dc45e74.jpg" 반환
     * 
     * @param originalFilename 기존 파일명
     * @return UUID로 생성된 파일명. 파일 확장자가 붙어 있다.
     */
    String generateUuidFilename(String originalFilename);
    
    /**
     * <p>UUID로 파일명을 생성해 반환하는 메소드. 생성된 파일명에 인자로 전해진 fileType의 확장자가 붙는다.
     * <p>예: <br>
     * fileType = FileType.JPG일 경우, "8a463aa4-b1dc-4f27-9c3f-53b94dc45e74.jpg" 반환
     * 
     * @param fileType 생성할 파일명의 타입. 확장자
     * @return fileType의 확장자가 붙은 UUID 랜덤 파일명
     */
    String generateUuidFilename(FileType fileType);
    
    /**
     * <p>외부에서 파일에 접근할 수 있도록 파일의 위치를 나타내는 URL을 만들어 반환한다.<br>
     * 
     * @param filename URL을 생성할 파일명
     * @return 파일의 위치를 나타내는 URL
     */
    String resolveFileUrl(String filename);
}
