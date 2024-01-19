package com.qosocial.v1api.profile.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class NoReservedWordsValidator implements ConstraintValidator<NoReservedWords, String> {
    /**
     Validation ensures the username is not EXACTLY a reserved word (not case sensitive)
     A username of "moveableroots" would be allowed
     A username of "root", "Root", "rOoT", etc would NOT be allowed
     */

    private final List<String> reservedWords = Arrays.asList(
            "admin", "administrator", "root", "sysadmin", "system", "webmaster", "moderator", "support",
            "guest", "user", "anonymous", "null", "undefined", "localhost", "server", "domain", "api",
            "website", "superuser", "manager", "director", "officer", "member", "employee", "http",
            "https", "www", "com", "net", "org", "email", "mail", "ftp", "help", "support", "about",
            "terms", "privacy", "login", "logout", "new", "delete", "edit"
    );

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return false;
        }

        // Check against reserved words (case-insensitive)
        return reservedWords.stream()
                .noneMatch(reservedWord -> reservedWord.equalsIgnoreCase(username.trim()));
    }
}

