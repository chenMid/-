package wakoo.fun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author HASEE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditVo {
    private String auditId;
    private Integer userId;
    private String auditPaht;
    private MultipartFile file;
    private Integer id;

    public AuditVo(Integer userId, String auditPaht,String auditId) {
        this.userId = userId;
        this.auditPaht = auditPaht;
        this.auditId = auditId;
    }
}
