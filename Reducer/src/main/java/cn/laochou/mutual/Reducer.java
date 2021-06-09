package cn.laochou.mutual;

import cn.laochou.config.Config;
import cn.laochou.handle.MessageHandler;
import cn.laochou.pojo.BaseMessage;
import cn.laochou.pojo.MessageTypeConst;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.net.Socket;

/**
 * Reducer 用来做聚合统计
 */
public class Reducer {

    private Reducer() {}

    private void initReducer() {
        try {
            Socket socket = new Socket("127.0.0.1", 8090);
            // 建立连接之后，需要进行请求服务器，并判断我们的Map是否已经完成，并且是否分给到我们的Reducer。
            BaseMessage baseMessage = new BaseMessage()
                    .setFrom(Config.CLIENT)
                    .setTo(Config.SERVER)
                    .setType(MessageTypeConst.HELLO)
                    .setInfo("Hello Server, I'm Reducer");
            String request = JSON.toJSONString(baseMessage);
            SocketMutual.send(socket, request);
            Thread reducer = new Thread(() -> {
                MessageHandler.handleMessage(socket);
            });
            reducer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void start() {
        new Reducer().initReducer();
    }

}
