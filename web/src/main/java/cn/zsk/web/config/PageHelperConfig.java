package cn.zsk.web.config;

import com.github.pagehelper.PageHelper;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zsk
 * @date 2018/1/2.
 *
 *
 * 此方案可替代 application.yml中 pagehelper配置
 */
//@Configuration
public class PageHelperConfig {


  //@Bean
  public PageHelper getPageHelper(){
    PageHelper pageHelper=new PageHelper();
    Properties properties=new Properties();
    properties.setProperty("helperDialect","mysql");
    properties.setProperty("reasonable","true");
    properties.setProperty("supportMethodsArguments","true");
    properties.setProperty("params","count=countSql");
    pageHelper.setProperties(properties);
    return pageHelper;
  }

}
