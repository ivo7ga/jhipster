package es.curso.jhipster.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import es.curso.jhipster.domain.Experiencia;
import es.curso.jhipster.repository.ExperienciaRepository;
import es.curso.jhipster.repository.search.ExperienciaSearchRepository;
import es.curso.jhipster.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link es.curso.jhipster.domain.Experiencia}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExperienciaResource {

    private final Logger log = LoggerFactory.getLogger(ExperienciaResource.class);

    private static final String ENTITY_NAME = "experiencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExperienciaRepository experienciaRepository;

    private final ExperienciaSearchRepository experienciaSearchRepository;

    public ExperienciaResource(ExperienciaRepository experienciaRepository, ExperienciaSearchRepository experienciaSearchRepository) {
        this.experienciaRepository = experienciaRepository;
        this.experienciaSearchRepository = experienciaSearchRepository;
    }

    /**
     * {@code POST  /experiencias} : Create a new experiencia.
     *
     * @param experiencia the experiencia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new experiencia, or with status {@code 400 (Bad Request)} if the experiencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/experiencias")
    public ResponseEntity<Experiencia> createExperiencia(@Valid @RequestBody Experiencia experiencia) throws URISyntaxException {
        log.debug("REST request to save Experiencia : {}", experiencia);
        if (experiencia.getId() != null) {
            throw new BadRequestAlertException("A new experiencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Experiencia result = experienciaRepository.save(experiencia);
        experienciaSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/experiencias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /experiencias/:id} : Updates an existing experiencia.
     *
     * @param id the id of the experiencia to save.
     * @param experiencia the experiencia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated experiencia,
     * or with status {@code 400 (Bad Request)} if the experiencia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the experiencia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/experiencias/{id}")
    public ResponseEntity<Experiencia> updateExperiencia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Experiencia experiencia
    ) throws URISyntaxException {
        log.debug("REST request to update Experiencia : {}, {}", id, experiencia);
        if (experiencia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, experiencia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!experienciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Experiencia result = experienciaRepository.save(experiencia);
        experienciaSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, experiencia.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /experiencias/:id} : Partial updates given fields of an existing experiencia, field will ignore if it is null
     *
     * @param id the id of the experiencia to save.
     * @param experiencia the experiencia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated experiencia,
     * or with status {@code 400 (Bad Request)} if the experiencia is not valid,
     * or with status {@code 404 (Not Found)} if the experiencia is not found,
     * or with status {@code 500 (Internal Server Error)} if the experiencia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/experiencias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Experiencia> partialUpdateExperiencia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Experiencia experiencia
    ) throws URISyntaxException {
        log.debug("REST request to partial update Experiencia partially : {}, {}", id, experiencia);
        if (experiencia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, experiencia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!experienciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Experiencia> result = experienciaRepository
            .findById(experiencia.getId())
            .map(existingExperiencia -> {
                if (experiencia.getTitulo() != null) {
                    existingExperiencia.setTitulo(experiencia.getTitulo());
                }
                if (experiencia.getDescripcion() != null) {
                    existingExperiencia.setDescripcion(experiencia.getDescripcion());
                }
                if (experiencia.getLocalizacion() != null) {
                    existingExperiencia.setLocalizacion(experiencia.getLocalizacion());
                }
                if (experiencia.getFecha() != null) {
                    existingExperiencia.setFecha(experiencia.getFecha());
                }

                return existingExperiencia;
            })
            .map(experienciaRepository::save)
            .map(savedExperiencia -> {
                experienciaSearchRepository.save(savedExperiencia);

                return savedExperiencia;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, experiencia.getId().toString())
        );
    }

    /**
     * {@code GET  /experiencias} : get all the experiencias.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of experiencias in body.
     */
    @GetMapping("/experiencias")
    public ResponseEntity<List<Experiencia>> getAllExperiencias(Pageable pageable) {
        log.debug("REST request to get a page of Experiencias");
        Page<Experiencia> page = experienciaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /experiencias/:id} : get the "id" experiencia.
     *
     * @param id the id of the experiencia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the experiencia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/experiencias/{id}")
    public ResponseEntity<Experiencia> getExperiencia(@PathVariable Long id) {
        log.debug("REST request to get Experiencia : {}", id);
        Optional<Experiencia> experiencia = experienciaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(experiencia);
    }

    /**
     * {@code DELETE  /experiencias/:id} : delete the "id" experiencia.
     *
     * @param id the id of the experiencia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/experiencias/{id}")
    public ResponseEntity<Void> deleteExperiencia(@PathVariable Long id) {
        log.debug("REST request to delete Experiencia : {}", id);
        experienciaRepository.deleteById(id);
        experienciaSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/experiencias?query=:query} : search for the experiencia corresponding
     * to the query.
     *
     * @param query the query of the experiencia search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/experiencias")
    public ResponseEntity<List<Experiencia>> searchExperiencias(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Experiencias for query {}", query);
        Page<Experiencia> page = experienciaSearchRepository.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
