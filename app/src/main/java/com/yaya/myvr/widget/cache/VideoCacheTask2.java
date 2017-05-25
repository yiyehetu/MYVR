package com.yaya.myvr.widget.cache;

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
import com.yaya.myvr.dao.Task_Table;
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

public class VideoCacheTask2 {
    // 存在下载任务
    private boolean isExist = false;
    // 手动标记
    private boolean isHandStart = false;
    private boolean isHandPause = false;

    private Map<String, FileDownloadListener> allFileListeners = new HashMap<>();
    // 进度集合
//    private Map<String, Integer> progressMap = new HashMap<>();

    public static final String START = "start";
    public static final String PROGRESS = "progress";
    public static final String PAUSE = "pause";
    public static final String COMPLETED = "completed";
    public static final String IDLE = "idle";

    private PendingTaskQueue queue;
    // 指定任务
    private Task handTask;


    private VideoCacheTask2() {
        queue = new PendingTaskQueue();
    }

    private static class InnerClass {
        private static VideoCacheTask2 INSTANCE = new VideoCacheTask2();
    }

    public static VideoCacheTask2 getInstance() {
        return InnerClass.INSTANCE;
    }

    public void addTask(Task task) {
        queue.enQueue(task);
        if (!isExist) {
            autoStartTask();
        }
    }

    /**
     * 启动自动任务
     */
    private void autoStartTask() {
        Task task = queue.autoDequeue();
        if (task == null) {
            isExist = false;
            return;
        }

        // 当前存在任务
        isExist = true;
        // 缓存文件夹
        final String cacheDir = new StringBuilder().append(FileDownloadUtils.getDefaultSaveRootPath())
                .append(File.separator)
                .append(ApiConst.VIDEO_CACHE)
                .append(File.separator)
                .append(task.videoId)
                .append(File.separator)
                .toString();
        // 缓存路径
        final String path = cacheDir + "origin.m3u8";
        downloadM3u8(task, path, cacheDir);
    }

    /**
     * 启动指定任务
     */
    private void startHandTask() {
//        if(handTask == null){
//            start();
//            return;
//        }

        // 当前存在任务
        isExist = true;
        // 缓存文件夹
        final String cacheDir = new StringBuilder().append(FileDownloadUtils.getDefaultSaveRootPath())
                .append(File.separator)
                .append(ApiConst.VIDEO_CACHE)
                .append(File.separator)
                .append(handTask.videoId)
                .append(File.separator)
                .toString();
        // 缓存路径
        final String path = cacheDir + "origin.m3u8";
        downloadM3u8(handTask, path, cacheDir);
    }


    private void downloadM3u8(final Task currTask, final String path, final String cacheDir) {
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
//                progressMap.put(currTask.videoId, currTask.progress);
                currTask.status = AppConst.DOWNLOADING;
                currTask.update();
                EventBus.getDefault().post(new AppEvent(START, new CacheProgress(currTask.videoId, currTask.progress, AppConst.DOWNLOADING)));

                List<VideoPath> pathList = readM3u8Data(path, cacheDir);
                if (pathList != null && pathList.size() > 0) {
                    downloadAllPartFile(currTask, pathList);
                } else {
                    // 本次无数据 需要下一次
                    autoStartTask();
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

        FileDownloader.getImpl().create(currTask.m3u8)
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
    private void downloadAllPartFile(final Task currTask, List<VideoPath> pathList) {
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
//                LogUtils.e(TAG, "tag:" + task.getTag() + "_pending...");
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
//                currTask.progress = currProgress;
//                currTask.update();
//
//                if (progressMap.get(currTask.videoId) < currProgress) {
//                    progressMap.put(currTask.videoId, currProgress);
//                    // 发送更新
//                    EventBus.getDefault().post(new AppEvent(PROGRESS, new CacheProgress(currTask.videoId, currProgress, AppConst.DOWNLOADING)));
//                }

                if(currTask.progress < currProgress){
                    // 数据库更新
                    currTask.progress = currProgress;
                    currTask.update();
                    // 发送更新
                    EventBus.getDefault().post(new AppEvent(PROGRESS, new CacheProgress(currTask.videoId, currProgress, AppConst.DOWNLOADING)));
                }

                // 继续下一个任务
                if (currProgress == 100) {
                    currTask.status = AppConst.DOWNLOADED;
                    currTask.update();
                    // 发送更新
                    EventBus.getDefault().post(new AppEvent(COMPLETED, new CacheProgress(currTask.videoId, 100, AppConst.DOWNLOADED)));
                    autoStartTask();
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

                // 暂停下载
                if (isHandPause) {
                    isHandPause = false;
                    currTask.status = AppConst.DOWNLOAD_PAUSE;
                    currTask.progress = currProgress;
                    currTask.update();
                    EventBus.getDefault().post(new AppEvent(PAUSE, new CacheProgress(currTask.videoId, currProgress, AppConst.DOWNLOAD_PAUSE)));

                    autoStartTask();
                    return;
                }

                // 立即下载
                if (isHandStart) {
                    isHandStart = false;
                    // 当前任务置为待下载
                    currTask.status = AppConst.IDLE;
                    currTask.progress = currProgress;
                    currTask.update();
                    EventBus.getDefault().post(new AppEvent(IDLE, new CacheProgress(currTask.videoId, currProgress, AppConst.IDLE)));
                    queue.enQueue(currTask);

                    startHandTask();
                    return;
                }


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
    public void handPause(String videoId) {
        FileDownloadListener allFileListener = allFileListeners.get(videoId);
        if (allFileListener != null) {
            isHandPause = true;
            FileDownloader.getImpl().pause(allFileListener);
            LogUtils.e(TAG, "暂停成功");
        }
    }


    /**
     * 启动指定任务
     *
     * @param videoId
     */
    public void handStart(String videoId) {
        handTask = queue.handDequeue(videoId);
        isHandStart = true;
        if (handTask == null) {
            // 读取本地数据库
            List<Task> tasks = SQLite.select()
                    .from(Task.class)
                    .where(Task_Table.videoId.eq(videoId))
                    .queryList();
            handTask = tasks.get(0);
        }

        // 判断是否存在下载任务
        if (isExist) {
            // 存在
            FileDownloader.getImpl().pauseAll();
        } else {
            // 不存在
            startHandTask();
        }
    }

    /**
     * 移除待下载任务
     * @param videoId
     */
    public void removePendingTask(String videoId) {
        queue.handDequeue(videoId);
    }

    /**
     * 添加待下载任务
     * @param task
     */
    public void addPendingTask(Task task) {
        queue.enQueue(task);
        task.status = AppConst.IDLE;
        task.update();
        EventBus.getDefault().post(new AppEvent(IDLE, new CacheProgress(task.videoId, task.progress, AppConst.IDLE)));

        if(!isExist){
            isExist = true;
            autoStartTask();
        }
    }

}
