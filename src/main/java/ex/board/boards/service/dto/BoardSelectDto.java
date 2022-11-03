package ex.board.boards.service.dto;

public class BoardSelectDto {
    private String boardSubject;
    private String boardContent;

    public BoardSelectDto(String boardSubject, String boardContent) {
        this.boardSubject = boardSubject;
        this.boardContent = boardContent;
    }

    protected BoardSelectDto() {
    }

    public String getBoardSubject() {
        return boardSubject;
    }

    public String getBoardContent() {
        return boardContent;
    }
}
