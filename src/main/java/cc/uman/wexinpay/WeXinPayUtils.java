package cc.uman.wexinpay;


import cc.uman.wexinpay.sdk.WXPay;
import cc.uman.wexinpay.sdk.WXPayUtil;
import cc.uman.wexinpay.util.WeiXinPayUtils;
import org.jdom.JDOMException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付utils
 * Created by Uman on 2018/2/24.
 * Email:icaishaohua@gmail.com
 */
public class WeXinPayUtils {

    /**
     * 统一下单，创建订单
     *
     * @param notify_url       异步回调地址
     * @param body             商品描述
     * @param out_trade_no     订单编号
     * @param total_fee        支付金额
     * @param spbill_create_ip 用户端实际ip
     * @return
     * @throws Exception
     */
    public static Map<String, String> order(String notify_url, String body, String out_trade_no, String total_fee, String spbill_create_ip) throws Exception {
        MyConfig config = MyConfig.getConfig().loadPropertiesFromSrc();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", body);
        data.put("out_trade_no", out_trade_no);
        data.put("fee_type", "CNY");
        data.put("total_fee", total_fee);
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("time_start", WeiXinPayUtils.getDateTime(WeiXinPayUtils.getCurrentTimeInLong() / 1000));
        data.put("notify_url", notify_url);
        data.put("trade_type", "APP");  // 此处指定为APP支付
        data.put("product_id", "12");
        Map<String, String> resp = wxpay.unifiedOrder(data);

        //二次签名
        SortedMap<String, String> secondParamenters = new TreeMap();
        secondParamenters.put("appid", config.getAppID());
        secondParamenters.put("partnerid", config.getMchID());
        secondParamenters.put("prepayid", String.valueOf(resp.get("prepay_id")));
        secondParamenters.put("noncestr", String.valueOf(resp.get("nonce_str")));
        secondParamenters.put("timestamp", String.valueOf(WeiXinPayUtils.getCurrentTimeInLong() / 1000));
        secondParamenters.put("package", "Sign=WXPay");
        String secodeSign = WXPayUtil.generateSignature(secondParamenters, config.getKey());
        secondParamenters.put("sign", secodeSign);
        return secondParamenters;
    }

    /**
     * 解析微信支付 回调的数据
     *
     * @param request
     * @return
     */
    public static boolean notify(HttpServletRequest request) throws IOException, JAXBException, JDOMException {
        //读取参数
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();
        //解析xml成map
        Map<String, String> m = new HashMap<String, String>();
        m = WeiXinPayUtils.doXMLParse(sb.toString());
        String return_code = m.get("return_code");
        if ("FAIL".equalsIgnoreCase(return_code)) {
            return false;
        }
        return true;
    }

    /**
     * 订单查询
     *
     * @param out_trade_no 订单编号
     * @return
     * @throws Exception
     */
    public static Map<String, String> orderQuery(String out_trade_no) throws Exception {
        MyConfig config = MyConfig.getConfig().loadPropertiesFromSrc();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", out_trade_no);
        try {
            Map<String, String> resp = wxpay.orderQuery(data);
            System.out.println(resp);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭订单
     *
     * @param out_trade_no
     * @return
     * @throws Exception
     */
    public static boolean closeOrder(String out_trade_no) throws Exception {
        MyConfig config = MyConfig.getConfig().loadPropertiesFromSrc();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", out_trade_no);
        try {
            Map<String, String> resp = wxpay.closeOrder(data);
            if ("SUCCESS".equals(resp.get("return_code"))) {
                return true;
            } else if ("FAIL".equals(resp.get("return_code"))) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 申请退款
     *
     * @param out_trade_no
     * @return
     * @throws Exception
     */
    public static Map<String, String> refund(String out_trade_no) throws Exception {
        MyConfig config = MyConfig.getConfig().loadPropertiesFromSrc();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", out_trade_no);
        try {
            Map<String, String> resp = wxpay.refund(data);
            System.out.println(resp);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询退款
     *
     * @param out_trade_no
     * @return
     */
    public static Map<String, String> refundQuery(String out_trade_no) {
        MyConfig config = MyConfig.getConfig().loadPropertiesFromSrc();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", out_trade_no);
        try {
            Map<String, String> resp = wxpay.refundQuery(data);
            System.out.println(resp);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 订单下载
     *
     * @return
     * @throws Exception
     */
    public static Map<String, String> downloadBill() throws Exception {
        MyConfig config = MyConfig.getConfig().loadPropertiesFromSrc();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("bill_date", "20140603");
        data.put("bill_type", "ALL");

        try {
            Map<String, String> resp = wxpay.downloadBill(data);
            System.out.println(resp);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            System.out.print(WeXinPayUtils.order("http://baidu.com", "测试支付", "21085254222", "1", "138.165.123.13"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
