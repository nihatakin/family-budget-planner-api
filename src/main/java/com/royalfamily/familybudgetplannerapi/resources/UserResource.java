package com.royalfamily.familybudgetplannerapi.resources;

import com.royalfamily.familybudgetplannerapi.domain.User;
import com.royalfamily.familybudgetplannerapi.helper.TokenConstants;
import com.royalfamily.familybudgetplannerapi.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User user) {
        User validateUser = userService.validateUser(user.getEmail(), user.getPassword());
        return new ResponseEntity<>(generateJWTToken(validateUser), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        User registerUser = userService.registerUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
        return new ResponseEntity<>(generateJWTToken(registerUser), HttpStatus.OK);
    }

    private Map<String, String> generateJWTToken(User user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expireDate = new Date(nowMillis + TokenConstants.TOKEN_VALIDITY);

        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, TokenConstants.API_SECRET_KEY)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .claim("userId", user.getUserId())
                .claim("email", user.getEmail())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }
}
