package com.gin.admin.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EncryptUtils {
	public static final String AES_ECB_PKCS5 = "AES/ECB/PKCS5Padding";
	public static final String AES_ECB_PKCS7 = "AES/ECB/PKCS7Padding";

	/**
	 * 使用MessageDigest加密
	 *
	 * @param encryptType 加密方式:"MD5,SHA1"
	 * @param input       加密内容
	 * @return 加密结果
	 */
	public final static String messageDigest(String encryptType, byte[] input) {
		try {
			MessageDigest md = MessageDigest.getInstance(encryptType);
			md.update(input);
			byte[] out = md.digest();
			return StringUtils.byte2hex(out);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(
					"no Provider supports a MessageDigestSpi implementation for the specified algorithm : "
							+ encryptType);
		}
	}

	/**
	 * 使用MessageDigest加密
	 *
	 * @param encryptType 加密方式:"MD5,SHA1"
	 * @param inputStream 加密内容
	 * @return 加密结果
	 */
	public final static String messageDigest(String encryptType, InputStream inputStream) {
		try {
			MessageDigest md = MessageDigest.getInstance(encryptType);
			byte[] input = new byte[1024000];
			int length;
			while ((length = inputStream.read(input)) > 0) {
				md.update(input, 0, length);
			}
			byte[] out = md.digest();
			return StringUtils.byte2hex(out);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(
					"no Provider supports a MessageDigestSpi implementation for the specified algorithm : "
							+ encryptType);
		} catch (IOException e) {
			throw new IllegalArgumentException("InputStream read error");
		}
	}

	/**
	 * 使用MessageDigest加密
	 *
	 * @param encryptType 加密方式:"MD5,SHA1"
	 * @param string      加密内容
	 * @param charsetName 编码
	 * @return 加密结果
	 * @throws UnsupportedEncodingException
	 */
	public final static String messageDigest(String encryptType, String string, String charsetName)
			throws UnsupportedEncodingException {
		if (charsetName == null) {
			charsetName = "utf-8";
		}
		return messageDigest(encryptType, string.getBytes(charsetName));
	}

	/**
	 * MD5加密,使用utf-8解码
	 *
	 * @param string 待加密字符串
	 * @return 加密结果
	 * @throws UnsupportedEncodingException
	 */
	public final static String md5(String string) throws UnsupportedEncodingException {
		return messageDigest("MD5", string, "utf-8");
	}

	/**
	 * MD5加密,使用指定编码
	 *
	 * @param string
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public final static String md5(String string, String charset) throws UnsupportedEncodingException {
		return messageDigest("MD5", string, charset);
	}

	/**
	 * MD5加密文件
	 *
	 * @param inputStream 输入流
	 * @return 加密结果
	 */
	public final static String md5(InputStream inputStream) {
		return messageDigest("MD5", inputStream);
	}

	/**
	 * SHA1加密,使用utf-8解码
	 *
	 * @param string 待加密字符串
	 * @return 加密结果
	 * @throws UnsupportedEncodingException
	 */
	public final static String sha1(String string) throws UnsupportedEncodingException {
		return messageDigest("SHA1", string, "utf-8");
	}

	/**
	 * SHA1加密,使用指定解码
	 *
	 * @param string
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public final static String sha1(String string, String charset) throws UnsupportedEncodingException {
		return messageDigest("SHA1", string, charset);
	}

	/**
	 * SHA1加密文件
	 *
	 * @param inputStream 输入流
	 * @return 加密结果
	 */
	public final static String sha1(InputStream inputStream) {
		return messageDigest("SHA1", inputStream);
	}

	/**
	 * 原始密钥获取KEY
	 *
	 * @param bytes 密钥
	 * @param type  加密方式(AES、DES)
	 * @return KEY
	 */
	public static Key getKey(byte[] bytes, String type) {
		Key key = new SecretKeySpec(bytes, type);
		return key;
	}

	/**
	 * 密钥经过处理后获取KEY
	 *
	 * @param string 密钥
	 * @param type   加密方式(AES、DES)
	 * @param length 密钥长度(128,256)
	 * @return KEY
	 * @throws UnsupportedEncodingException
	 */
	public static final Key getKey(String string, String type, int length) throws UnsupportedEncodingException {
		type = type.toUpperCase();
		String s = md5(string);
		byte[] bytes = s.getBytes();
		byte[] b = null;
		if ("DES".equals(type)) {
			b = new byte[8];
			for (int i = 0; i < b.length; i++) {
				b[i] = (byte) (bytes[i] & bytes[i + 1] ^ bytes[31 - i] & bytes[30 - i]);
			}
		} else if ("AES".equals(type)) {
			b = new byte[16];
			for (int i = 0; i < b.length; i++) {
				b[i] = (byte) (bytes[i] ^ bytes[31 - i]);
			}
		}
		return getKey(b, type);
	}

	/**
	 * AES/DES 加密/解密
	 *
	 * @param data           源数据
	 * @param mode           加密、解密(Cipher.ENCRYPT_MODE,Cipher.DECRYPT_MODE)
	 * @param transformation 加/解密方式(AES、DES、AES/ECB/PKCS7Padding、AES/ECB/PKCS5Padding、AES/
	 *                       BCB/PKCS7Padding)
	 * @param key            密钥
	 * @return 加密/解密处理结果
	 */
	public static final byte[] cipher(byte[] data, int mode, String transformation, Key key) {
		boolean useJdk = true;
		try {
			Cipher cipher = null;
			if (key.getEncoded().length <= 16) {
				cipher = Cipher.getInstance(transformation);
			} else {
				useJdk = false;
				String providerName = BouncyCastleProvider.PROVIDER_NAME;
				if (Security.getProvider(providerName) == null) {
					Security.addProvider(new BouncyCastleProvider());
				}
				cipher = Cipher.getInstance(transformation, providerName);
			}
			cipher.init(mode, key);
			byte[] target = cipher.doFinal(data);
			return target;
		} catch (Exception e) {
			String message;
			if (useJdk) {
				message = e.getMessage();
			} else {
				message = "解密失败 - " + transformation + " - 密文错误 / JRE没有更新JCE模块 / 没有导入 - bcprov jar包 - "
						+ e.getMessage();
			}
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * AES/DES 加密
	 *
	 * @param source         源数据
	 * @param transformation 加密方式(AES、DES、AES/ECB/PKCS7Padding、AES/ECB/PKCS5Padding、AES/BCB
	 *                       /PKCS7Padding)
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static final String cipherEncrypt(String source, String transformation, Key key)
			throws UnsupportedEncodingException {
		byte[] data = source.getBytes("utf-8");
		return StringUtils.byte2hex(cipher(data, Cipher.ENCRYPT_MODE, transformation, key));
	}

	/**
	 * AES/DES 解密
	 *
	 * @param source         源数据
	 * @param transformation 解密方式(AES、DES、AES/ECB/PKCS7Padding、AES/ECB/PKCS5Padding、AES/BCB
	 *                       /PKCS7Padding)
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static final String cipherDecrypt(String source, String transformation, Key key)
			throws UnsupportedEncodingException {
		byte[] data = StringUtils.hex2byte(source);
		byte[] bytes = cipher(data, Cipher.DECRYPT_MODE, transformation, key);
		return new String(bytes, "utf-8");
	}

	/**
	 * Base64编码字典表
	 */
	private static final char[] BASE64_ENCODE_TABLE = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', '+', '/', '=' };
	/**
	 * Base64解码字典表
	 */
	private static final byte[] DECODE_TABLE = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1,
			62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8,
			9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29,
			30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };

	/**
	 * Base64加密,适用于URL传参
	 *
	 * @param data
	 * @return
	 */
	public final static String base64Encode4Url(byte[] data) {
		String s = base64Encode(data);
		return s == null ? null : s.replace("+", "-").replace("/", "_").replace("=", "");
	}

	/**
	 * Base64加密
	 *
	 * @param data
	 * @return
	 */
	public final static String base64Encode(byte[] data) {
		if (data == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int padding = data.length % 3 == 0 ? 0 : 3 - data.length % 3;
		int length = data.length / 3 * 3 + (padding == 0 ? 0 : 3);
		// 每次处理3字节
		for (int i = 0; i < length; i += 3) {
			// 根据MIME规范,每输出76个字符后添加一个新行
			if (i > 0 && i / 3 * 4 % 76 == 0) {
				sb.append("\r\n");
			}
			// 三个字节,变成一个24位数字
			int d1 = data[i] << 16 & 0xff0000;
			int d2 = (i + 1 >= data.length ? '\0' : data[i + 1]) << 8 & 0xff00;
			int d3 = (i + 2 >= data.length ? '\0' : data[i + 2]) & 0xff;
			int n = d1 | d2 | d3;
			// 24位数字分隔成4个6位数字
			int n1 = n >> 18 & 63;
			int n2 = n >> 12 & 63;
			int n3 = n >> 6 & 63;
			int n4 = n & 63;
			// 这四个6位数字作为base64转换表的索引
			sb.append(BASE64_ENCODE_TABLE[n1]);
			sb.append(BASE64_ENCODE_TABLE[n2]);
			sb.append(i + 2 > length - padding ? '=' : BASE64_ENCODE_TABLE[n3]);
			sb.append(i + 3 > length - padding ? '=' : BASE64_ENCODE_TABLE[n4]);
		}
		return sb.toString();
	}

	/**
	 * Base64解密,适用于URL传参
	 *
	 * @param str
	 * @return
	 */
	public final static byte[] base64Decode4Url(String str) {
		StringBuilder sb = new StringBuilder(str.replace("-", "+").replace("_", "/"));
		while (sb.length() % 4 != 0) {
			sb.append("=");
		}
		return base64Decode(sb.toString());
	}

	/**
	 * Base64解密
	 *
	 * @param str
	 * @return
	 */
	public final static byte[] base64Decode(String str) {
		str = StringUtils.deleteWithOut(str, BASE64_ENCODE_TABLE);
		String trimed = StringUtils.trim(str, "=");
		int padding = str.length() - trimed.length();
		if (padding > 0) {
			StringBuilder s = new StringBuilder(trimed);
			for (int i = 0; i < padding; i++) {
				s.append("A");
			}
			trimed = s.toString();
		}
		byte[] data = trimed.getBytes();
		byte[] b = new byte[data.length / 4 * 3 - padding];
		for (int i = 0; i < data.length; i += 4) {
			int index = i / 4 * 3;
			int n1 = DECODE_TABLE[data[i]] << 18;
			int n2 = DECODE_TABLE[data[i + 1]] << 12;
			int n3 = DECODE_TABLE[data[i + 2]] << 6;
			int n4 = DECODE_TABLE[data[i + 3]];
			int n = n1 | n2 | n3 | n4;
			int d1 = n >>> 16 & 0xff;
			int d2 = n >>> 8 & 0xff;
			int d3 = n & 0xff;
			b[index] = (byte) d1;
			if (index + 1 < b.length) {
				b[index + 1] = (byte) d2;
			}
			if (index + 2 < b.length) {
				b[index + 2] = (byte) d3;
			}
		}
		return b;
	}
}