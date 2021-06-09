package cn.laochou.pojo;

/**
 * 任务描述
 */
public class TaskDesc {

    // 任务名称
    private String taskName;

    // 任务处理的文件名称
    private String fileName;

    // 输出文件个数
    private int outFileNumbers;

    // 文件地址
    private String filePath;

    public String getTaskName() {
        return taskName;
    }

    public TaskDesc setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public TaskDesc setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public int getOutFileNumbers() {
        return outFileNumbers;
    }

    public TaskDesc setOutFileNumbers(int outFileNumbers) {
        this.outFileNumbers = outFileNumbers;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public TaskDesc setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
}
