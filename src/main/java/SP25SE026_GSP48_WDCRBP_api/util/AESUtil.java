package SP25SE026_GSP48_WDCRBP_api.util;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AESUtil {
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;

    private static SecretKeySpec getKeySpec(String key) throws Exception {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, AES_ALGORITHM);
    }

    public static String encrypt(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, getKeySpec(key), parameterSpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        byte[] encryptedIvAndText = new byte[GCM_IV_LENGTH + encryptedBytes.length];
        System.arraycopy(iv, 0, encryptedIvAndText, 0, GCM_IV_LENGTH);
        System.arraycopy(encryptedBytes, 0, encryptedIvAndText, GCM_IV_LENGTH, encryptedBytes.length);
        return Base64.getUrlEncoder().encodeToString(encryptedIvAndText);
    }

    public static String decrypt(String encryptedData, String key) throws Exception {
        byte[] decodedData = Base64.getUrlDecoder().decode(encryptedData);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, decodedData, 0, GCM_IV_LENGTH);
        cipher.init(Cipher.DECRYPT_MODE, getKeySpec(key), parameterSpec);
        byte[] decryptedBytes = cipher.doFinal(decodedData, GCM_IV_LENGTH, decodedData.length - GCM_IV_LENGTH);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static Map<String, String> decryptMultipleFromQuery(String queryString, String key) throws Exception {
        Map<String, String> result = new HashMap<>();

        // Normalize & split
        String decodedQuery = URLDecoder.decode(queryString, StandardCharsets.UTF_8);
        String[] pairs = decodedQuery.split("&&");

        for (String pair : pairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                String param = parts[0];
                String encrypted = parts[1];
                String decrypted = decrypt(encrypted, key);
                result.put(param, decrypted);
            }
        }
        return result;
    }
}
