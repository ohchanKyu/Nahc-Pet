package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.dto.response.PostResponse;
import kr.ac.dankook.cultureApplication.entity.Comment;
import kr.ac.dankook.cultureApplication.entity.Member;
import kr.ac.dankook.cultureApplication.entity.Post;
import kr.ac.dankook.cultureApplication.exception.ErrorCode;
import kr.ac.dankook.cultureApplication.exception.PostNotFoundException;
import kr.ac.dankook.cultureApplication.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final RedisService redisService;
    private final FileService fileService;

    @Transactional
    public void deleteAllMemberPost(Member member) throws IOException {
        List<Post> targetPosts = postRepository.findByMemberId(member.getId());
        for(Post post : targetPosts){
            fileService.deletePostImage(extractImageFileNames(post.getContent(),"postImage"));
            List<Comment> comments = commentService.getAllCommentByPostId(post.getId());
            for(Comment comment : comments){
                commentService.deleteOneCommentProcess(comment.getId());
            }
            postRepository.deleteById(post.getId());
        }
    }
    @Transactional(readOnly = true)
    public Post findOneTargetPost(Long postId){
        Optional<Post> targetPost = postRepository.findById(postId);
        if (targetPost.isEmpty()){
            throw new PostNotFoundException(ErrorCode.POST_NOT_FOUND);
        }
        return targetPost.get();
    }

    @Transactional
    public Post addNewPostProcess(Member member, Post newPost) throws IOException {
        newPost.setMember(member);
        newPost.setTotalCount(0L);
        // temp File 에서 사용하지 않은 파일은 삭제
        fileService.filterTempImageList(extractImageFileNames(newPost.getContent(),"temp"));
        // 해당 글의 경로를 postImage로 변경
        newPost.setContent(fetchImagePaths(newPost.getContent()));
        // temp의 모든 파일을 PostImage로 이관
        fileService.transferTempToDirectory();
        return postRepository.save(newPost);
    }

    @Transactional
    public Post editPostProcess(Long postId, Post editPost) throws IOException {
        Optional<Post> targetPost = postRepository.findById(postId);
        if (targetPost.isEmpty()){
            throw new PostNotFoundException(ErrorCode.POST_NOT_FOUND);
        }
        if (!editPost.getContent().equals(targetPost.get().getContent())){

            // 기존에 postImage 파일에 존재하고 사용하지 않는 이미지 삭제
            List<String> originalList = extractImageFileNames(targetPost.get().getContent(),"postImage");
            List<String> newList = extractImageFileNames(editPost.getContent(), "postImage");
            List<String> deleteList = new ArrayList<>();
            for(String original : originalList){
                if (!newList.contains(original)){
                    deleteList.add(original);
                }
            }
            fileService.deletePostImage(deleteList);

            // temp File 에서 사용하지 않은 파일은 삭제
            fileService.filterTempImageList(extractImageFileNames(editPost.getContent(),"temp"));
            // 해당 글의 경로를 postImage로 변경
            editPost.setContent(fetchImagePaths(editPost.getContent()));
            // temp의 모든 파일을 PostImage로 이관
            fileService.transferTempToDirectory();
            // 원래 포스트의 내용을 변경
            targetPost.get().setContent(editPost.getContent());
        }
        if (!editPost.getTitle().equals(targetPost.get().getTitle())){
            targetPost.get().setTitle(editPost.getTitle());
        }
        return postRepository.save(targetPost.get());
    }

    @Transactional
    public boolean deleteOnePostProcess(Long postId) throws IOException {
        Optional<Post> targetPost = postRepository.findById(postId);
        if (targetPost.isEmpty()){
            throw new PostNotFoundException(ErrorCode.POST_NOT_FOUND);
        }
        // postImage 경로에 있는 이미지 삭제
        fileService.deletePostImage(extractImageFileNames(targetPost.get().getContent(),"postImage"));
        List<Comment> targetComments = commentService.getAllCommentByPostId(targetPost.get().getId());
        for(Comment comment : targetComments){
            commentService.deleteOneCommentProcess(comment.getId());
        }
        postRepository.deleteById(targetPost.get().getId());
        return true;
    }
    @Transactional
    public PostResponse updateTotalCountProcess(Long id,String ipAddress){
        Optional<Post> targetPost = postRepository.findById(id);
        if (targetPost.isEmpty()){
            throw new PostNotFoundException(ErrorCode.POST_NOT_FOUND);
        }
        boolean isExist = redisService.fetchCountByRedisCacheProcess(id,ipAddress);
        if (!isExist){
            targetPost.get().setTotalCount(targetPost.get().getTotalCount() + 1);
        }
        return new PostResponse(postRepository.save(targetPost.get()),targetPost.get().getMember().getName());
    }

    @Transactional(readOnly = true)
    public PostResponse getFetchOnePost(Long id){
        Optional<Post> targetPost = postRepository.findById(id);
        if (targetPost.isEmpty()){
            throw new PostNotFoundException(ErrorCode.POST_NOT_FOUND);
        }
        return new PostResponse(targetPost.get(),targetPost.get().getMember().getName());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findByUserIdProcess(Long memberId){
        List<Post> userPostsList = postRepository.findByMemberId(memberId);
        return fetchPosts(userPostsList);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findAllPostsProcess(String type , String keyword){
        List<Post> allPostsList = switch (type) {
            case "all" -> postRepository.findAll();
            case "date" -> postRepository.findAllByOrderByCreatedDateTimeDesc();
            case "count" -> postRepository.findAllByOrderByTotalCountDesc();
            case "keyword" -> postRepository.findByTitleContainingAndContentContaining(keyword,keyword);
            default -> null;
        };
        if (allPostsList == null){
            throw new PostNotFoundException(ErrorCode.POST_NOT_FOUND);
        }
        return fetchPosts(allPostsList);
    }
    private List<PostResponse> fetchPosts(List<Post> allPostsList) {
        List<PostResponse> fetchPostsList = new ArrayList<>();
        for(Post post : allPostsList){
            fetchPostsList.add(
                    new PostResponse(post,post.getMember().getName())
            );
        }
        return fetchPostsList;
    }

    private String fetchImagePaths(String content){
        return content.replaceAll("/upload/temp/", "/upload/postImage/");
    }
    private List<String> extractImageFileNames(String content,String ext) {
        List<String> fileNames = new ArrayList<>();
        String patternString = String.format("<img\\s+src=\"/upload/%s/([^\"]+)\"", ext);
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            fileNames.add(matcher.group(1));
        }
        return fileNames;
    }

}
