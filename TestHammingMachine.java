package main;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestHammingMachine {

	public static void main(String[] args) {
		
		// Initialize
		AtomicBoolean processFlag = new AtomicBoolean(false);
		int answers = 60;
		
		// In
		Channel twoIn = new Channel();
		Channel threeIn = new Channel();
		Channel fiveIn = new Channel();
		
		ArrayList<Channel> NODEIN = new ArrayList<Channel>(3);
		NODEIN.add(twoIn);
		NODEIN.add(threeIn);
		NODEIN.add(fiveIn);
		
		// Out
		Channel twoOut = new Channel();
		Channel threeOut = new Channel();
		Channel fiveOut = new Channel();
		
		ArrayList<Channel> NODEOUT = new ArrayList<Channel>(3);
		NODEOUT.add(twoOut);
		NODEOUT.add(threeOut);
		NODEOUT.add(fiveOut);
		
		//Initialize Hamming Nodes
		HammingNode TwoNode = new HammingNode(2,twoIn,twoOut);
		HammingNode ThreeNode = new HammingNode(3,threeIn,threeOut);
		HammingNode FiveNode = new HammingNode(5,fiveIn,fiveOut);
		
		//Select, Generate, Print
		Channel printChannel = new Channel();
		Channel threeInOneOut = new Channel();
		
		SelectHammingInt Select = new SelectHammingInt(NODEOUT,threeInOneOut,processFlag);
		GeneratorNode Generator = new GeneratorNode(threeInOneOut,printChannel,NODEIN,processFlag,answers);
		Printer P = new Printer(printChannel);
		
		// Start-up
		Generator.start();
		P.start();
		Select.start();
		
		TwoNode.start();
		ThreeNode.start();
		FiveNode.start();

	}

}
