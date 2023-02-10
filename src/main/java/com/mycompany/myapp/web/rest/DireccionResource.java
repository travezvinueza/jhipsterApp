package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Direccion;
import com.mycompany.myapp.repository.DireccionRepository;
import com.mycompany.myapp.service.DireccionService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Direccion}.
 */
@RestController
@RequestMapping("/api")
public class DireccionResource {

    private final Logger log = LoggerFactory.getLogger(DireccionResource.class);

    private static final String ENTITY_NAME = "direccion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DireccionService direccionService;

    private final DireccionRepository direccionRepository;

    public DireccionResource(DireccionService direccionService, DireccionRepository direccionRepository) {
        this.direccionService = direccionService;
        this.direccionRepository = direccionRepository;
    }

    /**
     * {@code POST  /direccions} : Create a new direccion.
     *
     * @param direccion the direccion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new direccion, or with status {@code 400 (Bad Request)} if the direccion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/direccions")
    public ResponseEntity<Direccion> createDireccion(@RequestBody Direccion direccion) throws URISyntaxException {
        log.debug("REST request to save Direccion : {}", direccion);
        if (direccion.getId() != null) {
            throw new BadRequestAlertException("A new direccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Direccion result = direccionService.save(direccion);
        return ResponseEntity
            .created(new URI("/api/direccions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /direccions/:id} : Updates an existing direccion.
     *
     * @param id the id of the direccion to save.
     * @param direccion the direccion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated direccion,
     * or with status {@code 400 (Bad Request)} if the direccion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the direccion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/direccions/{id}")
    public ResponseEntity<Direccion> updateDireccion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Direccion direccion
    ) throws URISyntaxException {
        log.debug("REST request to update Direccion : {}, {}", id, direccion);
        if (direccion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, direccion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!direccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Direccion result = direccionService.update(direccion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, direccion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /direccions/:id} : Partial updates given fields of an existing direccion, field will ignore if it is null
     *
     * @param id the id of the direccion to save.
     * @param direccion the direccion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated direccion,
     * or with status {@code 400 (Bad Request)} if the direccion is not valid,
     * or with status {@code 404 (Not Found)} if the direccion is not found,
     * or with status {@code 500 (Internal Server Error)} if the direccion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/direccions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Direccion> partialUpdateDireccion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Direccion direccion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Direccion partially : {}, {}", id, direccion);
        if (direccion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, direccion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!direccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Direccion> result = direccionService.partialUpdate(direccion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, direccion.getId().toString())
        );
    }

    /**
     * {@code GET  /direccions} : get all the direccions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of direccions in body.
     */
    @GetMapping("/direccions")
    public List<Direccion> getAllDireccions() {
        log.debug("REST request to get all Direccions");
        return direccionService.findAll();
    }

    /**
     * {@code GET  /direccions/:id} : get the "id" direccion.
     *
     * @param id the id of the direccion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the direccion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/direccions/{id}")
    public ResponseEntity<Direccion> getDireccion(@PathVariable Long id) {
        log.debug("REST request to get Direccion : {}", id);
        Optional<Direccion> direccion = direccionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(direccion);
    }

    /**
     * {@code DELETE  /direccions/:id} : delete the "id" direccion.
     *
     * @param id the id of the direccion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/direccions/{id}")
    public ResponseEntity<Void> deleteDireccion(@PathVariable Long id) {
        log.debug("REST request to delete Direccion : {}", id);
        direccionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
