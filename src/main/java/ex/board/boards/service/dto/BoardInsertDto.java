package ex.board.boards.service.dto;

import java.util.ArrayList;
import java.util.List;

public class BoardInsertDto {

    private String boardSubject;
    private String boardContent;

    private List<AttachDto> attachDtoList = new ArrayList<>();

    public BoardInsertDto(String boardSubject, String boardContent) {
        this.boardSubject = boardSubject;
        this.boardContent = boardContent;
    }

    public BoardInsertDto(String boardSubject, String boardContent, List<AttachDto> attachDtoList) {
        this.boardSubject = boardSubject;
        this.boardContent = boardContent;
        this.attachDtoList = attachDtoList;
    }

    protected BoardInsertDto() {
    }

    public String getBoardSubject() {
        return boardSubject;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public List<AttachDto> getAttachDtoList() {
        return attachDtoList;
    }
}
