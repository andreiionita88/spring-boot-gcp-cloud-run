package com.example.demo.posts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

import static com.example.demo.AppConfig.POST_CACHE;

@RestController
@RequestMapping("/posts")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostRepository repository;
    private final CacheManager cacheManager;

    @Autowired
    PostController(PostRepository repository, CacheManager cacheManager) {
        this.repository = repository;
        this.cacheManager = cacheManager;
    }

    @PostMapping("")
    public void createPost(@RequestBody PostDTO dto) {
        Post post = new Post();
        post.setText(dto.getText());
        post.setCreatedAt(Instant.now());
        post.setCreatedBy(dto.getCreatedBy());

        Post savedPost = repository.save(post);
        logger.info("Created a new post with ID {} by user {}", savedPost.getId(), savedPost.getCreatedBy());
    }

    @GetMapping("/last/{userId}")
    public PostDTO getLatestPost(@PathVariable Long userId) {
        Post probe = new Post();
        probe.setCreatedBy(userId);

        Cache cache = cacheManager.getCache(POST_CACHE);
        Iterable<Post> results = cache.get(userId, Iterable.class);

        if (results == null) {
            results = repository.findAll(
                    Example.of(probe),
                    Sort.by(Sort.Order.desc("createdAt"))
            );
            cache.put(userId, results);
        }

        if (results.iterator().hasNext()) {
            logger.info("Posts found for user with ID {}", userId);

            Post topResult = results.iterator().next();

            PostDTO resultDto = new PostDTO();
            resultDto.setText(topResult.getText());
            resultDto.setCreatedBy(topResult.getCreatedBy());

            return resultDto;
        }

        logger.warn("Could not find posts for user with ID {}", userId);
        return null;
    }

}
