package hu.schonherz.project.admin.service.api.encrypter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.NonNull;

public final class Encrypter {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private Encrypter() {
    }

    public static String encrypt(@NonNull final String password) {
        return ENCODER.encode(password);
    }

    public static boolean match(@NonNull final String currentEncryptedPass, final String plainTextPass) {
        return ENCODER.matches(plainTextPass, currentEncryptedPass);
    }
}
