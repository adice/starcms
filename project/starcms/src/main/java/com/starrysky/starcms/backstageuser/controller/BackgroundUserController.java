package com.starrysky.starcms.backstageuser.controller;

import com.starrysky.starcms.backstageuser.service.BackgroundUserService;
import com.starrysky.starcms.entity.BackgroundRole;
import com.starrysky.starcms.entity.BackgroundUser;
import com.starrysky.starcms.role.service.BackgroundRoleService;
import com.starrysky.starcms.security.HtmlUnEscapeUtil;
import com.starrysky.starcms.security.SecurityUser;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @ClassName BackgroundUserController
 * @Description
 * @Author adi
 * @Date 2022-07-26 16:47
 */
@Controller
@RequestMapping("/backstage/user")
public class BackgroundUserController {

    @Resource
    private BackgroundUserService backgroundUserService;
    @Resource
    private BackgroundRoleService backgroundRoleService;

//    @PostMapping("/login")
//    public String login(String name, String password, HttpServletRequest request, HttpSession session) {
//        BackgroundUser backgroundUser = null;
//        try {
//            backgroundUser = this.backgroundUserService.login(name, password);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (backgroundUser != null) {
//            backgroundUser.setPassword("");
//            session.setAttribute("user", backgroundUser);
//            return "redirect:/backstage/index";
//        } else {
//            request.setAttribute("loginfail", "账号密码错误，请重试！");
//            return "/backstage/login";
//        }
//    }

    //    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "redirect:/backstage/user/login";
//    }
    @RequestMapping("/checkname")
    @ResponseBody
    public String checkName(String name) {
        if (this.backgroundUserService.getByName(name)) {
            return "exist";
        } else {
            return "noexist";
        }
    }

    @RequestMapping("/list")
    public String list(String name, String realName, Integer state, Integer pageNum, Integer pageSize, HttpServletRequest request) {
        if (pageNum == null)
            pageNum = 1;
        if (pageSize == null)
            pageSize = 10;
        try {
            Page<BackgroundUser> page = this.backgroundUserService.list(name, realName, state, pageNum, pageSize);
            request.setAttribute("page", page);
            // 反转译HTML内容
            if(name != null)
                request.setAttribute("name", HtmlUtils.htmlUnescape(name));
            if(realName != null)
                request.setAttribute("realName", HtmlUtils.htmlUnescape(realName));
            request.setAttribute("activemenu", "usermenu");
            return "/backstage/user/list";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("activemenu", "homemenu");
            return "redirect:/backstage/index";
        }

    }

    @RequestMapping("/toadd")
    public String toAdd(HttpServletRequest request) {
        List<BackgroundRole> list = this.backgroundRoleService.listNormal();
        LinkedHashMap<BackgroundRole, Boolean> rolesMap = new LinkedHashMap<>(0);
        for (BackgroundRole r : list) {
            rolesMap.put(r, false);
        }
        request.setAttribute("roles", rolesMap);
        request.setAttribute("activemenu", "usermenu");
        return "/backstage/user/add";
    }

    @PostMapping("/add")
    public String add(String name, String realName, String password, String password1, String email, Integer[] role,
                      Integer dataRange, Integer state, HttpServletRequest request) {
        // 校验数据非空
        boolean checked = true;
        if (StringUtils.isEmpty(name)) {
            request.setAttribute("usererrorinfo", "请填写账号");
            checked = false;
        } else if (StringUtils.isEmpty(realName)) {
            request.setAttribute("usererrorinfo", "请填写真实姓名");
            checked = false;
        } else if (StringUtils.isEmpty(password)) {
            request.setAttribute("usererrorinfo", "请填写密码");
            checked = false;
        } else if (StringUtils.isEmpty(password1)) {
            request.setAttribute("usererrorinfo", "请填写确认密码");
            checked = false;
        } else if (!password.equals(password1)) {
            request.setAttribute("usererrorinfo", "密码和确认密码不一致");
            checked = false;
        } else if (role == null) {
            request.setAttribute("usererrorinfo", "请选择角色");
            checked = false;
        } else if (dataRange == null) {
            request.setAttribute("usererrorinfo", "请选择管理的数据范围");
            checked = false;
        } else if (state == null) {
            request.setAttribute("usererrorinfo", "请选择状态");
            checked = false;
        }
        // 验证成功，新建用户，失败返回重新填写
        if (checked) {
            BackgroundUser user = new BackgroundUser();
            user.setName(name);
            user.setRealName(realName);
            user.setPassword(password);
            user.setEmail(email);
            user.setDataRange(dataRange);
            user.setState(state);

            user.setRegistTime(new Date());
            user.setLoginCount(0);
            for (Integer id : role) {
                BackgroundRole backgroundRole = new BackgroundRole();
                backgroundRole.setId(id);
                user.getRoles().add(backgroundRole);
            }
            BackgroundUser newUser = this.backgroundUserService.add(user);
            request.setAttribute("usererrorinfo", "添加用户成功");
            return "forward:/backstage/user/toadd";
        } else {
            // 组装角色+是否选中
            List<BackgroundRole> roles = this.backgroundRoleService.listNormal();
            LinkedHashMap<BackgroundRole, Boolean> rolesMap = new LinkedHashMap<>(0);
            if (role == null) {
                for (BackgroundRole r : roles) {
                    rolesMap.put(r, false);
                }
            } else {
                List<Integer> temp = Arrays.asList(role);
                for (BackgroundRole r : roles) {
                    if (temp.contains(r.getId())) {
                        rolesMap.put(r, true);
                    } else {
                        rolesMap.put(r, false);
                    }
                }
            }
            request.setAttribute("roles", rolesMap);
            request.setAttribute("name", name);
            request.setAttribute("realName", realName);
            request.setAttribute("password", password);
            request.setAttribute("password1", password1);
            request.setAttribute("dataRange", dataRange);
            request.setAttribute("state", state);

            request.setAttribute("activemenu", "usermenu");
            return "/backstage/user/add";
        }
    }

