package rs.ac.uns.ftn.svtvezbe06.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public interface IndexingService {

    String indexDocument(MultipartFile documentFile);
}
