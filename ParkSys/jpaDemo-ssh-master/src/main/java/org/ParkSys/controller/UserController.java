package org.ParkSys.controller;

import org.ParkSys.bean.User;
import org.ParkSys.service.UserService;
import org.ParkSys.utils.UtilsMD5;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("login")
    @ResponseBody
    public Map Login(HttpServletRequest request) throws NoSuchAlgorithmException {
        String name = request.getParameter("j_username");
        String passwd = request.getParameter("j_password");
        String pwd = UtilsMD5.EncoderByMd5(passwd);
        User user = userService.login(name, pwd);
        Map map = new HashMap();
        if (user != null) {
            map.put("userId",user.getId());
            map.put("msg","success");
            map.put("userName",name);
            map.put("passWd",passwd);
            map.put("nickName",user.getNickName());
        } else {
            map.put("msg","failed");
        }
        return  map;
    }

    @RequestMapping("modifyPwd")
    @ResponseBody
    public Map modifyPwd(HttpServletRequest request) throws NoSuchAlgorithmException {
        String userid = request.getParameter("userid");
        long id = Long.parseLong(userid);
        String oldpwd = request.getParameter("Loginoldpwd");
        String opd = UtilsMD5.EncoderByMd5(oldpwd);
        String newpwd = request.getParameter("Loginnewpwd");
        String npd = UtilsMD5.EncoderByMd5(newpwd);
        String msg = userService.modifyPwd(id,opd,npd);
        Map map = new HashMap();
        map.put("msg",msg);
        return map;
    }

    @RequestMapping("getAll")
    @ResponseBody
    public Map getAll(HttpServletRequest request) {
        String numAndSize = request.getQueryString();
        String[] parms = numAndSize.split("&");
        int num = Integer.parseInt(parms[0].substring(parms[0].indexOf("=") + 1, parms[0].length()));
        int size = Integer.parseInt(parms[1].substring(parms[1].indexOf("=") + 1, parms[1].length()));
        String userName = "";
        if (parms.length > 2) {
            userName = parms[2].substring(parms[2].indexOf("=") + 1, parms[2].length());
        }
        List<User> users = userService.getAll(num, size,userName);
        long count = userService.count(userName);
        Map map = new HashMap();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", users);
        return map;
    }

    @RequestMapping("del")
    @ResponseBody
    public int del(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        String name = request.getParameter("userName");
        if (userService.delUser(id)) {
            return 1;
        }
        return 0;
    }

    @RequestMapping("edit")
    @ResponseBody
    public int edit(HttpServletRequest request) {
        String id = request.getParameter("userid");
        long id1 = Long.parseLong(id);
        String userName = request.getParameter("userName");
        String nickName = request.getParameter("nickName");
        String phoneNo = request.getParameter("phoneNo");
        String memo = request.getParameter("memo");
        if (userService.updateUser(id1, userName,nickName,phoneNo,memo)) {
            return 1;
        }
        return 0;
    }

    @RequestMapping("save")
    @ResponseBody
    public String save(HttpServletRequest request) throws NoSuchAlgorithmException {
        String name = request.getParameter("userName");
        String passwd = request.getParameter("passWd");
        String pwd = UtilsMD5.EncoderByMd5(passwd);
        String nickName = request.getParameter("nickName");
        String phoneNo = request.getParameter("phoneNo");
        String memo = request.getParameter("memo");
        return userService.creatUser(name, pwd,nickName,phoneNo,memo);
    }
}

