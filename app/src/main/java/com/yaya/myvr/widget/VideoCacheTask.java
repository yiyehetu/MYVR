package com.yaya.myvr.widget;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.bean.AppEvent;
import com.yaya.myvr.bean.CacheProgress;
import com.yaya.myvr.bean.VideoPath;
import com.yaya.myvr.dao.Task;
import com.yaya.myvr.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by admin on 2017/5/21.
 * <p>
 * 视频缓存任务
 */

public class VideoCacheTask {
    // 存在下载任务
    private boolean isExist = false;
    // 任务集合
    private List<Task> taskList = new ArrayList<>();
    // 当前任务索引
    private int index = 0;
    // 当前任务
    private Task currTask;
    // 监听集合
    private Map<String, FileDownloadListener> allFileListeners = new HashMap<>();
    // 索引集合
    private Map<String, Integer> indexMap = new HashMap<>();
    // 进度集合
    private Map<String, Integer> progressMap = new HashMap<>();

    public static final String START = "start";
    public static final String PROGRESS = "progress";
    public static final String PAUSE = "pause";
    public static final String COMPLETED = "completed";


    private VideoCacheTask() {
    }

    private static class InnerClass {
        private static VideoCacheTask INSTANCE = new VideoCacheTask();
    }

    public static VideoCacheTask getInstance() {
        return InnerClass.INSTANCE;
    }

    public Task getNextTask() {
        if (index <= taskList.size() - 1) {
            Task task = taskList.get(index);
            // 添加进索引集合
            index++;
            return task;
        } else {
            return null;
        }
    }


    public void addTask(Task task) {
        // 添加进索引集合
        indexMap.put(task.videoId, taskList.size());
        taskList.add(task);
        if (!isExist) {
            isExist = true;
            startTask();
        }
    }

    private void startTask() {
        currTask = getNextTask();
        LogUtils.e(TAG, "currTask = " + currTask);
        // 任务结束
        if (currTask == null) {
            isExist = false;
            return;
        }

        // 缓存文件夹
        final String cacheDir = new StringBuilder().append(FileDownloadUtils.getDefaultSaveRootPath())
                .append(File.separator)
                .append(ApiConst.VIDEO_CACHE)
                .append(File.separator)
                .append(currTask.videoId)
                .append(File.separator)
                .toString();
        // 缓存路径
        final String path = cacheDir + "origin.m3u8";
        downloadM3u8(currTask.m3u8, path, cacheDir);
    }

