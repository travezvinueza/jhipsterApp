package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Pais;
import com.mycompany.myapp.repository.PaisRepository;
import com.mycompany.myapp.service.PaisService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pais}.
 */
@Service
@Transactional
public class PaisServiceImpl implements PaisService {

    private final Logger log = LoggerFactory.getLogger(PaisServiceImpl.class);

    private final PaisRepository paisRepository;

    public PaisServiceImpl(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Override
    public Pais save(Pais pais) {
        log.debug("Request to save Pais : {}", pais);
        return paisRepository.save(pais);
    }

    @Override
    public Pais update(Pais pais) {
        log.debug("Request to update Pais : {}", pais);
        return paisRepository.save(pais);
    }

    @Override
    public Optional<Pais> partialUpdate(Pais pais) {
        log.debug("Request to partially update Pais : {}", pais);

        return paisRepository
            .findById(pais.getId())
            .map(existingPais -> {
                if (pais.getNombrePais() != null) {
                    existingPais.setNombrePais(pais.getNombrePais());
                }

                return existingPais;
            })
            .map(paisRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pais> findAll() {
        log.debug("Request to get all Pais");
        return paisRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pais> findOne(Long id) {
        log.debug("Request to get Pais : {}", id);
        return paisRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pais : {}", id);
        paisRepository.deleteById(id);
    }
}
