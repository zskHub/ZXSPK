package cn.zsk.web.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zsk
 * @date 2018/1/16.
 *
 */
@Component
public class FreemarkerShiroConfig implements InitializingBean {

  @Autowired
  private freemarker.template.Configuration configuration;

  @Override
  public void afterPropertiesSet() {
    configuration.setSharedVariable("shiro", new ShiroTags());
  }
}
