package cn.laochou.pojo;

public class TaskMessage extends BaseMessage{

    private TaskDesc taskDesc;

    public TaskDesc getTaskDesc() {
        return taskDesc;
    }

    public TaskMessage setTaskDesc(TaskDesc taskDesc) {
        this.taskDesc = taskDesc;
        return this;
    }
}
