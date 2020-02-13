package com.mycompany.myapp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link Entity1SearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class Entity1SearchRepositoryMockConfiguration {

    @MockBean
    private Entity1SearchRepository mockEntity1SearchRepository;

}
