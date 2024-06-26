package rs.ac.uns.ftn.svtvezbe06.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.svtvezbe06.model.entity.DummyIndex;

@Repository
public interface DummyIndexRepository
    extends ElasticsearchRepository<DummyIndex, String> {
}
