package com.decagon.oxygen.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class APIResponse<T> {
    private String message;
    private  Boolean success;
    private T payload;
}
