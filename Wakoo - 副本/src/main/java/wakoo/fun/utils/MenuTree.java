package wakoo.fun.utils;

import wakoo.fun.pojo.SysMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuTree {

    // 流程：数据库查出的菜单记录装载在承载菜单的列表中---
    //         构建树（获取根节点，遍历根节点，对每一个根节点构建子树）---得到最终树形菜单

    // 承载菜单的列表
    private List<SysMenu> menuList = new ArrayList<>();
    // 带参构造器，将数据库中的菜单数据记录，装载在我们承载菜单的列表中
    public MenuTree(List<SysMenu> menuList){
        this.menuList = menuList;
    }

    // 获取根节点
    public List<SysMenu> getRootNode(){
        List<SysMenu> rootNode = new ArrayList<>();
        for (SysMenu menu : menuList) {
            if (menu.getPid().equals("0")){
                Map<String,Object>map=new HashMap<>();
                map.put("title", menu.getName());
                map.put("icon", menu.getIcon());
                menu.setMeta(map);

                rootNode.add(menu);
            }
        }
        return rootNode;
    }

    public SysMenu buildChildren(SysMenu rootNode) {
        List<SysMenu> childrenTree = new ArrayList<>();
        for (SysMenu menu : menuList) {
            if (menu.getPid().equals(rootNode.getId())) {
                SysMenu childNode = buildChildren(menu);
                Map<String,Object>map=new HashMap<>();
                map.put("title", menu.getName());
                map.put("icon", menu.getIcon());
                menu.setMeta(map);
                childrenTree.add(childNode);

            }
        }
        rootNode.setChildren(childrenTree);
        return rootNode;
    }
    // 构建树
    public List<SysMenu> buildTree(){
        List<SysMenu> menus = getRootNode();
        for (SysMenu menu : menus) {
            buildChildren(menu);
        }
        return menus;
    }
}