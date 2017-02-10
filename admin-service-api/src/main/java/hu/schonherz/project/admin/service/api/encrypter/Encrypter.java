package hu.schonherz.project.admin.service.api.encrypter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.NonNull;

public final class Encrypter {

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private Encrypter() {
    }

    public static String encrypt(@NonNull final String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public static boolean match(@NonNull final String currentEncryptedPass, final String plainTextPass) {
        return bCryptPasswordEncoder.matches(plainTextPass, currentEncryptedPass);
    }
}
