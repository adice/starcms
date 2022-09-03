package com.starrysky.starcms.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName FileTypeUtil
 * @Description
 * @Author adi
 * @Date 2022-09-02 16:25
 */
public class FileTypeUtil {

    public static boolean checkFile(MultipartFile file, Set<FileTypeEnum> fileTypeSet, boolean isCheckMagic) {
        try {
            Set<String> suffixSet = new HashSet<>();
            for (FileTypeEnum fileTypeEnum : fileTypeSet) {
                suffixSet.add(fileTypeEnum.getSuffix());
            }
            if (checkSuffix(file, suffixSet)) {
                if(isCheckMagic) {
                    return checkMagicNumber(file, fileTypeSet);
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean checkMagicNumber(MultipartFile file, Set<FileTypeEnum> fileTypeSet) throws Exception {
        boolean checked = false;
        String magicNumber = readMagicNumber(file);
        for (FileTypeEnum fileTypeEnum : fileTypeSet) {
            if (fileTypeEnum.getMagicNumber().startsWith(magicNumber)) {
                checked = true;
                break;
            }
        }
        return checked;
    }

    private static boolean checkSuffix(MultipartFile file, Set<String> suffixSet) throws Exception {
        boolean checked = false;
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        for (String suffix : suffixSet) {
            if (suffix.toUpperCase().equalsIgnoreCase(fileSuffix)) {
                checked = true;
                break;
            }
        }
        return checked;
    }

    private static String readMagicNumber(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            byte[] fileHeader = new byte[4];
            is.read(fileHeader);
            return byteArray2Hex(fileHeader);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String byteArray2Hex(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        if (data == null || data.length <= 0) {
            return null;
        }
        for (byte datum : data) {
            int v = datum & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        try {
            InputStream is = new FileInputStream("D:\\b.mp3");
            byte[] fileHeader = new byte[8];
            is.read(fileHeader);
            String result = byteArray2Hex(fileHeader);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
