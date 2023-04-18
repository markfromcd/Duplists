import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Random;

public class ListTester implements Runnable {
	final private int numThr;
	final private List<Integer> list;
	final private CyclicBarrier interimBarr;
	final private CyclicBarrier printResBarr;
	final private Random rand = new Random();

	ListTester(int _numThr, List<Integer> _list) {
		numThr = _numThr;
		list = _list;

		Runnable interimRun = new Runnable() {
			public void run() {
				System.out.println("Next test...");
				assert(list.isEmpty());
			}
		};
		Runnable printResRun = new Runnable() {
			public void run() {
				System.out.println("Tests complete!");
			}
		};

		interimBarr = new CyclicBarrier(numThr, interimRun);
		printResBarr = new CyclicBarrier(numThr, printResRun);
	}

	/**
	 * Test to see if sentinels are in the list, and that removing items
	 * not in the list works. Be sure to write additional tests as well!
	 * NOTE: This means your implementation must have sentinel nodes,
	 * with minimum and maximum values at the beginning and end respectively.
	 */
	public void run() {
		// Child threads start here to begin testing-- tests are below
	
		// We suggest writing a series of zero-sum tests,
		// i.e. lists should be empty between tests. 
		// The interimBarr enforces this.
		try {
			containsOnEmptyListTest();
			interimBarr.await();
			sentinelsInEmptyListTest();
			interimBarr.await();
			removeEmpty();
			interimBarr.await();
			add100();
			shouldBeEmpty(false);
			//interimBarr.await();
			remove100();
			//interimBarr.await();
			//shouldBeEmpty(true);
			interimBarr.await();
			add100Remove100Same();
			interimBarr.await();
			shouldBeEmpty(true);
			interimBarr.await();
			add100Remove100Diff();
			interimBarr.await();
			shouldBeEmpty(true);
			interimBarr.await();
			printResBarr.await();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void add100(){
		for (int i = 0; i<100; i++){
			assert (list.add(10) == true) : "should be able to add value";
		}
		assert(list.contains(10) == true) : "should have value";
	}
	
	private void remove100(){
		for (int i = 0; i<100; i++){
			assert(list.remove(10) == true) : "should be able to remove value";
		}
	}
	
	private void removeEmpty(){
		assert (list.remove(10) == false) : "removing element that does not exist should return false";
	}
	
	private void add100Remove100Same(){
		for (int i = 0; i<100; i++){
			assert(list.add(10) == true) : "should be able to add element";
		}
		for (int i = 0; i<100; i++){
			assert(list.remove(10) == true) : "should be able to remove element";
		}
	}
	
	private void add100Remove100Diff(){
		int myRandInt = rand.nextInt(10000);
		for (int i = 0; i<100; i++){
			assert(list.add(myRandInt) == true) : "should be able to add element";
		}
		for (int i = 0; i<100; i++){
			assert(list.remove(myRandInt) == true) : "should be able to remove element";
		}
	}
	
	private void shouldBeEmpty(boolean check){
		assert(list.isEmpty() == check) : "list should be empty";
	}
	

	private final static int NUMREP = 100000;
	private void containsOnEmptyListTest() {
		for (int i = 0; i < 10; i++) {
			// Remove values not in list. If this is ever true, crash!
			assert list.contains(i) == false : "list should be empty";
		}
	}


	private void sentinelsInEmptyListTest() {	
		int i = 0;
		for (; i < NUMREP; i++) {
			// We use unique thread IDs as our values
			assert(list.add(Integer.MIN_VALUE) == false) : "should not be able to add min value";
			assert(list.add(Integer.MAX_VALUE) == false) : "should not be able to add max value";
		}
	}

}
	
