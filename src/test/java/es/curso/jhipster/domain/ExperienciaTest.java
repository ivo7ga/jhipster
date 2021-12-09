package es.curso.jhipster.domain;

import static org.assertj.core.api.Assertions.assertThat;

import es.curso.jhipster.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExperienciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Experiencia.class);
        Experiencia experiencia1 = new Experiencia();
        experiencia1.setId(1L);
        Experiencia experiencia2 = new Experiencia();
        experiencia2.setId(experiencia1.getId());
        assertThat(experiencia1).isEqualTo(experiencia2);
        experiencia2.setId(2L);
        assertThat(experiencia1).isNotEqualTo(experiencia2);
        experiencia1.setId(null);
        assertThat(experiencia1).isNotEqualTo(experiencia2);
    }
}
