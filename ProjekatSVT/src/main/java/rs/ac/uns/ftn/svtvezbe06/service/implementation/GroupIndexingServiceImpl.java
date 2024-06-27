package rs.ac.uns.ftn.svtvezbe06.service.implementation;


import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtvezbe06.exceptionhandling.exception.LoadingException;
import rs.ac.uns.ftn.svtvezbe06.exceptionhandling.exception.StorageException;
import rs.ac.uns.ftn.svtvezbe06.model.entity.DummyIndex;
import rs.ac.uns.ftn.svtvezbe06.model.entity.DummyTable;
import rs.ac.uns.ftn.svtvezbe06.model.entity.GroupIndex;
import rs.ac.uns.ftn.svtvezbe06.repository.DummyIndexRepository;
import rs.ac.uns.ftn.svtvezbe06.repository.DummyRepository;
import rs.ac.uns.ftn.svtvezbe06.repository.GroupIndexRepository;
import rs.ac.uns.ftn.svtvezbe06.service.FileService;
import rs.ac.uns.ftn.svtvezbe06.service.GroupIndexingService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupIndexingServiceImpl implements GroupIndexingService {

    private final FileService fileService;

    private final LanguageDetector languageDetector;

    private final GroupIndexRepository groupIndexRepository;

    @Override
    @Transactional
    public String indexDocument(MultipartFile documentFile, GroupIndex newIndex) {
        var documentContent = extractDocumentContent(documentFile);
        if (detectLanguage(documentContent).equals("SR")) {
            newIndex.setContentSr(documentContent);
        } else {
            newIndex.setContentEn(documentContent);
        }
        var serverFilename = fileService.store(documentFile, UUID.randomUUID().toString()); // sacuva u minio bazi
        newIndex.setServerFilename(serverFilename);

        groupIndexRepository.save(newIndex); // sacuva se u ES

        return serverFilename;
    }

    private String extractDocumentContent(MultipartFile multipartPdfFile) {
        String documentContent;
        try (var pdfFile = multipartPdfFile.getInputStream()) {
            var pdDocument = PDDocument.load(pdfFile);
            var textStripper = new PDFTextStripper();
            documentContent = textStripper.getText(pdDocument);
            pdDocument.close();
        } catch (IOException e) {
            throw new LoadingException("Error while trying to load PDF file content.");
        }

        return documentContent;
    }

    private String detectLanguage(String text) {
        var detectedLanguage = languageDetector.detect(text).getLanguage().toUpperCase();
        if (detectedLanguage.equals("HR")) {
            detectedLanguage = "SR";
        }

        return detectedLanguage;
    }

    private String detectMimeType(MultipartFile file) {
        var contentAnalyzer = new Tika();

        String trueMimeType;
        String specifiedMimeType;
        try {
            trueMimeType = contentAnalyzer.detect(file.getBytes());
            specifiedMimeType =
                Files.probeContentType(Path.of(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (IOException e) {
            throw new StorageException("Failed to detect mime type for file.");
        }

        if (!trueMimeType.equals(specifiedMimeType) &&
            !(trueMimeType.contains("zip") && specifiedMimeType.contains("zip"))) {
            throw new StorageException("True mime type is different from specified one, aborting.");
        }

        return trueMimeType;
    }
}