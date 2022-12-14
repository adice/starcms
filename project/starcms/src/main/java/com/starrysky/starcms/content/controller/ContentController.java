package com.starrysky.starcms.content.controller;

import com.starrysky.starcms.backstageuser.service.BackgroundUserService;
import com.starrysky.starcms.content.service.ContentService;
import com.starrysky.starcms.content3d.service.Content3DService;
import com.starrysky.starcms.contentallscene.service.ContentAllSceneService;
import com.starrysky.starcms.contentaudio.service.ContentAudioService;
import com.starrysky.starcms.contentbook.service.ContentBookService;
import com.starrysky.starcms.contentnews.service.ContentNewsService;
import com.starrysky.starcms.contentpic.service.ContentPicService;
import com.starrysky.starcms.contentrubbings.service.ContentRubbingsService;
import com.starrysky.starcms.contentvideo.service.ContentVideoService;
import com.starrysky.starcms.entity.*;
import com.starrysky.starcms.journal.service.JournalService;
import com.starrysky.starcms.security.HtmlUnEscapeUtil;
import com.starrysky.starcms.security.SecurityUser;
import com.starrysky.starcms.util.Constant;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.List;

/**
 * @ClassName ContentController
 * @Description 内容控制器，用于内容的管理
 * @Author adi
 * @Date 2022-08-02 09:24
 */
@Controller
@RequestMapping("/backstage/content")
public class ContentController {
    @Resource
    private ContentService contentService;
    @Resource
    private ContentBookService contentBookService;
    @Resource
    private ContentPicService contentPicService;
    @Resource
    private ContentRubbingsService contentRubbingsService;
    @Resource
    private ContentAudioService contentAudioService;
    @Resource
    private ContentVideoService contentVideoService;
    @Resource
    private Content3DService content3DService;
    @Resource
    private ContentAllSceneService contentAllSceneService;
    @Resource
    private ContentNewsService contentNewsService;
    @Resource
    private BackgroundUserService backgroundUserService;
    @Resource
    private JournalService journalService;

    /**
     * 内容列表
     *
     * @param title 标题
     * @param recommend 是否推荐
     * @param status 状态
     * @param channelId 栏目id
     * @param name 账户
     * @param realName 真实姓名
     * @param pageNum 页码
     * @param pageSize 每页数据量
     * @param request 请求
     * @return 列表页
     */
    @RequestMapping("/list")
    public String list(String title, Boolean recommend, Integer status, Integer channelId, String name, String realName, Integer pageNum, Integer pageSize, HttpServletRequest request) {
        if (pageNum == null)
            pageNum = 1;
        if (pageSize == null)
            pageSize = 10;
        try {
            // 获取栏目及子栏目id
            Integer[] channelIds = null;
            if (channelId != null) {
                List<Channel> list = (List<Channel>) request.getServletContext().getAttribute("channels");
                boolean beParent = false;
                for (Channel temp : list) {
                    if (temp.getId() == channelId) {
                        channelIds = new Integer[temp.getChildChannels().size() + 1];
                        channelIds[0] = channelId;
                        int i = 1;
                        for (Channel childChannel : temp.getChildChannels()) {
                            channelIds[i] = childChannel.getId();
                            i++;
                        }
                        beParent = true;
                        break;
                    }
                }
                if (!beParent) {
                    channelIds = new Integer[1];
                    channelIds[0] = channelId;
                }
            }
            // 获取用户，用于查看是否只管理自己的内容
            Integer userId = null;
//            Object obj = request.getSession().getAttribute("user");
            SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            BackgroundUser backgroundUser = backgroundUserService.getById(securityUser.getId());
            if (backgroundUser != null) {
                if (backgroundUser.getDataRange() == Constant.DATA_RANGE_MYSELF) {
                    userId = backgroundUser.getId();
                }
            }
            Page<Content> page = this.contentService.list(title, recommend, status, channelIds, userId, name, realName, pageNum, pageSize);
            request.setAttribute("page", page);
            request.setAttribute("activemenu", "contentmenu");
            // 反转译HTML内容
            if(title != null)
                request.setAttribute("title", HtmlUtils.htmlUnescape(title));
            if(name != null)
                request.setAttribute("name", HtmlUtils.htmlUnescape(name));
            if(realName != null)
                request.setAttribute("realName", HtmlUtils.htmlUnescape(realName));

            if (channelId == null) {
                request.setAttribute("activechildmenu", "ccmenu");
            } else {
                request.setAttribute("channelId", channelId);
                request.setAttribute("activechildmenu", "ccmenu" + channelId);
            }
            return "/backstage/content/list";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("activemenu", "homemenu");
            return "redirect:/backstage/index";
        }
    }

