package rs.ac.uns.ftn.svtvezbe06.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtvezbe06.model.entity.PostIndex;

import java.util.List;

@Service
public interface SearchPostService {

    Page<PostIndex> simpleSearch(List<String> keywords, Pageable pageable);

    Page<PostIndex> advancedSearch(List<String> expression, Pageable pageable);

    Page<PostIndex> oneChoiceSearch(List<String> expression, Pageable pageable);
    Page<PostIndex> numOfLikesSearch(Integer lowerBound, Integer upperBound, Pageable pageable);
}
