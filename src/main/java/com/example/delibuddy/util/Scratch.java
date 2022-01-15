package com.example.delibuddy.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class Scratch {

    String SECRET_KEY = "sorktkakddmldmaclagksrhfWkrlfhekslfwlfkehenfudnjgkwldksgdmadms";

    public static void main(String[] args) {
        String s = new Scratch().generateToken("2038012114");
        System.out.println("s = " + s);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 60)) // 2 달
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public String generateToken(String userId) {
        // createToken 이랑 합쳐도 되는 거 아님?
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userId);
    }
}