package rs.ac.uns.ftn.svtvezbe06.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtvezbe06.model.entity.GroupIndex;

@Service
public interface GroupIndexingService {

    String indexDocument(MultipartFile documentFile, GroupIndex newIndex);
    void updateNumOfPosts(int numOfPosts, int groupId);
    void updateAverageNumOfLikes(int numOfLikes, int groupId);
}
