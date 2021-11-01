package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 魔数与文件类型
 * 小端
 * 文本文件不可靠，如 txt java xml
 * https://en.wikipedia.org/wiki/List_of_file_signatures
 */
@Slf4j
public class FileTypeUtils {
    //记录各个文件头信息及对应的文件类型

    private static final Map<String, String> fileTypeMap = new ConcurrentHashMap<>();

    static {
        // images
        fileTypeMap.put("FFD8FFDB", "jpg");
        fileTypeMap.put("FFD8FFE000104A46", "jpg");
        fileTypeMap.put("49460001", "jpg");
        fileTypeMap.put("FFD8FFEE", "jpg");
        fileTypeMap.put("69660000", "jpg");
        fileTypeMap.put("FFD8FFE1", "jpg");
        fileTypeMap.put("89504E470D0A1A0A", "png");
        fileTypeMap.put("474946383761", "gif");
        fileTypeMap.put("474946383961", "gif");
        fileTypeMap.put("49492A00", "tif");
        fileTypeMap.put("424D", "bmp");
        fileTypeMap.put("00000100", "ico");

        fileTypeMap.put("38425053", "psd");
        fileTypeMap.put("25215053", "ps");

        //办公文档类
        fileTypeMap.put("D0CF11E0A1B11AE1", "doc"); //ppt、doc、xls msg
        fileTypeMap.put("7B5C72746631", "rtf"); // 日记本
        fileTypeMap.put("255044462D", "pdf");

        //视频或音频类
        fileTypeMap.put("52494646", "wav");
        fileTypeMap.put("57415645", "wav");
        fileTypeMap.put("41564920", "avi");
        fileTypeMap.put("4D546864", "mid");//midi
        fileTypeMap.put("000001BA", "mpg");
        fileTypeMap.put("000001B3", "mpg");//m2p vob mpg mpeg
        fileTypeMap.put("3026B2758E66CF11", "asf");
        fileTypeMap.put("A6D900AA0062CE6C", "asf");//asf wma wmv
        fileTypeMap.put("6674797069736F6D", "mp4");//不准
        fileTypeMap.put("494433", "mp3");
        fileTypeMap.put("FFFB", "mp3");
        fileTypeMap.put("FFF3", "mp3");
        fileTypeMap.put("FFF2", "mp3");
        fileTypeMap.put("664C6143", "flac");
        fileTypeMap.put("464C56", "flv");
        fileTypeMap.put("4F676753", "ogg");//ogg oga ogv
        fileTypeMap.put("1A45DFA3", "mkv");//mkv mka mks mk3d webm


        fileTypeMap.put("EFBBBF", "txt");//UTF-8 byte order mark
        fileTypeMap.put("FFFE", "txt");//UTF-16LE byte order mark
        fileTypeMap.put("FFFF", "txt");//UTF-16BE byte order mark
        fileTypeMap.put("FFFE0000", "txt");//UTF-32LE byte order
        fileTypeMap.put("0000FEFF", "txt");//UTF-32BE byte order


        //程序文件
        fileTypeMap.put("3C3F786D6C20", "xml");
        fileTypeMap.put("3C003F0078006D00", "xml");
        fileTypeMap.put("6C0020", "xml");
        fileTypeMap.put("003C003F0078006D", "xml");
        fileTypeMap.put("006C0020", "xml");
        fileTypeMap.put("3C0000003F000000", "xml");
        fileTypeMap.put("780000006D000000", "xml");
        fileTypeMap.put("6C00000020000000", "xml");
        fileTypeMap.put("0000003C0000003F", "xml");
        fileTypeMap.put("000000780000006D", "xml");
        fileTypeMap.put("4C6FA7949340", "xml");
        fileTypeMap.put("7061636B", "java");
        fileTypeMap.put("CAFEBABE", "class");
        fileTypeMap.put("4D5A9000", "exe");
        fileTypeMap.put("7F454C46", "elf");


        fileTypeMap.put("5265636569766564", "eml"); // 邮件


        //压缩包
        fileTypeMap.put("526172211A0700", "rar");//compressed archive v1.50
        fileTypeMap.put("526172211A070100", "rar");//compressed archive v5.00
        fileTypeMap.put("1F8B08", "gz");


        fileTypeMap.put("504B0304", "zip"); //zip aar apk docx epub ipa jar kmz maff odp ods odt pk3 pk4 pptx usdz vsdx xlsx xpi
        fileTypeMap.put("504B0506", "zip");//empty archive
        fileTypeMap.put("504B0708", "zip");//spanned archive
        fileTypeMap.put("377ABCAF271C", "7z");
        fileTypeMap.put("7573746172003030", "tar");
        fileTypeMap.put("7573746172202000", "tar");
        fileTypeMap.put("1F8B", "gz");
        fileTypeMap.put("04224D18", "lz4");
        fileTypeMap.put("FD377A585A 0", "xz");
        fileTypeMap.put("4344303031", "ios");
        fileTypeMap.put("6B6F6C79", "dmg");


        fileTypeMap.put("D4C3B2A1", "pcap");//Libpcap File Format
        fileTypeMap.put("4D3CB2A1", "pcap");//Libpcap File Format (nanosecond-resolution)

    }

