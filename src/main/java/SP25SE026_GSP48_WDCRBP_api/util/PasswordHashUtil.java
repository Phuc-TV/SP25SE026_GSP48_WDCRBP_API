package SP25SE026_GSP48_WDCRBP_api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHashUtil {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Method to hash a plain text password
    public static String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    // Method to check if the plain password matches the hashed one
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}
