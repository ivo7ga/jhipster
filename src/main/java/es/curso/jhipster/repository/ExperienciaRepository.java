package es.curso.jhipster.repository;

import es.curso.jhipster.domain.Experiencia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Experiencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExperienciaRepository extends JpaRepository<Experiencia, Long> {}
