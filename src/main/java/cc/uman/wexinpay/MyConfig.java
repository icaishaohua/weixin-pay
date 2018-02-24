package cc.uman.wexinpay;

import cc.uman.wexinpay.sdk.WXPayConfig;
import cc.uman.wexinpay.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Created by van on 2017/7/13.
 * Email:icaishaohua@gmail.com
 */
public class MyConfig implements WXPayConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String FILE_NAME = "uman_config.properties";
    /**
     * 微信APP_id
     */
    private static final String WeiXin_PAY_APP_ID = "WeiXin_PAY_APP_ID";
    /**
     * 微信商户号
     */
    private static final String WeiXin_PAY_MchID = "WeiXin_PAY_MchID";

    /**
     * 微信私钥
     */
    private static final String WeiXin_PAY_APP_KEY = "WeiXin_PAY_APP_KEY";

    /**
     * 微信安全证书地址
     */
    private static final String WeiXin_Cert_Path = "WeiXin_Cert_Path";

    /**
     * 属性文件对象.
     */
    private Properties properties;

    private String weixinPayAppId;
    private String weixinPayMchId;
    private String weixinPayAppKey;
    private String weixinPayCertPath;


    private byte[] certData;
    /**
     * 操作对象.
     */
    private static MyConfig config = new MyConfig();

    public static MyConfig getConfig() {
        return config;
    }

    public MyConfig() {
        super();
    }

    /**
     * 从classpath路径下加载配置参数
     */
    public MyConfig loadPropertiesFromSrc() {
        InputStream in = null;
        try {
            logger.info("从classpath: " + MyConfig.class.getClassLoader().getResource("").getPath() + " 获取属性文件" + FILE_NAME);
            in = MyConfig.class.getClassLoader().getResourceAsStream(FILE_NAME);
            if (null != in) {
                properties = new Properties();
                try {
                    properties.load(in);
                } catch (IOException e) {
                    throw e;
                }
            } else {
                logger.error(FILE_NAME + "属性文件未能在classpath指定的目录下 " + MyConfig.class.getClassLoader().getResource("").getPath() + " 找到!");
            }
            loadProperties(properties);
        } catch (IOException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogUtil.writeErrorLog(e.getMessage(), e);
                }
            }
        }
        return config;
    }

    public void loadProperties(Properties pro) throws IOException {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();

        logger.info("开始从属性文件中加载配置项");
        String value = null;
        value = pro.getProperty(WeiXin_PAY_APP_ID);
        if (!isEmpty(value)) {
            this.weixinPayAppId = value.trim();
            logger.info("配置项：微信WeiXin_PAY_APP_ID==>" + weixinPayAppId + "==>" + value + " 已加载");
        }

        value = pro.getProperty(WeiXin_PAY_MchID);
        if (!isEmpty(value)) {
            this.weixinPayMchId = value.trim();
            logger.info("配置项：微信WeiXin_PAY_MchID==>" + weixinPayMchId + "==>" + value + " 已加载");
        }

        value = pro.getProperty(WeiXin_PAY_APP_KEY);
        if (!isEmpty(value)) {
            this.weixinPayAppKey = value.trim();
            logger.info("配置项：微信weixinPayAppKey==>" + weixinPayAppKey + "==>" + value + " 已加载");
        }

        value = pro.getProperty(WeiXin_Cert_Path);
        value = path + value;
        if (!isEmpty(value)) {
            this.weixinPayCertPath = value.trim();
            File file = new File(weixinPayCertPath);
            InputStream certStream = new FileInputStream(file);
            this.certData = new byte[(int) file.length()];
            certStream.read(this.certData);
            certStream.close();
            logger.info("配置项：微信weixinPayAppKey==>" + certData.toString() + "==>" + value + " 已加载");
        }
    }

    public String getAppID() {
        return weixinPayAppId;
    }

    public String getMchID() {
        return weixinPayMchId;
    }

    public String getKey() {
        return weixinPayAppKey;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }


    /**
     * 判断字符串是否为NULL或空
     *
     * @param s 待判断的字符串数据
     * @return 判断结果 true-是 false-否
     */
    public static boolean isEmpty(String s) {
        return null == s || "".equals(s.trim());
    }


    public static void main(String args[]) {
        MyConfig.getConfig().loadPropertiesFromSrc();
    }
}
