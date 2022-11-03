package ex.board.boards.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import ex.board.boards.domain.Attach;
import ex.board.boards.domain.Board;
import ex.board.boards.domain.repository.AttachRepository;
import ex.board.boards.domain.repository.BoardRepository;
import ex.board.boards.service.dto.AttachDto;
import ex.board.boards.service.dto.BoardInsertDto;
import ex.board.boards.service.dto.BoardSelectDto;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardRepository boardRepository;
    private final AttachRepository attachRepository;

    Logger log = LoggerFactory.getLogger(AttachController.class);

    public BoardController(BoardRepository boardRepository, AttachRepository attachRepository) {
        this.boardRepository = boardRepository;
        this.attachRepository = attachRepository;
    }

    @PostMapping
    public String insertBoard(@RequestBody BoardInsertDto boardInsertDto) throws UnsupportedEncodingException {
        Board board = new Board(boardInsertDto.getBoardSubject(), boardInsertDto.getBoardContent());
        boardRepository.save(board);

        if (boardInsertDto.getAttachDtoList() != null){
            List<AttachDto> attachDtoList = boardInsertDto.getAttachDtoList();
            for (int i = 0; i < attachDtoList.size(); i++) {
                AttachDto attachDto = attachDtoList.get(i);
                Attach attach =
                        new Attach(attachDto.getFileName(), attachDto.getOriginalName(),
                                attachDto.getFolderPath(), board);
                attachRepository.save(attach);
            }
        }
        return "success";
    }



//    @PostMapping
//    public String insertBoard(@RequestBody BoardInsertDto boardInsertDto) throws UnsupportedEncodingException {
//        Board board = new Board(boardInsertDto.getBoardSubject(), boardInsertDto.getBoardContent());
//        boardRepository.save(board);
//
//        String image = boardInsertDto.getBoardContent();
//        String[] split = image.split("img src=\"/images/");
//        for (int i = 1; i < split.length; i++) {
//            String decodeSplit = URLDecoder.decode(split[i], "UTF-8");
//            String replace = decodeSplit.replace("|~|", File.separator);
//            String[] split2 = replace.split("title");
//            String path = split2[0].substring(0, split2[0].length()-2);
//            String folderPath = path.substring(0, 28);
//            String fileName = path.substring(29);
//            String originalName = path.substring(66);
//
//            System.out.println("folderPath = " + folderPath);
//            System.out.println("fileName = " + fileName);
//            System.out.println("originalName = " + originalName);
//
//            Attach attach = new Attach(fileName, originalName, folderPath, board);
//            attachRepository.save(attach);
//        }
//        return "success";
//    }

    @GetMapping("/{boardId}")
    public BoardSelectDto getBoardDetail(@PathVariable("boardId") Long id) {
        Optional<Board> findBoard = boardRepository.findById(id);
        String boardContent = findBoard.get().getBoardContent();
        String boardSubject = findBoard.get().getBoardSubject();
        BoardSelectDto boardSelectDto = new BoardSelectDto(boardSubject, boardContent);
        return boardSelectDto;
    }





}
