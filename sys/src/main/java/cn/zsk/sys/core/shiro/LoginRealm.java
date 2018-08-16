package cn.zsk.sys.core.shiro;

import cn.zsk.core.base.CurrentMenu;
import cn.zsk.core.base.CurrentRole;
import cn.zsk.core.base.CurrentUser;
import cn.zsk.sys.entity.SysMenu;
import cn.zsk.sys.entity.SysRole;
import cn.zsk.sys.entity.SysUser;
import cn.zsk.sys.service.*;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * @author zsk
 * @date 2017/12/4.
 *
 */
@Service
public class LoginRealm extends AuthorizingRealm{

  @Autowired
  private SysUserService userService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private MenuService menuService;

  @Autowired
  private RoleUserService roleUserService;

  @Autowired
  private RoleMenuService roleMenuService;

  /**
   * 获取认证
   * @param principalCollection
   * @return
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    String name= (String) principalCollection.getPrimaryPrincipal();
    SysUser user = userService.login(name);

    //超级管理员拥有所有的角色和权限
    if("31ac66522912498881c4c3c9f15fd73c".equals(user.getId())){
      //查询所有的角色信息
      SysRole sysRole = new SysRole();
      List<SysRole> sysRoleList = roleService.selectListByPage(sysRole);
      for(SysRole sysRole1:sysRoleList){
        info.addRole(sysRole1.getId());
      }
      //查询所有的菜单信息
      List<SysMenu> sysMenuList = menuService.getAllMenu();
      for(SysMenu sysMenu:sysMenuList){
        if(!StringUtils.isEmpty(sysMenu.getPermission())){
          info.addStringPermission(sysMenu.getPermission());
        }
      }
    }else{
      //根据用户获取角色 根据角色获取所有按钮权限
      CurrentUser cUser= (CurrentUser) ShiroUtil.getSession().getAttribute("curentUser");
      for(CurrentRole cRole:cUser.getCurrentRoleList()){
        info.addRole(cRole.getId());
      }
      for(CurrentMenu cMenu:cUser.getCurrentMenuList()){
        if(!StringUtils.isEmpty(cMenu.getPermission()))
          info.addStringPermission(cMenu.getPermission());
      }
    }
    return info;
  }

  /**
   * 获取授权
   * @param authenticationToken
   * @return
   * @throws AuthenticationException
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
      throws AuthenticationException {
    UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
    String name=upToken.getUsername();
    String username=(String)authenticationToken.getPrincipal();
    SysUser s=null;
    try {
      s = userService.login(username);
    }catch (Exception e){
      e.printStackTrace();
    }
    if(s==null){
      throw new UnknownAccountException("账户密码不正确");
    }else{
      CurrentUser currentUser=new CurrentUser(s.getId(),s.getUsername(),s.getAge(),s.getEmail(),s.getPhoto(),s.getRealName());
      Subject subject = ShiroUtil.getSubject();
      /**角色权限封装进去*/
      //根据用户获取菜单
      List<SysMenu> menuList=new ArrayList<>(new HashSet<>(menuService.getUserMenu(s.getId())));
      JSONArray json=menuService.getMenuJsonByUser(menuList);
      Session session= subject.getSession();
      session.setAttribute("menu",json);
      CurrentMenu currentMenu=null;
      List<CurrentMenu> currentMenuList=new ArrayList<>();
      List<SysRole> roleList=new ArrayList<>();
      for(SysMenu m:menuList){
       currentMenu=new CurrentMenu(m.getId(),m.getName(),m.getPId(),m.getUrl(),m.getOrderNum(),m.getIcon(),m.getPermission(),m.getMenuType(),m.getNum());
        currentMenuList.add(currentMenu);
          roleList.addAll(m.getRoleList());
      }
      roleList= new ArrayList<>(new HashSet<>(roleList));
      List<CurrentRole> currentRoleList=new ArrayList<>();
      CurrentRole role=null;
      for(SysRole r:roleList){
        role=new CurrentRole(r.getId(),r.getRoleName(),r.getRemark());
        currentRoleList.add(role);
      }
      currentUser.setCurrentRoleList(currentRoleList);
      currentUser.setCurrentMenuList(currentMenuList);
      session.setAttribute("curentUser",currentUser);
    }
    ByteSource byteSource=ByteSource.Util.bytes(username);
    return new SimpleAuthenticationInfo(username,s.getPassword(), byteSource, getName());
  }
}
