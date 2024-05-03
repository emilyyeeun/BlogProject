package me.yeeunhong.blogproject.dto;

import lombok.Getter;

@Getter
public class UpdatableDateResponse {
    private String message;
    public UpdatableDateResponse(String message) {
        this.message = message;

    }
}
