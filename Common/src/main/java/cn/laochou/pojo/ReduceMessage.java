package cn.laochou.pojo;


public class ReduceMessage extends BaseMessage{


    private TaskDesc taskDesc;

    private String[] reduceFiles;

    public String[] getReduceFiles() {
        return reduceFiles;
    }

    public ReduceMessage setReduceFiles(String[] reduceFiles) {
        this.reduceFiles = reduceFiles;
        return this;
    }

    public TaskDesc getTaskDesc() {
        return taskDesc;
    }

    public ReduceMessage setTaskDesc(TaskDesc taskDesc) {
        this.taskDesc = taskDesc;
        return this;
    }
}
