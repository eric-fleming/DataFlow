package main;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Gather implements Runnable {
	
	//Corresponding Channel and Input in SelectHammingInput
	private Channel selectInputChannel;
	private AtomicInteger nextHamInt;
	private CountDownLatch latch;
	
	
	public Gather(Channel hamNodeOutputChannel, AtomicInteger ham, CountDownLatch latch) {
		this.selectInputChannel = hamNodeOutputChannel;
		this.nextHamInt = ham;
		this.latch = latch;
	}

	@Override
	public void run() {
		//pull the next value out of the Channel and put it in the corresponding Channel
		int nextValue = selectInputChannel.take();
		nextHamInt.set(nextValue);
		this.latch.countDown();
	}

}
