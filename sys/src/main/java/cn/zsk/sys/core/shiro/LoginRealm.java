package cn.zsk.sys.core.shiro;

import cn.zsk.core.base.CurrentMenu;
import cn.zsk.core.base.CurrentRole;
import cn.zsk.core.base.CurrentUser;
import cn.zsk.sys.entity.SysMenu;
import cn.zsk.sys.entity.SysRole;
import cn.zsk.sys.entity.SysUser;
import cn.zsk.sys.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
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
import java.util.stream.Collectors;


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
    List<SysMenu> menuList = new ArrayList<>();
    Session session = null;
    Subject subject = ShiroUtil.getSubject();

    if("31ac66522912498881c4c3c9f15fd73c".equals(user.getId())){
      //************************************************************************查询所有的角色信息
      SysRole sysRole = new SysRole();
      List<SysRole> sysRoleList = roleService.selectListByPage(sysRole);
      for(SysRole sysRole1:sysRoleList){
        info.addRole(sysRole1.getId());
      }
      //***********************************************************************查询所有的资源权限信息
      List<SysMenu> sysMenuList = menuService.getAllMenu();
      for(SysMenu sysMenu:sysMenuList){
        if(!StringUtils.isEmpty(sysMenu.getPermission())){
          info.addStringPermission(sysMenu.getPermission());
        }
      }


      /*
      * 查询所有的菜单展示项
      * 查询菜单项的操作，在用户登录时有一次，这里用户授权时也有一次。两次的目的不同。
      *
      * 这里写菜单项加载的方法目的：当用户出现更改菜单的操作或者更改角色的操作，通过配合删除用户的角色信息的代码
      * （在ShiroUtil类中，调用本类中的clearCachedAuthorization方法删除用户权限信息），使代码重新加载用户权限
      * （也就是调用这个方法）时能够重新加载菜单信息，实现菜单项的刷新。
      *
      * 用户登录授权方法写菜单加载的目的：用户登录操作，进入到首页，在没有相关的操作时不会触发该方法为用户分配权限，
      * 所以在用户登录的时候需要先加载一下菜单。以后菜单或者角色改变时会触发该方法实现刷新。
      *
      * */

      //超级管理员用户拥有所有的菜单项
      menuList = new ArrayList<>(new HashSet<>(menuService.getAllMenu()));

      // 由于跳过了通过用户id查询用户角色信息的步骤，所以这里手动添加一下该超级管理员的相关说明
      for(SysMenu sysMenu:menuList){
        List<SysRole> sysRoleList1 = new ArrayList<>();
        sysRoleList1.stream().map(sysRole1 -> {
          sysRole.setRemark("超级管理员");
          sysRole.setRoleName("SuperAdmin");
          return sysRole;
        }).collect(Collectors.toList());
        sysMenu.setRoleList(sysRoleList1);
      }
      //处理查询出来的所有的菜单信息
      JSONArray json = menuService.getMenuJsonByUser(menuList,null);
      //查询该用户拥有的子系统，系统默认展示该用户拥有的子系统中的第一个子系统的菜单，其他的菜单项需要点击“其他系统”后再显示
      JSONArray superMenu =  JSONArray.parseArray(JSON.toJSONString(menuService.getAllSuperMenu()));
      session = subject.getSession();
      session.setAttribute("menu",json);
      session.setAttribute("superMenu",superMenu);


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



      //用户授权方法中的：根据用户获取所有菜单
      menuList=new ArrayList<>(new HashSet<>(menuService.getUserMenu(user.getId())));
      JSONArray json=menuService.getMenuJsonByUser(menuList,null);
      //查询该用户拥有的子系统，系统默认展示该用户拥有的子系统中的第一个子系统的菜单，其他的菜单项需要点击“其他系统”后再显示
      JSONArray superMenu =  JSONArray.parseArray(JSON.toJSONString(menuService.getUserSuperMenu(user.getId())));
      session= subject.getSession();
      session.setAttribute("menu",json);
      session.setAttribute("superMenu",superMenu);

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
    SysUser user=null;
    try {
      user = userService.login(username);
    }catch (Exception e){
      e.printStackTrace();
    }
    if(user==null){
      throw new UnknownAccountException("账户密码不正确");
    }else{
      CurrentUser currentUser=new CurrentUser(user.getId(),user.getUsername(),user.getAge(),user.getEmail(),user.getPhoto(),user.getRealName());
      Subject subject = ShiroUtil.getSubject();
      /**角色权限封装进去*/

      /*
      * 用户登录方法中的菜单加载，详情查看上面（用户授权方法中）的说明。
      * */
      //超级管理员拥有所有的角色和权限
      List<SysMenu> menuList = new ArrayList<>();
      Session session = null;
      if("31ac66522912498881c4c3c9f15fd73c".equals(user.getId())){
        /*
        * 超级管理员用户拥有所有的菜单项
        * */
        menuList = new ArrayList<>(new HashSet<>(menuService.getAllMenu()));
        /*
        * 由于跳过了通过用户id查询用户角色信息的步骤，所以这里手动添加一下该超级管理员的相关说明
        * */
        for(SysMenu sysMenu:menuList){
          List<SysRole> sysRoleList = new ArrayList<>();
          sysRoleList.stream().map(sysRole -> {
              sysRole.setRemark("超级管理员");
              sysRole.setRoleName("SuperAdmin");
              return sysRole;
          }).collect(Collectors.toList());
          sysMenu.setRoleList(sysRoleList);
        }
        /*
         * 处理查询出来的所有的菜单信息
         * */
        JSONArray json = menuService.getMenuJsonByUser(menuList,null);
        JSONArray superMenu =  JSONArray.parseArray(JSON.toJSONString(menuService.getAllSuperMenu()));
        session = subject.getSession();
        session.setAttribute("menu",json);
        session.setAttribute("superMenu",superMenu);


      }else {

        //根据用户获取菜单
        menuList=new ArrayList<>(new HashSet<>(menuService.getUserMenu(user.getId())));
        JSONArray json=menuService.getMenuJsonByUser(menuList,null);
        //查询该用户拥有的子系统，系统默认展示该用户拥有的子系统中的第一个子系统的菜单，其他的菜单项需要点击“其他系统”后再显示
        JSONArray superMenu =  JSONArray.parseArray(JSON.toJSONString(menuService.getUserSuperMenu(user.getId())));
        session= subject.getSession();
        session.setAttribute("menu",json);
        session.setAttribute("superMenu",superMenu);
      }


      session= subject.getSession();
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
    return new SimpleAuthenticationInfo(username,user.getPassword(), byteSource, getName());
  }

  /**
   * 清理权限缓存方法
   */
  public void clearCachedAuthorization(){
    //清空权限缓存
    this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
  }

}
