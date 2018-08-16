package cn.zsk.sys.core.quartz.CustomQuartz;


import cn.zsk.core.util.SpringUtil;
import cn.zsk.sys.entity.SysUser;
import cn.zsk.sys.service.SysUserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author zsk
 * @date 2018/1/7.
 *
 *
 * 定时
 */
public class JobDemo1 implements Job {

  @Autowired
  SysUserService sys;

  @Override
  public void execute(JobExecutionContext context) {
    System.out.println("JobDemo1：启动任务=======================");
    run();
    System.out.println("JobDemo1：下次执行时间====="+
        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            .format(context.getNextFireTime())+"==============");
  }

  public void run(){
    ApplicationContext applicationContext= SpringUtil.getApplicationContext();
    //可以 获取
    //SysUserService sys=SpringUtil.getBean(SysUserServiceImpl.class);
    List<SysUser> userList=sys.selectListByPage(new SysUser());
    System.out.println(userList.get(0).getUsername());
      System.out.println("JobDemo1：执行完毕=======================");

  }
}
