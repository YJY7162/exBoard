package ex.board.boards.domain;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "ATTACH_ID_GENERATOR",
        sequenceName = "ATTACH_SEQUENCE"
)
public class Attach {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ATTACH_ID_GENERATOR"
    )
    @Column(name = "attach_id")
    private Long id;

    private String fileName;
    private String originalName;
    private String folderPath;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id")
    private Board board;

    public Attach(String fileName, String originalName, String folderPath) {
        this.fileName = fileName;
        this.originalName = originalName;
        this.folderPath = folderPath;
    }

    public Attach(String fileName, String originalName, String folderPath, Board board) {
        this.fileName = fileName;
        this.originalName = originalName;
        this.folderPath = folderPath;
        this.board = board;
    }

    public Attach() {
    }

    public Long getId() {
        return id;
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
