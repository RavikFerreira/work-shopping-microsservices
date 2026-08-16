package com.shopping.core.controller;

import com.shopping.core.service.StorageService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.NOT_FOUND;

@Controller("/api/images")
public class UploadImageController {
    private static final Set<String> ALLOWED_TYPES = Set.of("image/png", "image/jpeg", "image/webp", "image/gif");
    private static final long MAX_SIZE_BYTES = 5L * 1024 * 1024; // 5MB

    private final StorageService storageService;

    public UploadImageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Post(uri = "/upload", consumes = MediaType.MULTIPART_FORM_DATA, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Map<String, String>> upload(CompletedFileUpload file) {
        String contentType = file.getContentType().map(Object::toString).orElse("application/octet-stream");

        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new HttpStatusException(BAD_REQUEST, "Tipo de arquivo não permitido: " + contentType);
        }
        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new HttpStatusException(BAD_REQUEST, "Arquivo excede o tamanho máximo de 5MB");
        }

        try (InputStream in = file.getInputStream()) {
            StorageService.StoredFile stored = storageService.store(file.getFilename(), contentType, in);
            String url = "/api/images/" + stored.storedFileName();
            return HttpResponse.created(Map.of("url", url));
        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao salvar arquivo: " + file.getFilename(), e);
        }
    }

    // Serve o arquivo diretamente pelo nome gerado (UUID + extensão).
    @Get("/{filename}")
    public HttpResponse<StreamedFile> serve(String filename) {
        try {
            InputStream inputStream = storageService.load(filename);
            String guessedType = URLConnection.guessContentTypeFromName(filename);
            MediaType mediaType = guessedType != null ? MediaType.of(guessedType) : MediaType.APPLICATION_OCTET_STREAM_TYPE;
            return HttpResponse.ok(new StreamedFile(inputStream, mediaType));
        } catch (IOException e) {
            throw new HttpStatusException(NOT_FOUND, "Imagem não encontrada: " + filename);
        }
    }
}
