package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.repository.SortOrder;

import java.time.Instant;
import java.util.*;

@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();

    @Autowired
    private UserService userService;


    public Collection<Post> findAll(Long from, Long size, String sort) {
        if (from == null || from > posts.size()) {
            throw new ConditionsNotMetException("Введен неверный пост для начала отсчета");
        }

        SortOrder sortOrder = SortOrder.from(sort);
        List<Post> result = new ArrayList<>();
        long start = from;
        long end = from + size;

        if (sortOrder == SortOrder.DESCENDING) {
            for (long i = end - 1; i >= start; i--) {
                if (posts.containsKey(i)) {
                    result.add(posts.get(i));
                }
            }
        } else { // ASCENDING or null
            for (long i = start; i < end; i++) {
                if (posts.containsKey(i)) {
                    result.add(posts.get(i));
                }
            }
        }

        return result;
    }

    public Post findPost(Long postId) {
        return posts.get(postId);
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        Optional<User> author = userService.findUserById(post.getAuthorId());
        if (author.isEmpty()) throw new ConditionsNotMetException("«Автор с id = " + post.getAuthorId()
                + " не найден»");

        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
