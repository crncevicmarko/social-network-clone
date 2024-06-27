package rs.ac.uns.ftn.svtvezbe06.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtvezbe06.model.entity.GroupIndex;

import java.util.List;

@Service
public interface SearchGroupAndPostService {

    Page<GroupIndex> simpleSearch(List<String> keywords, Pageable pageable, boolean isGroup);

    Page<GroupIndex> advancedSearch(List<String> expression, Pageable pageable, boolean isGroup);
}
