package gr.aueb.cf.schoolapp.service.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashUtil {
    private static final int WORKLOAD = 12;

    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt(WORKLOAD);
        return BCrypt.hashpw(password, salt);
    }
}
