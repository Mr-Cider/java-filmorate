package ru.yandex.practicum.filmorate.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendsDto {
    private Long userId;
    private Long friendId;
    private boolean acceptFriend;
}
