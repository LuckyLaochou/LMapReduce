package cn.laochou.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 获取 Socket IO
 */
public class SocketIOUtil {

    public static OutputStream getOutputStream(Socket socket) {
        // 局部变量是需要赋初值
        OutputStream out = null;
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(out == null) {
            System.out.println("get the outputStream fail but don't happen exception");
            // 退出程序
            System.exit(0);
        }
        return out;
    }

    public static InputStream getInputStream(Socket socket) {
        InputStream in = null;
        try {
            in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(in == null) {
            System.out.println("get the inputStream fail but don't happen exception");
            // 退出程序
            System.exit(0);
        }
        return in;
    }

    public static void close(Socket socket) {
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
