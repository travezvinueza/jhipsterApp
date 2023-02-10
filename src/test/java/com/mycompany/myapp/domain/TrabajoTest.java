package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrabajoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trabajo.class);
        Trabajo trabajo1 = new Trabajo();
        trabajo1.setId(1L);
        Trabajo trabajo2 = new Trabajo();
        trabajo2.setId(trabajo1.getId());
        assertThat(trabajo1).isEqualTo(trabajo2);
        trabajo2.setId(2L);
        assertThat(trabajo1).isNotEqualTo(trabajo2);
        trabajo1.setId(null);
        assertThat(trabajo1).isNotEqualTo(trabajo2);
    }
}
