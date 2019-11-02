package main;

import java.util.concurrent.LinkedBlockingQueue;

public class Channel {
	
	private LinkedBlockingQueue<Integer> queue;
	
	
	public Channel() {
		this.queue = new LinkedBlockingQueue<Integer>();
	}
	
	public int take() {
		int result = 0;
		try {
			 result = this.queue.take();
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		return result;
	}
	
	public void put(Integer a) {
		try {
			this.queue.put(a);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
	}
	
	public boolean notEmpty() {
		return queue.size() > 0;
	}
	
	
	
}
