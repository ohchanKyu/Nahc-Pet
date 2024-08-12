package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.entity.Comment;
import kr.ac.dankook.cultureApplication.entity.Member;
import kr.ac.dankook.cultureApplication.entity.Post;
import kr.ac.dankook.cultureApplication.exception.ErrorCode;
import kr.ac.dankook.cultureApplication.exception.NoEntityException;
import kr.ac.dankook.cultureApplication.exception.PostNotFoundException;
import kr.ac.dankook.cultureApplication.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public void deleteAllMemberComment(Member member){
        List<Comment> targetComments = commentRepository.findByMemberId(member.getId());
        for(Comment comment : targetComments){
            commentRepository.deleteById(comment.getId());
        }
    }

    @Transactional
    public Comment addCommentProcess(Member member, Post targetPost, Comment newComment) {
        newComment.setMember(member);
        newComment.setPost(targetPost);
        return commentRepository.save(newComment);
    }
    @Transactional(readOnly = true)
    public List<Comment> getAllCommentByMemberId(Member member){
        return commentRepository.findByMemberId(member.getId());
    }
    @Transactional(readOnly = true)
    public List<Comment> getAllCommentByPostId(Long postId){
        return commentRepository.findByPostId(postId);
    }
    @Transactional
    public boolean deleteOneCommentProcess(Long commentId){
        Optional<Comment> targetComment = commentRepository.findById(commentId);
        if (targetComment.isEmpty()){
            throw new NoEntityException(ErrorCode.NoEntityException);
        }
        commentRepository.deleteById(targetComment.get().getId());
        return true;
    }
    @Transactional
    public Comment editCommentProcess(Long commentId, Comment editComment){
        Optional<Comment> targetComment = commentRepository.findById(commentId);
        if (targetComment.isEmpty()){
            throw new NoEntityException(ErrorCode.NoEntityException);
        }
        if (!editComment.getContent().equals(targetComment.get().getContent())){
            targetComment.get().setContent(editComment.getContent());
        }
        return commentRepository.save(targetComment.get());
    }
}
