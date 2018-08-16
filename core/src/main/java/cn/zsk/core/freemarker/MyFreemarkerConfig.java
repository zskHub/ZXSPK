package cn.zsk.core.freemarker;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

/**
 * @author zsk
 * @date 2017/12/11.
 *
 */
public class MyFreemarkerConfig extends FreeMarkerConfigurer {

  @Override
  public void afterPropertiesSet() throws IOException, TemplateException {
    super.afterPropertiesSet();
    Configuration configuration=this.getConfiguration();
    configuration.setSharedVariable("shiro",new ShiroTags());
    configuration.setNumberFormat("#");
  }
}
