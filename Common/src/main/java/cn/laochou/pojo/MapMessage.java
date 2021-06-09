package cn.laochou.pojo;

import java.util.Map;

public class MapMessage extends BaseMessage{

    private Map<String, Integer> count;

    public Map<String, Integer> getCount() {
        return count;
    }

    public MapMessage setCount(Map<String, Integer> count) {
        this.count = count;
        return this;
    }
}
