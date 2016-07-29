package com.shiro.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shiro.model.User;
import com.shiro.security.PermissionSign;
import com.shiro.service.UserService;


/**
 * 用户控制器
 **/
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     * 
     * @param user
     * @param result
     * @return @Valid 
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(User user, BindingResult result, Model model, HttpServletRequest request) {
        try {
            Subject subject = SecurityUtils.getSubject();
            // 已登陆则 跳到首页
            if (subject.isAuthenticated()) {
                return "redirect:/";
            }
            if (result.hasErrors()) {
                model.addAttribute("error", "参数错误！");
                return "login";
            }
            /**
             *  身份验证，根据配置spring-shiro.xml 中的安全管理器配置的realm（此项目中是securityRealm）会跳转到
             *  securityRealm.java 中去进行身份验证，验证通过，此项目跳到index.jsp 页面
             *  在Index.jsp 中使用了 <shiro:hasAnyRoles name="super_admin"> ， <shiro:hasPermission name="user:create">
             *  类似这种需要验证角色，权限的，则又会去securityRealm.java 中去验证角色权限，来达到导航栏是否显示的目的
             *
             *
             */
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());   
            subject.login(token);
            // 验证成功在Session中保存用户信息
            final User authUserInfo = userService.selectByUsername(user.getUsername());
            request.getSession().setAttribute("userInfo", authUserInfo);
        } catch (AuthenticationException e) {
            // 身份验证失败
            model.addAttribute("error", "用户名或密码错误 ！");
            return "login";
        }
        String url=null;
        try {
        	 //这里获得登陆之前访问的一个页面，如果有登陆之后直接跳转到当前访问的页面，如果没有，则根据配置文件跳转到登陆成功后的页面
             url = WebUtils.getSavedRequest(request).getRequestUrl();
          
		} catch (Exception e) {
			// TODO: handle exception
		}
        if (url==null||"".equals(url)) {
        	return "redirect:/";
		}
        System.out.println(url);
        
        return "redirect:"+url;
    }
    
    
    public void autoLogin(HttpServletRequest request, HttpServletResponse response){
    	 Subject subject = SecurityUtils.getSubject();
    	 if (subject.isRemembered()) {
			
		}
    	
    	
    }
    

    /**
     * 用户登出
     * 
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.removeAttribute("userInfo");
        // 登出操作
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }

    /**
     * 基于角色 标识的权限控制案例 ，如果登陆的用户拥有这个角色，才会进入，否则根据spring-shiro.xml 配置
     * 直接进入了  <property name="unauthorizedUrl" value="/rest/page/401"/> 401页面
     */
    @RequestMapping(value = "/admin")
    @ResponseBody
 /*   @RequiresRoles(value = RoleSign.VIP_USER)*/
    public String admin() {
        return "拥有admin角色,能访问";
    }

    /**
     * 基于权限标识的权限控制案例
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    @RequiresPermissions(value = PermissionSign.USER_CREATE)
    public String create() {
        return "拥有user:create权限,能访问";
    }
}
