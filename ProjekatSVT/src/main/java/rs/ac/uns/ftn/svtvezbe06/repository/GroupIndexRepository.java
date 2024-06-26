package rs.ac.uns.ftn.svtvezbe06.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.svtvezbe06.model.entity.GroupIndex;
import rs.ac.uns.ftn.svtvezbe06.model.entity.PostIndex;

import java.util.Optional;

@Repository
public interface GroupIndexRepository extends ElasticsearchRepository<GroupIndex, String> {
    Optional<GroupIndex> findByGroupId(String groupId);
}
