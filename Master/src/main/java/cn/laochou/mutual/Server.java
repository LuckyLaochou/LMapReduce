package cn.laochou.mutual;

import cn.laochou.handle.MessageHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    private Server() {
        initServer();
    }

    private static final AtomicInteger no = new AtomicInteger(1);

    public void initServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8090);
            System.out.println("the server is starting and start listening");
            // 因为可能建立多个连接
            while (true) {
                // 开启监听
                // 获取建立的连接
                Socket socket = serverSocket.accept();
//                printlnHello(socket);
                Thread thread = new Thread(() -> {
                    // 对于每个socket单独用个线程进行处理
                    MessageHandler.handleMessage(socket);
                });
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 服务开启模式
    public static void start() {
        new Server();
    }


}
