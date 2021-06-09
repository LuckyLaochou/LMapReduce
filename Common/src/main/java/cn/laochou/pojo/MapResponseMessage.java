package cn.laochou.pojo;

public class MapResponseMessage extends BaseMessage{

    private TaskDesc taskDesc;

    private String[] middleFileName;

    public String[] getMiddleFileName() {
        return middleFileName;
    }

    public MapResponseMessage setMiddleFileName(String[] middleFileName) {
        this.middleFileName = middleFileName;
        return this;
    }

    public TaskDesc getTaskDesc() {
        return taskDesc;
    }

    public MapResponseMessage setTaskDesc(TaskDesc taskDesc) {
        this.taskDesc = taskDesc;
        return this;
    }
}
