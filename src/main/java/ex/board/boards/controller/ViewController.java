package ex.board.boards.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    @GetMapping("/test")
    public String insertPage() {
        return "smartEditor/boardInsert";
    }

    @GetMapping("/testContent")
    public String contentPage(@RequestParam String boardId) {
        return "smartEditor/boardContent";
    }
}
