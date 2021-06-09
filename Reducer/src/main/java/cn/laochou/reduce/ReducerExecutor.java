package cn.laochou.reduce;

import cn.laochou.config.Config;
import cn.laochou.io.FileUtil;
import cn.laochou.mutual.SocketMutual;
import cn.laochou.pojo.BaseMessage;
import cn.laochou.pojo.MessageTypeConst;
import cn.laochou.pojo.ReduceMessage;
import cn.laochou.pojo.ReduceResponseMessage;
import com.alibaba.fastjson.JSON;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Reducer 执行器
 */
public class ReducerExecutor {


    public static void executeReduce(Socket socket, ReduceMessage message) {
        Thread executeThread = new Thread(() -> {
            String[] reduceFiles = message.getReduceFiles();
            Map<String, Integer> count = new ConcurrentHashMap<>();
            CountDownLatch countDownLatch = new CountDownLatch(3);
            // 首先构造我们的 输出文件
            String file = FileUtil.generateFileName(message.getTaskDesc(), Config.FILETYPE_OUT)[0];
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for(int i = 0; i < reduceFiles.length; i++) {
                    final int fileIndex = i;
                    // 开启多个线程来读取
                    Thread readThread = new Thread(() -> {
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(reduceFiles[fileIndex]));
                            String line = "";
                            while ((line = reader.readLine()) != null) {
                                String[] entry = line.split(Config.CONTENT_SEPARATOR);
                                count.put(entry[0], count.getOrDefault(entry[0], 0) + 1);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        countDownLatch.countDown();
                    });
                    readThread.start();
                }
                // 等待三个线程都结束才能执行下面的。
                countDownLatch.await();
                for(Map.Entry<String, Integer> entry : count.entrySet()) {
                    String line = String.format(Config.MIDDLE_CONTENT_FORMAT, entry.getKey(), entry.getValue());
                    writer.write(line);
                    writer.flush();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("reduce finished");
            // 完成之后，需要告诉我们服务端，输出文件
            BaseMessage m = new ReduceResponseMessage()
                    .setOutFile(file)
                    .setFrom(Config.CLIENT)
                    .setTo(Config.SERVER)
                    .setType(MessageTypeConst.REDUCE_RESPONSE);
            String send = JSON.toJSONString(m);
            SocketMutual.send(socket, send);
            // 发送end请求
            m = new BaseMessage()
                    .setFrom(Config.CLIENT)
                    .setTo(Config.SERVER)
                    .setType(MessageTypeConst.END)
                    .setInfo("my reduce work is finished, please end");
            send = JSON.toJSONString(m);
            SocketMutual.send(socket, send);
        });
        executeThread.start();
    }

}