    @GetMapping("/{id}")
    public String content(@PathVariable("id") String id, HttpServletRequest request) {
        try {
            int contentId = Integer.parseInt(id);
            Content content = this.contentService.getById(contentId);
            content = HtmlUnEscapeUtil.unEscapeContent(content);
            request.setAttribute("content", content);
            switch (content.getChannel().getId()) {
                case Constant.CHANNEL_BOOK:
                    ContentBook contentBook = this.contentBookService.getByContent(content);
                    request.setAttribute("contentaddtion", HtmlUnEscapeUtil.unEscapeContentBook(contentBook));
                    break;
                case Constant.CHANNEL_PIC:
                case Constant.CHANNEL_MURAL:
                case Constant.CHANNEL_PAINTING:
                    ContentPic contentPic = this.contentPicService.getByContent(content);
                    request.setAttribute("contentaddtion", HtmlUnEscapeUtil.unEscapeContentPic(contentPic));
                    break;
                case Constant.CHANNEL_RUBBINGS:
                    ContentRubbings contentRubbings = this.contentRubbingsService.getByContent(content);
                    request.setAttribute("contentaddtion", HtmlUnEscapeUtil.unEscapeContentRubbings(contentRubbings));
                    break;
                case Constant.CHANNEL_AUDIO:
                    ContentAudio contentAudio = this.contentAudioService.getByContent(content);
                    request.setAttribute("contentaddtion", HtmlUnEscapeUtil.unEscapeContentAudio(contentAudio));
                    break;
                case Constant.CHANNEL_VIDEO:
                    ContentVideo contentVideo = this.contentVideoService.getByContent(content);
                    request.setAttribute("contentaddtion", HtmlUnEscapeUtil.unEscapeContentVideo(contentVideo));
                    break;
                case Constant.CHANNEL_3D:
                    Content3D content3D = this.content3DService.getByContent(content);
                    request.setAttribute("contentaddtion", HtmlUnEscapeUtil.unEscapeContent3D(content3D));
                    break;
                case Constant.CHANNEL_ALLSCENE:
                    ContentAllScene contentAllScene = this.contentAllSceneService.getByContent(content);
                    request.setAttribute("contentaddtion", HtmlUnEscapeUtil.unEscapeContentAllScene(contentAllScene));
                    break;
                case Constant.CHANNEL_JOURNAL:
                    ContentNews contentNews = this.contentNewsService.getByContent(content);
                    request.setAttribute("contentaddtion", HtmlUnEscapeUtil.unEscapeContentNews(contentNews));
            }
            request.setAttribute("activechildmenu", "ccmenu" + content.getChannel().getId());
            request.setAttribute("activemenu", "contentmenu");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("contentinfo", "查询出错，请稍后再试！");
        }
        return "/backstage/content/content";
    }


    @RequestMapping("/toadd")
    public String toAdd(Integer channelId, HttpServletRequest request) {
        request.setAttribute("activemenu", "contentmenu");
        if (channelId == null) {
            request.setAttribute("activechildmenu", "ccmenu");
            return "redirect:/backstage/content/list";
        } else {
            getChannelForTree(channelId, request);
            if (channelId == Constant.CHANNEL_JOURNAL) {
                List<Journal> list = this.journalService.listNormal();
                if (list != null && list.size() > 0) {
                    request.setAttribute("journals", list);
                } else {
                    request.setAttribute("contentinfo", "请先添加报刊");
                }
            }
            request.setAttribute("activechildmenu", "ccmenu" + channelId);
            return "/backstage/content/add";
        }
    }

