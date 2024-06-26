package rs.ac.uns.ftn.svtvezbe06.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svtvezbe06.model.dto.DummyDocumentFileDTO;
import rs.ac.uns.ftn.svtvezbe06.model.dto.DummyDocumentFileResponseDTO;
import rs.ac.uns.ftn.svtvezbe06.service.IndexingService;

@RestController
@RequestMapping("api/index") // route that indexes file
@RequiredArgsConstructor
public class IndexController {

    @Autowired
    private final IndexingService indexingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DummyDocumentFileResponseDTO addDocumentFile(
        @ModelAttribute DummyDocumentFileDTO documentFile) {
        System.out.println("Usli u indeksiranje fajla: " + documentFile.toString());
        try {
            var serverFilename = indexingService.indexDocument(documentFile.file());
            return new DummyDocumentFileResponseDTO(serverFilename);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("File processing failed", e);
        }
    }
}
