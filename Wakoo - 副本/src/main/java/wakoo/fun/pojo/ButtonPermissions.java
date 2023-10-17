package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wakoo.fun.dto.ButtonJurisdiction;
import wakoo.fun.service.TreeNode;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ButtonPermissions<T> implements TreeNode{
    private String id;
    private String label;
    private String pid;
    private String ppid;
    private Integer enabled;
    private Integer type;
    private List<? extends Object> children;


    @Override
    public <T extends TreeNode> void setChildren(List<T> children) {
        this.children=children;
    }

}
