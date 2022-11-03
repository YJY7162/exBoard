package ex.board.boards.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ex.board.boards.domain.Attach;
import ex.board.boards.service.dto.AttachListDto;

public interface AttachRepository extends JpaRepository<Attach, Long> {
    @Query("select a.originalName, a.id from Attach a where a.board.id = :boardId")
    List<Attach> findAttachList(@Param("boardId") Long boardId);
}
