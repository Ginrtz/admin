package com.gin.admin.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gin.admin.model.base.ResponseResult;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * jwt token工具类
 *
 * @author o1760
 */
@Component
public class JwtUtil {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	/** token秘钥 */
	final static String base64EncodedSecretKey = "MWEyYjNjNGQ1ZQ==";
	/** 过期时间,测试使用十分钟 */
	final static long TOKEN_EXPIRES_MINUTES = 10;

	/**
	 * 根据登录名生成token，并放入缓存
	 *
	 * @param userName
	 * @return
	 */
	public String getToken(String userName) {
		String token = Jwts.builder().setSubject(userName) // 主题
				.setIssuedAt(new Date()) // 签发时间
				.signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey) // 签名算法及秘钥
				.compact(); // 生成
		// token放到缓存并设置过期时间
		redisTemplate.boundValueOps(userName).set(token, JwtUtil.TOKEN_EXPIRES_MINUTES, TimeUnit.MINUTES);
		return token;
	}

	/**
	 * 验证token
	 *
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public ResponseResult checkToken(String token) {
		ResponseResult result = new ResponseResult();
		try {
			// 解析token，获取绑定的用户名
			final Claims claims = Jwts.parser().setSigningKey(base64EncodedSecretKey).parseClaimsJws(token).getBody();
			final String username = claims.getSubject();
			// 从redis中找已经绑定的token
			String token_old = (String) redisTemplate.boundValueOps(username).get();
			// 没有找到，表示已超时被丢弃
			if (StringUtils.isEmpty(token_old)) {
				result.setCode(ResponseResult.CODE_TOKEN_INVALID);
				result.setMessage("登录超时");
				return result;
			}
			// token和redis中保存的不同，表示重复生成了token，即多客户端登录
			if (!token_old.equals(token)) {
				result.setCode(ResponseResult.CODE_TOKEN_KICKED_OUT);
				result.setMessage("用户在其他地点登录");
				return result;
			}
			result.put("username", username);
		} catch (Exception e) {
			// token校验失败，可能是伪造的token
			result.setCode(ResponseResult.CODE_TOKEN_INVALID);
			result.setMessage("token无效");
			return result;
		}
		return result;
	}

	/**
	 * 移除token，相当于踢出用户
	 *
	 * @param username
	 * @return
	 */
	public ResponseResult removeToken(String username) {
		ResponseResult result = new ResponseResult();
		redisTemplate.delete(username);
		return result;
	}
}
