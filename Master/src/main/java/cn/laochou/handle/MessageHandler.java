package cn.laochou.handle;

import cn.laochou.config.Config;
import cn.laochou.config.ServerConfig;
import cn.laochou.io.SocketIOUtil;
import cn.laochou.mutual.SocketMutual;
import cn.laochou.pojo.*;
import cn.laochou.record.TaskRecord;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 处理 worker 或者 reduce 发来的信息
 */
public class MessageHandler {

    private static final AtomicInteger NO = new AtomicInteger(1);

    private static final AtomicInteger REDUCE_NO = new AtomicInteger(1);

    /**
     * 处理 worker 或者 reduce 发来的请求
     */
    public static void handleMessage(Socket socket) {
        while(!socket.isClosed()) {
            String message = SocketMutual.receive(socket);
            JSONObject json = JSONObject.parseObject(message);
            String type = json.getString("type");
            handleMessageByType(type, socket, json);
        }
    }

    private static void handleMessageByType(String type, Socket socket, JSONObject json) {
        switch (type) {
            case MessageTypeConst.HELLO:
                handleHello(socket, json);
                break;

            case MessageTypeConst.MAP:
                handleMap(socket, json);
                break;

            case MessageTypeConst.REDUCE:
                handleReduce(socket, json);
                break;
                
            case MessageTypeConst.MAP_RESPONSE:
                handleMapResponse(json);
                break;

            case MessageTypeConst.END:
                handleEnd(socket, json);
                break;

            case MessageTypeConst.REDUCE_ACCESS:
                handleReduceAccess(socket, json);
                break;

            case MessageTypeConst.REDUCE_RESPONSE:
                handleReduceResponse(socket, json);

            default:
                break;
        }
    }

    private static void handleReduce(Socket socket, JSONObject json) {
        // 分配 Reduce 任务
        System.out.printf("hello, I'm accepted the reduce that is %s\n", json.toJSONString());
        if(REDUCE_NO.get() > ServerConfig.reduceNumber) {
            System.out.println("the reduce task is over our setting, please check the mapreduce framework");
            return;
        }
        TaskDesc desc = new TaskDesc()
                .setTaskName(String.format("REDUCE_TASK_%s", REDUCE_NO.get()))
                .setOutFileNumbers(1);
        BaseMessage reduceMessage = new ReduceMessage()
                .setReduceFiles(TaskRecord.getTheReduceFiles(REDUCE_NO.get()))
                .setTaskDesc(desc)
                .setFrom(Config.SERVER)
                .setTo(Config.CLIENT)
                .setType(MessageTypeConst.REDUCE);
        REDUCE_NO.getAndIncrement();
        String response = JSON.toJSONString(reduceMessage);
        System.out.println(response);
        SocketMutual.send(socket, response);
    }

    private static void handleReduceResponse(Socket socket, JSONObject json) {
        System.out.printf("hello, I'm accepted the reduce response that is%s\n", json.toJSONString());
    }

    /**
     * 处理 reduce 的 询问 是否可以执行 reduce的任务。
     * @param socket
     * @param json
     */
    private static void handleReduceAccess(Socket socket, JSONObject json) {
        System.out.printf("hello, I'm accepted the reduce access request that is %s\n", json.toJSONString());
        boolean canDo = TaskRecord.canDoReduce();
        BaseMessage message = null;
        String response = "";
        if(canDo) {
            message = new ReduceAccessResponseMessage()
                    .setCanDo(true)
                    .setFrom(Config.SERVER)
                    .setType(Config.CLIENT)
                    .setType(MessageTypeConst.REDUCE_ACCESS_RESPONSE);
        }else {
            message = new ReduceAccessResponseMessage()
                    .setCanDo(false)
                    .setTimes(5000)
                    .setFrom(Config.SERVER)
                    .setTo(Config.CLIENT)
                    .setType(MessageTypeConst.REDUCE_ACCESS_RESPONSE);
        }
        response = JSON.toJSONString(message);
        SocketMutual.send(socket, response);
    }

    private static void handleEnd(Socket socket, JSONObject json) {
        System.out.printf("hello, I'm accepted the end request that is %s\n", json.toJSONString());
        SocketIOUtil.close(socket);
    }

    private static void handleMapResponse(JSONObject json) {
        System.out.printf("hello, I'm accepted the map task response message that is %s\n", json.toJSONString());
        MapResponseMessage message = JSON.parseObject(json.toJSONString(), MapResponseMessage.class);
        // 记录 map 所对应产生的 task
        TaskRecord.recordMapMiddleFile(message.getTaskDesc().getTaskName(), message.getMiddleFileName());
        // 记录任务，用来判断任务是否执行结束
        TaskRecord.updateMapRecord(message.getTaskDesc().getTaskName(), true);
    }


    private static void handleHello(Socket socket, JSONObject json) {
        System.out.printf("hello, I'm accepted the hello message that is %s\n", json.toJSONString());
        String info = "hello client , I'm Server, please request me about map or reduce task";
        BaseMessage message = new BaseMessage().setInfo(info)
                .setFrom(Config.SERVER).setTo(Config.CLIENT).setType(MessageTypeConst.HELLO);
        String response = JSON.toJSONString(message);
        SocketMutual.send(socket, response);
    }


    private static void handleMap(Socket socket, JSONObject json) {
        // 分配 Work 任务，并将任务信息发送给 Work
        System.out.printf("hello, I'm accepted the map request that is %s I will allocation the map task for you\n", json.toJSONString());
        TaskDesc taskDesc = new TaskDesc().setTaskName(String.format("MAP_TASK_%s", NO.get()))
                .setFileName(String.format("FILE_%s%s", NO.get(), Config.FILENAME_SUFFIX))
                .setOutFileNumbers(ServerConfig.reduceNumber).setFilePath(Config.FILE_PATH);
        // 任务增长
        NO.getAndIncrement();
        TaskRecord.recordMapTask(taskDesc.getTaskName());
        BaseMessage message = new TaskMessage()
                .setTaskDesc(taskDesc).setFrom(Config.SERVER)
                .setTo(Config.CLIENT).setInfo("OK, please execute the allocation task")
                .setType(MessageTypeConst.MAP);
        String response = JSON.toJSONString(message);
        SocketMutual.send(socket, response);
    }

}
