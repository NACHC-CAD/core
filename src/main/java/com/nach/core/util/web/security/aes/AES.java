package com.nach.core.util.web.security.aes;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	private SecretKeySpec secretKey;

	private byte[] keyBytes;

	public AES(String key) {
		MessageDigest sha = null;
		try {
			keyBytes = key.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			keyBytes = sha.digest(keyBytes);
			keyBytes = Arrays.copyOf(keyBytes, 16);
			secretKey = new SecretKeySpec(keyBytes, "AES");
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public String encrypt(String strToEncrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public String decrypt(String strToDecrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}
}