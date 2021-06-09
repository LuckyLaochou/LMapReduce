package cn.laochou.pojo;

public class BaseMessage{

    // 消息从哪里来
    protected String from;

    // 消息发送到哪里去
    protected String to;

    // 消息类型
    protected String type;


    private String info;

    public String getFrom() {
        return from;
    }

    public BaseMessage setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public BaseMessage setTo(String to) {
        this.to = to;
        return this;
    }

    public String getType() {
        return type;
    }

    public BaseMessage setType(String type) {
        this.type = type;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public BaseMessage setInfo(String info) {
        this.info = info;
        return this;
    }

    @Override
    public String toString() {
        return "BaseMessage{" +
                "info='" + info + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
