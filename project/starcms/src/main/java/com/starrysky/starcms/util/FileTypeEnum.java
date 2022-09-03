package com.starrysky.starcms.util;

/**
 * @ClassName FileTypeEnum
 * @Description
 * @Author adi
 * @Date 2022-09-02 16:29
 */
public enum FileTypeEnum {
    /**
     * JPEG  (jpg)
     */
    JPEG("JPEG", "FFD8FFE0"),

    JPG("JPG", "FFD8FFE"),

    /**
     * PNG
     */
    PNG("PNG", "89504E47"),

    /**
     * GIF
     */
    GIF("GIF", "47494638"),

    /**
     * TIFF (tif)
     */
    TIFF("TIF", "49492A00"),

    /**
     * Windows bitmap (bmp)
     */
    BMP("BMP", "424D"),

    /**
     * XML
     */
    XML("XML", "3C3F786D6C"),

    /**
     * HTML (html)
     */
    HTML("HTML", "68746D6C3E"),

    /**
     * Microsoft Word/Excel 注意：word 和 excel的文件头一样
     */
    XLS("XLS", "D0CF11E0"),

    /**
     * Microsoft Word/Excel 2007以上版本文件 注意：word 和 excel的文件头一样 504B030414000600080000002100
     */
    XLSX("XLSX", "504B0304"),
    /**
     * Microsoft Word/Excel 注意：word 和 excel的文件头一样
     */
    DOC("DOC", "D0CF11E0"),

    /**
     * Microsoft Word/Excel 2007以上版本文件 注意：word 和 excel的文件头一样
     */
    DOCX("DOCX", "504B0304"),

    /**
     * Adobe Acrobat (pdf) 255044462D312E
     */
    PDF("PDF", "255044462D312E"),

    /**
     * WAVE (wav)
     */
    WAV("WAV", "57415645"),

    /**
     * AVI
     */
    AVI("AVI", "41564920"),

    /**
     * MPEG (mpg)
     */
    MPG("MPG", "000001BA"),

    /**
     * Quicktime  (mov)
     */
    MOV("MOV", "6D6F6F76"),

    /**
     * MIDI (mid)
     */
    MID("MID", "4D546864"),

    /**
     * MP4
     */
    MP4("MP4", "00000020667479706D70"),

    /**
     * MP3
     */
    MP3("MP3", "4944330300"),

    /**
     * FLV
     */
    FLV("FLV", "464C5601"),

    /**
     * EXE Archive
     */
    EXE("EXE", "4D5A9000030000000400"),

    /**
     * ZIP Archive
     */
    ZIP("ZIP", "504B0304"),

    /**
     * RAR Archive
     */
    RAR("RAR", "52617221");

    /**
     * 后缀 大写字母
     */
    private final String suffix;

    /**
     * 魔数
     */
    private final String magicNumber;

    public String getSuffix() {
        return suffix;
    }

    public String getMagicNumber() {
        return magicNumber;
    }

    FileTypeEnum(String suffix, String magicNumber) {
        this.suffix = suffix;
        this.magicNumber = magicNumber;
    }

    public static FileTypeEnum getBySuffix(String suffix) {
        for (FileTypeEnum fileTypeEnum : values()) {
            if (fileTypeEnum.getSuffix().equals(suffix.toUpperCase())) {
                return fileTypeEnum;
            }
        }
        throw new IllegalArgumentException("unsupported file suffix : " + suffix);
    }
}
