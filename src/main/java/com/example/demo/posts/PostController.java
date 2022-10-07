package com.example.demo.posts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

import static com.example.demo.AppConfig.CachingConfig.POST_CACHE;

@RestController
@RequestMapping("/posts")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostRepository repository;

    @Autowired
    PostController(PostRepository repository) {
        this.repository = repository;
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

    @Cacheable(cacheNames = POST_CACHE, key="#userId")
    @GetMapping("/last/{userId}")
    public PostDTO getLatestPost(@PathVariable Long userId) {
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
