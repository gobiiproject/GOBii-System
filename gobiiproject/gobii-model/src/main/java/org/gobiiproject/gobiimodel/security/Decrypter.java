package org.gobiiproject.gobiimodel.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Super secret decryption class. If you want to know how it works, ask
 *
 * @author Josh Lamos-Sweeney
 *         #securitythroughobscurity
 */

public class Decrypter {

    private static String secretPostgresScrambleKey;

    // the result of getSecretKey() is the value that you feed to the encrypter
    private static final String secretKey = "wenc4+PN393s3+7l3/Ptmw==";

    private static String getSecretKey() {
        return getSecretKey(secretKey);
    }

    /**
     * Given encrypted text and an 'extra key' to make node-specific keys, gives you a decrypted value.
     *
     * @param encrypted
     * @param extraKey  ignored if null
     * @return
     */
    public static String decrypt(String encrypted, String extraKey) {
        try {
            String key = getSecretKey();
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            byte[] decoded = decode(encrypted);
            if (extraKey != null) {
                unscramble(decoded, extraKey);
            }
            byte[] original = cipher.doFinal(decoded);

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static byte unscramble(byte b, char c) {
        int unsigned = b & 0xFF;
        unsigned -= c & 0xFF;
        if (unsigned < 0) unsigned += 0x100;
        return (byte) unsigned;
    }

    private static void unscramble(byte[] b, String s) {
        for (int i = 0; i < b.length; i++) {
            char c = s.charAt(i % s.length());
            b[i] = unscramble(b[i], c);
        }
    }

    private static String getSecretKey(String encoded) {
        byte[] decoded = Base64.getDecoder().decode(encoded);
        unscramble(decoded, "z");
        return new String(decoded);
    }

    private static byte[] decode(String value) {
        return Base64.getDecoder().decode(value);
    }


}
