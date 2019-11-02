package main;

import java.util.ArrayList;

public class GeneratorNode implements Runnable{
	
	private final int STARTING_VALUE;
	private Channel integerFeed;
	private ArrayList<Channel> inputChannels;
	private Channel printChannel;
	
	
	public GeneratorNode(Channel intFeed, Channel p, ArrayList<Channel> inputs) {
		super();
		this.STARTING_VALUE = 1;
		this.integerFeed = intFeed;
		this.inputChannels = inputs;
		this.printChannel = p;
	}
	
	public void miniStart() {
		// Kicks off the DataFlow
		printChannel.put(STARTING_VALUE);
	}
	
	
	public void start() {
		// Kicks off the DataFlow
		printChannel.put(STARTING_VALUE);
		
		for( Channel c : inputChannels) {
			c.put(STARTING_VALUE);
		}
		
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		System.out.println("Generator Started");
		//Searches for result from the Feed
		//Delegates integer to printChannel and feeds back to input channels
		while(true) {
			int ham = integerFeed.take();
			System.out.println("your new ham : "+ham);
			printChannel.put(ham);
			for( Channel c : inputChannels) {
				c.put(ham);
			}	
		}
	}
	
	

}
