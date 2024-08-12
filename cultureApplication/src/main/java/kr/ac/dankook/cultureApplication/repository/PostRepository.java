package kr.ac.dankook.cultureApplication.repository;

import kr.ac.dankook.cultureApplication.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByMemberId(Long memberId);
    List<Post> findAllByOrderByCreatedDateTimeDesc();
    List<Post> findAllByOrderByTotalCountDesc();
    List<Post> findByTitleContainingAndContentContaining(String title,String content);
}
