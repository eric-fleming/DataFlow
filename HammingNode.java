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
		while(true) {
		
			int value = input.take();
			//Generator will send -1 when it has reached the specified number of outputs
			if(value == -1) {
				break;
			}
			output.put(constant* value);
		}
		//Double checks that it exits the loop
		System.out.println("Node"+constant+" : stopped");
	}
	
	

}
