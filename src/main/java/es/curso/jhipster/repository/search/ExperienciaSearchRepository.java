package es.curso.jhipster.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import es.curso.jhipster.domain.Experiencia;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Experiencia} entity.
 */
public interface ExperienciaSearchRepository extends ElasticsearchRepository<Experiencia, Long>, ExperienciaSearchRepositoryInternal {}

interface ExperienciaSearchRepositoryInternal {
    Page<Experiencia> search(String query, Pageable pageable);
}

class ExperienciaSearchRepositoryInternalImpl implements ExperienciaSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ExperienciaSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Experiencia> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Experiencia> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Experiencia.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
