package com.qosocial.v1api.auth.dto;

import jakarta.validation.constraints.*;

public class RegisterDto {
    @NotBlank(message = "Email is required")
    @Size(min = 6, max = 60, message = "Email must be between 6 and 60 characters")
    @Email(message = "Invalid email format")
    public String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()+=])(?=\\S+$).*$", message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and must not contain whitespace")
    public String password;

    @AssertTrue(message = "You must accept the Terms of Service Agreement and the Privacy Policy")
    private boolean acceptedTerms = false;


    public RegisterDto() {
    }

    public RegisterDto(String email, String password, boolean acceptedTerms) {
        this.email = email;
        this.password = password;
        this.acceptedTerms = acceptedTerms;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean hasAcceptedTerms() {
        return acceptedTerms;
    }

    public void setAcceptedTerms(boolean acceptedTerms) {
        this.acceptedTerms = acceptedTerms;
    }
}

