package ex.board.boards.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ex.board.boards.domain.Board;
import ex.board.member.domain.Member;
import ex.board.member.domain.repository.MemberRepository;

@SpringBootTest
@Transactional
class BoardRepositoryTest {

    @Autowired BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(value = false)
    public void basicCRUD() {

        Member member = new Member("member1");
        memberRepository.save(member);

        Board board1 = new Board("subject1", "content1", member);
        Board board2 = new Board("subject2", "content2", member);
        boardRepository.save(board1);
        boardRepository.save(board2);

        // 단건 조회 검증
        Board findboard1 = boardRepository.findById(board1.getId()).get();
        Board findboard2 = boardRepository.findById(board2.getId()).get();
        assertThat(findboard1).isEqualTo(board1);
        assertThat(findboard2).isEqualTo(board2);

        // 리스트 조회 검증
        List<Board> all = boardRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = boardRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
//        boardRepository.delete(board1);
//        boardRepository.delete(board2);
//
//        long deletedCount = boardRepository.count();
//        assertThat(deletedCount).isEqualTo(0);
    }


}
