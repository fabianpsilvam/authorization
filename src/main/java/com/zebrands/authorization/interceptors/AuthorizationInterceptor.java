package com.zebrands.authorization.interceptors;

import com.zebrands.authorization.annotations.Authorization;
import com.zebrands.authorization.constants.Constants;
import com.zebrands.authorization.exception.AuthorizationException;
import com.zebrands.authorization.service.ProfileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
@Log4j2
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private ProfileService profileService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            var filter = ((HandlerMethod) handler).getMethod().getAnnotation(Authorization.class);
            if (filter == null) {
                return true;
            }
            if (validateAuthorization((HandlerMethod) handler, request)) {
                var user = request.getHeader(Constants.AUTH_USER);
                if (!Arrays.asList(filter.userType()).contains(profileService.findProfileByUser(user))) {
                    throw new AuthorizationException("001", "usuario sin accesos al recurso: " + user);
                }
                return true;
            }
            log.info("User {} not is {}", request.getHeader(Constants.AUTH_USER), filter.userType());
            return false;
        }
        return true;
    }

    private boolean validateAuthorization(HandlerMethod handler, HttpServletRequest request) {
        boolean isValidaRequest;
        if (handler == null || handler.getMethod().getAnnotation(Authorization.class).userType() == null) {
            isValidaRequest = false;
        } else {
            isValidaRequest = request.getHeader(Constants.AUTH_USER) != null;
        }
        return isValidaRequest;
    }
}
