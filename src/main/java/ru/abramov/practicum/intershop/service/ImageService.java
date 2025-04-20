package ru.abramov.practicum.intershop.service;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;


public interface ImageService {

    Mono<String> save(MultipartFile imageFile);
}
