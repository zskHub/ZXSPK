package cn.zsk.wechat.wechat.controller;


import cn.zsk.wechat.business.utils.R;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@RestController
@RequestMapping("/wechat/jssdk")
public class WechatJSController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private WxMpService wxService;

  @RequestMapping("/getwxjsconfig")
  public @ResponseBody
  R getwxjsconfig(@RequestParam("url")String url,
                  HttpServletRequest req) throws WxErrorException {

    WxJsapiSignature wxJsapiSignature =wxService.createJsapiSignature(url);
    return R.ok().put("wxjsapisignature",wxJsapiSignature);
  }

}
