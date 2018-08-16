package cn.zsk.sys.service;

import cn.zsk.core.base.BaseService;
import cn.zsk.sys.entity.SysMenu;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * @author zsk
 * @date 2017/12/12.
 *
 */
public interface MenuService extends BaseService<SysMenu,String> {

  List<SysMenu> getMenuNotSuper();

  @Override
  int insert(SysMenu menu);


  List<SysMenu> getMenuChildren(String id);

  JSONArray getMenuJsonList();

  List<SysMenu> getMenuChildrenAll(String id);

  JSONArray getTreeUtil(String roleId);

  List<SysMenu> getUserMenu(String id);

  JSONArray getMenuJsonByUser(List<SysMenu> menuList);

  List<SysMenu> getAllMenu();
}
