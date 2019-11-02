package main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TestPrinter {

	public static void main(String[] args) {
		
		// Initialize Objects
		Channel printChannel = new Channel();
		Printer p = new Printer(60,printChannel);
		
		printChannel.put(1);
		printChannel.put(2);
		printChannel.put(3);
		printChannel.put(4);
		printChannel.put(5);
		printChannel.put(6);
		printChannel.put(8);
		printChannel.put(9);
		printChannel.put(10);
		printChannel.put(12);
		printChannel.put(15);
		printChannel.put(16);
		printChannel.put(18);
		printChannel.put(20);
		printChannel.put(24);
		printChannel.put(25);
		printChannel.put(27);
		printChannel.put(30);
		p.start();
		
		

	}

}
