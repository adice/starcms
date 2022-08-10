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
public class FileUtilController {
    @Resource
    private UploadFileUtil uploadFileUtil;

    @RequestMapping("/backstage/uploadpic")
    public List<UploadResponse> uploadPic(@RequestParam(value = "uploadfiles") MultipartFile[] files) {
        List<UploadResponse> list = new ArrayList<>();
        for(MultipartFile file : files) {
            list.add(uploadFileUtil.upoloadPic(file, UploadFileUtil.FILE_NAME_UUID));
        }
        return list;
    }

    @RequestMapping("/backstage/uploadfile")
    public List<UploadResponse> uploadFile(@RequestParam(value = "uploadfiles") MultipartFile[] files) {
        List<UploadResponse> list = new ArrayList<>();
        for(MultipartFile file : files) {
            list.add(uploadFileUtil.upoloadPic(file, UploadFileUtil.FILE_NAME_UUID));
        }
        return list;
    }

}
