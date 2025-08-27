package com.cocoh.movie.service;

import com.cocoh.movie.Entity.Director;
import com.cocoh.movie.dto.DirectorDto;
import com.cocoh.movie.repository.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final ImageService imageService;

    public Director saveDirector(DirectorDto dto, List<MultipartFile> file) {

        Director director = Director.builder()
                .name(dto.getName())
                .birthDay(dto.getBirthDay())
                .build();

        director.setDirector_image(null);

        directorRepository.save(director);

        String directoritem = "director";

        if (file != null && !file.isEmpty()) {
            List<String> savedImagePaths = imageService.uploadDirectorImage(director, file, directoritem);
            // 첫 번째 이미지 경로를 director_image에 저장
            director.setDirector_image(savedImagePaths.get(0));
            directorRepository.save(director);
        }

        Director saved = directorRepository.save(director);

        return saved;
    }

}
