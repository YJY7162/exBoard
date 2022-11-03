package ex.board.member.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import ex.board.boards.domain.Board;

@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    public Member(String username) {
        this.username = username;
    }

    protected Member() {
    }
}
