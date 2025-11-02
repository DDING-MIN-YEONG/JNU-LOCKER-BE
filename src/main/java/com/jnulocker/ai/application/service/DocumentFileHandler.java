package com.jnulocker.ai.application.service;

import com.jnulocker.ai.exception.DocumentFileException;
import com.jnulocker.ai.exception.DocumentParseFailedException;
import com.jnulocker.ai.exception.EmptyFileException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class DocumentFileHandler {

    private static final int DEFAULT_CHUNK_SIZE = 800;
    private static final int DEFAULT_OVERLAP_SIZE = 200;
    private static final int MIN_CHUNK_LENGTH = 350;
    private static final int MIN_CHUNK_LENGTH_TO_EMBED = 5;
    private static final boolean KEEP_SEPARATOR = true;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024L; // 10MB
    private static final List<String> SUPPORTED_FILE_TYPES = List.of("PDF", "TXT");

    private final TokenTextSplitter textSplitter =
            new TokenTextSplitter(
                    DEFAULT_CHUNK_SIZE,
                    MIN_CHUNK_LENGTH,
                    MIN_CHUNK_LENGTH_TO_EMBED,
                    DEFAULT_OVERLAP_SIZE,
                    KEEP_SEPARATOR);

    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw EmptyFileException.EXCEPTION;
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw DocumentFileException.FILE_SIZE_EXCEEDED;
        }

        String fileType = getFileType(file);
        if (!SUPPORTED_FILE_TYPES.contains(fileType)) {
            throw DocumentFileException.UNSUPPORTED_FILE_TYPE;
        }
    }

    public String getFileType(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw DocumentFileException.UNSUPPORTED_FILE_TYPE;
        }

        String extension =
                originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toUpperCase();

        return switch (extension) {
            case "PDF" -> "PDF";
            case "TXT" -> "TXT";
            default -> throw DocumentFileException.UNSUPPORTED_FILE_TYPE;
        };
    }

    public String generateFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String fileType = getFileType(file);
        String fileNameWithoutExt = removeFileExtension(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return fileNameWithoutExt + "---" + uuid + "." + fileType.toLowerCase();
    }

    private String removeFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return fileName;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public List<Document> parsePdf(MultipartFile file) throws IOException {
        Resource resource = new ByteArrayResource(file.getBytes());
        PdfDocumentReaderConfig config =
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageBottomMargin(0)
                        .withPageExtractedTextFormatter(
                                PdfDocumentReaderConfig.builder()
                                        .build()
                                        .pageExtractedTextFormatter)
                        .build();

        PagePdfDocumentReader reader = new PagePdfDocumentReader(resource, config);
        List<Document> documents = reader.get();
        return textSplitter.split(documents);
    }

    public List<Document> parseText(MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        Document document = new Document(content);
        return textSplitter.split(document);
    }

    public List<Document> parseDocument(MultipartFile file) {
        try {
            String fileType = getFileType(file);
            return switch (fileType) {
                case "PDF" -> parsePdf(file);
                case "TXT" -> parseText(file);
                default -> throw DocumentFileException.UNSUPPORTED_FILE_TYPE;
            };
        } catch (IOException e) {
            throw DocumentParseFailedException.EXCEPTION;
        }
    }
}
