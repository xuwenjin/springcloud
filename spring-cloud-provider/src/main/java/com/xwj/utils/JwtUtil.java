package com.xwj.utils;

import java.util.Date;

import com.xwj.common.CommonConsts;
import com.xwj.jwt.IJwtInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;

/**
 * jwt工具类
 */
public class JwtUtil {

	/** jwt签发者 */
	private static final String ISSUER = "xwj";

	/**
	 * 生成token(RS256加密 + 标准声明(建议但不强制使用))
	 * 
	 * @param privateKey
	 *            RSA私钥
	 */
	@SneakyThrows
	public static String generateTokenByPriKey(IJwtInfo jwtInfo, String privateKey, int expire) {
		Date now = new Date();
		Date expireDate = new Date(System.currentTimeMillis() + expire * 1000);
		JwtBuilder builder = Jwts.builder();
		String compactJws = builder.setIssuer(ISSUER)// jwt签发者
				.setSubject(jwtInfo.getUniqueName()) // jwt所面向的用户
				.setIssuedAt(now) // jwt的签发时间
				.setExpiration(expireDate) // jwt的过期时间，这个过期时间必须要大于签发时间
				.setNotBefore(now) // 当前时间之前不可用
				.claim(CommonConsts.JWT_KEY_USER_ID, jwtInfo.getId())
				.claim(CommonConsts.JWT_KEY_NAME, jwtInfo.getName())
				.signWith(SignatureAlgorithm.RS256, RSAUtil.getPrivateKey(privateKey)).compact();
		return compactJws;
	}

	/**
	 * RSA公钥解析token
	 * 
	 * @param pubKey
	 *            RSA公钥
	 */
	@SneakyThrows
	public static Claims parserTokenByPubKey(String token, String pubKey) {
		return Jwts.parser().setSigningKey(RSAUtil.getPublicKey(pubKey)).parseClaimsJws(token).getBody();
	}

	/**
	 * 是否过期
	 */
	@SneakyThrows
	public static boolean isExpirateByPubKey(String token, String pubKey) {
		Claims chaims = parserTokenByPubKey(token, pubKey);
		Integer exp = (Integer) chaims.get(Claims.EXPIRATION);
		long now = (long) System.currentTimeMillis() / 1000;
		return exp - now > 0;
	}

	/**
	 * 生成token(HS512加密 + 标准声明(建议但不强制使用))
	 * 
	 * @param secret
	 *            秘钥
	 */
	@SneakyThrows
	public static String generateToken(IJwtInfo jwtInfo, String secret, int expire) {
		Date now = new Date();
		Date expireDate = new Date(System.currentTimeMillis() + expire * 1000);
		JwtBuilder builder = Jwts.builder();
		String compactJws = builder.setIssuer(ISSUER)// jwt签发者
				.setSubject(jwtInfo.getUniqueName()) // jwt所面向的用户
				.setIssuedAt(now) // jwt的签发时间
				.setExpiration(expireDate) // jwt的过期时间，这个过期时间必须要大于签发时间
				.setNotBefore(now) // 当前时间之前不可用
				.claim(CommonConsts.JWT_KEY_USER_ID, jwtInfo.getId())
				.claim(CommonConsts.JWT_KEY_NAME, jwtInfo.getName()).signWith(SignatureAlgorithm.HS512, secret)
				.compact();
		return compactJws;
	}

	/**
	 * 解析token
	 * 
	 * @param secret
	 *            秘钥
	 */
	@SneakyThrows
	public static Claims parserToken(String token, String secret) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

}