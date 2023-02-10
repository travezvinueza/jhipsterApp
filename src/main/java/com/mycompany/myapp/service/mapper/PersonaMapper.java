package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Persona;
import com.mycompany.myapp.service.dto.PersonaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Persona} and its DTO {@link PersonaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonaMapper extends EntityMapper<PersonaDTO, Persona> {}
