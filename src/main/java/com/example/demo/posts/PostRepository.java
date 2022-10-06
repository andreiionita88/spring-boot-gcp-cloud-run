package com.example.demo.posts;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;

public interface PostRepository extends DatastoreRepository<Post, Long> {
}
