package com.kobo.coworker.common.security;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import javax.servlet.http.HttpServletRequest;

public class JwtUtils {

    public static String extractJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new GeneralException(ErrorStatus._UNAUTHORIZED);
    }
}
