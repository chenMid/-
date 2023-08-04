package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 代理订单实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    private Integer id;
    private String name;
    private String subclassName;
    private BigDecimal money;
    private String expiry;
    private Integer totalQuantity;
    private Integer numberOfUse;
    private Integer remainingOrder;
    private Date createTime;
    private Date updateTime;
}
