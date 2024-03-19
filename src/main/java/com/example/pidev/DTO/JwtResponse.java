package com.example.pidev.DTO;

import com.example.pidev.Entities.User;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private Long userId;
    private String jwtToken;

    private String username;

    private String roles;


    public JwtResponse(Long userId, String jwtToken,String username) {
        this.userId = userId;
        this.jwtToken = jwtToken;
        this.username = username;
    }
    public JwtResponse(Long userId , String jwtToken , String roles,String username)
    {  this.userId = userId;
        this.jwtToken = jwtToken;
        this.roles=roles;
        this.username = username;
    }


    public JwtResponse() {

    }

    public Long getUser() {
        return userId;
    }

    public void setUser(Long userId) {
        this.userId = userId;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUserRole() {
        return roles;
    }

    public void setUserRole(String roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "userId=" + userId +
                ", jwtToken='" + jwtToken + '\'' +
                ", username='" + username + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }
}
