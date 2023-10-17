package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**相关订单处理
 * @author HASEE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDto {
    private Integer id;
    private Integer name;
    private Integer subclassName;
    private Integer classFtypeId;
    private BigDecimal money;
    private String expiry;
    private String outExpiry;
    private String select;
    private Integer totalQuantity;
    private Integer numberOfUse;
    private Integer remainingOrder;
    private String createTime;
    private String updateTime;
    private Integer status;
}
