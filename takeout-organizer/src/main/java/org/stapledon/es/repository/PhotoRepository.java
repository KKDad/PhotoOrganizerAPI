package org.stapledon.es.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.stapledon.dto.Photo;

public interface PhotoRepository extends ElasticsearchRepository<Photo, String> {
    Page<Photo> findByName(String name, Pageable pageable);
}
