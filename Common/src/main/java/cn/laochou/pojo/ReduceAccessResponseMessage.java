package cn.laochou.pojo;

public class ReduceAccessResponseMessage extends BaseMessage{

    private boolean canDo;

    private String[] reduceFiles;

    // 间隔多久在访问一次
    private int times;

    public boolean getCanDo() {
        return canDo;
    }

    public ReduceAccessResponseMessage setCanDo(boolean canDo) {
        this.canDo = canDo;
        return this;
    }

    public String[] getReduceFiles() {
        return reduceFiles;
    }

    public ReduceAccessResponseMessage setReduceFiles(String[] reduceFiles) {
        this.reduceFiles = reduceFiles;
        return this;
    }

    public int getTimes() {
        return times;
    }

    public ReduceAccessResponseMessage setTimes(int times) {
        this.times = times;
        return this;
    }
}
