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
    JPEG("JPEG", "FFD8FF"),

    JPG("JPG", "FFD8FF"),

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
     * 16色位图(bmp)
     */
    BMP_16("BMP", "424D228C010000000000"),

    /**
     * 24位位图(bmp)
     */
    BMP_24("BMP", "424D8240090000000000"),

    /**
     * 256色位图(bmp)
     */
    BMP_256("BMP", "424D8E1B030000000000"),

    /**
     * CAD  (dwg)
     */
    DWG("DWG", "41433130"),

    /**
     * Adobe photoshop  (psd)
     */
    PSD("PSD", "38425053"),

    /**
     * Rich Text Format  (rtf)
     */
    RTF("RTF", "7B5C727466"),

    /**
     * XML
     */
    XML("XML", "3C3F786D6C"),

    /**
     * HTML (html)
     */
    HTML("HTML", "68746D6C3E"),

    /**
     * Email [thorough only] (eml)
     */
    EML("EML", "44656C69766572792D646174653A"),

    /**
     * doc;xls;dot;ppt;xla;ppa;pps;pot;msi;sdw;db
     */
    OLE2("OLE2", "0xD0CF11E0A1B11AE1"),

    /**
     * Microsoft Word/Excel 注意：word 和 excel的文件头一样
     */
    XLS("XLS", "D0CF11E0"),

    /**
     * Microsoft Word/Excel 注意：word 和 excel的文件头一样
     */
    DOC("DOC", "D0CF11E0"),

    /**
     * Microsoft Word/Excel 2007以上版本文件 注意：word 和 excel的文件头一样
     */
    DOCX("DOCX", "504B0304"),

    /**
     * Microsoft Word/Excel 2007以上版本文件 注意：word 和 excel的文件头一样 504B030414000600080000002100
     */
    XLSX("XLSX", "504B0304"),

    /**
     * Microsoft Access (mdb)
     */
    MDB("MDB", "5374616E64617264204A"),

    /**
     * Adobe Acrobat (pdf) 255044462D312E
     */
    PDF("PDF", "25504446"),

    /**
     * WAVE (wav)
     */
    WAV("WAV", "57415645"),

    /**
     * AVI
     */
    AVI("AVI", "41564920"),

    /**
     * Real Audio (ram)
     */
    RAM("RAM", "2E7261FD"),

    /**
     * Real Media (rm) rmvb/rm相同
     */
    RM("RM", "2E524D46"),

    /**
     * Real Media (rm) rmvb/rm相同
     */
    RMVB("RMVB", "2E524D46000000120001"),

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
    MP3("MP3", "49443303000000002176"),

    /**
     * FLV
     */
    FLV("FLV", "464C5601050000000900"),

    /**
     * EXE Archive
     */
    EXE("EXE", "4D5A9000030000000400"),

    /**
     * ZIP Archive
     */
    ZIP("ZIP", ""),

    /**
     * RAR Archive
     */
    RAR("RAR", "");

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
