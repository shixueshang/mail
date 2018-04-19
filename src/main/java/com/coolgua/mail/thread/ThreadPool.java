package com.coolgua.mail.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

	/**
	 * corePoolSize： 线程池维护线程的最少数量
	 * maximumPoolSize：线程池维护线程的最大数量
	 * keepAliveTime： 线程池维护线程所允许的空闲时间
	 * unit： 线程池维护线程所允许的空闲时间的单位
	 * workQueue： 线程池所使用的缓冲队列
	 */
	private static ExecutorService remoteDataHandleThread = new ThreadPoolExecutor(3, 10, 2 * 60, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());
	
	private static final ThreadPool instance = new ThreadPool();

	private ThreadPool() {
		
	}

	public static ThreadPool getInstance() {
		return instance;
	}

	public void handleRemoteData(Runnable command) {
		remoteDataHandleThread.execute(command);
	}

}
