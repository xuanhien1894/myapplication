package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Entity1;
import com.mycompany.myapp.repository.Entity1Repository;
import com.mycompany.myapp.repository.search.Entity1SearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Entity1}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class Entity1Resource {

    private final Logger log = LoggerFactory.getLogger(Entity1Resource.class);

    private static final String ENTITY_NAME = "entity1";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Entity1Repository entity1Repository;

    private final Entity1SearchRepository entity1SearchRepository;

    public Entity1Resource(Entity1Repository entity1Repository, Entity1SearchRepository entity1SearchRepository) {
        this.entity1Repository = entity1Repository;
        this.entity1SearchRepository = entity1SearchRepository;
    }

    /**
     * {@code POST  /entity-1-s} : Create a new entity1.
     *
     * @param entity1 the entity1 to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entity1, or with status {@code 400 (Bad Request)} if the entity1 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entity-1-s")
    public ResponseEntity<Entity1> createEntity1(@RequestBody Entity1 entity1) throws URISyntaxException {
        log.debug("REST request to save Entity1 : {}", entity1);
        if (entity1.getId() != null) {
            throw new BadRequestAlertException("A new entity1 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Entity1 result = entity1Repository.save(entity1);
        entity1SearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/entity-1-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entity-1-s} : Updates an existing entity1.
     *
     * @param entity1 the entity1 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entity1,
     * or with status {@code 400 (Bad Request)} if the entity1 is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entity1 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entity-1-s")
    public ResponseEntity<Entity1> updateEntity1(@RequestBody Entity1 entity1) throws URISyntaxException {
        log.debug("REST request to update Entity1 : {}", entity1);
        if (entity1.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Entity1 result = entity1Repository.save(entity1);
        entity1SearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entity1.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /entity-1-s} : get all the entity1S.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entity1S in body.
     */
    @GetMapping("/entity-1-s")
    public List<Entity1> getAllEntity1S() {
        log.debug("REST request to get all Entity1S");
        return entity1Repository.findAll();
    }

    /**
     * {@code GET  /entity-1-s/:id} : get the "id" entity1.
     *
     * @param id the id of the entity1 to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entity1, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entity-1-s/{id}")
    public ResponseEntity<Entity1> getEntity1(@PathVariable Long id) {
        log.debug("REST request to get Entity1 : {}", id);
        Optional<Entity1> entity1 = entity1Repository.findById(id);
        return ResponseUtil.wrapOrNotFound(entity1);
    }

    /**
     * {@code DELETE  /entity-1-s/:id} : delete the "id" entity1.
     *
     * @param id the id of the entity1 to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entity-1-s/{id}")
    public ResponseEntity<Void> deleteEntity1(@PathVariable Long id) {
        log.debug("REST request to delete Entity1 : {}", id);
        entity1Repository.deleteById(id);
        entity1SearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/entity-1-s?query=:query} : search for the entity1 corresponding
     * to the query.
     *
     * @param query the query of the entity1 search.
     * @return the result of the search.
     */
    @GetMapping("/_search/entity-1-s")
    public List<Entity1> searchEntity1S(@RequestParam String query) {
        log.debug("REST request to search Entity1S for query {}", query);
        return StreamSupport
            .stream(entity1SearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
