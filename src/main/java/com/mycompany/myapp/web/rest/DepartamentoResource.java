package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Departamento;
import com.mycompany.myapp.repository.DepartamentoRepository;
import com.mycompany.myapp.service.DepartamentoService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Departamento}.
 */
@RestController
@RequestMapping("/api")
public class DepartamentoResource {

    private final Logger log = LoggerFactory.getLogger(DepartamentoResource.class);

    private static final String ENTITY_NAME = "departamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartamentoService departamentoService;

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoResource(DepartamentoService departamentoService, DepartamentoRepository departamentoRepository) {
        this.departamentoService = departamentoService;
        this.departamentoRepository = departamentoRepository;
    }

    /**
     * {@code POST  /departamentos} : Create a new departamento.
     *
     * @param departamento the departamento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departamento, or with status {@code 400 (Bad Request)} if the departamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/departamentos")
    public ResponseEntity<Departamento> createDepartamento(@Valid @RequestBody Departamento departamento) throws URISyntaxException {
        log.debug("REST request to save Departamento : {}", departamento);
        if (departamento.getId() != null) {
            throw new BadRequestAlertException("A new departamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Departamento result = departamentoService.save(departamento);
        return ResponseEntity
            .created(new URI("/api/departamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /departamentos/:id} : Updates an existing departamento.
     *
     * @param id the id of the departamento to save.
     * @param departamento the departamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departamento,
     * or with status {@code 400 (Bad Request)} if the departamento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/departamentos/{id}")
    public ResponseEntity<Departamento> updateDepartamento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Departamento departamento
    ) throws URISyntaxException {
        log.debug("REST request to update Departamento : {}, {}", id, departamento);
        if (departamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Departamento result = departamentoService.update(departamento);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departamento.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /departamentos/:id} : Partial updates given fields of an existing departamento, field will ignore if it is null
     *
     * @param id the id of the departamento to save.
     * @param departamento the departamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departamento,
     * or with status {@code 400 (Bad Request)} if the departamento is not valid,
     * or with status {@code 404 (Not Found)} if the departamento is not found,
     * or with status {@code 500 (Internal Server Error)} if the departamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/departamentos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Departamento> partialUpdateDepartamento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Departamento departamento
    ) throws URISyntaxException {
        log.debug("REST request to partial update Departamento partially : {}, {}", id, departamento);
        if (departamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Departamento> result = departamentoService.partialUpdate(departamento);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departamento.getId().toString())
        );
    }

    /**
     * {@code GET  /departamentos} : get all the departamentos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departamentos in body.
     */
    @GetMapping("/departamentos")
    public List<Departamento> getAllDepartamentos() {
        log.debug("REST request to get all Departamentos");
        return departamentoService.findAll();
    }

    /**
     * {@code GET  /departamentos/:id} : get the "id" departamento.
     *
     * @param id the id of the departamento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departamento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/departamentos/{id}")
    public ResponseEntity<Departamento> getDepartamento(@PathVariable Long id) {
        log.debug("REST request to get Departamento : {}", id);
        Optional<Departamento> departamento = departamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departamento);
    }

    /**
     * {@code DELETE  /departamentos/:id} : delete the "id" departamento.
     *
     * @param id the id of the departamento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/departamentos/{id}")
    public ResponseEntity<Void> deleteDepartamento(@PathVariable Long id) {
        log.debug("REST request to delete Departamento : {}", id);
        departamentoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
