

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import java.nio.charset.StandardCharsets;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignUtil {

  public static boolean verify(String key, String sign, String body) {
    return sign.equals(generate(key, body));
  }

  public static boolean verify(String key, String sign, String body, String exclude) {
    return sign.equals(generate(key, body, exclude));
  }

  public static String generate(String key, Object body) {
    return generate(key, JSONObject.toJSONString(body));
  }

  public static String generate(String key, String body) {
    return generate(key, JSONObject.parseObject(body));
  }

  public static String generate(String key, String body, String exclude) {
    return generate(key, JSONObject.parseObject(body), exclude);
  }

  public static String generate(String key, JSONObject bodyJson) {
    return generate(key, bodyJson, "sign");
  }

  public static String generate(String key, JSONObject bodyJson, String exclude) {
    SortedMap<String, String> sortedMap = bodyJson.toJavaObject(new TypeReference<TreeMap<String, String>>(){});

    StringBuilder message = new StringBuilder();
    sortedMap.forEach((k, v) -> {
      if (!k.equals(exclude) && StrUtil.isNotEmpty(v)) {
        message.append(k).append("=").append(v).append("&");
      }
    });

    message.append("key=").append(key);
    System.out.println(message.toString());
    System.out.println(SecureUtil.md5(message.toString()));

    return SecureUtil.md5(message.toString());
  }

  /**
   * sha256_HMAC加密
   *
   * @param message 消息
   * @param secret 秘钥
   * @return 加密后字符串
   */
  public static String sha256HMAC(String message, String secret) {
    String hash = "";

    try {
      Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      sha256_HMAC.init(secretKey);
      byte[] bytes = sha256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8));
      hash = byteArrayToHexString(bytes);
    } catch (Exception ignored) {}

    return hash;
  }

  /**
   * 将加密后的字节数组转换成字符串
   *
   * @param b 字节数组
   * @return 字符串
   */
  public static String byteArrayToHexString(byte[] b) {
    StringBuilder hs = new StringBuilder();
    String stmp;

    for (int n = 0; b != null && n < b.length; n++) {
      stmp = Integer.toHexString(b[n] & 0XFF);
      if (stmp.length() == 1) {
        hs.append('0');
      }
      hs.append(stmp);
    }

    return hs.toString().toLowerCase();
  }

}
