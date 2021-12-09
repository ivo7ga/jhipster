package es.curso.jhipster.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ExperienciaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ExperienciaSearchRepositoryMockConfiguration {

    @MockBean
    private ExperienciaSearchRepository mockExperienciaSearchRepository;
}
