package cn.laochou.util;

import java.util.Random;

public class StringUtil {


    // a 的 acsII 值
    private static final int startValue = 97;

    private static final Random random = new Random();


    public static String generateString() {
        // 先随机出一个长度出来，长度尽可能长一点，重复率低
        int length =  1 + random.nextInt(25);
        char[] chars = new char[length];
        for(int i = 0; i < length; i++) {
            chars[i] = (char) (startValue + random.nextInt(26));
        }
        return new String(chars);
    }

}
