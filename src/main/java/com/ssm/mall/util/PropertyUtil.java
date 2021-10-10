package com.ssm.mall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertyUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;
    static {
        props = new Properties();
        try {
            props.load(new InputStreamReader(
                    PropertyUtil.class.getClassLoader()
                            .getResourceAsStream("props/ssm-mall.properties"), "utf-8"));
        } catch (IOException e) {
            logger.debug("props/ssm-mall.properties配置文件读取异常", e);
        }
    }

    //读取属性内容，失败返回null
    public static String getProperty(String key) {
        //注意trim的应用，因为配置文件书写时会加入部分空格
        String value = props.getProperty(key.trim());
        return StringUtils.isNotBlank(value) ? value.trim() : null;
    }

    //读取属性文件内容，失败返回defaultVal
    public static String getProperty(String key, String defaultVal) {
        String value = props.getProperty(key.trim());
        return StringUtils.isNotBlank(value) ? value.trim() : defaultVal;
    }
}
