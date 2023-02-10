package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Idioma;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contrato.
 */
@Entity
@Table(name = "contrato")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contrato implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_inicio")
    private Instant fechaInicio;

    @Column(name = "fecha_fin")
    private Instant fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "lenguaje")
    private Idioma lenguaje;

    @JsonIgnoreProperties(value = { "tareas", "empleado" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Trabajo trabajo;

    @JsonIgnoreProperties(value = { "direccion", "empleados" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Departamento departamento;

    @JsonIgnoreProperties(value = { "trabajos", "inmediatosuperior", "departamento" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Empleado empleado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contrato id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaInicio() {
        return this.fechaInicio;
    }

    public Contrato fechaInicio(Instant fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaFin() {
        return this.fechaFin;
    }

    public Contrato fechaFin(Instant fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Idioma getLenguaje() {
        return this.lenguaje;
    }

    public Contrato lenguaje(Idioma lenguaje) {
        this.setLenguaje(lenguaje);
        return this;
    }

    public void setLenguaje(Idioma lenguaje) {
        this.lenguaje = lenguaje;
    }

    public Trabajo getTrabajo() {
        return this.trabajo;
    }

    public void setTrabajo(Trabajo trabajo) {
        this.trabajo = trabajo;
    }

    public Contrato trabajo(Trabajo trabajo) {
        this.setTrabajo(trabajo);
        return this;
    }

    public Departamento getDepartamento() {
        return this.departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Contrato departamento(Departamento departamento) {
        this.setDepartamento(departamento);
        return this;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Contrato empleado(Empleado empleado) {
        this.setEmpleado(empleado);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contrato)) {
            return false;
        }
        return id != null && id.equals(((Contrato) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contrato{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", lenguaje='" + getLenguaje() + "'" +
            "}";
    }
}
