package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Empleado.
 */
@Entity
@Table(name = "empleado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "correo")
    private String correo;

    @Column(name = "nro_celular")
    private String nroCelular;

    @Column(name = "fecha_contrato")
    private Instant fechaContrato;

    @Column(name = "salario")
    private Long salario;

    @Column(name = "comision_porcentaje")
    private Long comisionPorcentaje;

    @OneToMany(mappedBy = "empleado")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tareas", "empleado" }, allowSetters = true)
    private Set<Trabajo> trabajos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "trabajos", "inmediatosuperior", "departamento" }, allowSetters = true)
    private Empleado inmediatosuperior;

    @ManyToOne
    @JsonIgnoreProperties(value = { "direccion", "empleados" }, allowSetters = true)
    private Departamento departamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empleado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return this.nombres;
    }

    public Empleado nombres(String nombres) {
        this.setNombres(nombres);
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Empleado apellidos(String apellidos) {
        this.setApellidos(apellidos);
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return this.correo;
    }

    public Empleado correo(String correo) {
        this.setCorreo(correo);
        return this;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNroCelular() {
        return this.nroCelular;
    }

    public Empleado nroCelular(String nroCelular) {
        this.setNroCelular(nroCelular);
        return this;
    }

    public void setNroCelular(String nroCelular) {
        this.nroCelular = nroCelular;
    }

    public Instant getFechaContrato() {
        return this.fechaContrato;
    }

    public Empleado fechaContrato(Instant fechaContrato) {
        this.setFechaContrato(fechaContrato);
        return this;
    }

    public void setFechaContrato(Instant fechaContrato) {
        this.fechaContrato = fechaContrato;
    }

    public Long getSalario() {
        return this.salario;
    }

    public Empleado salario(Long salario) {
        this.setSalario(salario);
        return this;
    }

    public void setSalario(Long salario) {
        this.salario = salario;
    }

    public Long getComisionPorcentaje() {
        return this.comisionPorcentaje;
    }

    public Empleado comisionPorcentaje(Long comisionPorcentaje) {
        this.setComisionPorcentaje(comisionPorcentaje);
        return this;
    }

    public void setComisionPorcentaje(Long comisionPorcentaje) {
        this.comisionPorcentaje = comisionPorcentaje;
    }

    public Set<Trabajo> getTrabajos() {
        return this.trabajos;
    }

    public void setTrabajos(Set<Trabajo> trabajos) {
        if (this.trabajos != null) {
            this.trabajos.forEach(i -> i.setEmpleado(null));
        }
        if (trabajos != null) {
            trabajos.forEach(i -> i.setEmpleado(this));
        }
        this.trabajos = trabajos;
    }

    public Empleado trabajos(Set<Trabajo> trabajos) {
        this.setTrabajos(trabajos);
        return this;
    }

    public Empleado addTrabajo(Trabajo trabajo) {
        this.trabajos.add(trabajo);
        trabajo.setEmpleado(this);
        return this;
    }

    public Empleado removeTrabajo(Trabajo trabajo) {
        this.trabajos.remove(trabajo);
        trabajo.setEmpleado(null);
        return this;
    }

    public Empleado getInmediatosuperior() {
        return this.inmediatosuperior;
    }

    public void setInmediatosuperior(Empleado empleado) {
        this.inmediatosuperior = empleado;
    }

    public Empleado inmediatosuperior(Empleado empleado) {
        this.setInmediatosuperior(empleado);
        return this;
    }

    public Departamento getDepartamento() {
        return this.departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Empleado departamento(Departamento departamento) {
        this.setDepartamento(departamento);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empleado)) {
            return false;
        }
        return id != null && id.equals(((Empleado) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empleado{" +
            "id=" + getId() +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", correo='" + getCorreo() + "'" +
            ", nroCelular='" + getNroCelular() + "'" +
            ", fechaContrato='" + getFechaContrato() + "'" +
            ", salario=" + getSalario() +
            ", comisionPorcentaje=" + getComisionPorcentaje() +
            "}";
    }
}
