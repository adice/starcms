package com.starrysky.starcms.util;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FileUtilController 用于文件上传
 * @Description 上传图片
 * @Author adi
 * @Date 2020-03-30 16:40
 */
@RestController
public class FileUploadController {
    @Resource
    private FileUploadUtil fileUploadUtil;

    @RequestMapping("/backstage/uploadpic")
    public List<FileUploadResponse> uploadPic(@RequestParam(value = "uploadfiles") MultipartFile[] files) {
        List<FileUploadResponse> list = new ArrayList<>();
        for(MultipartFile file : files) {
            list.add(fileUploadUtil.uploadPic(file));
        }
        return list;
    }

    @RequestMapping("/backstage/uploadpdf")
    public List<FileUploadResponse> uploadPdf(@RequestParam(value = "uploadfiles") MultipartFile[] files) {
        List<FileUploadResponse> list = new ArrayList<>();
        for(MultipartFile file : files) {
            list.add(fileUploadUtil.uploadPdf(file));
        }
        return list;
    }

    @RequestMapping("/backstage/uploadvideo")
    public List<FileUploadResponse> uploadVideo(@RequestParam(value = "uploadfiles") MultipartFile[] files) {
        List<FileUploadResponse> list = new ArrayList<>();
        for(MultipartFile file : files) {
            list.add(fileUploadUtil.uploadVideo(file));
        }
        return list;
    }

    @RequestMapping("/backstage/uploadaudio")
    public List<FileUploadResponse> uploadAudio(@RequestParam(value = "uploadfiles") MultipartFile[] files) {
        List<FileUploadResponse> list = new ArrayList<>();
        for(MultipartFile file : files) {
            list.add(fileUploadUtil.uploadAudio(file));
        }
        return list;
    }

    @RequestMapping("/backstage/uploadzip")
    public List<FileUploadResponse> uploadZip(@RequestParam(value = "uploadfiles") MultipartFile[] files) {
        List<FileUploadResponse> list = new ArrayList<>();
        for(MultipartFile file : files) {
            list.add(fileUploadUtil.uploadZip(file));
        }
        return list;
    }

}
