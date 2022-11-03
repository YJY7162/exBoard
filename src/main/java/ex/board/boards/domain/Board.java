package ex.board.boards.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import ex.board.boards.domain.Attach;
import ex.board.boards.domain.BaseEntity;
import ex.board.member.domain.Member;

@Entity
@SequenceGenerator(
        name = "BOARD_ID_GENERATOR",
        sequenceName = "BOARD_SEQUENCE"
)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "BOARD_ID_GENERATOR"
    )
    @Column(name = "board_id")
    private Long id;

    private String boardSubject;
    private String boardContent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    private List<Attach> attaches = new ArrayList<>();

    public Board(String boardSubject, String boardContent, Member member) {
        this.boardSubject = boardSubject;
        this.boardContent = boardContent;
        this.member = member;
    }
    public Board(String boardSubject, String boardContent) {
        this.boardSubject = boardSubject;
        this.boardContent = boardContent;
    }

    protected Board() {
    }

    public Long getId() {
        return id;
    }

    public String getBoardSubject() {
        return boardSubject;
    }

    public String getBoardContent() {
        return boardContent;
    }
}
