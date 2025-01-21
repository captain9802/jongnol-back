package com.example.jongnolback.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class ResponseDTO<T> {
    private T item;
    private List<T> items;
    private Page<T> pageItems;
    private int statusCode;
    private int errorCode;
    private String errorMessage;

    public void setData(T item) {
        this.item = item;  // item에 데이터 할당
    }
}
