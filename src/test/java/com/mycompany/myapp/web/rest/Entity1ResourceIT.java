package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AladinApp;
import com.mycompany.myapp.domain.Entity1;
import com.mycompany.myapp.repository.Entity1Repository;
import com.mycompany.myapp.repository.search.Entity1SearchRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link Entity1Resource} REST controller.
 */
@SpringBootTest(classes = AladinApp.class)
public class Entity1ResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    @Autowired
    private Entity1Repository entity1Repository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.Entity1SearchRepositoryMockConfiguration
     */
    @Autowired
    private Entity1SearchRepository mockEntity1SearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restEntity1MockMvc;

    private Entity1 entity1;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Entity1Resource entity1Resource = new Entity1Resource(entity1Repository, mockEntity1SearchRepository);
        this.restEntity1MockMvc = MockMvcBuilders.standaloneSetup(entity1Resource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entity1 createEntity(EntityManager em) {
        Entity1 entity1 = new Entity1()
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE);
        return entity1;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entity1 createUpdatedEntity(EntityManager em) {
        Entity1 entity1 = new Entity1()
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE);
        return entity1;
    }

    @BeforeEach
    public void initTest() {
        entity1 = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntity1() throws Exception {
        int databaseSizeBeforeCreate = entity1Repository.findAll().size();

        // Create the Entity1
        restEntity1MockMvc.perform(post("/api/entity-1-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entity1)))
            .andExpect(status().isCreated());

        // Validate the Entity1 in the database
        List<Entity1> entity1List = entity1Repository.findAll();
        assertThat(entity1List).hasSize(databaseSizeBeforeCreate + 1);
        Entity1 testEntity1 = entity1List.get(entity1List.size() - 1);
        assertThat(testEntity1.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEntity1.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the Entity1 in Elasticsearch
        verify(mockEntity1SearchRepository, times(1)).save(testEntity1);
    }

    @Test
    @Transactional
    public void createEntity1WithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entity1Repository.findAll().size();

        // Create the Entity1 with an existing ID
        entity1.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntity1MockMvc.perform(post("/api/entity-1-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entity1)))
            .andExpect(status().isBadRequest());

        // Validate the Entity1 in the database
        List<Entity1> entity1List = entity1Repository.findAll();
        assertThat(entity1List).hasSize(databaseSizeBeforeCreate);

        // Validate the Entity1 in Elasticsearch
        verify(mockEntity1SearchRepository, times(0)).save(entity1);
    }


    @Test
    @Transactional
    public void getAllEntity1S() throws Exception {
        // Initialize the database
        entity1Repository.saveAndFlush(entity1);

        // Get all the entity1List
        restEntity1MockMvc.perform(get("/api/entity-1-s?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entity1.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }
    
    @Test
    @Transactional
    public void getEntity1() throws Exception {
        // Initialize the database
        entity1Repository.saveAndFlush(entity1);

        // Get the entity1
        restEntity1MockMvc.perform(get("/api/entity-1-s/{id}", entity1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entity1.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE));
    }

    @Test
    @Transactional
    public void getNonExistingEntity1() throws Exception {
        // Get the entity1
        restEntity1MockMvc.perform(get("/api/entity-1-s/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntity1() throws Exception {
        // Initialize the database
        entity1Repository.saveAndFlush(entity1);

        int databaseSizeBeforeUpdate = entity1Repository.findAll().size();

        // Update the entity1
        Entity1 updatedEntity1 = entity1Repository.findById(entity1.getId()).get();
        // Disconnect from session so that the updates on updatedEntity1 are not directly saved in db
        em.detach(updatedEntity1);
        updatedEntity1
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE);

        restEntity1MockMvc.perform(put("/api/entity-1-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntity1)))
            .andExpect(status().isOk());

        // Validate the Entity1 in the database
        List<Entity1> entity1List = entity1Repository.findAll();
        assertThat(entity1List).hasSize(databaseSizeBeforeUpdate);
        Entity1 testEntity1 = entity1List.get(entity1List.size() - 1);
        assertThat(testEntity1.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEntity1.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the Entity1 in Elasticsearch
        verify(mockEntity1SearchRepository, times(1)).save(testEntity1);
    }

    @Test
    @Transactional
    public void updateNonExistingEntity1() throws Exception {
        int databaseSizeBeforeUpdate = entity1Repository.findAll().size();

        // Create the Entity1

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntity1MockMvc.perform(put("/api/entity-1-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entity1)))
            .andExpect(status().isBadRequest());

        // Validate the Entity1 in the database
        List<Entity1> entity1List = entity1Repository.findAll();
        assertThat(entity1List).hasSize(databaseSizeBeforeUpdate);

        // Validate the Entity1 in Elasticsearch
        verify(mockEntity1SearchRepository, times(0)).save(entity1);
    }

    @Test
    @Transactional
    public void deleteEntity1() throws Exception {
        // Initialize the database
        entity1Repository.saveAndFlush(entity1);

        int databaseSizeBeforeDelete = entity1Repository.findAll().size();

        // Delete the entity1
        restEntity1MockMvc.perform(delete("/api/entity-1-s/{id}", entity1.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Entity1> entity1List = entity1Repository.findAll();
        assertThat(entity1List).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Entity1 in Elasticsearch
        verify(mockEntity1SearchRepository, times(1)).deleteById(entity1.getId());
    }

    @Test
    @Transactional
    public void searchEntity1() throws Exception {
        // Initialize the database
        entity1Repository.saveAndFlush(entity1);
        when(mockEntity1SearchRepository.search(queryStringQuery("id:" + entity1.getId())))
            .thenReturn(Collections.singletonList(entity1));
        // Search the entity1
        restEntity1MockMvc.perform(get("/api/_search/entity-1-s?query=id:" + entity1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entity1.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }
}
