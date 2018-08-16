package len.service.impl;


import cn.zsk.core.base.BaseMapper;
import cn.zsk.core.base.impl.BaseServiceImpl;
import len.entity.UserLeave;
import len.mapper.UserLeaveMapper;
import len.service.UserLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zsk
 * @date 2018/1/21.
 *
 */
@Service
public class UserLeaveServiceImpl extends BaseServiceImpl<UserLeave,String> implements
        UserLeaveService {

  @Autowired
  UserLeaveMapper userLeaveMapper;

  @Override
  public BaseMapper<UserLeave,String> getMappser() {
    return userLeaveMapper;
  }
}
