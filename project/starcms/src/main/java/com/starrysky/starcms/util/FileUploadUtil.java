package com.starrysky.starcms.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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

    public FileUploadResponse uploadPic(MultipartFile file) {
        Set<FileTypeEnum> fileTypeEnumSet = new HashSet<>();
        fileTypeEnumSet.add(FileTypeEnum.JPEG);
        fileTypeEnumSet.add(FileTypeEnum.JPG);
        fileTypeEnumSet.add(FileTypeEnum.BMP);
        fileTypeEnumSet.add(FileTypeEnum.BMP_16);
        fileTypeEnumSet.add(FileTypeEnum.BMP_24);
        fileTypeEnumSet.add(FileTypeEnum.BMP_256);
        fileTypeEnumSet.add(FileTypeEnum.PNG);
        fileTypeEnumSet.add(FileTypeEnum.GIF);
        fileTypeEnumSet.add(FileTypeEnum.TIFF);
        return checkAndUploadFile(file, fileTypeEnumSet);
    }

    public FileUploadResponse uploadPdf(MultipartFile file) {
        Set<FileTypeEnum> fileTypeEnumSet = new HashSet<>();
        fileTypeEnumSet.add(FileTypeEnum.PDF);
        return checkAndUploadFile(file, fileTypeEnumSet);
    }

    public FileUploadResponse uploadVideo(MultipartFile file) {
        Set<FileTypeEnum> fileTypeEnumSet = new HashSet<>();
        fileTypeEnumSet.add(FileTypeEnum.MP4);
        fileTypeEnumSet.add(FileTypeEnum.AVI);
        fileTypeEnumSet.add(FileTypeEnum.FLV);
        fileTypeEnumSet.add(FileTypeEnum.MOV);
        return checkAndUploadFile(file, fileTypeEnumSet);
    }

    public FileUploadResponse uploadAudio(MultipartFile file) {
        Set<FileTypeEnum> fileTypeEnumSet = new HashSet<>();
        fileTypeEnumSet.add(FileTypeEnum.MP3);
        fileTypeEnumSet.add(FileTypeEnum.WAV);
        return checkAndUploadFile(file, fileTypeEnumSet);
    }

    public FileUploadResponse uploadZip(MultipartFile file) {
        Set<FileTypeEnum> fileTypeEnumSet = new HashSet<>();
        fileTypeEnumSet.add(FileTypeEnum.ZIP);
        fileTypeEnumSet.add(FileTypeEnum.RAR);
        return checkAndUploadFile(file, fileTypeEnumSet);
    }

    private FileUploadResponse checkAndUploadFile(MultipartFile file, Set<FileTypeEnum> fileTypeEnumSet) {
        if(FileTypeUtil.checkFile(file, fileTypeEnumSet)) {
            return uploadFile(file, FILE_NAME_UUID);
        } else {
            FileUploadResponse fileUploadResponse = new FileUploadResponse();
            fileUploadResponse.setCode(2);
            fileUploadResponse.setMsg("上传类型不符合要求");
            fileUploadResponse.setUrl("/");
            return fileUploadResponse;
        }
    }

    /**
     * @param file      上传的文件
     * @param nameStyle 命名方式，目前支持3种：1取当前时间命名，2UUID命名，3上传文件原名命名
     * @return java.lang.String 上传后的url
     * @description 上传文件，以月为单位分文件夹存储
     * @author adi
     */
    private FileUploadResponse uploadFile(MultipartFile file, int nameStyle) {

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
