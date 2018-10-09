package cn.zsk.sys.mapper;


import cn.zsk.core.base.BaseMapper;
import cn.zsk.sys.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu,String> {

    /*
    * 获取子系统选项
    * */
    List<SysMenu> getMenuSuper();

    /**获取元节点*/
    List<SysMenu> getMenuNotSuper();

    /**
     * 获取子节点
     * @return
     */
    List<SysMenu> getMenuChildren(String id);

    List<SysMenu> getMenuChildrenAll(String id);

    /**
     * 根据用户获取所有菜单
     * @param id
     * @return
     */
    List<SysMenu> getUserMenu(@Param("id") String id);


    /*
    * 查询用户拥有的子系统数
    * */
    List<SysMenu> getUserSuperMenu(@Param("id") String id);

    //查询所有菜单项
    List<SysMenu> getAllMenu();

    //查询所有的子系统信息
    List<SysMenu> getAllSuperMenu();
}