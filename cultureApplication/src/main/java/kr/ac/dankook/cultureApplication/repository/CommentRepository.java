package kr.ac.dankook.cultureApplication.repository;

import kr.ac.dankook.cultureApplication.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostId(Long postId);
    List<Comment> findByMemberId(Long memberId);
}
