package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Tarea;
import com.mycompany.myapp.repository.TareaRepository;
import com.mycompany.myapp.service.TareaService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tarea}.
 */
@Service
@Transactional
public class TareaServiceImpl implements TareaService {

    private final Logger log = LoggerFactory.getLogger(TareaServiceImpl.class);

    private final TareaRepository tareaRepository;

    public TareaServiceImpl(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Override
    public Tarea save(Tarea tarea) {
        log.debug("Request to save Tarea : {}", tarea);
        return tareaRepository.save(tarea);
    }

    @Override
    public Tarea update(Tarea tarea) {
        log.debug("Request to update Tarea : {}", tarea);
        return tareaRepository.save(tarea);
    }

    @Override
    public Optional<Tarea> partialUpdate(Tarea tarea) {
        log.debug("Request to partially update Tarea : {}", tarea);

        return tareaRepository
            .findById(tarea.getId())
            .map(existingTarea -> {
                if (tarea.getTitulo() != null) {
                    existingTarea.setTitulo(tarea.getTitulo());
                }
                if (tarea.getDescripcion() != null) {
                    existingTarea.setDescripcion(tarea.getDescripcion());
                }

                return existingTarea;
            })
            .map(tareaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tarea> findAll() {
        log.debug("Request to get all Tareas");
        return tareaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tarea> findOne(Long id) {
        log.debug("Request to get Tarea : {}", id);
        return tareaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tarea : {}", id);
        tareaRepository.deleteById(id);
    }
}
