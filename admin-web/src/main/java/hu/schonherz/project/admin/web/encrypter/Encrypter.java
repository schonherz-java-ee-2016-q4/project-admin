package hu.schonherz.project.admin.web.encrypter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.NonNull;

public final class Encrypter {

    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private Encrypter() {
    }

    public static String encrypt(@NonNull final String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public static boolean match(@NonNull final String currentPass, final String password) {
        return bCryptPasswordEncoder.matches(password, currentPass);
    }
}
