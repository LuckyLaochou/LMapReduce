package cn.laochou.work;

import cn.laochou.config.Config;
import cn.laochou.io.FileUtil;
import cn.laochou.mutual.SocketMutual;
import cn.laochou.pojo.*;
import com.alibaba.fastjson.JSON;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class WorkExecutor {

    /**
     * 执行分配的 work
     * @param desc 任务描述
     * @param socket 当前建立连接的对象，需要返回中间文件名
     */
    public static void executeWork(TaskDesc desc, Socket socket) {
        Thread worker = new Thread(() -> {
            Map<String, Integer> count = new HashMap<>();
            // 将文件名的内容进行读取
            System.out.println(desc.getFileName());
            FileUtil.read(desc.getFilePath() + desc.getFileName(), count);
            // 将数据写入中间文件
            String[] middleFileNames = FileUtil.generateFileName(desc, Config.FILETYPE_MIDDLE);
            // 为了方便Reduce进行处理，在这里需要进行洗牌。
            FileUtil.write(middleFileNames, count, Config.MIDDLE_CONTENT_FORMAT);
            // 需要将文件名进行反馈
            BaseMessage responseMessage = new MapResponseMessage().setTaskDesc(desc)
                    .setMiddleFileName(middleFileNames).setFrom(Config.CLIENT)
                    .setTo(Config.SERVER).setType(MessageTypeConst.MAP_RESPONSE);
            String response = JSON.toJSONString(responseMessage);
            SocketMutual.send(socket, response);
            System.out.println("handle finished");
            // 将当前任务发送给服务器，并告诉服务器 map worker is finished
            responseMessage = new BaseMessage()
                    .setFrom(Config.CLIENT)
                    .setTo(Config.SERVER)
                    .setType(MessageTypeConst.END)
                    .setInfo("my work is over, please end");
            response = JSON.toJSONString(responseMessage);
            SocketMutual.send(socket, response);
        }, desc.getTaskName());
        worker.start();
    }

}