    @RequestMapping("/toedit/{id}")
    public String toEdit(@PathVariable("id") int id, HttpServletRequest request) {
        BackgroundUser user = null;
        try {
            user = this.backgroundUserService.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user == null) {
            return "redirect:/backstage/user/list";
        } else {
            List<BackgroundRole> roles = this.backgroundRoleService.listNormal();
            LinkedHashMap<BackgroundRole, Boolean> rolesMap = new LinkedHashMap<>(0);
            for (BackgroundRole tempRole : roles) {
                boolean exist = false;
                for (BackgroundRole tempUserRole : user.getRoles()) {
                    if (tempRole.getId() == tempUserRole.getId()) {
                        exist = true;
                        break;
                    }
                }
                if (exist) {
                    rolesMap.put(tempRole, true);
                } else {
                    rolesMap.put(tempRole, false);
                }
            }

            user.setPassword("");
            user.setRoles(null);
            request.setAttribute("roles", rolesMap);
            request.setAttribute("activemenu", "usermenu");
            user = HtmlUnEscapeUtil.unEscapeUser(user); //反转译Html内容
            request.setAttribute("edituser", user);
            return "/backstage/user/edit";
        }
    }

    @PostMapping("/edit")
    public String edit(int id, String name, String realName, String password, String password1, String email, Integer[] role,
                       Integer dataRange, Integer state, HttpServletRequest request) {
        // 校验数据非空
        boolean checked = true;
        if (StringUtils.isEmpty(realName)) {
            request.setAttribute("userinfo", "请填写真实姓名");
            checked = false;
        } else if (password != null && !password.equals(password1)) {
            request.setAttribute("userinfo", "密码和确认密码不一致");
            checked = false;
        } else if (role == null) {
            request.setAttribute("userinfo", "请选择角色");
            checked = false;
        } else if (dataRange == null) {
            request.setAttribute("userinfo", "请选择管理的数据范围");
            checked = false;
        } else if (state == null) {
            request.setAttribute("userinfo", "请选择状态");
            checked = false;
        }
        // 验证成功，新建用户，失败返回重新填写
        if (checked) {
            BackgroundUser user = new BackgroundUser();
            user.setId(id);
            user.setName(name);
            user.setRealName(realName);
            user.setPassword(password);
            user.setEmail(email);
            user.setDataRange(dataRange);
            user.setState(state);
            for (Integer roleId : role) {
                BackgroundRole backgroundRole = new BackgroundRole();
                backgroundRole.setId(roleId);
                user.getRoles().add(backgroundRole);
            }
            try {
                this.backgroundUserService.edit(user);
                request.setAttribute("userinfo", "修改用户成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("userinfo", "修改用户失败");
            }
        }
        return "forward:/backstage/user/toedit/" + id;
    }

    @RequestMapping("toeditpwd")
    public String toEditPwd(HttpServletRequest request) {
        request.setAttribute("activemenu", "homemenu");
        return "/backstage/user/editpwd";
    }

    @PostMapping("/editpwd")
    public String editPwd(String password, String password1, HttpServletRequest request, HttpSession session) {
        // 校验数据非空
        boolean checked = true;
        if (StringUtils.isEmpty(password)) {
            request.setAttribute("userinfo", "请填写密码");
            checked = false;
        } else if (StringUtils.isEmpty(password1)) {
            request.setAttribute("userinfo", "请填写确认密码");
            checked = false;
        } else if (!password.equals(password1)) {
            request.setAttribute("userinfo", "密码和确认密码不一致");
            checked = false;
        }
        // 验证成功，新建用户，失败返回重新填写
        if (checked) {
//            Object obj = session.getAttribute("user");
            Object obj =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (obj != null) {
                SecurityUser user = (SecurityUser) obj;
                try {
                    this.backgroundUserService.editPwd(user.getId(), password);
                    request.setAttribute("userinfo", "修改成功");
                    return "forward:/backstage/user/toeditpwd";
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("userinfo", "修改失败");
                    return "forward:/backstage/user/toeditpwd";
                }

            } else {
                session.invalidate();
                return "redirect:/backstage/user/login";
            }
        } else {
            return "forward:/backstage/user/toeditpwd";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        this.backgroundUserService.delete(id);
        return "redirect:/backstage/user/list";
    }

    @GetMapping("/deletes/{ids}")
    public String deletes(@PathVariable("ids") String ids) {
        this.backgroundUserService.deletes(ids);
        return "redirect:/backstage/user/list";
    }
}
