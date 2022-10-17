package com.example.demo.posts;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

import static com.example.demo.AppConfig.CachingConfig.POST_CACHE;

@RestController
@RequestMapping("/posts")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    public static final String POSTS_TOPIC = "projects/jovial-sunrise-363621/topics/posts";

    private final PostRepository repository;
    private final Publisher publisher;

    @Autowired
    PostController(PostRepository repository) throws IOException {
        this.repository = repository;
        this.publisher = Publisher.newBuilder(POSTS_TOPIC).build();
    }

    @PostMapping("")
    public void createPost(@RequestBody PostDTO dto) throws ExecutionException, InterruptedException {
        Post post = new Post();
        post.setText(dto.getText());
        post.setCreatedAt(Instant.now());
        post.setCreatedBy(dto.getCreatedBy());

        Post savedPost = repository.save(post);
        logger.info("Created a new post with ID {} by user {}", savedPost.getId(), savedPost.getCreatedBy());

        PubsubMessage message = PubsubMessage.newBuilder()
                .setData(ByteString.copyFromUtf8(String.valueOf(savedPost.getId())))
                .build();

        ApiFuture<String> apiFuture = publisher.publish(message);
        String messageId = apiFuture.get();
        logger.info("Published message with ID {}", messageId);
    }

    @Cacheable(cacheNames = POST_CACHE, key="#userId")
    @GetMapping("/last/{userId}")
    public PostDTO getLatestPost(@PathVariable Long userId) {
        logger.info("Post is NOT CACHED. Retrieving from datastore.");
        Post result = repository.findTopByCreatedBy(
                    userId,
                    Sort.by(Sort.Order.desc("createdAt"))
            );

        if (result != null) {
            logger.info("Posts found for user with ID {}", userId);

            PostDTO resultDto = new PostDTO();
            resultDto.setText(result.getText());
            resultDto.setCreatedBy(result.getCreatedBy());

            return resultDto;
        }

        logger.warn("Could not find posts for user with ID {}", userId);
        return null;
    }

}
