package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tarea.
 */
@Entity
@Table(name = "tarea")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tarea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToMany(mappedBy = "tareas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tareas", "empleado" }, allowSetters = true)
    private Set<Trabajo> trabajos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tarea id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Tarea titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Tarea descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Trabajo> getTrabajos() {
        return this.trabajos;
    }

    public void setTrabajos(Set<Trabajo> trabajos) {
        if (this.trabajos != null) {
            this.trabajos.forEach(i -> i.removeTarea(this));
        }
        if (trabajos != null) {
            trabajos.forEach(i -> i.addTarea(this));
        }
        this.trabajos = trabajos;
    }

    public Tarea trabajos(Set<Trabajo> trabajos) {
        this.setTrabajos(trabajos);
        return this;
    }

    public Tarea addTrabajo(Trabajo trabajo) {
        this.trabajos.add(trabajo);
        trabajo.getTareas().add(this);
        return this;
    }

    public Tarea removeTrabajo(Trabajo trabajo) {
        this.trabajos.remove(trabajo);
        trabajo.getTareas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tarea)) {
            return false;
        }
        return id != null && id.equals(((Tarea) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tarea{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
