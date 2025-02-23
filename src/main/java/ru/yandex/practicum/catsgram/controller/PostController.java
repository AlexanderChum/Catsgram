package ru.yandex.practicum.catsgram.controller;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public Collection<Post> findAll(@RequestParam
                                    @Positive
                                    Long from,

                                    @RequestParam (defaultValue = "10")
                                    @Positive
                                    Long size,

                                    @RequestParam String sort) {
        return postService.findAll(from, size, sort);
    }

    @GetMapping("/post/{postId}")
    public Post findPost(@PathVariable Long postId) {
        return postService.findPost(postId);
    }

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}