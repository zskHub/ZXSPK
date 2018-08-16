package cn.zsk.sys.service.impl;

import cn.zsk.core.base.BaseMapper;
import cn.zsk.core.base.impl.BaseServiceImpl;
import cn.zsk.sys.entity.SysJob;
import cn.zsk.sys.mapper.SysJobMapper;
import cn.zsk.sys.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zsk
 * @date 2018/1/6.
 *
 */
@Service
public class JobServiceImpl  extends BaseServiceImpl<SysJob,String> implements JobService {

  @Autowired
  SysJobMapper jobMapper;
  @Override
  public BaseMapper<SysJob, String> getMappser() {
    return jobMapper;
  }
}
