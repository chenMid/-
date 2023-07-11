package wakoo.fun.utils;

import org.springframework.beans.factory.annotation.Autowired;
import wakoo.fun.dto.ButtonJurisdiction;
import wakoo.fun.pojo.ButtonPermissions;
import wakoo.fun.pojo.SysMenu;
import wakoo.fun.service.RoleService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuTreeRole {
    // 流程：数据库查出的菜单记录装载在承载菜单的列表中---
    //         构建树（获取根节点，遍历根节点，对每一个根节点构建子树）---得到最终树形菜单

    // 承载菜单的列表
    private List<ButtonPermissions> menuList = new ArrayList<>();
    // 带参构造器，将数据库中的菜单数据记录，装载在我们承载菜单的列表中
    public MenuTreeRole(List<ButtonPermissions> menuList){
        this.menuList = menuList;
    }

    // 获取根节点
    public List<ButtonPermissions> getRootNode(){
        List<ButtonPermissions> rootNode = new ArrayList<>();
        for (ButtonPermissions menu : menuList) {
            if (menu.getPid().equals("0")){
                rootNode.add(menu);
            }
        }
        return rootNode;
    }


    public ButtonPermissions buildChildren(ButtonPermissions rootNode) {
        List<ButtonPermissions> childrenTree = new ArrayList<>();
        for (ButtonPermissions menu : menuList) {
            if (menu.getPid().equals(rootNode.getId())) {
                ButtonPermissions childNode = buildChildren(menu);
                childrenTree.add(childNode);
            }
        }
        rootNode.setChildren(childrenTree);;
        return rootNode;
    }



    // 构建树
    public List<ButtonPermissions> buildTree(){
        List<ButtonPermissions> menus = getRootNode();
        for (ButtonPermissions menu : menus) {
            buildChildren(menu);
        }
        return menus;
    }
}