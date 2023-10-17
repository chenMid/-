package wakoo.fun.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 角色表
 *
 * @author zhuhuix
 * @date 2021-09-03
 */
@ApiModel(value = "角色表")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRole implements Serializable {
    private Long id;

    private String roleCode;

    private String roleName;

    private String description;

    private Boolean enabled = true;

    private Timestamp createTime;

    private Timestamp updateTime = Timestamp.valueOf(LocalDateTime.now());
}
