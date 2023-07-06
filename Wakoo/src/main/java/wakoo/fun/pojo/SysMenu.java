package wakoo.fun.pojo;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wakoo.fun.service.TreeNode;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 菜单表
 *
 * @author zhuhuix
 * @date 2021-10-06
 */
@ApiModel(value = "菜单表")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenu implements TreeNode {
        private String id;

        private String path;

        private String component;

        private String name;

        private String type;

        private String pid;

        private Boolean hidden;

        private String icon;

        private Boolean cache;

        private String url;

        private Integer sort;

        private Integer level;

        private Boolean enabled = true;

        private Timestamp createTime;

        private Timestamp updateTime = Timestamp.valueOf(LocalDateTime.now());

        private Map<String,Object>meta;

        private List<SysMenu> children;

        @Override
        public <T extends TreeNode> void setChildren(List<T> list) {
                this.children = (List<SysMenu>) list;
        }

}

