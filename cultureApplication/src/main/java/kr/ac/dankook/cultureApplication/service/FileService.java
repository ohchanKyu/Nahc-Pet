package kr.ac.dankook.cultureApplication.service;

import jakarta.servlet.ServletContext;
import kr.ac.dankook.cultureApplication.entity.PlaceReview;
import kr.ac.dankook.cultureApplication.entity.PlaceReviewImage;
import kr.ac.dankook.cultureApplication.repository.PlaceReviewImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final PlaceReviewImageRepository imageRepository;
    private final ServletContext servletContext;

    public void deletePostImage(List<String> pathList) throws IOException {
        String postImagePath = servletContext.getRealPath("/upload/postImage/");
        for(String path : pathList){
            deleteFile(postImagePath + path);
        }
    }
    public void filterTempImageList(List<String> pathList) throws IOException {

        String tempPath = servletContext.getRealPath("/upload/temp/");
        File tempDir = new File(tempPath);
        File[] targetFiles = tempDir.listFiles();

        if (targetFiles != null){
            for(File file : targetFiles){
                if (!pathList.contains(file.getName())){
                    String savePath = servletContext.getRealPath("/upload/temp/") + file.getName();
                    deleteFile(savePath);
                }
            }
        }
    }
    public void transferTempToDirectory(){
        String tempPath = servletContext.getRealPath("/upload/temp/");
        String postImagePath = servletContext.getRealPath("/upload/postImage/");

        File tempDir = new File(tempPath);
        File[] targetFiles = tempDir.listFiles();
        if (targetFiles != null) {
            for(File targetFile : targetFiles){
                Path sourcePath = targetFile.toPath();
                Path targetPath = Paths.get(postImagePath,targetFile.getName());
                try {
                    Files.move(sourcePath,targetPath);
                } catch (IOException e) {
                    log.info("Move to PostImage Dir -{}",targetFile.getName());
                }
            }
        }
    }

    public String ckFileTempUpload(MultipartHttpServletRequest request){

        MultipartFile uploadFile = request.getFile("upload");
        assert uploadFile != null;

        String originalFileName = uploadFile.getOriginalFilename();
        assert originalFileName != null;
        String saveFileName = getFetchFileName(originalFileName);

        String realPath = servletContext.getRealPath("/upload/temp/");
        String savePath = realPath + saveFileName;
        uploadFile(savePath,uploadFile);
        log.info("Save ck upload File. originalFileName-{} , saveFileName-{}", originalFileName,saveFileName);
        // Spring boot resource relative path
        return request.getContextPath() + "/upload/temp/" + saveFileName;
    }

    @Transactional
    public void fileUpload(MultipartFile file, PlaceReview placeReview){

        String uploadPath = servletContext.getRealPath("/upload/reviewImage/");
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        String saveFileName = getFetchFileName(originalFileName);

        String savePath = uploadPath + saveFileName;
        imageRepository.save(
                PlaceReviewImage.builder()
                .placeReview(placeReview)
                .path(savePath)
                .build());
        log.info("Save File. originalFileName-{} , saveFileName-{}",originalFileName,saveFileName);
        uploadFile(savePath,file);
    }
    public void deleteFile(String path) throws IOException {
        Files.delete(Paths.get(path));
    }
    public ByteArrayResource getImageFile(String savePath) throws IOException {
        Path path = Paths.get(savePath);
        byte[] fileData = Files.readAllBytes(path);
        return new ByteArrayResource(fileData);
    }
    private String getFetchFileName(String originalFileName) {
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID() + ext;
    }
    private void uploadFile(String savePath, MultipartFile uploadFile) {
        File file = new File(savePath);
        try {
            uploadFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload the file", e);
        }
    }
}
