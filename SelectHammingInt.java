package main;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * 
 * @author Eric Fleming
 * This class has four Channels:
 * three which are the new numbers and one channel where the minimum is output.
 * This object also holds three atomic integers, one piped from each inpit Channel.
 * If the HamInt is zero, this indicates that we should pull a new value from the channel
 * I use a threadpool executor to manage these updates.
 * The pool runs a Gather Runnable which is holds references to the Channel and AtomicInt pair.
 */
public class SelectHammingInt implements Runnable {
	
	private AtomicBoolean running;  //Generator node is responsible for changing this execution flag.
	private final ArrayList<Channel> HamChannels;
	private final ArrayList<AtomicInteger> HamInts;
	private final Channel feedForward;
	private final LinkedBlockingQueue<Runnable> workQueue;
	
	
	public SelectHammingInt(ArrayList<Channel> inputs, Channel output, AtomicBoolean r) {
		this.HamChannels = inputs;
		this.HamInts = new ArrayList<AtomicInteger>(inputs.size());
		this.feedForward = output;
		this.running = r;
		this.workQueue = new LinkedBlockingQueue<Runnable>();
	}
	
	
	public void start() {
		// Initialize
		
		for(int i=0; i<3; i++) {
			this.HamInts.add(i,new AtomicInteger(0));
		}
		
		Thread me = new Thread(this);
		me.start();
	}
	
	public void run() {
		this.running.set(true);
		ThreadPoolExecutor pool = new ThreadPoolExecutor(3,6,100,TimeUnit.MILLISECONDS,workQueue);
		
		while(true) {
			// Break out of while loop when you pass enough integers forward
			if(!running.get()) {break;}
			
			// Checks if all HamInts are not zero
			boolean hamIntState = allIntsNonZero();
			
			if(hamIntState) {
				// Select and print the non-zero minimum
				int min = this.Min();
				feedForward.put(min);
				refreshHamInts(min,pool);
			}
			
			else {
				// At least one HamInt is zero.
				// We need to replace zero values with next int from the corresponding channel
				refreshHamInts(0,pool);
			}
		}
		
		//clean up
		pool.shutdown();
		System.out.println("Select : stopped");
		
	}
	
	
	private boolean allIntsNonZero() {
		// returns true if all HamInts != 0
		boolean allGood = true;
		for(int i=0;i<HamInts.size();i++) {
			allGood = allGood && (HamInts.get(i).get() != 0);
		}
		return allGood;
	}
	
	
	private void refreshHamInts(int condition, ThreadPoolExecutor pool) {
		// New to count the number of Integers that meet the condition.
		int count = 0;
		
		for(int i=0;i<HamInts.size();i++) {
			if(HamInts.get(i).get() == condition) {
				count++;
			}
		}
		
		//Create latch to make sure the new ints are inplace before the next trip around the while-loop
		CountDownLatch CDL = new CountDownLatch(count);
		
		// Loop through the ham integers.
		// Take a new HamInt from the matching channel if it meets the condition.
		// ThreadPool spawns a thread to run the task.
		for(int i=0;i<HamInts.size();i++) {
			if(HamInts.get(i).get() == condition) {
				Gather g = new Gather(HamChannels.get(i),HamInts.get(i),CDL);
				pool.execute(g);
			}
		}
		
		try {
			CDL.await();
		}catch(InterruptedException e) {
			
		}
	}
	
	
	private int Min() {
		// Determine the minimum value of the 3 HamInts.
		int min = HamInts.get(0).get();
		for(int i=0;i<HamInts.size();i++) {
			if(HamInts.get(i).get() < min) {
				min = HamInts.get(i).get();
			}
		}
		return min;
	}

}
