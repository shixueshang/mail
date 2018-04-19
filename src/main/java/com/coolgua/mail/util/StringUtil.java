package com.coolgua.mail.util;

import com.coolgua.common.util.SecurityUtil;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lihongde on 2017/8/23 10:22.
 */
public class StringUtil {

  private final static String encoding = "UTF-8";

  public static String md5(String text, String salt) {
    try {
      return SecurityUtil.getVeryfyCode(salt, text);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 创建指定数量的随机字符串
   *
   * @param numberFlag 是否是数字
   */
  public static String createRandom(boolean numberFlag, int length) {
    String retStr = "";
    String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
    int len = strTable.length();
    boolean bDone = true;
    do {
      retStr = "";
      int count = 0;
      for (int i = 0; i < length; i++) {
        double dblR = Math.random() * len;
        int intR = (int) Math.floor(dblR);
        char c = strTable.charAt(intR);
        if (('0' <= c) && (c <= '9')) {
          count++;
        }
        retStr += strTable.charAt(intR);
      }
      if (count >= 2) {
        bDone = false;
      }
    } while (bDone);

    return retStr;
  }


  public static String getUUID() {
    UUID uuid = UUID.randomUUID();
    String str = uuid.toString();
    // 去掉"-"符号
    String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23)
        + str.substring(24);
    return temp;
  }

  /**
   * 合法E-mail地址：
   *    1. 必须包含一个并且只有一个符号“@”
   *    2. 第一个字符不得是“@”或者“.”
   *    3. 不允许出现“@.”或者.@
   *    4. 结尾不得是字符“@”或者“.”
   *    5. 允许“@”前的字符中出现“＋”
   *    6. 不允许“＋”在最前面，或者“＋@”
   *
   * @param email
   * @return
   */
  public static boolean isValidEmail(String email){
    boolean flag = false;
    try {
      Pattern pattern = Pattern.compile(Constant.REG_MAIL);
      Matcher matcher = pattern.matcher(email);
      flag = matcher.matches();
    } catch (Exception e) {
      flag = false;
    }
    return flag;
  }

  public static boolean isValidFileExt(String fileName){
    boolean flag = false;
    try {
      Pattern pattern = Pattern.compile(Constant.REG_FILEEXT);
      Matcher matcher = pattern.matcher(fileName);
      flag = matcher.matches();
    } catch (Exception e) {
      flag = false;
    }
    return flag;
  }

}