    /**
     * 根据文件的输入流获取文件类型
     *
     * @param inputStream 文件内容流
     * @return 文件类型
     */
    public static String getFileType(InputStream inputStream) {
        String fileHeaderHexString = getFileHeaderHexByInputStream(inputStream);
        log.info("fileHeaderHexString : {}", fileHeaderHexString);
        System.err.println(fileHeaderHexString);
        for (Map.Entry<String, String> entry : fileTypeMap.entrySet()) {
            if (fileHeaderHexString.startsWith(entry.getKey())) {
                return fileTypeMap.get(entry.getKey());
            }
        }
        return null;
    }

    /**
     * 根据文件的输入流获取文件类型
     *
     * @param path
     * @return 文件类型
     */
    public static String getFileType(String path) {
        String fileHeaderHexString = getFileHeaderHexByPath(path);
        log.info("fileHeaderHexString : {}", fileHeaderHexString);
        System.err.println(fileHeaderHexString);
        for (Map.Entry<String, String> entry : fileTypeMap.entrySet()) {
            if (fileHeaderHexString.startsWith(entry.getKey())) {
                return fileTypeMap.get(entry.getKey());
            }
        }
        return null;
    }


    /**
     * 获取文件头（即文件魔数），通过文件流
     *
     * @param inputStream
     * @return fileHeaderHex - 文件头，即文件魔数
     */
    private static String getFileHeaderHexByInputStream(InputStream inputStream) {
        byte[] b = new byte[28];
        try {
            inputStream.read(b, 0, 28);
        } catch (IOException e) {
            log.error(String.valueOf(e));
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(String.valueOf(e));
                }
            }
        }
        return bytesToHexString(b);
    }

    /**
     * 获取文件头（即文件魔数），根据文件路径
     *
     * @param path
     * @return fileHeaderHex - 文件头，即文件魔数
     */
    private static String getFileHeaderHexByPath(String path) {
        byte[] b = new byte[28];
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
            inputStream.read(b, 0, b.length);
        } catch (IOException e) {
            log.error(String.valueOf(e));
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(String.valueOf(e));
                }
            }
        }
        return bytesToHexString(b);
    }

    /**
     * 将要读取文件头信息的文件的byte数组转换成string类型表示
     * <p/>
     * 下面这段代码就是用来对文件类型作验证的方法，
     * <p/>
     * 将字节数组的前四位转换成16进制字符串，并且转换的时候，要先和0xFF做一次与运算。
     * <p/>
     * 这是因为，整个文件流的字节数组中，有很多是负数，进行了与运算后，可以将前面的符号位都去掉，
     * <p/>
     * 这样转换成的16进制字符串最多保留两位，如果是正数又小于10，那么转换后只有一位，
     * <p/>
     * 需要在前面补0，这样做的目的是方便比较，取完前四位这个循环就可以终止了
     *
     * @param src 要读取文件头信息的文件的byte数组
     * @return 文件头信息
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }
}
