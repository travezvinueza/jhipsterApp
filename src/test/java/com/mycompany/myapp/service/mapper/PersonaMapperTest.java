package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonaMapperTest {

    private PersonaMapper personaMapper;

    @BeforeEach
    public void setUp() {
        personaMapper = new PersonaMapperImpl();
    }
}
