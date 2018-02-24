package cc.uman.wexinpay.sdk;

import java.io.InputStream;

/**
 * Created by Uman on 2018/2/24.
 * Email:icaishaohua@gmail.com
 */
public interface WXPayConfig {
    String getAppID();

    String getMchID();

    String getKey();

    InputStream getCertStream();

    int getHttpConnectTimeoutMs();

    int getHttpReadTimeoutMs();
}
