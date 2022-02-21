package com.zebrands.authorization.handlers;

import com.zebrands.authorization.exception.AuthorizationException;
import com.zebrands.authorization.exception.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;


@ControllerAdvice
public class ExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @org.springframework.web.bind.annotation.ExceptionHandler(AuthorizationException.class)
    public Error manageUnauthorizedException(HttpServletRequest request, AuthorizationException exception) {

        return new Error(exception.getCode(), exception.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidParameterException.class)
    public Error manageUnauthorizedException(HttpServletRequest request, InvalidParameterException exception) {

        return new Error("002", exception.getMessage());
    }
}
