package cn.laochou.pojo;

public class ReduceResponseMessage extends BaseMessage{

    private String outFile;

    public String getOutFile() {
        return outFile;
    }

    public ReduceResponseMessage setOutFile(String outFile) {
        this.outFile = outFile;
        return this;
    }
}
