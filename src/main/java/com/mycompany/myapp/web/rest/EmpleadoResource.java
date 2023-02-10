package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Empleado;
import com.mycompany.myapp.repository.EmpleadoRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Empleado}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EmpleadoResource {

    private final Logger log = LoggerFactory.getLogger(EmpleadoResource.class);

    private static final String ENTITY_NAME = "empleado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoResource(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    /**
     * {@code POST  /empleados} : Create a new empleado.
     *
     * @param empleado the empleado to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new empleado, or with status {@code 400 (Bad Request)} if the empleado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/empleados")
    public ResponseEntity<Empleado> createEmpleado(@RequestBody Empleado empleado) throws URISyntaxException {
        log.debug("REST request to save Empleado : {}", empleado);
        if (empleado.getId() != null) {
            throw new BadRequestAlertException("A new empleado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Empleado result = empleadoRepository.save(empleado);
        return ResponseEntity
            .created(new URI("/api/empleados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /empleados/:id} : Updates an existing empleado.
     *
     * @param id the id of the empleado to save.
     * @param empleado the empleado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleado,
     * or with status {@code 400 (Bad Request)} if the empleado is not valid,
     * or with status {@code 500 (Internal Server Error)} if the empleado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/empleados/{id}")
    public ResponseEntity<Empleado> updateEmpleado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Empleado empleado
    ) throws URISyntaxException {
        log.debug("REST request to update Empleado : {}, {}", id, empleado);
        if (empleado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empleadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Empleado result = empleadoRepository.save(empleado);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, empleado.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /empleados/:id} : Partial updates given fields of an existing empleado, field will ignore if it is null
     *
     * @param id the id of the empleado to save.
     * @param empleado the empleado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleado,
     * or with status {@code 400 (Bad Request)} if the empleado is not valid,
     * or with status {@code 404 (Not Found)} if the empleado is not found,
     * or with status {@code 500 (Internal Server Error)} if the empleado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/empleados/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Empleado> partialUpdateEmpleado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Empleado empleado
    ) throws URISyntaxException {
        log.debug("REST request to partial update Empleado partially : {}, {}", id, empleado);
        if (empleado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empleadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Empleado> result = empleadoRepository
            .findById(empleado.getId())
            .map(existingEmpleado -> {
                if (empleado.getNombres() != null) {
                    existingEmpleado.setNombres(empleado.getNombres());
                }
                if (empleado.getApellidos() != null) {
                    existingEmpleado.setApellidos(empleado.getApellidos());
                }
                if (empleado.getCorreo() != null) {
                    existingEmpleado.setCorreo(empleado.getCorreo());
                }
                if (empleado.getNroCelular() != null) {
                    existingEmpleado.setNroCelular(empleado.getNroCelular());
                }
                if (empleado.getFechaContrato() != null) {
                    existingEmpleado.setFechaContrato(empleado.getFechaContrato());
                }
                if (empleado.getSalario() != null) {
                    existingEmpleado.setSalario(empleado.getSalario());
                }
                if (empleado.getComisionPorcentaje() != null) {
                    existingEmpleado.setComisionPorcentaje(empleado.getComisionPorcentaje());
                }

                return existingEmpleado;
            })
            .map(empleadoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, empleado.getId().toString())
        );
    }

    /**
     * {@code GET  /empleados} : get all the empleados.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of empleados in body.
     */
    @GetMapping("/empleados")
    public ResponseEntity<List<Empleado>> getAllEmpleados(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Empleados");
        Page<Empleado> page = empleadoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /empleados/:id} : get the "id" empleado.
     *
     * @param id the id of the empleado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the empleado, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/empleados/{id}")
    public ResponseEntity<Empleado> getEmpleado(@PathVariable Long id) {
        log.debug("REST request to get Empleado : {}", id);
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(empleado);
    }

    /**
     * {@code DELETE  /empleados/:id} : delete the "id" empleado.
     *
     * @param id the id of the empleado to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable Long id) {
        log.debug("REST request to delete Empleado : {}", id);
        empleadoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
