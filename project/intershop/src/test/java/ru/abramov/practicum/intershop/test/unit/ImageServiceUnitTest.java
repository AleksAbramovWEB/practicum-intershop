package ru.abramov.practicum.intershop.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.abramov.practicum.intershop.service.ImageService;
import ru.abramov.practicum.intershop.service.impl.ImageServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = ImageServiceUnitTest.Config.class)
class ImageServiceUnitTest {

    @Autowired
    private Environment environment;

    @Autowired
    private FilePart filePart;

    @Autowired
    private ImageService imageService;

    private static final String UPLOAD_DIR = System.getenv("TEST_UPLOAD_IMAGE_DIR");

    @BeforeEach
    void setUp() throws Exception {
        reset(filePart);

        when(environment.getProperty("upload.image.dir")).thenReturn(UPLOAD_DIR);
        Files.createDirectories(Path.of(UPLOAD_DIR));
    }

    @Test
    void save_ShouldSaveFileToCorrectPath_AndReturnFilename() {
        String fileName = "test.png";
        Path expectedPath = Path.of(UPLOAD_DIR).resolve(fileName);

        when(filePart.filename()).thenReturn(fileName);
        when(filePart.transferTo(expectedPath)).thenReturn(Mono.empty());

        StepVerifier.create(imageService.save(filePart))
                .expectNext("/" + fileName)
                .verifyComplete();

        verify(filePart, times(1)).transferTo(expectedPath);
    }

    @Configuration
    static class Config {
        @Bean
        public FilePart filePart() {
            return Mockito.mock(FilePart.class);
        }

        @Bean
        public Environment environment() {
            return Mockito.mock(Environment.class);
        }

        @Bean
        public ImageService imageService(Environment environment) {
            return new ImageServiceImpl(environment);
        }
    }
}
