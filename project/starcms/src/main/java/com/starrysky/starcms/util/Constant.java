package com.starrysky.starcms.util;

/**
 * @ClassName Constant
 * @Description
 * @Author adi
 * @Date 2020-03-11 18:35
 */
public class Constant {

    public static final String UPLOAD_PATH = "D:/starcms/contents/";

    // 栏目编号
    public static final int CHANNEL_BOOK = 1;
    public static final int CHANNEL_PIC = 2;
    public static final int CHANNEL_RUBBINGS = 3;
    public static final int CHANNEL_AUDIO = 4;
    public static final int CHANNEL_VIDEO = 5;
    public static final int CHANNEL_3D = 6;
    public static final int CHANNEL_ALLSCENE = 7;
    public static final int CHANNEL_MURAL = 8;
    public static final int CHANNEL_PAINTING = 9;
    public static final int CHANNEL_JOURNAL = 10;

    // 内容状态，1-草稿，2-审核中，3-审核通过，4-审核失败，5-回收站（删除）
    public static final int CONTENT_STATUS_DRAFT = 1;
    public static final int CONTENT_STATUS_AUDITING = 2;
    public static final int CONTENT_STATUS_AUDITSUCCESS = 3;
    public static final int CONTENT_STATUS_AUDITFAILURE = 4;
    public static final int CONTENT_STATUS_RECYLE = 5;

    // 用户、栏目的状态，1-正常，2-禁用
    public static final int STATE_NORMAL = 1;
    public static final int STATE_FORBIDDEN = 2;

    // 用户对数据的管理范围，1-所有人的数据，2-自己的数据
    public static final int DATA_RANGE_ALL = 1;
    public static final int DATA_RANGE_MYSELF = 2;

    // 验证码
    public static final String KAPTCHA_SESSION_KEY = "vcode";

}
