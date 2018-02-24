package cc.uman.wexinpay.sdk;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.*;
import cc.uman.wexinpay.sdk.WXPayConstants.*;

/**
 * Created by Uman on 2018/2/24.
 * Email:icaishaohua@gmail.com
 */
public class WXPayUtil {
    public WXPayUtil() {
    }

    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        HashMap data = new HashMap();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        ByteArrayInputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
        Document doc = documentBuilder.parse(stream);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();

        for(int idx = 0; idx < nodeList.getLength(); ++idx) {
            Node node = nodeList.item(idx);
            if(node.getNodeType() == 1) {
                Element element = (Element)node;
                data.put(element.getNodeName(), element.getTextContent());
            }
        }

        try {
            stream.close();
        } catch (Exception var10) {
            ;
        }

        return data;
    }

    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);
        Iterator tf = data.keySet().iterator();

        while(tf.hasNext()) {
            String transformer = (String)tf.next();
            String source = (String)data.get(transformer);
            if(source == null) {
                source = "";
            }

            source = source.trim();
            Element writer = document.createElement(transformer);
            writer.appendChild(document.createTextNode(source));
            root.appendChild(writer);
        }

        TransformerFactory tf1 = TransformerFactory.newInstance();
        Transformer transformer1 = tf1.newTransformer();
        DOMSource source1 = new DOMSource(document);
        transformer1.setOutputProperty("encoding", "UTF-8");
        transformer1.setOutputProperty("indent", "yes");
        StringWriter writer1 = new StringWriter();
        StreamResult result = new StreamResult(writer1);
        transformer1.transform(source1, result);
        String output = writer1.getBuffer().toString();

        try {
            writer1.close();
        } catch (Exception var12) {
            ;
        }

        return output;
    }

    public static String generateSignedXml(Map<String, String> data, String key) throws Exception {
        return generateSignedXml(data, key, SignType.MD5);
    }

    public static String generateSignedXml(Map<String, String> data, String key, SignType signType) throws Exception {
        String sign = generateSignature(data, key, signType);
        data.put("sign", sign);
        return mapToXml(data);
    }

    public static boolean isSignatureValid(String xmlStr, String key) throws Exception {
        Map data = xmlToMap(xmlStr);
        if(!data.containsKey("sign")) {
            return false;
        } else {
            String sign = (String)data.get("sign");
            return generateSignature(data, key).equals(sign);
        }
    }

    public static boolean isSignatureValid(Map<String, String> data, String key) throws Exception {
        return isSignatureValid(data, key, SignType.MD5);
    }

    public static boolean isSignatureValid(Map<String, String> data, String key, SignType signType) throws Exception {
        if(!data.containsKey("sign")) {
            return false;
        } else {
            String sign = (String)data.get("sign");
            return generateSignature(data, key, signType).equals(sign);
        }
    }

    public static String generateSignature(Map<String, String> data, String key) throws Exception {
        return generateSignature(data, key, SignType.MD5);
    }

    public static String generateSignature(Map<String, String> data, String key, SignType signType) throws Exception {
        Set keySet = data.keySet();
        String[] keyArray = (String[])keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        String[] var6 = keyArray;
        int var7 = keyArray.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String k = var6[var8];
            if(!k.equals("sign") && ((String)data.get(k)).trim().length() > 0) {
                sb.append(k).append("=").append(((String)data.get(k)).trim()).append("&");
            }
        }

        sb.append("key=").append(key);
        if(SignType.MD5.equals(signType)) {
            return MD5(sb.toString()).toUpperCase();
        } else if(SignType.HMACSHA256.equals(signType)) {
            return HMACSHA256(sb.toString(), key);
        } else {
            throw new Exception(String.format("Invalid sign_type: %s", new Object[]{signType}));
        }
    }

    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        byte[] var4 = array;
        int var5 = array.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            byte item = var4[var6];
            sb.append(Integer.toHexString(item & 255 | 256).substring(1, 3));
        }

        return sb.toString().toUpperCase();
    }

    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        byte[] var6 = array;
        int var7 = array.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            byte item = var6[var8];
            sb.append(Integer.toHexString(item & 255 | 256).substring(1, 3));
        }

        return sb.toString().toUpperCase();
    }
}
