package ru.abramov.practicum.intershop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.abramov.practicum.intershop.service.ImageService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Environment environment;
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/webp");

    @Override
    public Mono<String> save(MultipartFile imageFile) {
        return Mono.fromCallable(() -> {
            if (imageFile == null || imageFile.isEmpty()) {
                return null;
            }

            String mimeType = Files.probeContentType(
                    Path.of(Objects.requireNonNull(imageFile.getOriginalFilename()))
            );

            if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
                throw new IllegalArgumentException("Unsupported image type: " + mimeType);
            }

            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

            Path uploadPath = Paths.get(
                    Objects.requireNonNull(environment.getProperty("upload.image.dir"))
            );

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path imagePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

            return "/" + fileName;
        }).subscribeOn(Schedulers.boundedElastic())
          .flatMap(path -> path == null ? Mono.empty() : Mono.just(path));
    }
}
