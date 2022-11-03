package ex.board.boards.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ex.board.boards.domain.Attach;
import ex.board.boards.domain.Board;
import ex.board.boards.service.dto.AttachDto;
import ex.board.boards.service.dto.AttachListDto;
import ex.board.boards.service.dto.BoardInsertDto;

@SpringBootTest
@Transactional
class AttachRepositoryTest {

    @Autowired
    AttachRepository attachRepository;

    @Autowired
    BoardRepository boardRepository;

    @Test
    public void findAttachListTest() {
        List<AttachDto> attachDtoList = new ArrayList<>();
        attachDtoList.add(new AttachDto("testFile", "testFileOrigin", "testtest"));
        attachDtoList.add(new AttachDto("testFile2", "testFileOrigin2", "testtest"));
        BoardInsertDto boardInsertDto = new BoardInsertDto("test", "test");
        Board board = new Board(boardInsertDto.getBoardSubject(), boardInsertDto.getBoardContent());
        boardRepository.save(board);
        Attach attach1 = new Attach(
                attachDtoList.get(0).getFileName(),
                attachDtoList.get(0).getOriginalName(),
                attachDtoList.get(0).getFolderPath(),
                board);

        Attach attach2 = new Attach(
                attachDtoList.get(1).getFileName(),
                attachDtoList.get(1).getOriginalName(),
                attachDtoList.get(1).getFolderPath(),
                board);

        attachRepository.save(attach1);
        attachRepository.save(attach2);
        Board testBoard = boardRepository.findMemberByBoardContent("test");
        List<Attach> attachList = attachRepository.findAttachList(testBoard.getId());
        List<AttachListDto> attachListDtos = new ArrayList<>();
        for (Attach attach : attachList) {
            AttachListDto attachListDto = new AttachListDto(attach.getId(), attach.getOriginalName());
            attachListDtos.add(attachListDto);
        }

        assertThat(attachListDtos.get(0).getOriginalName()).isEqualTo("testFileOrigin");
        assertThat(attachListDtos.get(1).getOriginalName()).isEqualTo("testFileOrigin2");
    }

}
