package cn.zsk.sys.controller;

import cn.zsk.core.util.Checkbox;
import cn.zsk.sys.core.annotation.Log;
import cn.zsk.sys.core.filter.VerifyCodeUtils;
import cn.zsk.sys.core.shiro.ShiroUtil;
import cn.zsk.sys.entity.SysUser;
import cn.zsk.sys.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author zsk
 * @date 2017/12/4.
 *
 * 登录、退出页面
 */
@Controller
@Slf4j
public class LoginController {

    @Autowired
    private SysUserService userService;


    @GetMapping(value = "")
    public String loginInit() {
        return loginCheck();
    }

    @GetMapping(value = "goLogin")
    public String goLogin(Model model, ServletRequest request) {
        Subject sub = SecurityUtils.getSubject();
        if (sub.isAuthenticated()) {
            return "/main/main";
        } else {
            model.addAttribute("message", "请重新登录");
            return "/login";
        }
    }

    @GetMapping(value = "/login")
    public String loginCheck() {
        Subject sub = SecurityUtils.getSubject();
        Boolean flag2 = sub.isRemembered();
        boolean flag = sub.isAuthenticated() || flag2;
        Session session = sub.getSession();
        if (flag) {
            return "/main/main";
        }
        return "/login";
    }

    /**
     * 登录动作
     *
     * @param user
     * @param model
     * @param rememberMe
     * @return
     */
    @ApiOperation(value = "/login", httpMethod = "POST", notes = "登录method")
    @PostMapping(value = "/login")
    public String login(SysUser user, Model model, String rememberMe, HttpServletRequest request) {
        String codeMsg = (String) request.getAttribute("shiroLoginFailure");
        if ("code.error".equals(codeMsg)) {
            model.addAttribute("message", "验证码错误");
            return "/login";
        }
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername().trim(),
                user.getPassword());
        Subject subject = ShiroUtil.getSubject();
        String msg = null;
        try {
            subject.login(token);
            //subject.hasRole("admin");
            if (subject.isAuthenticated()) {
                return "redirect:/main";
            }
        } catch (UnknownAccountException e) {
            msg = "用户名/密码错误";
        } catch (IncorrectCredentialsException e) {
            msg = "用户名/密码错误";
        } catch (ExcessiveAttemptsException e) {
            msg = "登录失败多次，账户锁定10分钟";
        }
        if (msg != null) {
            model.addAttribute("message", msg);
        }
        return "/login";
    }

    @GetMapping("/main")
    public String main(){
        return "main/main";
    }

    //查看基本资料
    @GetMapping("/getBasicMsg")
    public String getBasicMsg(String userId,Model model){
        SysUser sysUser = userService.selectByPrimaryKey(userId);
        List<Checkbox> checkboxList = userService.getUserRoleByJson(userId);
        model.addAttribute("boxJson", checkboxList);
        model.addAttribute("user",sysUser);

        return "/main/basic_msg";
    }
    //安全设置
    @GetMapping("/getSecuritySettings")
    public String getSecuritySettings(String userId,Model model){
        SysUser sysUser = userService.selectByPrimaryKey(userId);
        model.addAttribute("user",sysUser);

        return "/main/security_settings";
    }

    @Log(desc = "用户退出平台")
    @GetMapping(value = "/logout")
    public String logout() {
        Subject sub = SecurityUtils.getSubject();
        sub.logout();
        return "/login";
    }


    @GetMapping(value = "/getCode")
    public void getYzm(HttpServletResponse response, HttpServletRequest request) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpg");

            //生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(5);
            log.info("verifyCode:{}",verifyCode);
            //存入会话session
            HttpSession session = request.getSession(true);
            session.setAttribute("_code", verifyCode.toLowerCase());
            //生成图片
            int w = 146, h = 33;
            VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
