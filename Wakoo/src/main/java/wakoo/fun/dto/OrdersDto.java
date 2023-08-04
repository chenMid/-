package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDto {
    private Integer id;
    private Integer classCampusId;
    private Integer classTypeId;
    private BigDecimal money;
    private String expiry;
    private Integer totalQuantity;
    private Integer numberOfUse;
    private Integer remainingOrder;
    private String createTime;
    private String updateTime;
}
