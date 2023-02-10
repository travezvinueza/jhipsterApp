package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Departamento;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Departamento}.
 */
public interface DepartamentoService {
    /**
     * Save a departamento.
     *
     * @param departamento the entity to save.
     * @return the persisted entity.
     */
    Departamento save(Departamento departamento);

    /**
     * Updates a departamento.
     *
     * @param departamento the entity to update.
     * @return the persisted entity.
     */
    Departamento update(Departamento departamento);

    /**
     * Partially updates a departamento.
     *
     * @param departamento the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Departamento> partialUpdate(Departamento departamento);

    /**
     * Get all the departamentos.
     *
     * @return the list of entities.
     */
    List<Departamento> findAll();

    /**
     * Get the "id" departamento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Departamento> findOne(Long id);

    /**
     * Delete the "id" departamento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