    @PostMapping("/add")
    public String add(Content content, String seriesName, String authorName, String cover, String attachments, String time, String place, String publisher,
                      String pic, String path, Date newsTime, Integer section, String position, Integer journalId, Integer channelId, HttpServletRequest request) {
        switch (channelId) {
            case Constant.CHANNEL_BOOK:
                addBook(content, seriesName, authorName, cover, attachments, channelId, request);
                break;
            case Constant.CHANNEL_PIC:
            case Constant.CHANNEL_MURAL:
            case Constant.CHANNEL_PAINTING:
                addPic(content, time, place, publisher, pic, channelId, request);
                break;
            case Constant.CHANNEL_RUBBINGS:
                addRubbings(content, time, place, publisher, cover, path, channelId, request);
                break;
            case Constant.CHANNEL_AUDIO:
                addAudio(content, time, place, publisher, cover, path, channelId, request);
                break;
            case Constant.CHANNEL_VIDEO:
                addVideo(content, time, place, publisher, cover, path, channelId, request);
                break;
            case Constant.CHANNEL_3D:
                add3D(content, publisher, cover, path, channelId, request);
                break;
            case Constant.CHANNEL_ALLSCENE:
                addAllScene(content, publisher, cover, path, channelId, request);
                break;
            case Constant.CHANNEL_JOURNAL:
                addNews(content, newsTime, section, position, journalId, path, channelId, request);
        }
        return "forward:/backstage/content/toadd?channelId=" + channelId;
    }

