# Data Flow
The goal of this project is to print the Hamming Numbers (2^x * 3^y * 5^z) in order where the underlying architeture depends on waiting for information to arrive, processing what you get, and passing it on further.

### Classes

+  **Channel:** this is a wrapping type around a BlockingQueue<Integer>.  I just have three methods in the interface, put, take, and isEmpty.  These objects are mean to used with composition to communicate between Nodes.  That is, at least two Node Objects should have a reference to each Channel: one to put integers and the other to take integers.

+  **HammingNodes:** these are just simple Runnable Objects that continuously look for incoming information, then multiply it by the Hamming factor - 2, 3, or 5 - and send the new int to the output Channel.  When the HammingNode receives an input of -1, this indicates that the process can stop running.

+ **SelectHammingInt:** this is the real work horse in the overall process.  The object has an ArrayList<Channel>.  These are the same channels that each HammingNode outputs.  This object will run as long as the running AtomicBoolean is true.  The object is initialized with three 0's in the inputs.  A zero indicates that you should pull a new value from one of the Channels.  When all three inputs are non-zero, we will select the min and pass it to the GeneratorNode.  Now we loop back over the inputs looking through the inputs for the minimum.  If we see it, pull the next int from the inpit channels.  A latch is used to make sure that this process completes before the next iteration of the while-loop which asks for the new minimum.  Otherwise we could end up printing the same number twice.

+ **GeneratorNode** this object starts and ends the whole life-cycle of the program.  When this object is started, it sets the running AtomicBoolean to true.  Then it sends 1, the starting number to the Print Object and the three HammingNodes.  From here we start listening to the SelectHammingInt Object for the next Hamming integer: again to print and pass back to the HammingNodes.  There is an instance variable which counts down until it hits zero.  When this happends we have selected as many Hamming integers as we need, so -1 is sent to the HammingNodes and the Print Object - they terminate.  Then the running AtomicBoolean is set to false, telling the SelectHammingInt Object to stop executing.  And finally the current Object stops running as well.

### Extending
I wrote this in such a way that would be easy to extend this process to other types of numbers.  All you would need to od is add another HammmingNode, and add the corresponding input and output Channels to the Node and ArrayList<Channels>.  So you could find numbers of the form (2^x * 3^y * 5^z * 7^w) etc...
  
Also keep in mind that the factors should be prime, or if for some reason that isn't the case you can settle for relatively prime.  Example: 8 and 15 would be relatively prime since no number which divides 8 also divided 15, and visa-versa.  Of course since every number is divisible by 1 this is not a concern.
