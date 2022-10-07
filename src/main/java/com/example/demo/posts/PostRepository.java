package com.example.demo.posts;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import org.springframework.data.domain.Sort;

public interface PostRepository extends DatastoreRepository<Post, Long> {

    Post findTopByCreatedBy(Long createdBy, Sort sort);
}
