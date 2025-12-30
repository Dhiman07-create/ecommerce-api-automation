package models;

import lombok.Data;


@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private int id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String image;
}