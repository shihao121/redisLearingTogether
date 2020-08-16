package com.north.learning.redis.friend.comtroller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/users")
public class FriendController {

    final
    RedisTemplate<String, String> redisTemplate;

    public FriendController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping(value = "/{userId}/friends/{friendId}")
    public void add(@PathVariable String userId, @PathVariable String friendId){
        redisTemplate.opsForSet().add(userId, friendId);
    }

    @DeleteMapping(value = "/{userId}/friends/{friendId}")
    public void del(@PathVariable String userId, @PathVariable String friendId){
        redisTemplate.opsForSet().remove(userId, friendId);
    }

    @GetMapping(value = "/{userId}/friends")
    public Set<String> get(@PathVariable String userId){
        return redisTemplate.opsForSet().members(userId);
    }

    @GetMapping(value = "/{userId}/intersectFriends")
    public Set<String> getFriends(@PathVariable String userId, @RequestParam String comparedUserId){
        return redisTemplate.opsForSet().intersect(Stream.of(userId, comparedUserId)
                .collect(Collectors.toList()));
    }

    @GetMapping(value = "/{userId}/differentFriends")
    public Set<String> getDifferentFriends(@PathVariable String userId, @RequestParam String comparedUserId){
        return redisTemplate.opsForSet().difference(userId, comparedUserId);
    }
}