    /**
     * 下载m3u8文件
     *
     * @param m3u8
     * @param path
     * @param cacheDir
     */
    private void downloadM3u8(String m3u8, final String path, final String cacheDir) {
        FileDownloadListener downloadListener = new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (this != task.getListener()) {
                    return;
                }
                LogUtils.e(TAG, "m3u8 pending...");

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (this != task.getListener()) {
                    return;
                }
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                if (this != task.getListener()) {
                    return;
                }
                LogUtils.e(TAG, "m3u8 completed...");

                Integer progress = progressMap.get(currTask.videoId);
                if (progress == null) {
                    // 第一次下载
                    progressMap.put(currTask.videoId, 0);
                    currTask.progress = 0;
                    currTask.status = AppConst.DOWNLOADING;
                    currTask.update();
                    EventBus.getDefault().post(new AppEvent(START, new CacheProgress(currTask.videoId, 0, AppConst.DOWNLOADING)));
                } else {
                    // 继续下载
                    currTask.status = AppConst.DOWNLOADING;
                    EventBus.getDefault().post(new AppEvent(PROGRESS, new CacheProgress(currTask.videoId, progress, AppConst.DOWNLOADING)));
                }

                List<VideoPath> pathList = readM3u8Data(path, cacheDir);
                if (pathList != null && pathList.size() > 0) {
                    downloadAllPartFile(pathList);
                } else {
                    startTask();
                }
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (this != task.getListener()) {
                    return;
                }
                LogUtils.e(TAG, "m3u8 paused...");
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                if (this != task.getListener()) {
                    return;
                }
                LogUtils.e(TAG, "m3u8 error..." + e.getMessage());
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                if (this != task.getListener()) {
                    return;
                }
            }
        };

        FileDownloader.getImpl().create(m3u8)
                .setPath(path)
                .setListener(downloadListener)
                .start();
    }

    /**
     * 读取并写入m3u8文件
     *
     * @param path
     * @param cacheDir
     * @return
     */
    public List<VideoPath> readM3u8Data(String path, String cacheDir) {
        List<VideoPath> list = new ArrayList<>();
        int count = 0;

        File newFile = new File(cacheDir + "new.m3u8");
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        FileWriter fileWriter = null;
        try {
            fileReader = new FileReader(new File(path));
            bufferedReader = new BufferedReader(fileReader);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            fileWriter = new FileWriter(newFile);

            String buffer = null;
            while ((buffer = bufferedReader.readLine()) != null) {
                if (buffer.length() > 0 && buffer.startsWith("http://")) {
                    // 添加到集合
                    VideoPath videoPath = new VideoPath();
                    videoPath.setOriginPath(buffer);
                    String newPath;
                    if (buffer.endsWith("ts")) {
                        newPath = cacheDir + count + ".ts";
                    } else {
                        newPath = cacheDir + count + ".ds";
                    }
                    videoPath.setNewPath(newPath);
                    list.add(videoPath);
                    count++;

                    // 写入本地
                    LogUtils.e(TAG, "newPath = " + newPath);
                    fileWriter.write(newPath + "\n");
                } else {
                    fileWriter.write(buffer + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    /**
     * 下载读取后的片段文件
     *
     * @param pathList
     */
    private void downloadAllPartFile(List<VideoPath> pathList) {
        final List<BaseDownloadTask> tasks = new ArrayList<>();
        for (int i = 0; i < pathList.size(); i++) {
            BaseDownloadTask task = FileDownloader.getImpl()
                    .create(pathList.get(i).getOriginPath())
                    .setPath(pathList.get(i).getNewPath())
                    .setTag(i + 1);
            tasks.add(task);
        }

        FileDownloadListener downloadListener = new FileDownloadListener() {
            private final float MAX = tasks.size();
            private int currProgress = 0;
            private boolean isPause = false;


            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (this != task.getListener()) {
                    return;
                }
                // reset
                isPause = false;
                LogUtils.e(TAG, "tag:" + task.getTag() + "_pending...");
            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                if (this != task.getListener()) {
                    return;
                }
                LogUtils.e(TAG, "tag:" + task.getTag() + "_connected...");
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (this != task.getListener()) {
                    return;
                }
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                if (this != task.getListener()) {
                    return;
                }

                int positon = (int) task.getTag();
                currProgress = (int) (positon / MAX * 100);
                LogUtils.e(TAG, "tag:" + task.getTag() + "_completed..." + "progress = " + currProgress);

                // 数据库更新
                currTask.progress = currProgress;
                currTask.update();

                if (progressMap.get(currTask.videoId) < currProgress) {
                    progressMap.put(currTask.videoId, currProgress);
                    // 发送更新
                    EventBus.getDefault().post(new AppEvent(PROGRESS, new CacheProgress(currTask.videoId, currProgress, AppConst.DOWNLOADING)));
                }

                // 继续下一个任务
                if (currProgress == 100) {
                    currTask.status = AppConst.DOWNLOADED;
                    currTask.update();
                    // 发送更新
                    EventBus.getDefault().post(new AppEvent(COMPLETED, new CacheProgress(currTask.videoId, 100, AppConst.DOWNLOADED)));
                    startTask();
                }
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (this != task.getListener()) {
                    return;
                }

                // 单次pause
                if (isPause) {
                    return;
                }
                isPause = true;


                LogUtils.e(TAG, "tag:" + task.getTag() + "_paused..." + "progress = " + currProgress);
                currTask.status = AppConst.DOWNLOAD_PAUSE;
                currTask.update();
                EventBus.getDefault().post(new AppEvent(PAUSE, new CacheProgress(currTask.videoId, currProgress, AppConst.DOWNLOAD_PAUSE)));

                // 开启下一个任务
                if (isStart) {
                    if (indexMap.get(videoId) != null) {
                        index = indexMap.get(videoId);
                    }
                    isStart = false;
                }
                startTask();
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                if (this != task.getListener()) {
                    return;
                }

                LogUtils.e(TAG, "tag:" + task.getTag() + "_error..." + "progress = " + currProgress);
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                if (this != task.getListener()) {
                    return;
                }
            }
        };
        allFileListeners.put(currTask.videoId, downloadListener);

        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(downloadListener);
        queueSet.disableCallbackProgressTimes();
        queueSet.setAutoRetryTimes(1);
        queueSet.downloadSequentially(tasks);
        queueSet.start();
    }

    /**
     * 暂停任务
     *
     * @param videoId
     */
    public void pause(String videoId) {
        FileDownloadListener allFileListener = allFileListeners.get(videoId);
        if (allFileListener != null) {
            FileDownloader.getImpl().pause(allFileListener);
            LogUtils.e(TAG, "暂停成功");
        }
    }

    private boolean isStart = false;
    private String videoId;

    public void start(String videoId) {
        if (taskList.isEmpty()) {
            // 当前无任务
            List<Task> tasks = SQLite.select()
                    .from(Task.class)
                    .queryList();
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                // 读取索引和进度
                indexMap.put(task.videoId, i);
                progressMap.put(task.videoId, task.progress);
                taskList.add(task);
            }

            index = indexMap.get(videoId);
            startTask();
        } else {
            if (currTask != null) {
                // 当前有任务
                pause(currTask.videoId);
                isStart = true;
                this.videoId = videoId;
            } else {
                // 当前无任务
                index = indexMap.get(videoId);
                startTask();
            }

        }
    }

}
