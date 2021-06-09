package cn.laochou.mutual;

import cn.laochou.config.Config;
import cn.laochou.handle.MessageHandler;
import cn.laochou.io.SocketIOUtil;
import cn.laochou.pojo.BaseMessage;
import cn.laochou.pojo.MessageTypeConst;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.net.Socket;

public class Worker {

    private Socket socket = null;

    public void initWork() {
        try {
            socket = new Socket("127.0.0.1", 8090);
            // 建立连接之后，发送问好信息
            BaseMessage message = new BaseMessage().setInfo("Hello Server").setFrom(Config.CLIENT).setTo(Config.SERVER).setType(MessageTypeConst.HELLO);
            String info = JSON.toJSONString(message);
            SocketMutual.send(socket, info);
            MessageHandler.handleMessage(socket);
            System.out.println("Socket connection is closed success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(socket == null) System.out.println("build connection failed");
    }

    private Worker() {
        initWork();
    }

    public static void start() {
        new Worker();
    }


}
