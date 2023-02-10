package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Tarea;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Tarea}.
 */
public interface TareaService {
    /**
     * Save a tarea.
     *
     * @param tarea the entity to save.
     * @return the persisted entity.
     */
    Tarea save(Tarea tarea);

    /**
     * Updates a tarea.
     *
     * @param tarea the entity to update.
     * @return the persisted entity.
     */
    Tarea update(Tarea tarea);

    /**
     * Partially updates a tarea.
     *
     * @param tarea the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Tarea> partialUpdate(Tarea tarea);

    /**
     * Get all the tareas.
     *
     * @return the list of entities.
     */
    List<Tarea> findAll();

    /**
     * Get the "id" tarea.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Tarea> findOne(Long id);

    /**
     * Delete the "id" tarea.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
