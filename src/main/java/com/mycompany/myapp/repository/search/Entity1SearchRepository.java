package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Entity1;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Entity1} entity.
 */
public interface Entity1SearchRepository extends ElasticsearchRepository<Entity1, Long> {
}
