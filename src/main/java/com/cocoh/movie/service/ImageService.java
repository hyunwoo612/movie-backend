package com.cocoh.movie.service;

import com.cocoh.movie.Entity.Image;
import com.cocoh.movie.Entity.Movie;
import com.cocoh.movie.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

        public List<String> uploadImage(Movie movie, List<MultipartFile> images) {
            List<String> savedFileNames = new ArrayList<>();
            try {
                String uploadDir = "src/main/resources/static/images/";

                for (MultipartFile file : images) {
                    String fileName = saveImage(file, uploadDir);

                    Image image = new Image(movie, fileName);
                    imageRepository.save(image);

                    savedFileNames.add(fileName);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return savedFileNames;
        }

        private String saveImage(MultipartFile file, String uploadDir) throws IOException {
            // 파일 이름 생성
            String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + file.getOriginalFilename();
            // 실제 파일이 저장될 경로
            String filePath = uploadDir + fileName;
            // DB에 저장할 경로 문자열
            String dbFilePath = "/images/" + fileName;

            Path path = Paths.get(filePath); // Path 객체 생성
            Files.createDirectories(path.getParent()); // 디렉토리 생성
            Files.write(path, file.getBytes()); // 디렉토리에 파일 저장

            return dbFilePath;
        }

}
