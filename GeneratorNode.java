package main;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class GeneratorNode implements Runnable{
	
	private AtomicBoolean running;  //signals to other objects to stop execution.
	private final int STARTING_VALUE = 1;
	private final int STOPPING_VALUE = -1;
	private int STOP; //how many ints do you want?
	
	private Channel integerFeed;
	private ArrayList<Channel> hamNodeInputChannels;
	private Channel printChannel;
	
	
	public GeneratorNode(Channel intFeed, Channel p, ArrayList<Channel> inputs, AtomicBoolean r, int stop) {
		this.STOP = stop;
		this.integerFeed = intFeed;
		this.hamNodeInputChannels = inputs;
		this.printChannel = p;
		this.running = r;
	}
	
	//This code snippet gets called many times so I abstracted it.
	private void sendToHamNodes(int n) {
		for( Channel c : hamNodeInputChannels) {
			c.put(n);
		}
	}
	
	
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		//Everyone can start
		this.running.set(true);
		System.out.println("Generator Started");
		
		printChannel.put(STARTING_VALUE);
		this.STOP--;
		
		sendToHamNodes(STARTING_VALUE);
		
		while(running.get()) {
			//request next integer, decrement, print, and feed integer back into the system.
			int ham = integerFeed.take();
			this.STOP--;
			printChannel.put(ham);
			sendToHamNodes(ham);	
			
			if(STOP == 0) {
				//Everyone should stop
				this.running.set(false);
			}
		}
		
		//send stop message to HamNodes
		sendToHamNodes(STOPPING_VALUE);
		printChannel.put(STOPPING_VALUE);
		System.out.println("Generator : stopped");
	}
		
}
