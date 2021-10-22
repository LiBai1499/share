

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.SortedMap;
import java.util.TreeMap;

public class SignUtilV5 {

  public static boolean verify(String key, String requestSign, String body) {
    String sign = SecureUtil.md5(QueryStringSignUtil.generateQueryString(body).toString() + key);
    System.out.println(sign);
    System.out.println("requestSign:" + requestSign);
    return sign.equals(requestSign);
  }

  public static void main(String[] args) {
//    String sign = SecureUtil.md5(QueryStringSignUtil.generateQueryString(packageSignParams()).toString() + "36DC05AB41AAC67FC7D4397B5B27AA4C");
//    System.out.println(sign);
//    String requestSign = "736c8b13ff81d464cd55770e12942751";
//    System.out.println("requestSign:" + requestSign);
//    System.out.println(sign.equals(requestSign));

//    2ed8c3618999f4d2daa478e058940421
//    requestSign:736c8b13ff81d464cd55770e12942751
//    http://vvt.leeidc.cn/submit.php?out_trade_no=PO636208195508899840&money=1&name=UP&
//    sign=2ed8c3618999f4d2daa478e058940421
//    &return_url=http%3A%2F%2Fanp.mejia.wang%3FisPay%3D1%26payId%3D2834&pid=717993&type=wxpay&notify_url=http%3A%2F%2Fv.xiaoyu2.xyz%2Fim%2Freward%2Forder%2Fnotify%2Forder&sign_type=MD5

//    {"money":"1","name":"UP","out_trade_no":"PO636208195508899840","pid":717993,"sign":"736c8b13ff81d464cd55770e12942751","sign_type":"MD5","trade_no":"2021102222241418152","trade_status":"TRADE_SUCCESS","type":"wxpay"}

    JSONObject row = new JSONObject();
    row.put("money", "1");
    row.put("name", "UP");
    row.put("out_trade_no", "PO636208195508899840");
    row.put("pid", 717993);
//    row.put("sign", "736c8b13ff81d464cd55770e12942751");
//    row.put("sign_type", "MD5");
    row.put("trade_no", "2021102222241418152");
    row.put("trade_status", "TRADE_SUCCESS");
    row.put("type", "wxpay");

    // 验证数据签名
    if (!SignUtil.verify("36DC05AB41AAC67FC7D4397B5B27AA4C", "736c8b13ff81d464cd55770e12942751", JSONObject.toJSONString(row))) {
      System.out.println("验证数据签名 sign check：");
    } else {
      System.out.println("success：");
    }
  }

  public static String packageSignParams() {
    JSONObject row = new JSONObject();
    row.put("pid", 717993);
    row.put("type", "wxpay");
    row.put("out_trade_no", "PO636208195508899840");
    row.put("name", "UP");
    row.put("money", 1);
    row.put("notify_url", "http://v.xiaoyu2.xyz/im/reward/order/notify/order");
    row.put("return_url", "http://anp.mejia.wang?isPay=1&payId=2834");
    return row.toJSONString();
  }


}
