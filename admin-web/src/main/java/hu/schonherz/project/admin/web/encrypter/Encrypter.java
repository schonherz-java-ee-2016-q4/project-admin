package hu.schonherz.project.admin.web.encrypter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class Encrypter {

    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private Encrypter() {
    }

    public static String encrypt(final String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public static boolean match(final String currentPass, final String password) {
        return bCryptPasswordEncoder.matches(password, currentPass);
    }
}
