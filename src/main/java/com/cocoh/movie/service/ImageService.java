package com.cocoh.movie.service;

import com.cocoh.movie.Entity.Director;
import com.cocoh.movie.Entity.Image;
import com.cocoh.movie.Entity.Movie;
import com.cocoh.movie.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;

        public List<String> uploadMovieImage(Movie movie, List<MultipartFile> images, String item) {
            List<String> savedFileNames = new ArrayList<>();
            try {
                String baseDir = "upload"; // 외부 저장 경로 (루트)
                String uploadDir = baseDir + "/images/" + item + "/";

                for (MultipartFile file : images) {
                    String fileName = saveImage(file, uploadDir, item);
                    Image image = new Image(movie, fileName);
                    imageRepository.save(image);
                    savedFileNames.add(fileName);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return savedFileNames;
        }

    public List<String> uploadDirectorImage(Director director, List<MultipartFile> images, String item) {
        List<String> savedFileNames = new ArrayList<>();
        try {
            String baseDir = "upload"; // 외부 저장 경로 (루트)
            String uploadDir = baseDir + "/images/" + item + "/";

            for (MultipartFile file : images) {
                String fileName = saveImage(file, uploadDir, item);
                Image image = new Image(director, fileName);
                imageRepository.save(image);
                savedFileNames.add(fileName);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return savedFileNames;
    }

    private String saveImage(MultipartFile file, String uploadDir, String item) throws IOException {
        // 고유 파일명 생성
        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + file.getOriginalFilename();
        String filePath = uploadDir + fileName;

        log.info("Saved image path: {}", Paths.get(uploadDir + fileName).toAbsolutePath());

        // 파일 시스템에 저장
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        // 브라우저에서 접근할 URL 경로
        String dbFilePath = "/images/" + item + "/" + fileName;
        log.info("Image saved: {}", dbFilePath);
        return dbFilePath;
    }
}
