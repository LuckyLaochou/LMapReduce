package cn.laochou.shuffle;

/**
 * 洗牌工具
 */
public class ShuffleUtil {

    public static int getHash(String key) {
        int h = 0;
        int off = 0;
        char[] chars = key.toCharArray();
        long len = key.length();
        for (long i = 0; i < len; i++) {
            h = 31 * h + chars[off++];
        }
        return h & Integer.MAX_VALUE;
    }

    public static int getIndex(String key, int reduceNumbers) {
        return getHash(key) % reduceNumbers;
    }

}
