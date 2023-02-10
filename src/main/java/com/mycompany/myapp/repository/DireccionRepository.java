package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Direccion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Direccion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {}
