package wakoo.fun.vo;

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
    private String typeName;
    private String subclassName;
}
