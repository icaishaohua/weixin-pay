package cc.uman.wexinpay.bean;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * <xml>
 * <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
 * <attach><![CDATA[支付测试]]></attach>
 * <bank_type><![CDATA[CFT]]></bank_type>
 * <fee_type><![CDATA[CNY]]></fee_type>
 * <is_subscribe><![CDATA[Y]]></is_subscribe>
 * <mch_id><![CDATA[10000100]]></mch_id>
 * <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>
 * <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>
 * <out_trade_no><![CDATA[1409811653]]></out_trade_no>
 * <result_code><![CDATA[SUCCESS]]></result_code>
 * <return_code><![CDATA[SUCCESS]]></return_code>
 * <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>
 * <sub_mch_id><![CDATA[10000100]]></sub_mch_id>
 * <time_end><![CDATA[20140903131540]]></time_end>
 * <total_fee>1</total_fee><coupon_fee><![CDATA[10]]></coupon_fee>
 * <coupon_count><![CDATA[1]]></coupon_count>
 * <coupon_type><![CDATA[CASH]]></coupon_type>
 * <coupon_id><![CDATA[10000]]></coupon_id>
 * <coupon_fee><![CDATA[100]]></coupon_fee>
 * <trade_type><![CDATA[JSAPI]]></trade_type>
 * <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>
 * </xml>
 * <p>
 * 异步回调解析对象
 * Created by Uman on 2018/2/24.
 * Email:icaishaohua@gmail.com
 */
public class NotifyBean implements Serializable {
    private String appid;
    private String attach;
    private String bank_type;
    private String fee_type;
    private String is_subscribe;
    private String mch_id;
    private String nonce_str;
    private String openid;
    private String out_trade_no;
    private String result_code;
    private String return_code;
    private String sign;
    private String sub_mch_id;
    private String time_end;
    private String total_fee;
    private String coupon_count;
    private String coupon_id;
    private String coupon_fee;
    private String trade_type;
    private String transaction_id;

    public NotifyBean() {
    }

    public NotifyBean(String appid, String attach,
                      String bank_type, String fee_type,
                      String is_subscribe, String mch_id,
                      String nonce_str, String openid,
                      String out_trade_no, String result_code,
                      String return_code, String sign,
                      String sub_mch_id, String time_end,
                      String total_fee, String coupon_count,
                      String coupon_id, String coupon_fee,
                      String trade_type, String transaction_id) {
        this.appid = appid;
        this.attach = attach;
        this.bank_type = bank_type;
        this.fee_type = fee_type;
        this.is_subscribe = is_subscribe;
        this.mch_id = mch_id;
        this.nonce_str = nonce_str;
        this.openid = openid;
        this.out_trade_no = out_trade_no;
        this.result_code = result_code;
        this.return_code = return_code;
        this.sign = sign;
        this.sub_mch_id = sub_mch_id;
        this.time_end = time_end;
        this.total_fee = total_fee;
        this.coupon_count = coupon_count;
        this.coupon_id = coupon_id;
        this.coupon_fee = coupon_fee;
        this.trade_type = trade_type;
        this.transaction_id = transaction_id;
    }

    @XmlAttribute(name = "appid")
    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    @XmlAttribute(name = "attach")
    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    @XmlAttribute(name = "bank_type")
    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    @XmlAttribute(name = "fee_type")
    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    @XmlAttribute(name = "is_subscribe")
    public String getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(String is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    @XmlAttribute(name = "mch_id")
    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    @XmlAttribute(name = "nonce_str")
    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    @XmlAttribute(name = "openid")
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @XmlAttribute(name = "out_trade_no")
    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    @XmlAttribute(name = "result_code")
    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    @XmlAttribute(name = "return_code")
    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    @XmlAttribute(name = "sign")
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @XmlAttribute(name = "sub_mch_id")
    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    @XmlAttribute(name = "time_end")
    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    @XmlAttribute(name = "total_fee")
    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    @XmlAttribute(name = "coupon_count")
    public String getCoupon_count() {
        return coupon_count;
    }

    public void setCoupon_count(String coupon_count) {
        this.coupon_count = coupon_count;
    }

    @XmlAttribute(name = "coupon_id")
    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    @XmlAttribute(name = "coupon_fee")
    public String getCoupon_fee() {
        return coupon_fee;
    }

    public void setCoupon_fee(String coupon_fee) {
        this.coupon_fee = coupon_fee;
    }

    @XmlAttribute(name = "trade_type")
    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    @XmlAttribute(name = "transaction_id")
    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    @Override
    public String toString() {
        return "NotifyBean{" +
                "appid='" + appid + '\'' +
                ", attach='" + attach + '\'' +
                ", bank_type='" + bank_type + '\'' +
                ", fee_type='" + fee_type + '\'' +
                ", is_subscribe='" + is_subscribe + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", openid='" + openid + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", result_code='" + result_code + '\'' +
                ", return_code='" + return_code + '\'' +
                ", sign='" + sign + '\'' +
                ", sub_mch_id='" + sub_mch_id + '\'' +
                ", time_end='" + time_end + '\'' +
                ", total_fee='" + total_fee + '\'' +
                ", coupon_count='" + coupon_count + '\'' +
                ", coupon_id='" + coupon_id + '\'' +
                ", coupon_fee='" + coupon_fee + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", transaction_id='" + transaction_id + '\'' +
                '}';
    }
}
