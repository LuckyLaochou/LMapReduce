package cn.laochou.handle;

import cn.laochou.config.Config;
import cn.laochou.io.SocketIOUtil;
import cn.laochou.mutual.SocketMutual;
import cn.laochou.pojo.BaseMessage;
import cn.laochou.pojo.MessageTypeConst;
import cn.laochou.pojo.TaskDesc;
import cn.laochou.pojo.TaskMessage;
import cn.laochou.work.WorkExecutor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.Socket;

/**
 * Worker 信息处理器
 */
public class MessageHandler {

    public static void handleMessage(Socket socket) {
        while (!socket.isClosed()) {
            String message = SocketMutual.receive(socket);
            if(message == null) break;
            JSONObject json = JSON.parseObject(message);
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
            
            default:
                break;
        }
    }

    private static void handleHello(Socket socket, JSONObject json) {
        System.out.printf("Yes sir, I'm accepted the hello message that is %s\n", json.toJSONString());
        // 发送 map信息，希望服务端分配 map task
        BaseMessage message = new BaseMessage().setInfo("I want to get a map task, please server can allocation")
                .setFrom(Config.CLIENT).setTo(Config.SERVER).setType(MessageTypeConst.MAP);
        String request = JSON.toJSONString(message);
        SocketMutual.send(socket, request);
    }
    
    private static void handleMap(Socket socket, JSONObject json) {
        System.out.printf("Yes sir, I'm Client, I accepted the map task that is %s, I will execute it\n", json.toJSONString());
        TaskMessage message = JSON.parseObject(json.toJSONString(), TaskMessage.class);
        WorkExecutor.executeWork(message.getTaskDesc(), socket);
    }

}
