package ru.abramov.practicum.intershop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.intershop.service.ImageService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Environment environment;

    public Mono<String> save(FilePart filePart) {
        String uploadDir = Objects.requireNonNull(environment.getProperty("upload.image.dir"));

        Path path = Paths.get(uploadDir + "/" + filePart.filename());
        return filePart.transferTo(path)
                .thenReturn("/" + filePart.filename());
    }
}