    @RequestMapping("/toedit/{id}")
    public String toEdit(@PathVariable("id") int id, HttpServletRequest request) {
        Content content = null;
        try {
            content = this.contentService.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (content == null) {
            request.setAttribute("activechildmenu", "ccmenu");
            return "redirect:/backstage/content/list";
        } else {
            getChannelForTree(content.getChannel().getId(), request);
            switch (content.getChannel().getId()) {
                case Constant.CHANNEL_BOOK:
                    ContentBook contentBook = this.contentBookService.getByContent(content);
                    contentBook = HtmlUnEscapeUtil.unEscapeContentBook(contentBook);   // 反转译html
                    request.setAttribute("contentaddtion", contentBook);
                    break;
                case Constant.CHANNEL_PIC:
                case Constant.CHANNEL_MURAL:
                case Constant.CHANNEL_PAINTING:
                    ContentPic contentPic = this.contentPicService.getByContent(content);
                    contentPic = HtmlUnEscapeUtil.unEscapeContentPic(contentPic);   // 反转译html
                    request.setAttribute("contentaddtion", contentPic);
                    break;
                case Constant.CHANNEL_RUBBINGS:
                    ContentRubbings contentRubbings = this.contentRubbingsService.getByContent(content);
                    contentRubbings = HtmlUnEscapeUtil.unEscapeContentRubbings(contentRubbings);    // 反转译html
                    request.setAttribute("contentaddtion", contentRubbings);
                    break;
                case Constant.CHANNEL_AUDIO:
                    ContentAudio contentAudio = this.contentAudioService.getByContent(content);
                    contentAudio = HtmlUnEscapeUtil.unEscapeContentAudio(contentAudio);    // 反转译html
                    request.setAttribute("contentaddtion", contentAudio);
                    break;
                case Constant.CHANNEL_VIDEO:
                    ContentVideo contentVideo = this.contentVideoService.getByContent(content);
                    contentVideo = HtmlUnEscapeUtil.unEscapeContentVideo(contentVideo);    // 反转译html
                    request.setAttribute("contentaddtion", contentVideo);
                    break;
                case Constant.CHANNEL_3D:
                    Content3D content3D = this.content3DService.getByContent(content);
                    content3D = HtmlUnEscapeUtil.unEscapeContent3D(content3D);    // 反转译html
                    request.setAttribute("contentaddtion", content3D);
                    break;
                case Constant.CHANNEL_ALLSCENE:
                    ContentAllScene contentAllScene = this.contentAllSceneService.getByContent(content);
                    contentAllScene = HtmlUnEscapeUtil.unEscapeContentAllScene(contentAllScene);    // 反转译html
                    request.setAttribute("contentaddtion", contentAllScene);
                    break;
                case Constant.CHANNEL_JOURNAL:
                    ContentNews contentNews = this.contentNewsService.getByContent(content);
                    contentNews = HtmlUnEscapeUtil.unEscapeContentNews(contentNews);    // 反转译html
                    request.setAttribute("contentaddtion", contentNews);
                    if (content.getChannel().getId() == Constant.CHANNEL_JOURNAL) {
                        List<Journal> list = this.journalService.listNormal();
                        if (list != null && list.size() > 0) {
                            for(Journal journal : list) {   // 反转译html
                                journal = HtmlUnEscapeUtil.unEscapeJournal(journal);
                            }
                            request.setAttribute("journals", list);
                        }
                    }
                    break;
            }
            content = HtmlUnEscapeUtil.unEscapeContent(content);
            request.setAttribute("content", content);
            request.setAttribute("activechildmenu", "ccmenu" + content.getChannel().getId());
            request.setAttribute("activemenu", "contentmenu");
            return "/backstage/content/edit";
        }
    }

    @PostMapping("/edit")
    public String edit(Content content, String seriesName, String authorName, String cover, String attachments, String time, String place, String publisher,
                      String pic, String path, Date newsTime, Integer section, String position, Integer journalId, Integer channelId, HttpServletRequest request) {
        switch (channelId) {
            case Constant.CHANNEL_BOOK:
                editBook(content, seriesName, authorName, cover, attachments, channelId, request);
                break;
            case Constant.CHANNEL_PIC:
            case Constant.CHANNEL_MURAL:
            case Constant.CHANNEL_PAINTING:
                editPic(content, time, place, publisher, pic, channelId, request);
                break;
            case Constant.CHANNEL_RUBBINGS:
                editRubbings(content, time, place, publisher, cover, path, channelId, request);
                break;
            case Constant.CHANNEL_AUDIO:
                editAudio(content, time, place, publisher, cover, path, channelId, request);
                break;
            case Constant.CHANNEL_VIDEO:
                editVideo(content, time, place, publisher, cover, path, channelId, request);
                break;
            case Constant.CHANNEL_3D:
                edit3D(content, publisher, cover, path, channelId, request);
                break;
            case Constant.CHANNEL_ALLSCENE:
                editAllScene(content, publisher, cover, path, channelId, request);
                break;
            case Constant.CHANNEL_JOURNAL:
                editNews(content, journalId, newsTime, section, position, path, channelId, request);
                break;
        }
        return "forward:/backstage/content/toedit/" + content.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, @RequestParam("channelId") Integer channelId) {
        this.contentService.delete(id);
        if (channelId == null)
            return "redirect:/backstage/content/list";
        else
            return "redirect:/backstage/content/list?channelId=" + channelId;
    }

    @GetMapping("/deletes/{ids}")
    public String deletes(@PathVariable("ids") String ids, @RequestParam("channelId") Integer channelId) {
        this.contentService.deletes(ids);
        if (channelId == null)
            return "redirect:/backstage/content/list";
        else
            return "redirect:/backstage/content/list?channelId=" + channelId;
    }

    @GetMapping("/check/{id}")
    public String check(@PathVariable("id") int id, @RequestParam("channelId") Integer channelId) {
        this.contentService.check(id);
        if (channelId == null)
            return "redirect:/backstage/content/list";
        else
            return "redirect:/backstage/content/list?channelId=" + channelId;
    }

    @GetMapping("/checks/{ids}")
    public String checks(@PathVariable("ids") String ids, @RequestParam("channelId") Integer channelId) {
        this.contentService.checks(ids);
        if (channelId == null)
            return "redirect:/backstage/content/list";
        else
            return "redirect:/backstage/content/list?channelId=" + channelId;
    }

    @GetMapping("/deny/{id}")
    public String deny(@PathVariable("id") int id, @RequestParam("channelId") Integer channelId) {
        this.contentService.deny(id);
        if (channelId == null)
            return "redirect:/backstage/content/list";
        else
            return "redirect:/backstage/content/list?channelId=" + channelId;
    }

    @GetMapping("/denys/{ids}")
    public String denys(@PathVariable("ids") String ids, @RequestParam("channelId") Integer channelId) {
        this.contentService.denys(ids);
        if (channelId == null)
            return "redirect:/backstage/content/list";
        else
            return "redirect:/backstage/content/list?channelId=" + channelId;
    }

    /**
     * 如果是1级栏目则直接存入request，如果是二级栏目，则获取其一级栏目存入request
     *
     * @param channelId
     * @param request
     */
    public void getChannelForTree(Integer channelId, HttpServletRequest request) {
        request.setAttribute("channelId", channelId);
        List<Channel> list = null;
        Object obj = request.getServletContext().getAttribute("channels");
        if (obj != null) {
            list = (List<Channel>) obj;
        }
        // 获取同1级栏目，如果当前传入参数是2级栏目，则获取该栏目的1级栏目
        for (Channel channel : list) {
            if (channel.getId() == channelId) {
                request.setAttribute("channelTitle", channel.getTitle());
                request.setAttribute("parentChannel", channel);
                break;
            }
            if (channel.getChildChannels().size() > 0) {
                for (Channel childChannel : channel.getChildChannels()) {
                    if (childChannel.getId() == channelId) {
                        request.setAttribute("channelTitle", childChannel.getTitle());
                        request.setAttribute("parentChannel", channel);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 添加书籍
     *
     * @param content 内容
     * @param seriesName 丛书名
     * @param authorName 作者名
     * @param cover 封面
     * @param attachments PDF附件
     * @param channelId 栏目id
     * @param request 请求
     */
    public void addBook(Content content, String seriesName, String authorName, String cover, String attachments, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(cover)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(attachments)) {
            request.setAttribute("contentinfo", "请上传附件");
            checked = false;
        }
        if (checked) {
            try {
                // 自己实现登录时
//                BackgroundUser backgroundUser = (BackgroundUser) session.getAttribute("user");
                // 基于SpringSecurit
                SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                BackgroundUser backgroundUser = new BackgroundUser();
                backgroundUser.setId(securityUser.getId());

                this.contentService.addBook(content, channelId, backgroundUser, seriesName, authorName, cover, attachments);
                request.setAttribute("contentinfo", "填加书籍成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "填加书籍失败，请稍后再试");
            }
        } else {
            // 普通属性
            request.setAttribute("channelId", channelId);
            request.setAttribute("title", content.getTitle());
            request.setAttribute("shortTitle", content.getShortTitle());
            request.setAttribute("recommend", content.isRecommend());
            request.setAttribute("status", content.getStatus());
            request.setAttribute("tags", content.getTags());
            request.setAttribute("txt", content.getTxt());
            // 跟书籍有关的属性
            request.setAttribute("cover", cover);
            request.setAttribute("attachments", attachments);
            request.setAttribute("seriesName", seriesName);
            request.setAttribute("authorName", authorName);
        }
    }

    /**
     * 修改书籍
     *
     * @param content 内容
     * @param seriesName 丛书名
     * @param authorName 作者鸣
     * @param cover 封面
     * @param attachments PDF附件
     * @param channelId 栏目id
     * @param request 请求
     */
    public void editBook(Content content, String seriesName, String authorName, String cover, String attachments, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(cover)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(attachments)) {
            request.setAttribute("contentinfo", "请上传附件");
            checked = false;
        }
        // 验证成功，新增内容，失败返回重新填写
        if (checked) {
            try {
                this.contentService.editBook(content, channelId, seriesName, authorName, cover, attachments);
                request.setAttribute("contentinfo", "修改书籍成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "修改书籍失败，请稍后再试");
            }
        }
    }

    /**
     * 添加图片
     *
     * @param content
     * @param time
     * @param place
     * @param publisher
     * @param pic
     * @param channelId
     * @param request
     */
    public void addPic(Content content, String time, String place, String publisher, String pic, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(pic)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        }
        if (checked) {
            try {
                // 自己实现登录时
//                BackgroundUser backgroundUser = (BackgroundUser) session.getAttribute("user");
                // 基于SpringSecurit
                SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                BackgroundUser backgroundUser = new BackgroundUser();
                backgroundUser.setId(securityUser.getId());
                this.contentService.addPic(content, channelId, backgroundUser, time, place, publisher, pic);
                request.setAttribute("contentinfo", "填加图片成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "填加图片失败，请稍后再试");
            }
        } else {
            // 普通属性
            request.setAttribute("channelId", channelId);
            request.setAttribute("title", content.getTitle());
            request.setAttribute("shortTitle", content.getShortTitle());
            request.setAttribute("recommend", content.isRecommend());
            request.setAttribute("status", content.getStatus());
            request.setAttribute("tags", content.getTags());
            request.setAttribute("txt", content.getTxt());
            // 跟书籍有关的属性
            request.setAttribute("pic", pic);
            request.setAttribute("time", time);
            request.setAttribute("place", place);
            request.setAttribute("publisher", publisher);
        }
    }

    /**
     * 修改图片
     *
     * @param content
     * @param time
     * @param place
     * @param publisher
     * @param pic
     * @param channelId
     * @param request
     */
    public void editPic(Content content, String time, String place, String publisher, String pic, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(pic)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        }
        // 验证成功，新增内容，失败返回重新填写
        if (checked) {
            try {
                this.contentService.editPic(content, channelId, time, place, publisher, pic);
                request.setAttribute("contentinfo", "修改图片成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "修改图片失败，请稍后再试");
            }
        }
    }

    /**
     * 添加拓片
     *
     * @param content
     * @param time
     * @param place
     * @param publisher
     * @param cover
     * @param path
     * @param channelId
     * @param request
     */
    public void addRubbings(Content content, String time, String place, String publisher, String cover, String path, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(cover)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传拓片");
            checked = false;
        }
        if (checked) {
            try {
                // 自己实现登录时
//                BackgroundUser backgroundUser = (BackgroundUser) session.getAttribute("user");
                // 基于SpringSecurit
                SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                BackgroundUser backgroundUser = new BackgroundUser();
                backgroundUser.setId(securityUser.getId());
                this.contentService.addRubbings(content, channelId, backgroundUser, time, place, publisher, cover, path);
                request.setAttribute("contentinfo", "填加拓片成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "填加拓片失败，请稍后再试");
            }
        } else {
            // 普通属性
            request.setAttribute("channelId", channelId);
            request.setAttribute("title", content.getTitle());
            request.setAttribute("shortTitle", content.getShortTitle());
            request.setAttribute("recommend", content.isRecommend());
            request.setAttribute("status", content.getStatus());
            request.setAttribute("tags", content.getTags());
            request.setAttribute("txt", content.getTxt());
            // 跟书籍有关的属性
            request.setAttribute("cover", cover);
            request.setAttribute("time", time);
            request.setAttribute("place", place);
            request.setAttribute("publisher", publisher);
            request.setAttribute("path", path);
        }
    }

    /**
     * 修改拓片
     *
     * @param content
     * @param time
     * @param place
     * @param publisher
     * @param cover
     * @param path
     * @param channelId
     * @param request
     */
    public void editRubbings(Content content, String time, String place, String publisher, String cover, String path, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(cover)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传内容");
            checked = false;
        }
        // 验证成功，新增内容，失败返回重新填写
        if (checked) {
            try {
                this.contentService.editRubbings(content, channelId, time, place, publisher, cover, path);
                request.setAttribute("contentinfo", "修改拓片成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "修改拓片失败，请稍后再试");
            }
        }
    }

    /**
     * 添加音频
     *
     * @param content
     * @param time
     * @param place
     * @param publisher
     * @param cover
     * @param path
     * @param channelId
     * @param request
     */
    public void addAudio(Content content, String time, String place, String publisher, String cover, String path, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(cover)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传音频");
            checked = false;
        }
        if (checked) {
            try {
                // 自己实现登录时
//                BackgroundUser backgroundUser = (BackgroundUser) session.getAttribute("user");
                // 基于SpringSecurit
                SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                BackgroundUser backgroundUser = new BackgroundUser();
                backgroundUser.setId(securityUser.getId());
                this.contentService.addAudio(content, channelId, backgroundUser, time, place, publisher, cover, path);
                request.setAttribute("contentinfo", "填加音频成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "填加音频失败，请稍后再试");
            }
        } else {
            // 普通属性
            request.setAttribute("channelId", channelId);
            request.setAttribute("title", content.getTitle());
            request.setAttribute("shortTitle", content.getShortTitle());
            request.setAttribute("recommend", content.isRecommend());
            request.setAttribute("status", content.getStatus());
            request.setAttribute("tags", content.getTags());
            request.setAttribute("txt", content.getTxt());
            // 跟书籍有关的属性
            request.setAttribute("cover", cover);
            request.setAttribute("time", time);
            request.setAttribute("place", place);
            request.setAttribute("publisher", publisher);
            request.setAttribute("path", path);
        }
    }

    /**
     * 修改音频
     *
     * @param content
     * @param time
     * @param place
     * @param publisher
     * @param cover
     * @param path
     * @param channelId
     * @param request
     */
    public void editAudio(Content content, String time, String place, String publisher, String cover, String path, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(cover)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传内容");
            checked = false;
        }
        // 验证成功，新增内容，失败返回重新填写
        if (checked) {
            try {
                this.contentService.editAudio(content, channelId, time, place, publisher, cover, path);
                request.setAttribute("contentinfo", "修改音频成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "修改音频失败，请稍后再试");
            }
        }
    }

    /**
     * 添加视频
     *
     * @param content
     * @param time
     * @param place
     * @param publisher
     * @param cover
     * @param path
     * @param channelId
     * @param request
     */
    public void addVideo(Content content, String time, String place, String publisher, String cover, String path, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(cover)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传视频");
            checked = false;
        }
        if (checked) {
            try {
                // 自己实现登录时
//                BackgroundUser backgroundUser = (BackgroundUser) session.getAttribute("user");
                // 基于SpringSecurit
                SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                BackgroundUser backgroundUser = new BackgroundUser();
                backgroundUser.setId(securityUser.getId());
                this.contentService.addVideo(content, channelId, backgroundUser, time, place, publisher, cover, path);
                request.setAttribute("contentinfo", "填加视频成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "填加视频失败，请稍后再试");
            }
        } else {
            // 普通属性
            request.setAttribute("channelId", channelId);
            request.setAttribute("title", content.getTitle());
            request.setAttribute("shortTitle", content.getShortTitle());
            request.setAttribute("recommend", content.isRecommend());
            request.setAttribute("status", content.getStatus());
            request.setAttribute("tags", content.getTags());
            request.setAttribute("txt", content.getTxt());
            // 跟书籍有关的属性
            request.setAttribute("cover", cover);
            request.setAttribute("time", time);
            request.setAttribute("place", place);
            request.setAttribute("publisher", publisher);
            request.setAttribute("path", path);
        }
    }

    /**
     * 修改视频
     *
     * @param content
     * @param time
     * @param place
     * @param publisher
     * @param cover
     * @param path
     * @param channelId
     * @param request
     */
    public void editVideo(Content content, String time, String place, String publisher, String cover, String path, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(cover)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传内容");
            checked = false;
        }
        // 验证成功，新增内容，失败返回重新填写
        if (checked) {
            try {
                this.contentService.editVideo(content, channelId, time, place, publisher, cover, path);
                request.setAttribute("contentinfo", "修改视频成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "修改视频失败，请稍后再试");
            }
        }
    }

    /**
     * 添加3D模型
     *
     * @param content
     * @param publisher
     * @param cover
     * @param path
     * @param channelId
     * @param request
     */
    public void add3D(Content content, String publisher, String cover, String path, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(cover)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传内容");
            checked = false;
        }
        if (checked) {
            try {
                // 自己实现登录时
//                BackgroundUser backgroundUser = (BackgroundUser) session.getAttribute("user");
                // 基于SpringSecurit
                SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                BackgroundUser backgroundUser = new BackgroundUser();
                backgroundUser.setId(securityUser.getId());

                this.contentService.add3D(content, channelId, backgroundUser, publisher, cover, path);
                request.setAttribute("contentinfo", "填加3D模型成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "填加3D模型失败，请稍后再试");
            }
        } else {
            // 普通属性
            request.setAttribute("channelId", channelId);
            request.setAttribute("title", content.getTitle());
            request.setAttribute("shortTitle", content.getShortTitle());
            request.setAttribute("recommend", content.isRecommend());
            request.setAttribute("status", content.getStatus());
            request.setAttribute("tags", content.getTags());
            request.setAttribute("txt", content.getTxt());
            // 跟书籍有关的属性
            request.setAttribute("cover", cover);
            request.setAttribute("path", path);
            request.setAttribute("publisher", publisher);
        }
    }

    /**
     * 修改3D模型
     *
     * @param content
     * @param publisher
     * @param cover
     * @param path
     * @param channelId
     * @param request
     */
    public void edit3D(Content content, String publisher, String cover, String path, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(cover)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传内容");
            checked = false;
        }
        // 验证成功，新增内容，失败返回重新填写
        if (checked) {
            try {
                this.contentService.edit3D(content, channelId, publisher, cover, path);
                request.setAttribute("contentinfo", "修改3D模型成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "修改3D模型失败，请稍后再试");
            }
        }
    }

    /**
     * 添加360全景
     *
     * @param content
     * @param publisher
     * @param cover
     * @param path
     * @param channelId
     * @param request
     */
    public void addAllScene(Content content, String publisher, String cover, String path, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(cover)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传内容");
            checked = false;
        }
        if (checked) {
            try {
                // 自己实现登录时
//                BackgroundUser backgroundUser = (BackgroundUser) session.getAttribute("user");
                // 基于SpringSecurit
                SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                BackgroundUser backgroundUser = new BackgroundUser();
                backgroundUser.setId(securityUser.getId());

                this.contentService.addAllScene(content, channelId, backgroundUser, publisher, cover, path);
                request.setAttribute("contentinfo", "填加360全景成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "填加360全景失败，请稍后再试");
            }
        } else {
            // 普通属性
            request.setAttribute("channelId", channelId);
            request.setAttribute("title", content.getTitle());
            request.setAttribute("shortTitle", content.getShortTitle());
            request.setAttribute("recommend", content.isRecommend());
            request.setAttribute("status", content.getStatus());
            request.setAttribute("tags", content.getTags());
            request.setAttribute("txt", content.getTxt());
            // 跟书籍有关的属性
            request.setAttribute("cover", cover);
            request.setAttribute("path", path);
            request.setAttribute("publisher", publisher);
        }
    }

    /**
     * 修改360全景
     *
     * @param content
     * @param publisher
     * @param cover
     * @param path
     * @param channelId
     * @param request
     */
    public void editAllScene(Content content, String publisher, String cover, String path, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(cover)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传内容");
            checked = false;
        }
        // 验证成功，新增内容，失败返回重新填写
        if (checked) {
            try {
                this.contentService.editAllScene(content, channelId, publisher, cover, path);
                request.setAttribute("contentinfo", "修改360全景成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "修改360全景失败，请稍后再试");
            }
        }
    }

    /**
     * 添加报刊的新闻
     * @param content
     * @param newsTime
     * @param section
     * @param position
     * @param journalId
     * @param path
     * @param channelId
     * @param request
     */
    public void addNews(Content content, Date newsTime, Integer section, String position, Integer journalId, String path, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传视频");
            checked = false;
        } else if (journalId == null) {
            request.setAttribute("contentinfo", "请选择报刊");
            checked = false;
        } else if (newsTime == null) {
            request.setAttribute("contentinfo", "请设置报刊日期");
            checked = false;
        } else if (section == null) {
            request.setAttribute("contentinfo", "请设置报刊版面");
            checked = false;
        } else if (StringUtils.isEmpty(position)) {
            request.setAttribute("contentinfo", "请设置新闻位置");
            checked = false;
        }
        if (checked) {
            try {
                // 自己实现登录时
                SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                BackgroundUser backgroundUser = new BackgroundUser();
                backgroundUser.setId(securityUser.getId());
                this.contentService.addNews(content, channelId, backgroundUser, journalId, newsTime, section, position, path, request);
                request.setAttribute("contentinfo", "填加报刊的新闻成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "填加报刊的新闻失败，请稍后再试");
            }
        } else {
            // 普通属性
            request.setAttribute("channelId", channelId);
            request.setAttribute("title", content.getTitle());
            request.setAttribute("shortTitle", content.getShortTitle());
            request.setAttribute("recommend", content.isRecommend());
            request.setAttribute("status", content.getStatus());
            request.setAttribute("tags", content.getTags());
            request.setAttribute("txt", content.getTxt());
            // 跟书籍有关的属性
            request.setAttribute("journalId", journalId);
            request.setAttribute("newsTime", newsTime);
            request.setAttribute("section", section);
            request.setAttribute("position", position);
            request.setAttribute("path", path);
        }
    }

    /**
     * 修改报刊的新闻
     * @param content
     * @param journalId
     * @param newsTime
     * @param section
     * @param position
     * @param path
     * @param channelId
     * @param request
     */
    public void editNews(Content content, Integer journalId, Date newsTime, Integer section, String position, String path, Integer channelId, HttpServletRequest request) {
        boolean checked = true;
        if (channelId == null) {
            request.setAttribute("contentinfo", "请选择栏目类型");
            checked = false;
        } else if (StringUtils.isEmpty(content.getTitle())) {
            request.setAttribute("contentinfo", "请填写标题");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传图片");
            checked = false;
        } else if (StringUtils.isEmpty(path)) {
            request.setAttribute("contentinfo", "请上传内容");
            checked = false;
        } else if (journalId == null) {
            request.setAttribute("contentinfo", "请选择报刊");
            checked = false;
        } else if (newsTime == null) {
            request.setAttribute("contentinfo", "请填写时间");
            checked = false;
        } else if (section == null) {
            request.setAttribute("contentinfo", "请填写版块");
            checked = false;
        } else if (StringUtils.isEmpty(position)) {
            request.setAttribute("contentinfo", "请设置新闻位置");
            checked = false;
        }
        // 验证成功，新增内容，失败返回重新填写
        if (checked) {
            try {
                this.contentService.editNews(content, channelId, journalId, newsTime, section, position, path);
                request.setAttribute("contentinfo", "修改报刊的新闻成功");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("contentinfo", "修改报刊的新闻失败，请稍后再试");
            }
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}