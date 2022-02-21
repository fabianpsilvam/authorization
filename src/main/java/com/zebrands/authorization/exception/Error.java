package com.zebrands.authorization.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {

    private String code;
    private String detail;
}
