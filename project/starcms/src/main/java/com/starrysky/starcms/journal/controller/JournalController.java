package com.starrysky.starcms.journal.controller;

import com.starrysky.starcms.entity.*;
import com.starrysky.starcms.journal.service.JournalService;
import com.starrysky.starcms.security.HtmlUnEscapeUtil;
import com.starrysky.starcms.util.Constant;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName JournalController
 * @Description
 * @Author adi
 * @Date 2022-08-18 20:06
 */
@Controller
@RequestMapping("/backstage/journal")
public class JournalController {
    @Resource
    private JournalService journalService;

    @RequestMapping("/list")
    public String list(String title, Integer state, Integer pageNum, Integer pageSize, HttpServletRequest request) {
        if (pageNum == null)
            pageNum = 1;
        if (pageSize == null)
            pageSize = 10;
        try {
            Page<Journal> page = this.journalService.list(title, state, pageNum, pageSize);
            request.setAttribute("page", page);
            // 反转译HTML内容
            if(title != null)
                request.setAttribute("keywords", HtmlUtils.htmlUnescape(title));
            request.setAttribute("activemenu", "journalmenu");
            return "/backstage/journal/list";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("activemenu", "homemenu");
            return "redirect:/backstage/index";
        }
    }

    @RequestMapping("/checktitle")
    @ResponseBody
    public String checkName(String title) {
        if (StringUtils.isEmpty(title)) {
            return "null";
        } else if (this.journalService.getByTitle(title)) {
            return "exist";
        } else {
            return "noexist";
        }
    }

    @RequestMapping("/toadd")
    public String toAdd(HttpServletRequest request) {
        request.setAttribute("activemenu", "journalmenu");
        return "/backstage/journal/add";
    }

    @PostMapping("/add")
    public String add(Journal journal, HttpServletRequest request) {
        boolean checked = true;
        if (journal.getTitle() == null) {
            request.setAttribute("journalinfo", "请添加报刊名");
            checked = false;
        } else if (StringUtils.isEmpty(journal.getCover())) {
            request.setAttribute("journalinfo", "请上传图片");
            checked = false;
        }
        if (checked) {
            try {
                this.journalService.add(journal);
                request.setAttribute("journalinfo", "填加报刊成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("journalinfo", "填加报刊失败，请稍后再试");
            }
        } else {
            // 普通属性
            request.setAttribute("title", journal.getTitle());
            request.setAttribute("cover", journal.getCover());
            request.setAttribute("beginTime", journal.getBeginTime());
            request.setAttribute("endTime", journal.getEndTime());
            request.setAttribute("publisher", journal.getPublisher());
            request.setAttribute("address", journal.getAddress());
            request.setAttribute("phone", journal.getPhone());
            request.setAttribute("state", journal.getState());
        }
        return "forward:/backstage/journal/toadd";
    }

    @RequestMapping("/toedit/{id}")
    public String toEdit(@PathVariable("id") int id, HttpServletRequest request) {
        Journal journal = null;
        try {
            journal = this.journalService.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (journal == null) {
            request.setAttribute("activemenu", "journalmenu");
            return "redirect:/backstage/journal/list";
        } else {
            journal = HtmlUnEscapeUtil.unEscapeJournal(journal);    // 反转译Html
            request.setAttribute("journal", journal);
            request.setAttribute("activemenu", "journalmenu");
            return "/backstage/journal/edit";
        }
    }

    @PostMapping("/edit")
    public String edit(Journal journal, HttpServletRequest request) {
        // 校验数据非空
        boolean checked = true;
        if (journal.getTitle() == null) {
            request.setAttribute("journalinfo", "请添加报刊名");
            checked = false;
        } else if (StringUtils.isEmpty(journal.getCover())) {
            request.setAttribute("journalinfo", "请上传图片");
            checked = false;
        }
        // 验证成功，新建用户，失败返回重新填写
        if (checked) {
            try {
                this.journalService.edit(journal);
                request.setAttribute("journalinfo", "修改报刊成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("journalinfo", "修改报刊失败");
            }
        }
        return "forward:/backstage/journal/toedit/" + journal.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        this.journalService.delete(id);
        return "redirect:/backstage/journal/list";
    }

    @GetMapping("/deletes/{ids}")
    public String deletes(@PathVariable("ids") String ids) {
        this.journalService.deletes(ids);
        return "redirect:/backstage/journal/list";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
