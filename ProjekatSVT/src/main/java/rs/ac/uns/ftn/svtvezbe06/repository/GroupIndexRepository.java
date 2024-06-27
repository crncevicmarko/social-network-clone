package rs.ac.uns.ftn.svtvezbe06.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.svtvezbe06.model.entity.GroupIndex;

@Repository
public interface GroupIndexRepository
    extends ElasticsearchRepository<GroupIndex, String> {
}
