package cn.laochou.record;

import cn.laochou.config.ServerConfig;
import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务记录器
 */
public class TaskRecord {

    // 记录 map 的个数
    private static int mapNumbers = ServerConfig.mapNumber;

    // 记录 任务的执行结果
    private static final Map<String, Boolean> recordMapResult = new ConcurrentHashMap<>();

    // 记录 Map 任务产出的中间文件
    private static final Map<String, String[]> recordMapMiddleFile = new ConcurrentHashMap<>();


    // 记录任务
    public static void recordMapTask(String taskName) {
        recordMapResult.put(taskName, false);
    }

    // 更改任务
    public static void updateMapRecord(String taskName, boolean value) {
        if(recordMapResult.containsKey(taskName)) {
            recordMapResult.put(taskName, value);
            return;
        }
        recordMapResult.put(taskName, false);
    }

    public static void recordMapMiddleFile(String taskName, String[] middleFiles) {
        recordMapMiddleFile.put(taskName, middleFiles);
        System.out.println(JSON.toJSONString(recordMapMiddleFile));
    }


    /**
     * 判断 reduce 是否可以开始执行
     * 注意，在这里需要我们的 map task 完全完成
     * @return
     */
    public static boolean canDoReduce() {
        if(recordMapResult.size() != mapNumbers) {
            return false;
        }
        boolean can = true;
        for(boolean item : recordMapResult.values()) {
            if (!item) {
                can = false;
                break;
            }
        }
        return can;
    }


    public static String[] getTheReduceFiles(int index) {
        // 如果下标超过了 reduceNumber，其实是不存在中间文件的
        if(index >= ServerConfig.reduceNumber) {
            return new String[]{};
        }
        // 看有多少个 map numbers，因为每个 reduce 都需要处理 一个 map 所对应的中间文件
        String[] reduceFiles = new String[mapNumbers];
        for(int i = 1; i <= mapNumbers; i++) {
            String key = String.format("MAP_TASK_%s", i);
            reduceFiles[i-1] = recordMapMiddleFile.get(key)[index];
        }
        return reduceFiles;
    }
}
