package wakoo.fun.controller.kaptchaController;

import io.swagger.annotations.Api;
import jdk.nashorn.internal.runtime.options.OptionTemplate;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wakoo.fun.config.PassToken;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "Admin")
public class verifyCode {

    @Resource
    private StringRedisTemplate stringRedisTemplate ;

    @PassToken
    @GetMapping("/getKaptchaImage")
    public ResponseEntity<Map<String, String>> getKaptchaImage(HttpServletRequest request) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = request.getSession(true);
        session.setAttribute("verify_code", text);
        String key = java.util.UUID.randomUUID().toString();
        // 构建返回给前端的JSON对象
        Map<String, String> response = new HashMap<>();
        stringRedisTemplate.opsForValue().set(String.valueOf(key), text, 60, TimeUnit.SECONDS);
        response.put("key", key);
        String imageBase64 = encodeImageToBase64(image);
        response.put("image", "data:image/png;base64," + imageBase64);
        return ResponseEntity.ok(response);
    }
    // 将图片转换为Base64编码的字符串
    private String encodeImageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        byte[] imageBytes = os.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
