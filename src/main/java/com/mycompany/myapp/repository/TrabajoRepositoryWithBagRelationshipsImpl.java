package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Trabajo;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TrabajoRepositoryWithBagRelationshipsImpl implements TrabajoRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Trabajo> fetchBagRelationships(Optional<Trabajo> trabajo) {
        return trabajo.map(this::fetchTareas);
    }

    @Override
    public Page<Trabajo> fetchBagRelationships(Page<Trabajo> trabajos) {
        return new PageImpl<>(fetchBagRelationships(trabajos.getContent()), trabajos.getPageable(), trabajos.getTotalElements());
    }

    @Override
    public List<Trabajo> fetchBagRelationships(List<Trabajo> trabajos) {
        return Optional.of(trabajos).map(this::fetchTareas).orElse(Collections.emptyList());
    }

    Trabajo fetchTareas(Trabajo result) {
        return entityManager
            .createQuery("select trabajo from Trabajo trabajo left join fetch trabajo.tareas where trabajo is :trabajo", Trabajo.class)
            .setParameter("trabajo", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Trabajo> fetchTareas(List<Trabajo> trabajos) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, trabajos.size()).forEach(index -> order.put(trabajos.get(index).getId(), index));
        List<Trabajo> result = entityManager
            .createQuery(
                "select distinct trabajo from Trabajo trabajo left join fetch trabajo.tareas where trabajo in :trabajos",
                Trabajo.class
            )
            .setParameter("trabajos", trabajos)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
