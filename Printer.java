package main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Printer implements Runnable{
	
	private Channel jobs;
	private final int size;
	private AtomicInteger completed;

	public Printer(int size, Channel printJobs) {
		this.size = size;
		this.completed = new AtomicInteger(0);
		this.jobs = printJobs;
	}
	
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		while(true) {
			int job= jobs.take();
			this.print(job);
			completed.set(completed.get()+1);
			if(completed.get() == size) {
				break;
			}
		}
		
	}

	
	
	private void print(int job) {
		System.out.println(job);
		
	}

	
	
	

}
