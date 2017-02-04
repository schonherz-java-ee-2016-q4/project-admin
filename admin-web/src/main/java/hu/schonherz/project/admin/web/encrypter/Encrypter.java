package hu.schonherz.project.admin.web.encrypter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encrypter {
	static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	public static String encrypt(String password) {	
		String encryptedPass = bCryptPasswordEncoder.encode(password);
		return encryptedPass;
	}
	
	public static boolean match(String currentPass, String password) {
		return bCryptPasswordEncoder.matches(password, currentPass);
	}
}
