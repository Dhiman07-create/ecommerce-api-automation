package utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class Encryption {

    public static SecretKeySpec createSecretKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String secretKey = "Secret";
        byte[] salt = new String("12345678").getBytes();
        int iterationCount = 40000;
        int keyLength = 128;
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }
        public static String encrypt(String dataToEncrypt) {
            try {
                Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                pbeCipher.init(Cipher.ENCRYPT_MODE, createSecretKey());
                AlgorithmParameters parameters = pbeCipher.getParameters();
                IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
                byte[] cryptoText = pbeCipher.doFinal(dataToEncrypt.getBytes(StandardCharsets.UTF_8));
                byte[] iv = ivParameterSpec.getIV();
                return base64Encode(iv) + ":" + base64Encode(cryptoText);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }

    private static String base64Encode(byte[] bytes) { return Base64.getEncoder().encodeToString(bytes);}

    public static String decrypt(String string) {
        try {
            String iv = string.split("")[0];
            String property = string.split("")[1];
            Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            pbeCipher.init(Cipher.DECRYPT_MODE, createSecretKey(), new IvParameterSpec(base64Decode(iv)));
            return new String(pbeCipher.doFinal(base64Decode (property)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }

    public static String decryptPassword(String encryptPassword) {
        var inputDataString = "RVFBIEF1d69tYXRpb24=";
        var setPassword = new String(org.apache.commons.codec.binary.Base64.decodeBase64(inputDataString.getBytes()));
        var decrypt = new StandardPBEStringEncryptor();
        decrypt.setPassword(setPassword);
        return decrypt.decrypt(encryptPassword.replaceAll("ENC[()]", ""));
    }
}

