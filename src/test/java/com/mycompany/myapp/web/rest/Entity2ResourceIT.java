package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AladinApp;
import com.mycompany.myapp.domain.Entity2;
import com.mycompany.myapp.repository.Entity2Repository;
import com.mycompany.myapp.repository.search.Entity2SearchRepository;
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
 * Integration tests for the {@link Entity2Resource} REST controller.
 */
@SpringBootTest(classes = AladinApp.class)
public class Entity2ResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    @Autowired
    private Entity2Repository entity2Repository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.Entity2SearchRepositoryMockConfiguration
     */
    @Autowired
    private Entity2SearchRepository mockEntity2SearchRepository;

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

    private MockMvc restEntity2MockMvc;

    private Entity2 entity2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Entity2Resource entity2Resource = new Entity2Resource(entity2Repository, mockEntity2SearchRepository);
        this.restEntity2MockMvc = MockMvcBuilders.standaloneSetup(entity2Resource)
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
    public static Entity2 createEntity(EntityManager em) {
        Entity2 entity2 = new Entity2()
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE);
        return entity2;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entity2 createUpdatedEntity(EntityManager em) {
        Entity2 entity2 = new Entity2()
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE);
        return entity2;
    }

    @BeforeEach
    public void initTest() {
        entity2 = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntity2() throws Exception {
        int databaseSizeBeforeCreate = entity2Repository.findAll().size();

        // Create the Entity2
        restEntity2MockMvc.perform(post("/api/entity-2-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entity2)))
            .andExpect(status().isCreated());

        // Validate the Entity2 in the database
        List<Entity2> entity2List = entity2Repository.findAll();
        assertThat(entity2List).hasSize(databaseSizeBeforeCreate + 1);
        Entity2 testEntity2 = entity2List.get(entity2List.size() - 1);
        assertThat(testEntity2.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEntity2.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the Entity2 in Elasticsearch
        verify(mockEntity2SearchRepository, times(1)).save(testEntity2);
    }

    @Test
    @Transactional
    public void createEntity2WithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entity2Repository.findAll().size();

        // Create the Entity2 with an existing ID
        entity2.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntity2MockMvc.perform(post("/api/entity-2-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entity2)))
            .andExpect(status().isBadRequest());

        // Validate the Entity2 in the database
        List<Entity2> entity2List = entity2Repository.findAll();
        assertThat(entity2List).hasSize(databaseSizeBeforeCreate);

        // Validate the Entity2 in Elasticsearch
        verify(mockEntity2SearchRepository, times(0)).save(entity2);
    }


    @Test
    @Transactional
    public void getAllEntity2S() throws Exception {
        // Initialize the database
        entity2Repository.saveAndFlush(entity2);

        // Get all the entity2List
        restEntity2MockMvc.perform(get("/api/entity-2-s?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entity2.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }
    
    @Test
    @Transactional
    public void getEntity2() throws Exception {
        // Initialize the database
        entity2Repository.saveAndFlush(entity2);

        // Get the entity2
        restEntity2MockMvc.perform(get("/api/entity-2-s/{id}", entity2.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entity2.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE));
    }

    @Test
    @Transactional
    public void getNonExistingEntity2() throws Exception {
        // Get the entity2
        restEntity2MockMvc.perform(get("/api/entity-2-s/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntity2() throws Exception {
        // Initialize the database
        entity2Repository.saveAndFlush(entity2);

        int databaseSizeBeforeUpdate = entity2Repository.findAll().size();

        // Update the entity2
        Entity2 updatedEntity2 = entity2Repository.findById(entity2.getId()).get();
        // Disconnect from session so that the updates on updatedEntity2 are not directly saved in db
        em.detach(updatedEntity2);
        updatedEntity2
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE);

        restEntity2MockMvc.perform(put("/api/entity-2-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntity2)))
            .andExpect(status().isOk());

        // Validate the Entity2 in the database
        List<Entity2> entity2List = entity2Repository.findAll();
        assertThat(entity2List).hasSize(databaseSizeBeforeUpdate);
        Entity2 testEntity2 = entity2List.get(entity2List.size() - 1);
        assertThat(testEntity2.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEntity2.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the Entity2 in Elasticsearch
        verify(mockEntity2SearchRepository, times(1)).save(testEntity2);
    }

    @Test
    @Transactional
    public void updateNonExistingEntity2() throws Exception {
        int databaseSizeBeforeUpdate = entity2Repository.findAll().size();

        // Create the Entity2

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntity2MockMvc.perform(put("/api/entity-2-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entity2)))
            .andExpect(status().isBadRequest());

        // Validate the Entity2 in the database
        List<Entity2> entity2List = entity2Repository.findAll();
        assertThat(entity2List).hasSize(databaseSizeBeforeUpdate);

        // Validate the Entity2 in Elasticsearch
        verify(mockEntity2SearchRepository, times(0)).save(entity2);
    }

    @Test
    @Transactional
    public void deleteEntity2() throws Exception {
        // Initialize the database
        entity2Repository.saveAndFlush(entity2);

        int databaseSizeBeforeDelete = entity2Repository.findAll().size();

        // Delete the entity2
        restEntity2MockMvc.perform(delete("/api/entity-2-s/{id}", entity2.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Entity2> entity2List = entity2Repository.findAll();
        assertThat(entity2List).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Entity2 in Elasticsearch
        verify(mockEntity2SearchRepository, times(1)).deleteById(entity2.getId());
    }

    @Test
    @Transactional
    public void searchEntity2() throws Exception {
        // Initialize the database
        entity2Repository.saveAndFlush(entity2);
        when(mockEntity2SearchRepository.search(queryStringQuery("id:" + entity2.getId())))
            .thenReturn(Collections.singletonList(entity2));
        // Search the entity2
        restEntity2MockMvc.perform(get("/api/_search/entity-2-s?query=id:" + entity2.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entity2.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }
}
