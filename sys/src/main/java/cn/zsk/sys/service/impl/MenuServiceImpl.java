package cn.zsk.sys.service.impl;

import cn.zsk.core.base.BaseMapper;
import cn.zsk.core.base.impl.BaseServiceImpl;
import cn.zsk.core.util.RandomCodeUtil;
import cn.zsk.core.util.SnowflakeIdUtil;
import cn.zsk.core.util.TreeUtil;
import cn.zsk.sys.entity.SysMenu;
import cn.zsk.sys.entity.SysRoleMenu;
import cn.zsk.sys.mapper.SysMenuMapper;
import cn.zsk.sys.mapper.SysRoleMenuMapper;
import cn.zsk.sys.service.MenuService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zsk
 * @date 2017/12/12.
 *
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<SysMenu, String> implements MenuService {

    @Autowired
    private SysMenuMapper menuDao;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Override
    public BaseMapper<SysMenu, String> getMappser() {
        return menuDao;
    }

    @Override
    public List<SysMenu> getMenuSuper() {
        return menuDao.getMenuSuper();
    }

    @Override
    public List<SysMenu> getMenuNotSuper() {
        return menuDao.getMenuNotSuper();
    }

    @Override
    public int insert(SysMenu menu) {
        return menuDao.insert(menu);
    }


    @Override
    public List<SysMenu> getMenuChildren(String id) {
        return menuDao.getMenuChildren(id);
    }

    public SysMenu child(SysMenu sysMenu, List<SysMenu> sysMenus, Integer pNum, Integer num) {
        List<SysMenu> childSysMenu = sysMenus.stream().filter(s ->
                s.getPId().equals(sysMenu.getId())).collect(Collectors.toList());
        sysMenus.removeAll(childSysMenu);
        SysMenu m;
        for (SysMenu menu : childSysMenu) {
            ++num;
            m = child(menu, sysMenus, pNum, num);
            sysMenu.addChild(menu);
        }
        return sysMenu;
    }

    @Override
    public JSONArray getMenuJsonList() {
        //查询所有的菜单项
        List<SysMenu> sysMenus = selectAll();
        //获取没有pid的最上级菜单
        List<SysMenu> supers = sysMenus.stream().filter(sysMenu ->
                StringUtils.isEmpty(sysMenu.getPId()))
                .collect(Collectors.toList());

        sysMenus.removeAll(supers);
        supers.sort(Comparator.comparingInt(SysMenu::getOrderNum));
        JSONArray jsonArr = new JSONArray();
        for (SysMenu sysMenu : supers) {
            SysMenu child = child(sysMenu, sysMenus, 0, 0);
            jsonArr.add(child);
        }
        return jsonArr;
    }

    @Override
    public JSONArray getMenuJsonByUser(List<SysMenu> menuList,String superMenuName) {
        JSONArray jsonArr = new JSONArray();
        Collections.sort(menuList, (o1, o2) -> {
            if (o1.getOrderNum() == null || o2.getOrderNum() == null) {
                return -1;
            }
            if (o1.getOrderNum() > o2.getOrderNum()) {
                return 1;
            }
            if (o1.getOrderNum().equals(o2.getOrderNum())) {
                return 0;
            }
            return -1;
        });

        //引进来雪花算法的工具类
        int workerId = 0;
        int datacenterId=0;
        SnowflakeIdUtil idWorker = new SnowflakeIdUtil(workerId, datacenterId);

        for (SysMenu menu : menuList) {
            if (StringUtils.isEmpty(menu.getPId())) {
                SysMenu sysMenu = getChilds(menu,menuList,idWorker);
                jsonArr.add(sysMenu);
            }
        }
        if(jsonArr.size() != 0){
            /*
            * 判断要加载的子系统内容，如果子系统为空默认加载该用户拥有的第一个子系统。
            * 判断方法：加载该用户拥有的所有的菜单，然后通过传过来的子系统的名称（superMenuName），进行筛选，获取该子系统下的菜单项。
            *
            * */
            if(superMenuName != null){
                List<SysMenu> sysMenuList = JSONObject.parseArray(jsonArr.toJSONString(),SysMenu.class);
                List<SysMenu> sysMenuList1 = sysMenuList.stream().filter(sysMenu -> superMenuName.equals(sysMenu.getName())).collect(Collectors.toList());
                jsonArr = JSONArray.parseArray(JSON.toJSONString(sysMenuList1));
                JSONObject jo = jsonArr.getJSONObject(0);
                jsonArr = (JSONArray) jo.get("children");
            }else {
                JSONObject jo = jsonArr.getJSONObject(0);
                jsonArr = (JSONArray) jo.get("children");
            }

        }
        return jsonArr;
    }

    /*
    *由于layerui选项卡中没有面板都需要有一个唯一的id作为标识。
    *在生成随机数作为面板id的时候，这里采用了一下雪花算法生成一个id，将这个id作为Random函数的基数（其实这里完全没有必要这么麻烦，
    * 可以直接用一个简单的random类，这里就是玩一下。）
    *
    * 同时这里声明一下，关于雪花算法的使用，这里可能会有错误。后期了解更多，继续修改。
    *
    *具体雪花算法，可以查看该类了解。
    * workerId和datacenterId两个参数的范围是0-31
    *
    * */
    public SysMenu getChilds(SysMenu menu, List<SysMenu> menuList,SnowflakeIdUtil idWorker) {

        for (SysMenu menus : menuList) {
            //这里根据pid匹配，并且排除按钮和子系统选择项
            if (menu.getId().equals(menus.getPId()) && menus.getMenuType() != 2 && menus.getMenuType() != 3) {
                //生成随机数作为选项卡每个面板的id
                int tableId = Integer.valueOf(RandomCodeUtil.getRandNumber(5,idWorker));
                SysMenu m = getChilds(menus,  menuList,idWorker);
                m.setNum(tableId);
                menu.addChild(m);
            }
        }
        return menu;

    }

    @Override
    public List<SysMenu> getMenuChildrenAll(String id) {
        return menuDao.getMenuChildrenAll(id);
    }

    @Override
    public JSONArray getTreeUtil(String roleId) {
        TreeUtil treeUtil = null;
        List<SysMenu> sysMenus = selectAll();
        List<SysMenu> supers = sysMenus.stream().filter(sysMenu ->
                StringUtils.isEmpty(sysMenu.getPId()))
                .collect(Collectors.toList());
        sysMenus.removeAll(supers);
        supers.sort(Comparator.comparingInt(SysMenu::getOrderNum));
        JSONArray jsonArr = new JSONArray();
        for (SysMenu sysMenu : supers) {
            treeUtil = getChildByTree(sysMenu, sysMenus, 0, null, roleId);
            jsonArr.add(treeUtil);
        }
        return jsonArr;

    }

    @Override
    public List<SysMenu> getUserMenu(String id) {
        return menuDao.getUserMenu(id);
    }

    @Override
    public List<SysMenu> getUserSuperMenu(String id) {
        return menuDao.getUserSuperMenu(id);
    }

    public TreeUtil getChildByTree(SysMenu sysMenu, List<SysMenu> sysMenus, int layer, String pId, String roleId) {
        layer++;
        List<SysMenu> childSysMenu = sysMenus.stream().filter(s ->
                s.getPId().equals(sysMenu.getId())).collect(Collectors.toList());
        sysMenus.removeAll(childSysMenu);
        TreeUtil treeUtil = new TreeUtil();
        treeUtil.setId(sysMenu.getId());
        treeUtil.setName(sysMenu.getName());
        treeUtil.setLayer(layer);
        treeUtil.setPId(pId);
        /**判断是否存在*/
        if (!StringUtils.isEmpty(roleId)) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(sysMenu.getId());
            sysRoleMenu.setRoleId(roleId);
            int count = roleMenuMapper.selectCountByCondition(sysRoleMenu);
            if (count > 0) {
                treeUtil.setChecked(true);
            }
        }
        for (SysMenu menu : childSysMenu) {
            TreeUtil m = getChildByTree(menu, sysMenus, layer, menu.getId(), roleId);
            treeUtil.getChildren().add(m);
        }
        return treeUtil;
    }

    @Override
    public List<SysMenu> getAllMenu() {
        return menuDao.getAllMenu();
    }

    @Override
    public List<SysMenu> getAllSuperMenu() {
        return menuDao.getAllSuperMenu();
    }
}
