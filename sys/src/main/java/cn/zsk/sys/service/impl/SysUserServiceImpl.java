package cn.zsk.sys.service.impl;


import cn.zsk.core.base.BaseMapper;
import cn.zsk.core.base.impl.BaseServiceImpl;
import cn.zsk.core.exception.MyException;
import cn.zsk.core.util.Checkbox;
import cn.zsk.core.util.JsonUtil;
import cn.zsk.core.util.Md5Util;
import cn.zsk.sys.entity.SysRole;
import cn.zsk.sys.entity.SysRoleUser;
import cn.zsk.sys.entity.SysUser;
import cn.zsk.sys.mapper.SysRoleUserMapper;
import cn.zsk.sys.mapper.SysUserMapper;
import cn.zsk.sys.service.RoleService;
import cn.zsk.sys.service.RoleUserService;
import cn.zsk.sys.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsk
 * @date 2017/12/4.
 *
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser,String> implements SysUserService {

  @Autowired
  SysUserMapper sysUserMapper;

  @Autowired
  SysRoleUserMapper sysRoleUserMapper;

  @Autowired
  RoleService roleService;

  @Autowired
  RoleUserService roleUserService;
  @Override
  public BaseMapper<SysUser, String> getMappser() {
    return sysUserMapper;
  }


  @Override
  public SysUser login(String username) {
    return sysUserMapper.login(username);
  }



  @Override
  public int deleteByPrimaryKey(String id) {
    return sysUserMapper.deleteByPrimaryKey(id);
  }

  @Override
  public int insert(SysUser record) {
    return sysUserMapper.insert(record);
  }

  @Override
  public int insertSelective(SysUser record) {

    String pwd= Md5Util.getMD5(record.getPassword().trim(),record.getUsername().trim());
    record.setPassword(pwd);
    return super.insertSelective(record);
  }

  @Override
  public SysUser selectByPrimaryKey(String id) {
    return sysUserMapper.selectByPrimaryKey(id);
  }

  @Override
  public int updateByPrimaryKeySelective(SysUser record) {
    return super.updateByPrimaryKeySelective(record);
  }

  @Override
  public int updateByPrimaryKey(SysUser record) {
    return sysUserMapper.updateByPrimaryKey(record);
  }

  @Override
  public List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser) {
    return sysRoleUserMapper.selectByCondition(sysRoleUser);
  }

  /**
   * 分页查询
   * @param
   * @return
   */
  @Override
  public List<SysUser> selectListByPage(SysUser sysUser) {
    return sysUserMapper.selectListByPage(sysUser);
  }

  @Override
  public int count() {
    return sysUserMapper.count();
  }

  @Override
  public int add(SysUser user) {
    //密码加密
  String pwd= Md5Util.getMD5(user.getPassword().trim(),user.getUsername().trim());
  user.setPassword(pwd);
    return sysUserMapper.add(user);
  }

  @Override
  public JsonUtil delById(String id, boolean flag) {
    if (StringUtils.isEmpty(id)) {
      return JsonUtil.error("获取数据失败");
    }
    JsonUtil j=new JsonUtil();
    try {
      SysUser sysUser = selectByPrimaryKey(id);
      if("31ac66522912498881c4c3c9f15fd73c".equals(id)){
        return JsonUtil.error("超管无法删除");
      }
      SysRoleUser roleUser=new SysRoleUser();
      roleUser.setUserId(id);
      int count=roleUserService.selectCountByCondition(roleUser);
      if(count>0){
        return JsonUtil.error("账户已经绑定角色，无法删除");
      }
      if (flag) {
        //逻辑
        sysUser.setDelFlag(Byte.parseByte("1"));
        updateByPrimaryKeySelective(sysUser);
      } else {
        //物理
        sysUserMapper.delById(id);
      }
      j.setMsg("删除成功");
    } catch (MyException e) {
      j.setMsg("删除失败");
      j.setFlag(false);
      e.printStackTrace();
    }
    return j;

  }

  @Override
  public int checkUser(String username) {
    return sysUserMapper.checkUser(username);
  }

  @Override
  public List<Checkbox> getUserRoleByJson(String id){
    List<SysRole> roleList=roleService.selectListByPage(new SysRole());
    SysRoleUser sysRoleUser =new SysRoleUser();
    sysRoleUser.setUserId(id);
    List<SysRoleUser>  kList= selectByCondition(sysRoleUser);
    System.out.println(kList.size());
    List<Checkbox> checkboxList=new ArrayList<>();
    Checkbox checkbox=null;
    for(SysRole sysRole:roleList){
      checkbox=new Checkbox();
      checkbox.setId(sysRole.getId());
      checkbox.setName(sysRole.getRoleName());
      for(SysRoleUser sysRoleUser1 :kList){
        if(sysRoleUser1.getRoleId().equals(sysRole.getId())){
          checkbox.setCheck(true);
        }
      }
      checkboxList.add(checkbox);
    }
    return checkboxList;
  }

  @Override
  public int rePass(SysUser user) {
    return sysUserMapper.rePass(user);
  }

  @Override
  public List<SysUser> getUserByRoleId(String roleId,int page,int limit)
  {
    Map map = new HashMap<>();
    map.put("roleId",roleId);
    map.put("page",(page-1)*limit );
    map.put("limit",limit);
    return sysUserMapper.getUserByRoleId(map);
  }

  @Override
  public int countUserByRoleId(String roleId,int page,int limit)
  {
    Map map = new HashMap<>();
    map.put("roleId",roleId);
    map.put("page",(page-1)*limit );
    map.put("limit",limit);
    return sysUserMapper.countUserByRoleId(map);
  }
}
