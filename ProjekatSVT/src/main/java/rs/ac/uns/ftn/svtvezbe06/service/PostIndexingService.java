package rs.ac.uns.ftn.svtvezbe06.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtvezbe06.model.entity.GroupIndex;
import rs.ac.uns.ftn.svtvezbe06.model.entity.PostIndex;

@Service
public interface PostIndexingService {

    String indexDocument(MultipartFile documentFile, PostIndex newIndex);

    void updateNumOfLikes(int numberOfLikes, int postId);
}
