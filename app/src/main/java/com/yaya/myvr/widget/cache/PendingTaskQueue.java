package com.yaya.myvr.widget.cache;

import com.yaya.myvr.dao.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/5/22.
 * <p>
 * 待下载任务队列
 */

public class PendingTaskQueue {
    private List<Task> taskList = new ArrayList<>();
    private Map<String, Task> map = new HashMap<>();

    /**
     * 入队
     *
     * @param task
     */
    public void enQueue(Task task) {
        taskList.add(task);
        map.put(task.videoId, task);
    }

    /**
     * 手动出队
     *
     * @param videoId
     * @return
     */
    public Task handDequeue(String videoId) {
        Task task = map.get(videoId);
        if(task != null){
            taskList.remove(task);
            map.remove(videoId);
        }
        return task;
    }

    /**
     * 自动出队
     *
     * @return
     */
    public Task autoDequeue() {
        if(taskList.isEmpty()){
            return null;
        }

        Task task = taskList.remove(0);
        return task;
    }

    /**
     * 第一个任务
     * @return
     */
    public Task firstTask(){
        if(taskList.isEmpty()){
            return null;
        }
        return taskList.get(0);
    }

}
