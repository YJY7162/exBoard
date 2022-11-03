package ex.board.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ex.board.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
