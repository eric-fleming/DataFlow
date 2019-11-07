package main;

public class Printer implements Runnable{
	
	private Channel jobs;

	public Printer(Channel printJobs) {
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
			// Stopping message for last integer to print
			if(job == -1) {
				break;
			}
			print(job);
		}
		//Checks that loop exits.
		System.out.println("Print : Complete");
		
	}

	private void print(int job) {
		System.out.println(job);
		
	}

	
	
	

}
