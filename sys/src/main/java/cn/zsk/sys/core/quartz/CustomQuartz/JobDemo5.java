package cn.zsk.sys.core.quartz.CustomQuartz;


import cn.zsk.core.util.SpringUtil;
import cn.zsk.sys.entity.SysUser;
import cn.zsk.sys.service.SysUserService;
import cn.zsk.sys.service.impl.SysUserServiceImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author zsk
 * @date 2018/1/7.
 *
 *
 * 定时测试类
 */
public class JobDemo5 implements Job{


  @Override
  public void execute(JobExecutionContext context) {
    System.out.println("JobDemo5：启动任务=======================");
    run();
    System.out.println("JobDemo5：下次执行时间====="+
        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            .format(context.getNextFireTime())+"==============");
  }

  public void run(){
    ApplicationContext applicationContext= SpringUtil.getApplicationContext();
    SysUserService sys=SpringUtil.getBean(SysUserServiceImpl.class);
    List<SysUser> userList=sys.selectListByPage(new SysUser());
    System.out.println(userList.get(0).getUsername());
      System.out.println("JobDemo5：执行完毕=======================");

  }
}
