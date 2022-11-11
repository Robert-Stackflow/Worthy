package com.cloudchewie.client.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Map;

import io.jsonwebtoken.MalformedJwtException;

public class JwtUtil {

    public static Map<String, Claim> getPayload(String jwtToken) {
        try {
            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            return decodedJWT.getClaims();
        } catch (MalformedJwtException e) {
            throw new RuntimeException("token格式不合法");
        }
    }
}
