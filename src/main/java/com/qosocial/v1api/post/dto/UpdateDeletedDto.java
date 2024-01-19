package com.qosocial.v1api.post.dto;

public class UpdateDeletedDto {
    private boolean wantsToDelete;

    public UpdateDeletedDto() {
    }

    public UpdateDeletedDto(boolean wantsToDelete) {
        this.wantsToDelete = wantsToDelete;
    }

    public boolean getWantsToDelete() {
        return wantsToDelete;
    }

    public void setWantsToDelete(boolean wantsToDelete) {
        this.wantsToDelete = wantsToDelete;
    }
}
