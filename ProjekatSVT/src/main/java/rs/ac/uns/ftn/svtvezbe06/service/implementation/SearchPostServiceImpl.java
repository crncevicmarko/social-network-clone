package rs.ac.uns.ftn.svtvezbe06.service.implementation;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtvezbe06.exceptionhandling.exception.MalformedQueryException;
import rs.ac.uns.ftn.svtvezbe06.model.entity.GroupIndex;
import rs.ac.uns.ftn.svtvezbe06.model.entity.PostIndex;
import rs.ac.uns.ftn.svtvezbe06.service.SearchPostService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchPostServiceImpl implements SearchPostService {

    private final ElasticsearchOperations elasticsearchTemplate;

    @Override
    public Page<PostIndex> simpleSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder =
            new NativeQueryBuilder().withQuery(buildSimpleSearchQuery(keywords))
                .withPageable(pageable);

        System.out.println("Query: "+searchQueryBuilder.getQuery().toString());

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostIndex> advancedSearch(List<String> expression, Pageable pageable) {
        System.out.println("Received expression: " + expression);
        System.out.println("Received expression size: " + expression.size());

        if (expression.size() != 3) {
            throw new MalformedQueryException("Search query malformed.");
        }

        String operation0 = expression.get(0);
        String operation1 = expression.get(1);
        String operation2 = expression.get(2);
        System.out.println("First in list: "+ operation0);
        System.out.println("First in list: "+ operation1);
        System.out.println("First in list: "+ operation2);
        expression.remove(1);
        var searchQueryBuilder =
            new NativeQueryBuilder().withQuery(buildAdvancedSearchQuery(expression, operation1))
                .withPageable(pageable);

        System.out.println("searchQueryBuilder: "+searchQueryBuilder.getQuery().toString());

        return runQuery(searchQueryBuilder.build());
//        return null;
    }

    @Override
    public Page<PostIndex> oneChoiceSearch(List<String> expression, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildOnChoiceSearchQuery(expression))
                        .withPageable(pageable);

        System.out.println("Query: "+searchQueryBuilder.getQuery().toString());

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostIndex> numOfLikesSearch(Integer lowerBound, Integer upperBound, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildNumOfLikesSearchQuery(lowerBound, upperBound))
                        .withPageable(pageable);

        System.out.println("Query: "+searchQueryBuilder.getQuery().toString());

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostIndex> numOfCommentsSearch(Integer lowerBound, Integer upperBound, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildNumOfCommentsSearchQuery(lowerBound, upperBound))
                        .withPageable(pageable);

        System.out.println("Query: "+searchQueryBuilder.getQuery().toString());

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostIndex> advanceddSearch(String content, String commentsContent, List<Integer> likeRangeList, List<Integer> commentRangeList, String operation, Pageable pageable) {
        List<HighlightField> requiredHighlights = Arrays.asList(
                new HighlightField("content"),
                new HighlightField("commentContent"),
                new HighlightField("content_sr"),
                new HighlightField("content_en")
        );

        Query searchQuery = buildAdvanceddSearchQuery(content, commentsContent, likeRangeList, commentRangeList, operation);
        System.out.println("Search query: " + searchQuery);

        try {
            NativeQuery queryBuilder = new NativeQueryBuilder().withQuery(searchQuery).withPageable(pageable).withHighlightQuery(new HighlightQuery(new Highlight(requiredHighlights), PostIndex.class)).build();
            System.out.println("Query builder: " + queryBuilder);
            SearchHits<PostIndex> searchHits = elasticsearchTemplate.search(queryBuilder, PostIndex.class);
            System.out.println("Hits: " + searchHits);
            List<PostIndex> results = new ArrayList<>();
            for (SearchHit<PostIndex> hit : searchHits.getSearchHits()) {
                PostIndex postIndex = hit.getContent();
                postIndex.setHighlights(hit.getHighlightFields());
                results.add(postIndex);
                System.out.println("Results: "+ postIndex.getHighlights().size());
            }


            return new PageImpl<>(results, pageable, searchHits.getTotalHits());

        } catch (Exception e) {
            System.out.println("Error: " + e);
            throw new RuntimeException("Error executing Elasticsearch search", e);
        }
    }

    private Query buildSimpleSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                // Term Query - simplest
                // Matches documents with exact term in "title" field
//            b.should(sb -> sb.term(m -> m.field("title").value(token)));

                // Terms Query
                // Matches documents with any of the specified terms in "title" field
//            var terms = new ArrayList<>(List.of("dummy1", "dummy2"));
//            var titleTerms = new TermsQueryField.Builder()
//                .value(terms.stream().map(FieldValue::of).toList())
//                .build();
//            b.should(sb -> sb.terms(m -> m.field("title").terms(titleTerms)));

                // Match Query - full-text search with fuzziness
                // Matches documents with fuzzy matching in "title" field
                b.should(sb -> sb.match(
                    m -> m.field("content").fuzziness(Fuzziness.ONE.asString()).query("*"+token+"*")));
                b.should(sb -> sb.match(
                        m -> m.field("commentContent").fuzziness(Fuzziness.ONE.asString()).query("*"+token+"*")));

                // Match Query - full-text search in other fields
                // Matches documents with full-text search in other fields
                b.should(sb -> sb.match(m -> m.field("content_sr").query(token)));
                b.should(sb -> sb.match(m -> m.field("content_sr").query(token)));
                b.should(sb -> sb.match(m -> m.field("content_en").query(token)));

                // Wildcard Query - unsafe
                // Matches documents with wildcard matching in "title" field
//            b.should(sb -> sb.wildcard(m -> m.field("title").value("*" + token + "*")));

                // Regexp Query - unsafe
                // Matches documents with regular expression matching in "title" field
//            b.should(sb -> sb.regexp(m -> m.field("title").value(".*" + token + ".*")));

                // Boosting Query - positive gives better score, negative lowers score
                // Matches documents with boosted relevance in "title" field
//            b.should(sb -> sb.boosting(bq -> bq.positive(m -> m.match(ma -> ma.field("title").query(token)))
//                                              .negative(m -> m.match(ma -> ma.field("description").query(token)))
//                                              .negativeBoost(0.5f)));

                // Match Phrase Query - useful for exact-phrase search
                // Matches documents with exact phrase match in "title" field
//            b.should(sb -> sb.matchPhrase(m -> m.field("title").query(token)));

                // Fuzzy Query - similar to Match Query with fuzziness, useful for spelling errors
                // Matches documents with fuzzy matching in "title" field
//            b.should(sb -> sb.match(
//                m -> m.field("title").fuzziness(Fuzziness.ONE.asString()).query(token)));

                // Range query - not applicable for dummy index, searches in the range from-to
            });
            return b;
        })))._toQuery();
    }

    private Query buildAdvancedSearchQuery(List<String> operands, String operation) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            var field1 = operands.get(0).split(":")[0];
            var value1 = operands.get(0).split(":")[1];
            var field2 = operands.get(1).split(":")[0];
            var value2 = operands.get(1).split(":")[1];

            System.out.println("field1:"+field1);
            System.out.println("value1:"+value1);
            System.out.println("field2:"+field2);
            System.out.println("value2:"+value2);

            switch (operation) {
                case "AND":
                    System.out.println("operation: "+operation);
                    b.must(sb -> sb.match(
                        m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
                    b.must(sb -> sb.match(m -> m.field(field2).query(value2)));
                    break;
                case "OR":
                    b.should(sb -> sb.match(
                        m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
                    b.should(sb -> sb.match(m -> m.field(field2).query(value2)));
                    break;
                case "NOT":
                    b.must(sb -> sb.match(
                        m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
                    b.mustNot(sb -> sb.match(m -> m.field(field2).query(value2)));
                    break;
            }

            return b;
        })))._toQuery();
    }

    private Query buildAdvanceddSearchQuery(String content, String commentsContent, List<Integer> likeRangeList, List<Integer> commentRangeList, String operation) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            if ("AND".equalsIgnoreCase(operation)) {
                if (content != null && !content.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("content").fuzziness(Fuzziness.ONE.asString()).query(content)))
                            .should(subShould -> subShould.matchPhrase(m -> m.field("content").query(content)))
                            .should(subShould -> subShould.match(m -> m.field("content_sr").fuzziness(Fuzziness.ONE.asString()).query(content)))
                            .should(subShould -> subShould.matchPhrase(m -> m.field("content_sr").query(content)))
                            .should(subShould -> subShould.match(m -> m.field("content_en").fuzziness(Fuzziness.ONE.asString()).query(content)))
                            .should(subShould -> subShould.matchPhrase(m -> m.field("content_en").query(content)))
                    ));
                }
                if (commentsContent != null && !commentsContent.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("commentContent").fuzziness(Fuzziness.ONE.asString()).query(commentsContent)))
                            .should(subShould -> subShould.matchPhrase(m -> m.field("commentContent").query(commentsContent)))
                    ));
                }
                if (likeRangeList != null && !likeRangeList.isEmpty()) {
                    for (int i = 0; i < likeRangeList.size(); i += 2) {
                        int lowerBound = likeRangeList.get(i);
                        int upperBound = likeRangeList.get(i + 1);
                        b.must(sb -> sb.range(r -> r.field("numberOfLikes").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
                if (commentRangeList != null && !commentRangeList.isEmpty()) {
                    for (int i = 0; i < commentRangeList.size(); i += 2) {
                        int lowerBound = commentRangeList.get(i);
                        int upperBound = commentRangeList.get(i + 1);
                        b.must(sb -> sb.range(r -> r.field("numberOfComments").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
            } else if ("OR".equalsIgnoreCase(operation)) {
                if (content != null && !content.isEmpty()) {
                    b.should(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("content").fuzziness(Fuzziness.ONE.asString()).query(content)))
                            .should(subShould -> subShould.matchPhrase(m -> m.field("content").query(content)))
                            .should(subShould -> subShould.match(m -> m.field("content_sr").fuzziness(Fuzziness.ONE.asString()).query(content)))
                            .should(subShould -> subShould.matchPhrase(m -> m.field("content_sr").query(content)))
                            .should(subShould -> subShould.match(m -> m.field("content_en").fuzziness(Fuzziness.ONE.asString()).query(content)))
                            .should(subShould -> subShould.matchPhrase(m -> m.field("content_en").query(content)))
                    ));
                }
                if (commentsContent != null && !commentsContent.isEmpty()) {
                    b.should(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("commentContent").fuzziness(Fuzziness.ONE.asString()).query(commentsContent)))
                            .should(subShould -> subShould.matchPhrase(m -> m.field("commentContent").query(commentsContent)))
                    ));
                }
                if (likeRangeList != null && !likeRangeList.isEmpty()) {
                    for (int i = 0; i < likeRangeList.size(); i += 2) {
                        int lowerBound = likeRangeList.get(i);
                        int upperBound = likeRangeList.get(i + 1);
                        b.should(sb -> sb.range(r -> r.field("numberOfLikes").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
                if (commentRangeList != null && !commentRangeList.isEmpty()) {
                    for (int i = 0; i < commentRangeList.size(); i += 2) {
                        int lowerBound = commentRangeList.get(i);
                        int upperBound = commentRangeList.get(i + 1);
                        b.should(sb -> sb.range(r -> r.field("numberOfComments").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
            }
            return b;
        })))._toQuery();
    }


    private Query buildOnChoiceSearchQuery(List<String> operands) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            var field1 = operands.get(0).split(":")[0];
            var value1 = operands.get(0).split(":")[1];

            System.out.println("field1:"+field1);
            System.out.println("value1:"+value1);

            switch (field1) {
                case "name", "description", "content_sr":
                    b.should(sb -> sb.match(
                            m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
                    break;
            }

            return b;
        })))._toQuery();
    }
    private Query buildNumOfLikesSearchQuery(Integer lowerBound, Integer upperBound) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            b.should(sb -> sb
                .range(r -> r
                    .field("numberOfLikes")
                    .gte(JsonData.of(lowerBound))
                    .lte(JsonData.of(upperBound))
                )
            );
            return b;
        })))._toQuery();
    }

    private Query buildNumOfCommentsSearchQuery(Integer lowerBound, Integer upperBound) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            b.should(sb -> sb
                    .range(r -> r
                            .field("numberOfComments")
                            .gte(JsonData.of(lowerBound))
                            .lte(JsonData.of(upperBound))
                    )
            );
            return b;
        })))._toQuery();
    }

    private Page<PostIndex> runQuery(NativeQuery searchQuery) {

        var searchHits = elasticsearchTemplate.search(searchQuery, PostIndex.class,
            IndexCoordinates.of("post_index"));

        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());

        return (Page<PostIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }
}
