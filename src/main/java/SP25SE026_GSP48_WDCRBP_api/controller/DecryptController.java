package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.util.AESUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/decrypt")
@CrossOrigin(origins = "*")
public class DecryptController {

        private static final String AES_KEY = "YourSecretKey123"; // same key as used to encrypt

        @GetMapping("/decrypt-data")
        public ResponseEntity<?> decryptData(@RequestParam("value") String encryptedValue) {
            try {
                String decrypted = AESUtil.decrypt(encryptedValue, AES_KEY);
                return CoreApiResponse.success(decrypted, "Giải mã thành công");
            } catch (Exception e) {
                return CoreApiResponse.error("Invalid or malformed encrypted data.");
            }
        }

        @GetMapping("/decrypt-multi")
        public ResponseEntity<?> decryptMultiValues(@RequestParam("value") String encryptedQuery) {
            try {
                Map<String, String> decrypted = AESUtil.decryptMultipleFromQuery(encryptedQuery, "YourSecretKey123");
                return CoreApiResponse.success(decrypted,"Giải mã thành công");
            } catch (Exception e) {
                return CoreApiResponse.error("Invalid or malformed encrypted data.");
            }
        }
}
