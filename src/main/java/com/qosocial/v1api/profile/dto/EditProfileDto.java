package com.qosocial.v1api.profile.dto;

import jakarta.validation.constraints.Size;

public class EditProfileDto {

    @Size(min = 0, max = 200, message = "Bio must not exceed 200 characters")
    public String bio;

    private boolean deletingPicture;

    public EditProfileDto() {
    }

    public EditProfileDto(String bio, boolean deletingPicture) {
        this.bio = bio;
        this.deletingPicture = deletingPicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isDeletingPicture() {
        return deletingPicture;
    }

    public void setDeletingPicture(boolean deletingPicture) {
        this.deletingPicture = deletingPicture;
    }
}
