package ru.abramov.practicum.intershop.service.impl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.abramov.practicum.intershop.service.ImageService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Environment environment;
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/webp");

    @SneakyThrows
    @Override
    public Optional<String> save(MultipartFile imageFile) {

        if (imageFile == null || imageFile.isEmpty()) {
            return Optional.empty();
        }

        String mimeType = Files.probeContentType(
                Path.of(
                        Objects.requireNonNull(
                                imageFile.getOriginalFilename()
                        )
                )
        );

        if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
            throw new IllegalArgumentException("Unsupported image type: " + mimeType);
        }

        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

        Path uploadPath = Paths.get(
                Objects.requireNonNull(
                        environment.getProperty("upload.image.dir")
                )
        );

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path imagePath = uploadPath.resolve(fileName);

        Files.copy(
                imageFile.getInputStream(),
                imagePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        return Optional.of("/" + fileName);
    }

}
