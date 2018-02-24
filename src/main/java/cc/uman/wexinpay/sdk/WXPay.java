package cc.uman.wexinpay.sdk;

import cc.uman.wexinpay.sdk.WXPayConstants.SignType;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Uman on 2018/2/24.
 * Email:icaishaohua@gmail.com
 */
public class WXPay {
    private WXPayConfig config;
    private WXPayConstants.SignType signType;
    private boolean useSandbox;

    public WXPay(WXPayConfig config) {
        this(config, SignType.MD5, false);
    }

    public WXPay(WXPayConfig config, SignType signType) {
        this(config, signType, false);
    }

    public WXPay(WXPayConfig config, SignType signType, boolean useSandbox) {
        this.config = config;
        this.signType = signType;
        this.useSandbox = useSandbox;
    }

    public Map<String, String> fillRequestData(Map<String, String> reqData) throws Exception {
        reqData.put("appid", this.config.getAppID());
        reqData.put("mch_id", this.config.getMchID());
        reqData.put("nonce_str", WXPayUtil.generateNonceStr());
        if (SignType.MD5.equals(this.signType)) {
            reqData.put("sign_type", "MD5");
        } else if (SignType.HMACSHA256.equals(this.signType)) {
            reqData.put("sign_type", "HMAC-SHA256");
        }

        reqData.put("sign", WXPayUtil.generateSignature(reqData, this.config.getKey(), this.signType));
        return reqData;
    }

