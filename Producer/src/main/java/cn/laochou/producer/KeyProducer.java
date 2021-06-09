package cn.laochou.producer;

import cn.laochou.config.Config;
import cn.laochou.config.ProducerConfig;
import cn.laochou.util.StringUtil;

import java.io.*;

/**
 * 键值对生成器
 */
public class KeyProducer {

    /**
     * 生成键值对
     */
    public void produceKey(String[] files) {
        Thread producer = new Thread(() -> {
            startProducer(files);
            System.out.println("the keys are produce successed");
        });
        producer.start();
    }

    private void startProducer(String[] files) {
        try {
            for(String file : files) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for(long i = 0; i < ProducerConfig.LINE_NUMBER; i++) {
                    String key = StringUtil.generateString();
                    writer.write(key + Config.LINE_END_CHAR);
                }
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private KeyProducer() {}

    public static void start(String[] files) {
        new KeyProducer().produceKey(files);
    }

}
