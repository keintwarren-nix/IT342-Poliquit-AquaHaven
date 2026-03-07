package edu.cit.poliquit.aquahaven.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private boolean success;
    private Object data;
    private String error;
}