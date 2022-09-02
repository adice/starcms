package com.starrysky.starcms.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName UploadFileUtil
 * @Description 用于上传文件的工具类
 * @Author adi
 * @Date 2020-04-22 16:53
 */
@Component
public class FileUploadUtil {
    /**
     * 文件命名的不同方式
     */
    public static final int FILE_NAME_TIME = 1;
    public static final int FILE_NAME_UUID = 2;
    public static final int FILE_NAME_ORIGINAL = 3;

    /**
     * @param file      上传的文件
     * @param nameStyle 命名方式，目前支持3种：1取当前时间命名，2UUID命名，3上传文件原名命名
     * @return java.lang.String 上传后的url
     * @description 上传文件，以月为单位分文件夹存储
     * @author adi
     */
    public FileUploadResponse upoloadFile(MultipartFile file, int nameStyle) {
        // 获取上传文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        // 根据要求设置上传后的文件名
        String fileName = null;
        if (nameStyle == FILE_NAME_TIME) {
            fileName = nameFileByTime() + suffix;
        } else if (nameStyle == FILE_NAME_UUID) {
            fileName = nameFileByUUID() + suffix;
        } else if (nameStyle == FILE_NAME_ORIGINAL) {
            fileName = file.getOriginalFilename();
        }
        String dirTime = new SimpleDateFormat("yyyyMM").format(new Date());
        File directory = new File(Constant.UPLOAD_PATH + dirTime);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // 指定上传路径
        File localFile = new File(directory.getAbsolutePath() + "/" + fileName);
        // 写入本地文件
        try {
            file.transferTo(localFile);
            FileUploadResponse fileUploadResponse = new FileUploadResponse();
            fileUploadResponse.setCode(1);
            fileUploadResponse.setMsg("上传成功");
            fileUploadResponse.setUrl("/" + dirTime + "/" + fileName);
            return fileUploadResponse;
        } catch (IOException e) {
            e.printStackTrace();
            FileUploadResponse fileUploadResponse = new FileUploadResponse();
            fileUploadResponse.setCode(2);
            fileUploadResponse.setMsg("上传失败");
            fileUploadResponse.setUrl("/");
            return fileUploadResponse;
        }
    }

    /**
     * @return java.lang.String
     * @description 取当前时间，用于命名文件
     * @author adi
     */
    private static String nameFileByTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * @return java.lang.String
     * @description 取UUID，用于命名文件
     * @author adi
     */
    private static String nameFileByUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }
}
