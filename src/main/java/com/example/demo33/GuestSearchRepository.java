package com.example.demo33;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestSearchRepository extends ElasticsearchRepository<GuestDocument, String> {
}