package cn.laochou.mutual;

import cn.laochou.config.Config;
import cn.laochou.io.SocketIOUtil;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Socket 通信
 */
public class SocketMutual {

    /**
     * 发送消息
     * @param socket 和服务器建立连接的对象
     * @param message 需要发送的消息
     */
    public static void send(Socket socket, String message) {
        OutputStream out = SocketIOUtil.getOutputStream(socket);
        message += Config.MESSAGE_END_CHAR;
        try {
            out.write(message.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收消息
     * @param socket 和服务器建立连接的对象
     * @return 接收到的消息
     */
    public static String receive(Socket socket) {
        InputStream in = SocketIOUtil.getInputStream(socket);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
