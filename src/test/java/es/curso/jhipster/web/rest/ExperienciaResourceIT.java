package es.curso.jhipster.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.curso.jhipster.IntegrationTest;
import es.curso.jhipster.domain.Experiencia;
import es.curso.jhipster.repository.ExperienciaRepository;
import es.curso.jhipster.repository.search.ExperienciaSearchRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ExperienciaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExperienciaResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALIZACION = "AAAAAAAAAA";
    private static final String UPDATED_LOCALIZACION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/experiencias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/experiencias";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExperienciaRepository experienciaRepository;

    /**
     * This repository is mocked in the es.curso.jhipster.repository.search test package.
     *
     * @see es.curso.jhipster.repository.search.ExperienciaSearchRepositoryMockConfiguration
     */
    @Autowired
    private ExperienciaSearchRepository mockExperienciaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExperienciaMockMvc;

    private Experiencia experiencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Experiencia createEntity(EntityManager em) {
        Experiencia experiencia = new Experiencia()
            .titulo(DEFAULT_TITULO)
            .descripcion(DEFAULT_DESCRIPCION)
            .localizacion(DEFAULT_LOCALIZACION)
            .fecha(DEFAULT_FECHA);
        return experiencia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Experiencia createUpdatedEntity(EntityManager em) {
        Experiencia experiencia = new Experiencia()
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .localizacion(UPDATED_LOCALIZACION)
            .fecha(UPDATED_FECHA);
        return experiencia;
    }

    @BeforeEach
    public void initTest() {
        experiencia = createEntity(em);
    }

    @Test
    @Transactional
    void createExperiencia() throws Exception {
        int databaseSizeBeforeCreate = experienciaRepository.findAll().size();
        // Create the Experiencia
        restExperienciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(experiencia)))
            .andExpect(status().isCreated());

        // Validate the Experiencia in the database
        List<Experiencia> experienciaList = experienciaRepository.findAll();
        assertThat(experienciaList).hasSize(databaseSizeBeforeCreate + 1);
        Experiencia testExperiencia = experienciaList.get(experienciaList.size() - 1);
        assertThat(testExperiencia.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testExperiencia.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testExperiencia.getLocalizacion()).isEqualTo(DEFAULT_LOCALIZACION);
        assertThat(testExperiencia.getFecha()).isEqualTo(DEFAULT_FECHA);

        // Validate the Experiencia in Elasticsearch
        verify(mockExperienciaSearchRepository, times(1)).save(testExperiencia);
    }

    @Test
    @Transactional
    void createExperienciaWithExistingId() throws Exception {
        // Create the Experiencia with an existing ID
        experiencia.setId(1L);

        int databaseSizeBeforeCreate = experienciaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExperienciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(experiencia)))
            .andExpect(status().isBadRequest());

        // Validate the Experiencia in the database
        List<Experiencia> experienciaList = experienciaRepository.findAll();
        assertThat(experienciaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Experiencia in Elasticsearch
        verify(mockExperienciaSearchRepository, times(0)).save(experiencia);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = experienciaRepository.findAll().size();
        // set the field null
        experiencia.setTitulo(null);

        // Create the Experiencia, which fails.

        restExperienciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(experiencia)))
            .andExpect(status().isBadRequest());

        List<Experiencia> experienciaList = experienciaRepository.findAll();
        assertThat(experienciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExperiencias() throws Exception {
        // Initialize the database
        experienciaRepository.saveAndFlush(experiencia);

        // Get all the experienciaList
        restExperienciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experiencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].localizacion").value(hasItem(DEFAULT_LOCALIZACION)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @Test
    @Transactional
    void getExperiencia() throws Exception {
        // Initialize the database
        experienciaRepository.saveAndFlush(experiencia);

        // Get the experiencia
        restExperienciaMockMvc
            .perform(get(ENTITY_API_URL_ID, experiencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(experiencia.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.localizacion").value(DEFAULT_LOCALIZACION))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExperiencia() throws Exception {
        // Get the experiencia
        restExperienciaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExperiencia() throws Exception {
        // Initialize the database
        experienciaRepository.saveAndFlush(experiencia);

        int databaseSizeBeforeUpdate = experienciaRepository.findAll().size();

        // Update the experiencia
        Experiencia updatedExperiencia = experienciaRepository.findById(experiencia.getId()).get();
        // Disconnect from session so that the updates on updatedExperiencia are not directly saved in db
        em.detach(updatedExperiencia);
        updatedExperiencia.titulo(UPDATED_TITULO).descripcion(UPDATED_DESCRIPCION).localizacion(UPDATED_LOCALIZACION).fecha(UPDATED_FECHA);

        restExperienciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExperiencia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExperiencia))
            )
            .andExpect(status().isOk());

        // Validate the Experiencia in the database
        List<Experiencia> experienciaList = experienciaRepository.findAll();
        assertThat(experienciaList).hasSize(databaseSizeBeforeUpdate);
        Experiencia testExperiencia = experienciaList.get(experienciaList.size() - 1);
        assertThat(testExperiencia.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testExperiencia.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testExperiencia.getLocalizacion()).isEqualTo(UPDATED_LOCALIZACION);
        assertThat(testExperiencia.getFecha()).isEqualTo(UPDATED_FECHA);

        // Validate the Experiencia in Elasticsearch
        verify(mockExperienciaSearchRepository).save(testExperiencia);
    }

    @Test
    @Transactional
    void putNonExistingExperiencia() throws Exception {
        int databaseSizeBeforeUpdate = experienciaRepository.findAll().size();
        experiencia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExperienciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, experiencia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experiencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Experiencia in the database
        List<Experiencia> experienciaList = experienciaRepository.findAll();
        assertThat(experienciaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Experiencia in Elasticsearch
        verify(mockExperienciaSearchRepository, times(0)).save(experiencia);
    }

    @Test
    @Transactional
    void putWithIdMismatchExperiencia() throws Exception {
        int databaseSizeBeforeUpdate = experienciaRepository.findAll().size();
        experiencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experiencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Experiencia in the database
        List<Experiencia> experienciaList = experienciaRepository.findAll();
        assertThat(experienciaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Experiencia in Elasticsearch
        verify(mockExperienciaSearchRepository, times(0)).save(experiencia);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExperiencia() throws Exception {
        int databaseSizeBeforeUpdate = experienciaRepository.findAll().size();
        experiencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienciaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(experiencia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Experiencia in the database
        List<Experiencia> experienciaList = experienciaRepository.findAll();
        assertThat(experienciaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Experiencia in Elasticsearch
        verify(mockExperienciaSearchRepository, times(0)).save(experiencia);
    }

    @Test
    @Transactional
    void partialUpdateExperienciaWithPatch() throws Exception {
        // Initialize the database
        experienciaRepository.saveAndFlush(experiencia);

        int databaseSizeBeforeUpdate = experienciaRepository.findAll().size();

        // Update the experiencia using partial update
        Experiencia partialUpdatedExperiencia = new Experiencia();
        partialUpdatedExperiencia.setId(experiencia.getId());

        partialUpdatedExperiencia.localizacion(UPDATED_LOCALIZACION).fecha(UPDATED_FECHA);

        restExperienciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExperiencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExperiencia))
            )
            .andExpect(status().isOk());

        // Validate the Experiencia in the database
        List<Experiencia> experienciaList = experienciaRepository.findAll();
        assertThat(experienciaList).hasSize(databaseSizeBeforeUpdate);
        Experiencia testExperiencia = experienciaList.get(experienciaList.size() - 1);
        assertThat(testExperiencia.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testExperiencia.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testExperiencia.getLocalizacion()).isEqualTo(UPDATED_LOCALIZACION);
        assertThat(testExperiencia.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void fullUpdateExperienciaWithPatch() throws Exception {
        // Initialize the database
        experienciaRepository.saveAndFlush(experiencia);

        int databaseSizeBeforeUpdate = experienciaRepository.findAll().size();

        // Update the experiencia using partial update
        Experiencia partialUpdatedExperiencia = new Experiencia();
        partialUpdatedExperiencia.setId(experiencia.getId());

        partialUpdatedExperiencia
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .localizacion(UPDATED_LOCALIZACION)
            .fecha(UPDATED_FECHA);

        restExperienciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExperiencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExperiencia))
            )
            .andExpect(status().isOk());

        // Validate the Experiencia in the database
        List<Experiencia> experienciaList = experienciaRepository.findAll();
        assertThat(experienciaList).hasSize(databaseSizeBeforeUpdate);
        Experiencia testExperiencia = experienciaList.get(experienciaList.size() - 1);
        assertThat(testExperiencia.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testExperiencia.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testExperiencia.getLocalizacion()).isEqualTo(UPDATED_LOCALIZACION);
        assertThat(testExperiencia.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void patchNonExistingExperiencia() throws Exception {
        int databaseSizeBeforeUpdate = experienciaRepository.findAll().size();
        experiencia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExperienciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, experiencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(experiencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Experiencia in the database
        List<Experiencia> experienciaList = experienciaRepository.findAll();
        assertThat(experienciaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Experiencia in Elasticsearch
        verify(mockExperienciaSearchRepository, times(0)).save(experiencia);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExperiencia() throws Exception {
        int databaseSizeBeforeUpdate = experienciaRepository.findAll().size();
        experiencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(experiencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Experiencia in the database
        List<Experiencia> experienciaList = experienciaRepository.findAll();
        assertThat(experienciaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Experiencia in Elasticsearch
        verify(mockExperienciaSearchRepository, times(0)).save(experiencia);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExperiencia() throws Exception {
        int databaseSizeBeforeUpdate = experienciaRepository.findAll().size();
        experiencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienciaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(experiencia))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Experiencia in the database
        List<Experiencia> experienciaList = experienciaRepository.findAll();
        assertThat(experienciaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Experiencia in Elasticsearch
        verify(mockExperienciaSearchRepository, times(0)).save(experiencia);
    }

    @Test
    @Transactional
    void deleteExperiencia() throws Exception {
        // Initialize the database
        experienciaRepository.saveAndFlush(experiencia);

        int databaseSizeBeforeDelete = experienciaRepository.findAll().size();

        // Delete the experiencia
        restExperienciaMockMvc
            .perform(delete(ENTITY_API_URL_ID, experiencia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Experiencia> experienciaList = experienciaRepository.findAll();
        assertThat(experienciaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Experiencia in Elasticsearch
        verify(mockExperienciaSearchRepository, times(1)).deleteById(experiencia.getId());
    }

    @Test
    @Transactional
    void searchExperiencia() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        experienciaRepository.saveAndFlush(experiencia);
        when(mockExperienciaSearchRepository.search("id:" + experiencia.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(experiencia), PageRequest.of(0, 1), 1));

        // Search the experiencia
        restExperienciaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + experiencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experiencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].localizacion").value(hasItem(DEFAULT_LOCALIZACION)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }
}
