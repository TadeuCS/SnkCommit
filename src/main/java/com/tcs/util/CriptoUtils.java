package com.tcs.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Tirq
 */
public final class CriptoUtils {

    private static final String hexDigits = "0123456789abcdef";
    
    public static final List<String> ALGORITHMS = Arrays.asList("MD5", "SHA-1",
            "SHA-256", "SHA-512");

    private static final String AES = "AES";
    private static final String AES_INSTANCE = "AES/ECB/NoPadding";
    private static final String RC4 = "RC4";
    private static final String RC4_INSTANCE = "RC4/ECB/NoPadding";

    public static String criptografaSenha(String senha) {
        return cripto(cripto(senha, "MD5", "AES"), "SHA1", "AES");
    }

    public static String decriptografaSenha(String senha) {
        return decripto(decripto(senha, "SHA1", "AES"), "MD5", "AES");
    }

    public static byte[] digest(byte[] input, String algorithm)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.reset();
        return md.digest(input);
    }


    public static String byteArrayToHexString(byte[] b) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int j = ((int) b[i]) & 0xFF;
            buf.append(hexDigits.charAt(j / 16));
            buf.append(hexDigits.charAt(j % 16));
        }
        return buf.toString();
    }

    public static byte[] hexStringToByteArray(String hexa)
            throws IllegalArgumentException {
        if (hexa.length() % 2 != 0) {
            throw new IllegalArgumentException("String hexa inválida");
        }
        byte[] b = new byte[hexa.length() / 2];
        for (int i = 0; i < hexa.length(); i += 2) {
            b[i / 2] = (byte) ((hexDigits.indexOf(hexa.charAt(i)) << 4) | (hexDigits
                    .indexOf(hexa.charAt(i + 1))));
        }
        return b;
    }

    public static byte[] encode(byte[] input, String key, boolean aes)
            throws NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException,
            NoSuchPaddingException {
        SecretKeySpec skeySpec = new SecretKeySpec(hashKey(key).getBytes(),
                aes ? AES : RC4);
        Cipher cipher = Cipher.getInstance(aes ? AES_INSTANCE : RC4_INSTANCE);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(input);
        return encrypted;
    }

    public static byte[] decode(byte[] input, String key, boolean aes)
            throws NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException,
            NoSuchPaddingException {
        SecretKeySpec skeySpec = new SecretKeySpec(hashKey(key).getBytes(),
                aes ? AES : RC4);
        Cipher cipher = Cipher.getInstance(aes ? AES_INSTANCE : RC4_INSTANCE);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(input);
        return decrypted;
    }

    public static String hashKey(String key) throws NoSuchAlgorithmException {
        return CriptoUtils.byteArrayToHexString(digest(key.getBytes(), "MD5"))
                .substring(0, 16);
    }

    public static String nullPadString(String original) {
        StringBuffer output = new StringBuffer(original);
        int remain = output.length() % 16;
        if (remain != 0) {
            remain = 16 - remain;
            for (int i = 0; i < remain; i++) {
                output.append((char) 0);
            }
        }
        return output.toString();
    }

    public static String fromHex(byte[] hex) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hex.length; i++) {
            sb.append(Integer.toString((hex[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return sb.toString();
    }

    public static byte[] toHex(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] generateHashNio(FileInputStream inputStream, String algorithm) throws IOException {
        if (inputStream == null) {
            return null;
        }
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
            FileChannel channel = inputStream.getChannel();
            ByteBuffer buff = ByteBuffer.allocate(2048);
            while (channel.read(buff) != -1) {
                buff.flip();
                md.update(buff);
                buff.clear();
            }
            byte[] hashValue = md.digest();
            return hashValue;
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {

            }
        }
    }

    public static String cripto(String text, String key, String algorithm) {
        try {
            return fromHex(encode(CriptoUtils
                    .nullPadString(text).getBytes(), key, algorithm
                    .equals("AES") ? true : false));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String decripto(String text, String key, String algorithm) {
        try {
            return new String(decode(toHex(text), key,
                    algorithm.equals("AES") ? true : false)).trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static MessageDigest md = null;

    static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }

    private static char[] hexCodes(byte[] text) {
        char[] hexOutput = new char[text.length * 2];
        String hexString;

        for (int i = 0; i < text.length; i++) {
            hexString = "00" + Integer.toHexString(text[i]);
            hexString.toUpperCase().getChars(hexString.length() - 2,
                    hexString.length(), hexOutput, i * 2);
        }
        return hexOutput;
    }

    public static String cryptMD5(String texto) {
        if (md != null) {
            return new String(hexCodes(md.digest(texto.getBytes())));
        }
        return null;
    }
    
}
