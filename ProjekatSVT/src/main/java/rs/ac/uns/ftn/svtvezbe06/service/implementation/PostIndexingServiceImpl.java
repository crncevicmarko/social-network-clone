package rs.ac.uns.ftn.svtvezbe06.service.implementation;


import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtvezbe06.exceptionhandling.exception.LoadingException;
import rs.ac.uns.ftn.svtvezbe06.exceptionhandling.exception.NotFoundException;
import rs.ac.uns.ftn.svtvezbe06.exceptionhandling.exception.StorageException;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe06.model.entity.PostIndex;
import rs.ac.uns.ftn.svtvezbe06.repository.PostIndexRepository;
import rs.ac.uns.ftn.svtvezbe06.service.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostIndexingServiceImpl implements PostIndexingService {

    private final FileService fileService;

    private final LanguageDetector languageDetector;

    private final PostIndexRepository postIndexRepository;

    @Autowired
    private CommentService commentService;
//    private final ElasticsearchRestTemplate elasticsearchTemplate;

//    @Autowired
//    private final RestHighLevelClient client;
//
//    @Autowired
//    public PostIndexingServiceImpl(RestHighLevelClient client, PostIndexRepository postIndexRepository, LanguageDetector languageDetector, FileService fileService) {
//        this.client = client;
//        this.postIndexRepository = postIndexRepository;
//        this.languageDetector = languageDetector;
//        this.fileService = fileService;
//    }

    @Override
    @Transactional
    public String indexDocument(MultipartFile documentFile, PostIndex newIndex) {
        var documentContent = extractDocumentContent(documentFile);
        if (detectLanguage(documentContent).equals("SR")) {
            newIndex.setContentSr(documentContent);
        } else {
            newIndex.setContentEn(documentContent);
        }
        System.out.println("Index: "+newIndex.getContent()+ " Comment content: "+newIndex.getCommentsContent());
        var serverFilename = fileService.store(documentFile, UUID.randomUUID().toString()); // sacuva u minio bazi
        newIndex.setServerFilename(serverFilename);

        postIndexRepository.save(newIndex); // sacuva se u ES

        return serverFilename;
    }

    @Override
    public void updateNumOfLikes(int numberOfLikes, int postId) {
        System.out.println("Usli u UpdateNumOfLikes: "+"Broj lajkova za post je: "+numberOfLikes +" Id objave je: "+postId);
        var postIndex = postIndexRepository.findByPostId(String.valueOf(postId))
                .orElseThrow(() -> new NotFoundException("Post index not found"));

        postIndex.setNumberOfLikes(numberOfLikes);

        postIndexRepository.save(postIndex);
    }

    @Override
    public void updateCommentContent(int postId) {
        var content = getAllCommentsForPost(postId);
        System.out.println("Usli u UpdateCommentContent "+ "Comment Content: "+ content +" Id objave je: "+postId);
        var postIndex = postIndexRepository.findByPostId(String.valueOf(postId))
                .orElseThrow(() -> new NotFoundException("Post index not found"));

        postIndex.setCommentsContent(content);

        postIndexRepository.save(postIndex);

    }

    @Override
    public void updateNumberOfComments(int postId) {
        var numberOfComments = getNumOfAllCommentsForPost(postId);
        System.out.println("Usli u UpdateNumberOfComments "+ "Number of Comments: "+ numberOfComments +" Id objave je: "+postId);
        var postIndex = postIndexRepository.findByPostId(String.valueOf(postId))
                .orElseThrow(() -> new NotFoundException("Post index not found"));

        postIndex.setNumberOfComments(numberOfComments);

        postIndexRepository.save(postIndex);
    }

    public String getAllCommentsForPost(int postId){
        List<Comment> comments = commentService.findAllByPostId(postId);
        String commentContent = "";
        for(Comment comment: comments){
            commentContent += comment.getText()+" ";
        }
        return commentContent;
    }

    public int getNumOfAllCommentsForPost(int postId){
        List<Comment> comments = commentService.findAllByPostId(postId);
        return comments.size();
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