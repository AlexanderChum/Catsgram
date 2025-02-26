package ru.yandex.practicum.catsgram.controller;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.repository.SortOrder;
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
    public Collection<Post> findAll(@RequestParam Long from,
                                    @RequestParam (defaultValue = "10") Long size,
                                    @RequestParam String sort) {
        SortOrder sortOrder = SortOrder.from(sort);
        if (size <= 0) throw new ParameterNotValidException(size.toString(),
                "Некорректный размер выборки. Размер должен быть больше нуля");
        if (from < 0) throw new ParameterNotValidException(from.toString(),
                "Некорректный стартовый пост. Стартовый пост должен быть больше или равен 0");
        if (sortOrder == null) throw new ParameterNotValidException(sort,
                "Некорректный тип сортировки. Должен быть по возрастанию или убыванию");

        return postService.findAll(from, size, sort);
    }

    @GetMapping("/post/{postId}")
    public Post findPost(@PathVariable Long postId) {
        return postService.findById(postId);
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