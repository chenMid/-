package wakoo.fun.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wakoo.fun.service.AdminAdministrationService;
import wakoo.fun.vo.AgentIdrId;

import javax.annotation.Resource;
import java.util.List;

/** 角色工具类
 * @author HASEE
 */
@Component
public class RoleUtils  {

    public static int findParent(List<AgentIdrId> data, Integer targetId, int[] validIds) {
        for (AgentIdrId item : data) {
            if (item.getId().equals(targetId)) {
                for (int validId : validIds) {
                    if (item.getRid() == validId) {
                        return item.getRid();
                    }
                }
                return findParent(data, item.getRid(), validIds);
            }
        }
        return -1;
    }

    public int getParentId(int parentId, Integer role,List<AgentIdrId> roles) {
        // 定义一个整型数组validIds，包含有效的角色ID
        int[] validIds = {0, 1, 2, 3};
        // 遍历validIds数组，将每个元素依次赋值给变量val
        for (int val : validIds) {
            // 判断role是否和val相等
            if (role.equals(val)) {
                // 如果相等，将parentId赋值为role
                parentId = role;
                // 跳出循环，结束遍历
                break;
            } else {
                // 调用RoleUtils的findParent方法，传入roles、role和validIds参数，根据规则获取新的parentId值，并赋值给parentId变量
                parentId = RoleUtils.findParent(roles, role, validIds);
            }
        }
        // 返回parentId的值作为方法的返回值
        return parentId;
    }
}
