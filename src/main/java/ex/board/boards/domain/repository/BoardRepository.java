package ex.board.boards.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ex.board.boards.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findMemberByBoardContent(String boardContent); // 단건
}
