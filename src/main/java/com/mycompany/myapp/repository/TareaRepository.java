package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Tarea;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tarea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {}
