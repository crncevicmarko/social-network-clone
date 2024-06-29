package rs.ac.uns.ftn.svtvezbe06.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtvezbe06.model.entity.GroupIndex;

import java.util.List;

@Service
public interface SearchGroupService {

    Page<GroupIndex> simpleSearch(List<String> keywords, Pageable pageable);

    Page<GroupIndex> advancedSearch(List<String> expression, Pageable pageable);

    Page<GroupIndex> oneChoiceSearch(List<String> expression, Pageable pageable);
    Page<GroupIndex> numOfPostsSearch(Integer lowerBound, Integer upperBound, Pageable pageable);
}
