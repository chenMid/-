package wakoo.fun.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yi qiang
 * @date 2021/10/5 14:35
 */
public interface TreeNode {
    /**
     * 获取id
     *
     * @return id
     */
    String getId();

    /**
     * 获取父类id
     *
     * @return 父类id
     */
    String getPid();

    /**
     * 获取子类集合
     *
     * @return 子类集合
     */
    <T extends TreeNode> List<T> getChildren();

    /**
     * 设置子类集合
     *
     * @param children 子类集合
     */
    <T extends TreeNode> void setChildren(List<T> children);

}