    public boolean isResponseSignatureValid(Map<String, String> reqData) throws Exception {
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), this.signType);
    }

    public boolean isPayResultNotifySignatureValid(Map<String, String> reqData) throws Exception {
        String signTypeInData = (String) reqData.get("sign_type");
        SignType signType;
        if (signTypeInData == null) {
            signType = SignType.MD5;
        } else {
            signTypeInData = signTypeInData.trim();
            if (signTypeInData.length() == 0) {
                signType = SignType.MD5;
            } else if ("MD5".equals(signTypeInData)) {
                signType = SignType.MD5;
            } else {
                if (!"HMAC-SHA256".equals(signTypeInData)) {
                    throw new Exception(String.format("Unsupported sign_type: %s", new Object[]{signTypeInData}));
                }

                signType = SignType.HMACSHA256;
            }
        }

        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), signType);
    }

    public String requestWithoutCert(String strUrl, Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String UTF8 = "UTF-8";
        String reqBody = WXPayUtil.mapToXml(reqData);
        URL httpUrl = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setConnectTimeout(connectTimeoutMs);
        httpURLConnection.setReadTimeout(readTimeoutMs);
        httpURLConnection.connect();
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(reqBody.getBytes(UTF8));
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
        StringBuffer stringBuffer = new StringBuffer();
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }

        String resp = stringBuffer.toString();
        if (stringBuffer != null) {
            try {
                bufferedReader.close();
            } catch (IOException var18) {
                var18.printStackTrace();
            }
        }

        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException var17) {
                var17.printStackTrace();
            }
        }

        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException var16) {
                var16.printStackTrace();
            }
        }

        return resp;
    }

    public String requestWithCert(String strUrl, Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String UTF8 = "UTF-8";
        String reqBody = WXPayUtil.mapToXml(reqData);
        URL httpUrl = new URL(strUrl);
        char[] password = this.config.getMchID().toCharArray();
        InputStream certStream = this.config.getCertStream();
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(certStream, password);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), (TrustManager[]) null, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setConnectTimeout(connectTimeoutMs);
        httpURLConnection.setReadTimeout(readTimeoutMs);
        httpURLConnection.connect();
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(reqBody.getBytes(UTF8));
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
        StringBuffer stringBuffer = new StringBuffer();
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }

        String resp = stringBuffer.toString();
        if (stringBuffer != null) {
            try {
                bufferedReader.close();
            } catch (IOException var24) {
                ;
            }
        }

        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException var23) {
                ;
            }
        }

        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException var22) {
                ;
            }
        }

        if (certStream != null) {
            try {
                certStream.close();
            } catch (IOException var21) {
                ;
            }
        }

        return resp;
    }

    public Map<String, String> processResponseXml(String xmlStr) throws Exception {
        String RETURN_CODE = "return_code";
        Map respData = WXPayUtil.xmlToMap(xmlStr);
        if (respData.containsKey(RETURN_CODE)) {
            String return_code = (String) respData.get(RETURN_CODE);
            if (return_code.equals("FAIL")) {
                return respData;
            } else if (return_code.equals("SUCCESS")) {
                if (this.isResponseSignatureValid(respData)) {
                    return respData;
                } else {
                    throw new Exception(String.format("Invalid sign value in XML: %s", new Object[]{xmlStr}));
                }
            } else {
                throw new Exception(String.format("return_code value %s is invalid in XML: %s", new Object[]{return_code, xmlStr}));
            }
        } else {
            throw new Exception(String.format("No `return_code` in XML: %s", new Object[]{xmlStr}));
        }
    }

    public Map<String, String> microPay(Map<String, String> reqData) throws Exception {
        return this.microPay(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> microPay(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "https://api.mch.weixin.qq.com/sandboxnew/pay/micropay";
        } else {
            url = "https://api.mch.weixin.qq.com/pay/micropay";
        }

        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    public Map<String, String> unifiedOrder(Map<String, String> reqData) throws Exception {
        return this.unifiedOrder(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> unifiedOrder(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";
        } else {
            url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        }

        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    public Map<String, String> orderQuery(Map<String, String> reqData) throws Exception {
        return this.orderQuery(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> orderQuery(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "https://api.mch.weixin.qq.com/sandboxnew/pay/orderquery";
        } else {
            url = "https://api.mch.weixin.qq.com/pay/orderquery";
        }

        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    public Map<String, String> reverse(Map<String, String> reqData) throws Exception {
        return this.reverse(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> reverse(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "https://api.mch.weixin.qq.com/sandboxnew/secapi/pay/reverse";
        } else {
            url = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
        }

        String respXml = this.requestWithCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    public Map<String, String> closeOrder(Map<String, String> reqData) throws Exception {
        return this.closeOrder(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> closeOrder(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "https://api.mch.weixin.qq.com/sandboxnew/pay/closeorder";
        } else {
            url = "https://api.mch.weixin.qq.com/pay/closeorder";
        }

        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    public Map<String, String> refund(Map<String, String> reqData) throws Exception {
        return this.refund(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> refund(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "https://api.mch.weixin.qq.com/sandboxnew/secapi/pay/refund";
        } else {
            url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
        }

        String respXml = this.requestWithCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    public Map<String, String> refundQuery(Map<String, String> reqData) throws Exception {
        return this.refundQuery(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> refundQuery(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "https://api.mch.weixin.qq.com/sandboxnew/pay/refundquery";
        } else {
            url = "https://api.mch.weixin.qq.com/pay/refundquery";
        }

        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    public Map<String, String> downloadBill(Map<String, String> reqData) throws Exception {
        return this.downloadBill(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> downloadBill(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "https://api.mch.weixin.qq.com/sandboxnew/pay/downloadbill";
        } else {
            url = "https://api.mch.weixin.qq.com/pay/downloadbill";
        }

        String respStr = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs).trim();
        Object ret;
        if (respStr.indexOf("<") == 0) {
            ret = WXPayUtil.xmlToMap(respStr);
        } else {
            ret = new HashMap();
            ((Map) ret).put("return_code", "SUCCESS");
            ((Map) ret).put("return_msg", "ok");
            ((Map) ret).put("data", respStr);
        }

        return (Map) ret;
    }

    public Map<String, String> report(Map<String, String> reqData) throws Exception {
        return this.report(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> report(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "https://api.mch.weixin.qq.com/sandboxnew/pay/report";
        } else {
            url = "https://api.mch.weixin.qq.com/pay/report";
        }

        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    public Map<String, String> shortUrl(Map<String, String> reqData) throws Exception {
        return this.shortUrl(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> shortUrl(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "https://api.mch.weixin.qq.com/sandboxnew/tools/shorturl";
        } else {
            url = "https://api.mch.weixin.qq.com/tools/shorturl";
        }

        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    public Map<String, String> authCodeToOpenid(Map<String, String> reqData) throws Exception {
        return this.authCodeToOpenid(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> authCodeToOpenid(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "https://api.mch.weixin.qq.com/sandboxnew/tools/authcodetoopenid";
        } else {
            url = "https://api.mch.weixin.qq.com/tools/authcodetoopenid";
        }

        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }
}
