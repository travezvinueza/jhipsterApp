package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Contrato;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contrato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {}
