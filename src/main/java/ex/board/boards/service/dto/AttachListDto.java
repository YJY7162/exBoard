package ex.board.boards.service.dto;

import java.util.ArrayList;
import java.util.List;

public class AttachListDto {

    private Long id;
    private String originalName;


    public AttachListDto(Long id, String originalName) {
        this.id = id;
        this.originalName = originalName;
    }

    protected AttachListDto() {
    }

    public String getOriginalName() {
        return originalName;
    }

    public Long getId() {
        return id;
    }
}
