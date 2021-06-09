package cn.laochou.handle;

import cn.laochou.config.Config;
import cn.laochou.mutual.SocketMutual;
import cn.laochou.pojo.*;
import cn.laochou.reduce.ReducerExecutor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.net.Socket;

/**
 * 消息处理
 */
public class MessageHandler {


    private static volatile boolean continueAccess = true;

    private static volatile int waitTime = 5000;
    
    
    public static void handleMessage(Socket socket) {
        // 在这里需要另外开启一个线程，监听我们的 map task 是否完成
        Thread listenMap = new Thread(() -> {
            listenMapWorkIsFinish(socket);
        });
        listenMap.start();
        while (!socket.isClosed()) {
            String message = SocketMutual.receive(socket);
            if(message == null) break;
            JSONObject json = JSON.parseObject(message);
            String type = json.getString("type");
            handleMessageByType(type, socket, json);
        }
    }

    private static void listenMapWorkIsFinish(Socket socket) {
        while (continueAccess) {
            System.out.println("send reduce access");
            BaseMessage message = new BaseMessage()
                    .setFrom(Config.CLIENT)
                    .setTo(Config.SERVER)
                    .setType(MessageTypeConst.REDUCE_ACCESS)
                    .setInfo("reduce access");
            String request = JSON.toJSONString(message);
            SocketMutual.send(socket, request);
            try {
                // 每 5 秒访问一次 master 询问 map task 是否执行完毕。
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleMessageByType(String type, Socket socket, JSONObject json) {
    
        switch (type) {
            case MessageTypeConst.HELLO:
                handleHello(socket, json);
                break;
                
            case MessageTypeConst.REDUCE:
                handleReduce(socket, json);
                break;

            case MessageTypeConst.REDUCE_ACCESS_RESPONSE:
                handleReduceAccessResponse(socket, json);
            
            case MessageTypeConst.END:
                handleEnd(socket, json);
                break;
        }
    
    }

    private static void handleReduceAccessResponse(Socket socket, JSONObject json) {
        System.out.printf("Yes sir, I'm client, I accepted the reduce access response that is %s\n", json.toJSONString());
        ReduceAccessResponseMessage message = JSON.parseObject(json.toJSONString(), ReduceAccessResponseMessage.class);
        // 可能根据我们的服务器的调度而进行修改
        waitTime = message.getTimes();
        if(message.getCanDo()) {
            continueAccess = false;
            BaseMessage m = new BaseMessage().setInfo("I want to get a reduce task, please server can allocation")
                .setFrom(Config.CLIENT).setTo(Config.SERVER).setType(MessageTypeConst.REDUCE);
            String request = JSON.toJSONString(m);
            SocketMutual.send(socket, request);
        }
    }

    private static void handleEnd(Socket socket, JSONObject json) {
    }



    private static void handleReduce(Socket socket, JSONObject json) {
        System.out.printf("Yes sir, I'm Client, I accepted the reduce task that is %s, I will execute it\n", json.toJSONString());
        ReduceMessage message = JSON.parseObject(json.toJSONString(), ReduceMessage.class);
        ReducerExecutor.executeReduce(socket, message);
    }

    private static void handleHello(Socket socket, JSONObject json) {
        System.out.printf("Yes sir, I'm accepted the hello message that is %s\n", json.toJSONString());

    }


}
