package main;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * 
 * @author ericfleming
 * This class has four Channels:
 * three which are the new numbers and one channel where the minimum is output.
 * This object also holds three atomic integers, one piped from each inpit Channel.
 * If the HamInt is zero, this indicates that we should pull a new value from the channel
 * I use a threadpool executor to manage these updates.
 * The pool runs a Gather Runnable which is holds references to the Channel and AtomicInt pair.
 */
public class SelectHammingInt implements Runnable {
	
	private final ArrayList<Channel> HamChannels;
	private final ArrayList<AtomicInteger> HamInts;
	private final Channel feedForward;
	
	private final LinkedBlockingQueue<Runnable> workQueue;
	
	

	public SelectHammingInt(ArrayList<Channel> inputs, Channel output) {
		this.HamChannels = inputs;
		this.HamInts = new ArrayList<AtomicInteger>(inputs.size());
		this.feedForward = output;
		this.workQueue = new LinkedBlockingQueue<Runnable>();
	}
	
	
	
	public void start() {
		
		// Initialize
		for(int i=0; i<3; i++) {
			this.HamInts.add(i,new AtomicInteger(0));
		}
		
		//Spin off run method
		Thread me = new Thread(this);
		me.start();
		//System.out.println("Me");
		
	}
	
	public void run() {
		
		ThreadPoolExecutor pool = new ThreadPoolExecutor(3,6,500,TimeUnit.MILLISECONDS,workQueue);
		
		while(true) {
			
			// processes the state, checks if all HamInts are not zero
			boolean allGood = true;
			for(int i=0;i<HamInts.size();i++) {
				allGood = allGood && (HamInts.get(i).get() != 0);
			}
			
			System.out.println("allgood : "+allGood);
			
			if(allGood) {
				// Select and print the non-zero minimum
				int min = this.Min();
				feedForward.put(min);
				
				// clears the min
				for(int i=0;i<HamInts.size();i++) {
					if(HamInts.get(i).get() == min) {
						//System.out.println("blocked when replacing min");
						Gather g = new Gather(HamChannels.get(i),HamInts.get(i));
						pool.execute(g);
						//int nextValue = HamChannels.get(i).take();
						//HamInts.get(i).set(nextValue);
					}
				}
				
			}
			else {
				//we need to replace zero values with next int
				for(int i=0;i<HamInts.size();i++) {
					if(HamInts.get(i).get() == 0) {
						System.out.println("blocked when replacing zero");
						//int nextValue = HamChannels.get(i).take();
						//HamInts.get(i).set(nextValue);
						Gather g = new Gather(HamChannels.get(i),HamInts.get(i));
						pool.execute(g);
					}
				}
			}
			
			
		}
		
	}

	public int Min() {
		int min = HamInts.get(0).get();
		for(int i=0;i<HamInts.size();i++) {
			if(HamInts.get(i).get() < min) {
				min = HamInts.get(i).get();
			}
		}
		return min;
	}

}
