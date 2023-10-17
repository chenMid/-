package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HASEE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Videos {
    private Integer id;
    private String typeName;
    private Double videoLength;
    private String classTypeName;
    private Integer which;
    private String title;
    private String videoImageUrl;
}
