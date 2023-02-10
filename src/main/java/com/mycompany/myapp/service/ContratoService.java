package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Contrato;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Contrato}.
 */
public interface ContratoService {
    /**
     * Save a contrato.
     *
     * @param contrato the entity to save.
     * @return the persisted entity.
     */
    Contrato save(Contrato contrato);

    /**
     * Updates a contrato.
     *
     * @param contrato the entity to update.
     * @return the persisted entity.
     */
    Contrato update(Contrato contrato);

    /**
     * Partially updates a contrato.
     *
     * @param contrato the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Contrato> partialUpdate(Contrato contrato);

    /**
     * Get all the contratoes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Contrato> findAll(Pageable pageable);

    /**
     * Get the "id" contrato.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Contrato> findOne(Long id);

    /**
     * Delete the "id" contrato.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
