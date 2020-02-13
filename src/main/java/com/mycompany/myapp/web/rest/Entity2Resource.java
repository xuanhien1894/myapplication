package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Entity2;
import com.mycompany.myapp.repository.Entity2Repository;
import com.mycompany.myapp.repository.search.Entity2SearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Entity2}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class Entity2Resource {

    private final Logger log = LoggerFactory.getLogger(Entity2Resource.class);

    private static final String ENTITY_NAME = "entity2";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Entity2Repository entity2Repository;

    private final Entity2SearchRepository entity2SearchRepository;

    public Entity2Resource(Entity2Repository entity2Repository, Entity2SearchRepository entity2SearchRepository) {
        this.entity2Repository = entity2Repository;
        this.entity2SearchRepository = entity2SearchRepository;
    }

    /**
     * {@code POST  /entity-2-s} : Create a new entity2.
     *
     * @param entity2 the entity2 to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entity2, or with status {@code 400 (Bad Request)} if the entity2 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entity-2-s")
    public ResponseEntity<Entity2> createEntity2(@RequestBody Entity2 entity2) throws URISyntaxException {
        log.debug("REST request to save Entity2 : {}", entity2);
        if (entity2.getId() != null) {
            throw new BadRequestAlertException("A new entity2 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Entity2 result = entity2Repository.save(entity2);
        entity2SearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/entity-2-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entity-2-s} : Updates an existing entity2.
     *
     * @param entity2 the entity2 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entity2,
     * or with status {@code 400 (Bad Request)} if the entity2 is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entity2 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entity-2-s")
    public ResponseEntity<Entity2> updateEntity2(@RequestBody Entity2 entity2) throws URISyntaxException {
        log.debug("REST request to update Entity2 : {}", entity2);
        if (entity2.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Entity2 result = entity2Repository.save(entity2);
        entity2SearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entity2.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /entity-2-s} : get all the entity2S.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entity2S in body.
     */
    @GetMapping("/entity-2-s")
    public List<Entity2> getAllEntity2S() {
        log.debug("REST request to get all Entity2S");
        return entity2Repository.findAll();
    }

    /**
     * {@code GET  /entity-2-s/:id} : get the "id" entity2.
     *
     * @param id the id of the entity2 to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entity2, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entity-2-s/{id}")
    public ResponseEntity<Entity2> getEntity2(@PathVariable Long id) {
        log.debug("REST request to get Entity2 : {}", id);
        Optional<Entity2> entity2 = entity2Repository.findById(id);
        return ResponseUtil.wrapOrNotFound(entity2);
    }

    /**
     * {@code DELETE  /entity-2-s/:id} : delete the "id" entity2.
     *
     * @param id the id of the entity2 to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entity-2-s/{id}")
    public ResponseEntity<Void> deleteEntity2(@PathVariable Long id) {
        log.debug("REST request to delete Entity2 : {}", id);
        entity2Repository.deleteById(id);
        entity2SearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/entity-2-s?query=:query} : search for the entity2 corresponding
     * to the query.
     *
     * @param query the query of the entity2 search.
     * @return the result of the search.
     */
    @GetMapping("/_search/entity-2-s")
    public List<Entity2> searchEntity2S(@RequestParam String query) {
        log.debug("REST request to search Entity2S for query {}", query);
        return StreamSupport
            .stream(entity2SearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
