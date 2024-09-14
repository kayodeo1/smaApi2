package auth.kayodeo1.com;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PasswordCrypto {
    private static final String ALGORITHM = "AES";

    public static String encrypt(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedData, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    public static void main(String[] args) throws Exception {
		PasswordCrypto pwd = new PasswordCrypto();
		ArrayList<String> keys  = new ArrayList();
		keys.add("LSMIST");
		keys.add("2024");
		keys.add("JaVa");
		keys.add("Mira");
		String key = KeyGenerator.generateKey(keys);
		String value =PasswordCrypto.decrypt("viioKQ2MQdwcTSaeMEEntw=="
				+ "", key);
		System.out.println(value);

	}
}

