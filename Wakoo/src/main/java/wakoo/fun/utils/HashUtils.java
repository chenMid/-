package wakoo.fun.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    /**
     * 对密码进行哈希处理
     *
     * @param password 待哈希的密码
     * @return 哈希后的密码
     */
    public static String hash(String password) {
        try {
            // 创建MD5哈希算法实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将密码转换为字节数组并进行哈希计算
            byte[] hashBytes = md.digest(password.getBytes());

            // 将哈希值转换为十六进制字符串表示
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // 哈希算法不可用，抛出异常或进行其他错误处理
            throw new RuntimeException("Unable to perform hash operation", e);
        }
    }
}