package main;

import java.util.concurrent.atomic.AtomicInteger;

public class Gather implements Runnable {
	
	//Corresponding Channel and Input in SelectHammingInput
	private Channel HamChannelFeed;
	private AtomicInteger HamInt;
	
	
	public Gather(Channel hamChannelFeed, AtomicInteger ham) {
		this.HamChannelFeed = hamChannelFeed;
		this.HamInt = ham;
	}

	@Override
	public void run() {
		//pull the next value out of the Channel and put it in the corresponding Channel
		int nextValue = HamChannelFeed.take();
		HamInt.set(nextValue);
	}

}
