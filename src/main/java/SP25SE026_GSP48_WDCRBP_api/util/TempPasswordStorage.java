package SP25SE026_GSP48_WDCRBP_api.util;

import java.util.concurrent.ConcurrentHashMap;

public class TempPasswordStorage {
    private static final ConcurrentHashMap<Long, String> passwordMap = new ConcurrentHashMap<>();

    public static void storePlainPassword(Long userId, String plainPassword) {
        passwordMap.put(userId, plainPassword);
    }

    public static String getPlainPassword(Long userId) {
        return passwordMap.remove(userId); // remove after sending
    }
}
