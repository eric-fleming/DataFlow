package main;

public class HammingNode implements Runnable{
	
	private final int constant;		//never changes
	private final Channel input;	//guarded by Java.util.concurrent
	private final Channel output;	//guarded by Java.util.concurrent
	
	public HammingNode(int constant, Channel input, Channel output) {
		this.constant = constant;
		this.input = input;
		this.output = output;
	}

	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		System.out.println("Ham Node = "+constant);
		while(true) {
			int value = input.take();
			output.put(constant* value);
		}
	}
	
	

}
