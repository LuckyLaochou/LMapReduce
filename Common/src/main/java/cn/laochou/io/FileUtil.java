package cn.laochou.io;

import cn.laochou.config.Config;
import cn.laochou.pojo.TaskDesc;
import cn.laochou.shuffle.ShuffleUtil;

import java.io.*;
import java.util.Map;

/**
 * 文件工具
 */
public class FileUtil {



    public static boolean write(String[] fileNames, Map<String, Integer> count, String format) {
        int numbers = fileNames.length;
        BufferedWriter[] writers = new BufferedWriter[numbers];
        try {
            for(int i = 0; i < numbers; i++) {
                writers[i] = new BufferedWriter(new FileWriter(fileNames[i]));
            }
            for(Map.Entry<String, Integer> entry : count.entrySet()) {
                // 进行洗牌，并写入指定文件
                int index = ShuffleUtil.getIndex(entry.getKey(), numbers);
                writers[index].write(String.format(format, entry.getKey(), entry.getValue()));
                writers[index].flush();
            }
        } catch (IOException e) {
            System.out.println("get the file writer happen exception");
            e.printStackTrace();
            return false;
        }finally {
            for(BufferedWriter writer : writers) {
                if(writer != null) {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }


    public static void read(String file, Map<String, Integer> count) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                String[] datas = line.split(Config.CONTENT_SEPARATOR);
                count.put(datas[0], count.getOrDefault(datas[0], 0) + 1);
            }
        } catch (FileNotFoundException e) {
            System.out.printf("the fileName of %s not found", file);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成文件名，并创建文件
     * @param desc 任务描述
     * @param fileType 文件类型
     * @return
     */
    public static String[] generateFileName(TaskDesc desc, String fileType) {
        String[] fileNames = new String[desc.getOutFileNumbers()];
        String prefix = String.format("%s_%s", desc.getTaskName(), fileType);
        String item = "";
        for(int i = 0; i < desc.getOutFileNumbers(); i++) {
            item = String.format("%s_%s_%s", prefix, i, Config.FILENAME_SUFFIX);
            fileNames[i] = String.format("%s%s", Config.FILE_PATH, item);
            createFile(Config.FILE_PATH + item);
        }
        return fileNames;
    }


    private static void createFile(String file) {
        File f = new File(file);
        if(!f.exists()) {
            try {
                boolean result = f.createNewFile();
                if(result) {
                    System.out.printf("crate the file %s success\n", file);
                    return ;
                }
                System.out.printf("crate the file %s failed\n", file);
            } catch (IOException e) {
                System.out.printf("create the file %s happen exception\n", file);
                e.printStackTrace();
            }
        }
    }

}
