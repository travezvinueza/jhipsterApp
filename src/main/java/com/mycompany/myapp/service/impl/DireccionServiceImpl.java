package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Direccion;
import com.mycompany.myapp.repository.DireccionRepository;
import com.mycompany.myapp.service.DireccionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Direccion}.
 */
@Service
@Transactional
public class DireccionServiceImpl implements DireccionService {

    private final Logger log = LoggerFactory.getLogger(DireccionServiceImpl.class);

    private final DireccionRepository direccionRepository;

    public DireccionServiceImpl(DireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

    @Override
    public Direccion save(Direccion direccion) {
        log.debug("Request to save Direccion : {}", direccion);
        return direccionRepository.save(direccion);
    }

    @Override
    public Direccion update(Direccion direccion) {
        log.debug("Request to update Direccion : {}", direccion);
        return direccionRepository.save(direccion);
    }

    @Override
    public Optional<Direccion> partialUpdate(Direccion direccion) {
        log.debug("Request to partially update Direccion : {}", direccion);

        return direccionRepository
            .findById(direccion.getId())
            .map(existingDireccion -> {
                if (direccion.getCalle() != null) {
                    existingDireccion.setCalle(direccion.getCalle());
                }
                if (direccion.getCodigoPostal() != null) {
                    existingDireccion.setCodigoPostal(direccion.getCodigoPostal());
                }
                if (direccion.getCiudad() != null) {
                    existingDireccion.setCiudad(direccion.getCiudad());
                }
                if (direccion.getProvincia() != null) {
                    existingDireccion.setProvincia(direccion.getProvincia());
                }

                return existingDireccion;
            })
            .map(direccionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Direccion> findAll() {
        log.debug("Request to get all Direccions");
        return direccionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Direccion> findOne(Long id) {
        log.debug("Request to get Direccion : {}", id);
        return direccionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Direccion : {}", id);
        direccionRepository.deleteById(id);
    }
}
