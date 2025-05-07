package ru.abramov.practicum.intershop.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;


public interface ImageService {

    Mono<String> save(FilePart filePart);
}
