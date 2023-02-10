package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Departamento;
import com.mycompany.myapp.repository.DepartamentoRepository;
import com.mycompany.myapp.service.DepartamentoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Departamento}.
 */
@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {

    private final Logger log = LoggerFactory.getLogger(DepartamentoServiceImpl.class);

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public Departamento save(Departamento departamento) {
        log.debug("Request to save Departamento : {}", departamento);
        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento update(Departamento departamento) {
        log.debug("Request to update Departamento : {}", departamento);
        return departamentoRepository.save(departamento);
    }

    @Override
    public Optional<Departamento> partialUpdate(Departamento departamento) {
        log.debug("Request to partially update Departamento : {}", departamento);

        return departamentoRepository
            .findById(departamento.getId())
            .map(existingDepartamento -> {
                if (departamento.getNombreDepartamento() != null) {
                    existingDepartamento.setNombreDepartamento(departamento.getNombreDepartamento());
                }

                return existingDepartamento;
            })
            .map(departamentoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Departamento> findAll() {
        log.debug("Request to get all Departamentos");
        return departamentoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Departamento> findOne(Long id) {
        log.debug("Request to get Departamento : {}", id);
        return departamentoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Departamento : {}", id);
        departamentoRepository.deleteById(id);
    }
}
