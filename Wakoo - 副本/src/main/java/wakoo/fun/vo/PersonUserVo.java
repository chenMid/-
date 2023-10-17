package wakoo.fun.vo;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author HASEE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonUserVo {
    private Integer id;
    private Integer agentId;
    private String subclassName;
}
