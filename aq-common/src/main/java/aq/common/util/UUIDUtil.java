package aq.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UUIDUtil {

    /**
     * 功能：去掉UUID中的“-”
     */
    public static String replaceUUID(String uuid) {
        uuid = uuid.replaceAll("\\-", "");
        return uuid;
    }
    /**
     * 功能：获取UUID并去除“-”
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("\\-", "");
    }

    /**
     * 功能：获取当前时间作为编号
     * @return
     */
    public static String getRandomReqNo() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return df.format(new Date()).toString();// new Date()为获取当前系统时间，也可使用当前时间戳
    }
}
