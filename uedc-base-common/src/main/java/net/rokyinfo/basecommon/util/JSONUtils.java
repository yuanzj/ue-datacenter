package net.rokyinfo.basecommon.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/21.
 */
public class JSONUtils {

    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;

    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
    }

    public static final String toJSONString(Object object) {

        return JSON.toJSONString(object, SerializerFeature.WriteDateUseDateFormat);
    }

    public static String toJSONString(String dateFormat, String jsonText) {

        mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
        return JSON.toJSONString(jsonText, mapping);
    }
}
