package ex.board.boards.service.dto;

public class AttachDto {
    private String fileName;
    private String originalName;
    private String folderPath;

    public AttachDto(String fileName, String originalName, String folderPath) {
        this.fileName = fileName;
        this.originalName = originalName;
        this.folderPath = folderPath;
    }

    protected AttachDto() {
    }

    public String getFileName() {
        return fileName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getFolderPath() {
        return folderPath;
    }
}
