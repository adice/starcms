package com.starrysky.starcms.content.controller;

import com.starrysky.starcms.backstageuser.service.BackgroundUserService;
import com.starrysky.starcms.content.service.ContentService;
import com.starrysky.starcms.content3d.service.Content3DService;
import com.starrysky.starcms.contentallscene.service.ContentAllSceneService;
import com.starrysky.starcms.contentaudio.service.ContentAudioService;
import com.starrysky.starcms.contentbook.service.ContentBookService;
import com.starrysky.starcms.contentpic.service.ContentPicService;
import com.starrysky.starcms.contentrubbings.service.ContentRubbingsService;
import com.starrysky.starcms.contentvideo.service.ContentVideoService;
import com.starrysky.starcms.entity.*;
import com.starrysky.starcms.security.SecurityUser;
import com.starrysky.starcms.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @ClassName ContentController
 * @Description
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
    private BackgroundUserService backgroundUserService;

    /**
     * 内容列表
     * @param title
     * @param recommend
     * @param status
     * @param channelId
     * @param name
     * @param realName
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
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
                if (beParent == false) {
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

    @RequestMapping("/toadd")
    public String toAdd(Integer channelId, HttpServletRequest request) {
        request.setAttribute("activemenu", "contentmenu");
        if (channelId == null) {
            request.setAttribute("activechildmenu", "ccmenu");
            return "redirect:/backstage/content/list";
        } else {
            getChannelForTree(channelId, request);
            request.setAttribute("activechildmenu", "ccmenu" + channelId);
            return "/backstage/content/add";
        }
    }

    @PostMapping("/add")
    public String add(Content content, String seriesName, String authorName, String cover, String attachments, String time, String place, String publisher, String pic, String path, Integer channelId, HttpServletRequest request, HttpSession session) {
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
                addAllScene(content, publisher,cover, path, channelId, request);
                break;
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
                    request.setAttribute("contentaddtion", contentBook);
                    break;
                case Constant.CHANNEL_PIC:
                case Constant.CHANNEL_MURAL:
                case Constant.CHANNEL_PAINTING:
                    ContentPic contentPic = this.contentPicService.getByContent(content);
                    request.setAttribute("contentaddtion", contentPic);
                    break;
                case Constant.CHANNEL_RUBBINGS:
                    ContentRubbings contentRubbings = this.contentRubbingsService.getByContent(content);
                    request.setAttribute("contentaddtion", contentRubbings);
                    break;
                case Constant.CHANNEL_AUDIO:
                    ContentAudio contentAudio = this.contentAudioService.getByContent(content);
                    request.setAttribute("contentaddtion", contentAudio);
                    break;
                case Constant.CHANNEL_VIDEO:
                    ContentVideo contentVideo = this.contentVideoService.getByContent(content);
                    request.setAttribute("contentaddtion", contentVideo);
                    break;
                case Constant.CHANNEL_3D:
                    Content3D content3D = this.content3DService.getByContent(content);
                    request.setAttribute("contentaddtion", content3D);
                    break;
                case Constant.CHANNEL_ALLSCENE:
                    ContentAllScene contentAllScene = this.contentAllSceneService.getByContent(content);
                    request.setAttribute("contentaddtion", contentAllScene);
                    break;
            }
            request.setAttribute("content", content);
            request.setAttribute("activechildmenu", "ccmenu" + content.getChannel().getId());
            request.setAttribute("activemenu", "contentmenu");
            return "/backstage/content/edit";
        }
    }

    @PostMapping("/edit")
    public String edit(Content content, String seriesName, String authorName, String cover, String attachments, String time, String place, String publisher, String pic, String path, Integer channelId, HttpServletRequest request) {
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
     * @param content
     * @param seriesName
     * @param authorName
     * @param cover
     * @param attachments
     * @param channelId
     * @param request
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
     * @param content
     * @param seriesName
     * @param authorName
     * @param cover
     * @param attachments
     * @param channelId
     * @param request
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
        } else if(StringUtils.isEmpty(path)){
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
        } else if(StringUtils.isEmpty(path)){
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
        } else if(StringUtils.isEmpty(path)){
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

}