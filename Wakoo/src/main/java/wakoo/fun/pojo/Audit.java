package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**用户=审核实体类
 * @author HASEE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Audit {
    private Integer id;
    private String personName;
    private String sex;
    private Integer age;
    private String personUser;
    private String agent;
    private String contract;
    private String studentClass;
    private Integer status;
    private String createTime;
    private String updateTime;
    private Integer orderId;
    private MultipartFile file;
}
