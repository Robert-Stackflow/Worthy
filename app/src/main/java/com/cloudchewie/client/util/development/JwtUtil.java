/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:14:24
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.util.development;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;

import java.util.Map;

import io.jsonwebtoken.MalformedJwtException;

public class JwtUtil {

    /**
     * 获取Jwt的负载
     */
    public static Map<String, Claim> getPayload(String token) {
        try {
            return JWT.decode(token).getClaims();
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Illegal token format!");
        }
    }
}
