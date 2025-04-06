package ru.abramov.practicum.intershop.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.multipart.MultipartFile;
import ru.abramov.practicum.intershop.service.ImageService;
import ru.abramov.practicum.intershop.service.impl.ImageServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = ImageServiceUnitTest.Config.class)
class ImageServiceUnitTest {

    @Autowired
    private Environment environment;

    @Autowired
    private MultipartFile imageFile;

    @Autowired
    private ImageService imageService;

    private static final String UPLOAD_DIR = System.getenv("TEST_UPLOAD_IMAGE_DIR");

    @BeforeEach
    void setUp() throws Exception {
        reset(imageFile);

        when(environment.getProperty("upload.image.dir")).thenReturn(UPLOAD_DIR);

        Files.createDirectories(Path.of(UPLOAD_DIR));
    }

    @Test
    void save_ShouldReturnEmptyOptional_WhenFileIsNull() {
        Optional<String> result = imageService.save(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void save_ShouldReturnEmptyOptional_WhenFileIsEmpty() {
        when(imageFile.isEmpty()).thenReturn(true);

        Optional<String> result = imageService.save(imageFile);
        assertTrue(result.isEmpty());

        verify(imageFile, times(1)).isEmpty();
    }

    @Test
    void save_ShouldThrowException_WhenMimeTypeIsNotAllowed() throws Exception {
        when(imageFile.getOriginalFilename()).thenReturn("file.txt");
        when(imageFile.getInputStream()).thenReturn(InputStream.nullInputStream());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            imageService.save(imageFile);
        });

        assertTrue(exception.getMessage().startsWith("Unsupported image type"));

        verify(imageFile, times(1)).getOriginalFilename();
    }

    @Test
    void save_ShouldSaveFileAndReturnPath_WhenFileIsValid() throws Exception {
        when(imageFile.getOriginalFilename()).thenReturn("test.jpg");
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageFile.getInputStream()).thenReturn(new ByteArrayInputStream("fake image data".getBytes()));

        Optional<String> result = imageService.save(imageFile);

        assertTrue(result.isPresent());
        Path savedFile = Path.of(UPLOAD_DIR, result.get());
        assertTrue(Files.exists(savedFile));

        Files.deleteIfExists(savedFile);
    }

    @Configuration
    static class Config {
        @Bean
        public MultipartFile imageFile() {
            return Mockito.mock(MultipartFile.class);
        }
        @Bean
        public Environment environment() {
            return Mockito.mock(Environment.class);
        }
        @Bean
        public ImageService postRepository(Environment environment) {
            return new ImageServiceImpl(environment);
        }
    }
}

