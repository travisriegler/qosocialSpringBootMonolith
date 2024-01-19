package com.qosocial.v1api.profile.dto;

import com.qosocial.v1api.profile.validation.NoReservedWords;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateProfileDto {

    @NotBlank(message = "Username is required")
    @Size(min = 1, max = 20, message = "Username must be between 1 and 20 characters")
    @Pattern(regexp = "^[\\S]+$", message = "Username must not contain any whitespace characters")
    @NoReservedWords(message = "Username must not contain reserved words")
    public String username;

    @Size(min = 0, max = 200, message = "Bio must not exceed 200 characters")
    public String bio;


    public CreateProfileDto() {
    }

    public CreateProfileDto(String username, String bio) {
        this.username = username;
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
