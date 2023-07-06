package wakoo.fun.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @desc   使用token验证用户是否登录
 * @author zm
 **/
public class JwtUtils {
    private static final long EXPIRATION_TIME_SECONDS = 60; // token过期时间设为1小时
    private static final String SECRET_KEY = UUID.randomUUID().toString(); // 随机生成签名密钥
    /**
     * 生成JWT token
     * @param userId 用户ID
     * @return JWT token字符串
     */
    public static String generateToken(Integer userId) {
        System.out.println(SECRET_KEY);
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME_SECONDS * 1000);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 校验并解析JWT token
     * @param token JWT token字符串
     * @return 解析出的用户ID
     */
    public static Long verifyAndParseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            if(expirationDate.before(new Date())) {
                return null;
            }
            System.out.println(claims.getSubject());
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }
}
