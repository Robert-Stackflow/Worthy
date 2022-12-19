/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:14:24
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.util.dev;

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
