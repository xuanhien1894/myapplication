package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Entity2;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Entity2} entity.
 */
public interface Entity2SearchRepository extends ElasticsearchRepository<Entity2, Long> {
}
