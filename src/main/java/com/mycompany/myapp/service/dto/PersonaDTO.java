package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Persona} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 50)
    private String nombre;

    @NotNull
    @Size(min = 4, max = 50)
    private String apellido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonaDTO)) {
            return false;
        }

        PersonaDTO personaDTO = (PersonaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            "}";
    }
}
