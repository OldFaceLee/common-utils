package com.ai.commonUtils.thread;


import org.apache.log4j.Logger;

import java.util.concurrent.*;

/**
 * @author: [djh]杜佳恒
 * @date: Created at 17:35 on 2018/12/14
 * @description: 系统线程池
 */

public class ThreadPoolProvider {
    private static Logger logger = Logger.getLogger(ThreadPoolProvider.class);

    /**
     * 默认线程池核心线程数为服务器(核心数 * 2);最大线程数为(核心数 * 4)
     */
    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors() << 1;

    /**
     * 使用LinkedBlockingDeque 避免JVM初始化时就分配内存空间，造成内存浪费
     */
    private static final BlockingQueue<Runnable> QUEUE = new LinkedBlockingDeque<>(4096);

    private static final ThreadRejectHandler REJECT_HANDLER = new ThreadRejectHandler();

    private static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE << 1,
            60, TimeUnit.SECONDS, QUEUE, REJECT_HANDLER);

    public static ThreadPoolExecutor getPool() {
        return POOL;
    }

    public static void destroyThreadPool() {
        logger.info("销毁线程");
        POOL.shutdown();
    }

    private static class ThreadRejectHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            logger.info("线程池已满,需要执行拒绝处理");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.info("线程休眠被中断");
            }
            executor.execute(r);
        }
    }

}
