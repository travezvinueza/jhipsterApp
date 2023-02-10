package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Trabajo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TrabajoRepositoryWithBagRelationships {
    Optional<Trabajo> fetchBagRelationships(Optional<Trabajo> trabajo);

    List<Trabajo> fetchBagRelationships(List<Trabajo> trabajos);

    Page<Trabajo> fetchBagRelationships(Page<Trabajo> trabajos);
}
