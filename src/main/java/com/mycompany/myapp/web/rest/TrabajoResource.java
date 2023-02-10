package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Trabajo;
import com.mycompany.myapp.repository.TrabajoRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Trabajo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TrabajoResource {

    private final Logger log = LoggerFactory.getLogger(TrabajoResource.class);

    private static final String ENTITY_NAME = "trabajo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrabajoRepository trabajoRepository;

    public TrabajoResource(TrabajoRepository trabajoRepository) {
        this.trabajoRepository = trabajoRepository;
    }

    /**
     * {@code POST  /trabajos} : Create a new trabajo.
     *
     * @param trabajo the trabajo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trabajo, or with status {@code 400 (Bad Request)} if the trabajo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trabajos")
    public ResponseEntity<Trabajo> createTrabajo(@RequestBody Trabajo trabajo) throws URISyntaxException {
        log.debug("REST request to save Trabajo : {}", trabajo);
        if (trabajo.getId() != null) {
            throw new BadRequestAlertException("A new trabajo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Trabajo result = trabajoRepository.save(trabajo);
        return ResponseEntity
            .created(new URI("/api/trabajos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trabajos/:id} : Updates an existing trabajo.
     *
     * @param id the id of the trabajo to save.
     * @param trabajo the trabajo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trabajo,
     * or with status {@code 400 (Bad Request)} if the trabajo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trabajo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trabajos/{id}")
    public ResponseEntity<Trabajo> updateTrabajo(@PathVariable(value = "id", required = false) final Long id, @RequestBody Trabajo trabajo)
        throws URISyntaxException {
        log.debug("REST request to update Trabajo : {}, {}", id, trabajo);
        if (trabajo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trabajo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trabajoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Trabajo result = trabajoRepository.save(trabajo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trabajo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trabajos/:id} : Partial updates given fields of an existing trabajo, field will ignore if it is null
     *
     * @param id the id of the trabajo to save.
     * @param trabajo the trabajo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trabajo,
     * or with status {@code 400 (Bad Request)} if the trabajo is not valid,
     * or with status {@code 404 (Not Found)} if the trabajo is not found,
     * or with status {@code 500 (Internal Server Error)} if the trabajo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trabajos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Trabajo> partialUpdateTrabajo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Trabajo trabajo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Trabajo partially : {}, {}", id, trabajo);
        if (trabajo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trabajo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trabajoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Trabajo> result = trabajoRepository
            .findById(trabajo.getId())
            .map(existingTrabajo -> {
                if (trabajo.getTituloTrabajo() != null) {
                    existingTrabajo.setTituloTrabajo(trabajo.getTituloTrabajo());
                }
                if (trabajo.getSalarioMin() != null) {
                    existingTrabajo.setSalarioMin(trabajo.getSalarioMin());
                }
                if (trabajo.getSalarioMax() != null) {
                    existingTrabajo.setSalarioMax(trabajo.getSalarioMax());
                }

                return existingTrabajo;
            })
            .map(trabajoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trabajo.getId().toString())
        );
    }

    /**
     * {@code GET  /trabajos} : get all the trabajos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trabajos in body.
     */
    @GetMapping("/trabajos")
    public ResponseEntity<List<Trabajo>> getAllTrabajos(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Trabajos");
        Page<Trabajo> page;
        if (eagerload) {
            page = trabajoRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = trabajoRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trabajos/:id} : get the "id" trabajo.
     *
     * @param id the id of the trabajo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trabajo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trabajos/{id}")
    public ResponseEntity<Trabajo> getTrabajo(@PathVariable Long id) {
        log.debug("REST request to get Trabajo : {}", id);
        Optional<Trabajo> trabajo = trabajoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(trabajo);
    }

    /**
     * {@code DELETE  /trabajos/:id} : delete the "id" trabajo.
     *
     * @param id the id of the trabajo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trabajos/{id}")
    public ResponseEntity<Void> deleteTrabajo(@PathVariable Long id) {
        log.debug("REST request to delete Trabajo : {}", id);
        trabajoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
