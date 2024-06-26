package rs.ac.uns.ftn.svtvezbe06.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.svtvezbe06.model.dto.SearchQueryDTO;
import rs.ac.uns.ftn.svtvezbe06.model.entity.DummyIndex;
import rs.ac.uns.ftn.svtvezbe06.service.SearchService;

@RestController
@RequestMapping("api/search") // route that search files metadata
@RequiredArgsConstructor
public class SearchController {

    @Autowired
    private final SearchService searchService;

    @PostMapping("/simple")
    public Page<DummyIndex> simpleSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                         Pageable pageable) {
        return searchService.simpleSearch(simpleSearchQuery.keywords(), pageable);
    }

    @PostMapping("/advanced")
    public Page<DummyIndex> advancedSearch(@RequestBody SearchQueryDTO advancedSearchQuery,
                                           Pageable pageable) {
        return searchService.advancedSearch(advancedSearchQuery.keywords(), pageable);
    }
}
