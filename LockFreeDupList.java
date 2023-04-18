import java.util.concurrent.atomic.AtomicMarkableReference;

/*
 * Class implementing our LockFreeDupList
 */
public class LockFreeDupList<T> implements LockFreeInterface<T> {
	/* TODO: Add the necessary fields and implement all of the methods 
	 * in this file to create a Lock-Free List that supports duplicate 
	 * elements. Feel free to add whatever helper methods you deem
	 * necessary
	 */
	 
	Node<T> head, tail;
	class Window{
		public Node<T> pred, curr;
		Window(Node<T> myPred, Node<T> myCurr) {
			pred = myPred; curr = myCurr;
		}
	}
	
	Window find(Node<T> head, int key) {
		//TODO
		//throw new UnsupportedOperationException();
		Node<T> pred = null, curr = null, succ = null;
		boolean[] marked = {false};
		boolean snip;
		while (true) {
			pred = head;
			curr = pred.next.getReference();
			boolean flag = false;
			while (true) {
				succ = curr.next.get(marked);
				// delete the marked node
				while (marked[0]) {
					snip = pred.next.compareAndSet(curr, succ, false, false);
					if (!snip) {
						flag = true;
						break;
					}
					curr = succ;
					succ = curr.next.get(marked);
				}
				if (flag) break;
				if (curr.key >= key) return new Window(pred, curr);
				pred = curr;
				curr = succ;
			}
		}
	}

	LockFreeDupList() {
		head = new Node(null, Integer.MIN_VALUE);
		tail = new Node(null, Integer.MAX_VALUE);
		head.next.set(tail, false);
	}

	public boolean isEmpty() {
		return head.next.getReference() == tail;
	}

	public boolean add(T item) {
		int key = item.hashCode();
		if (key == Integer.MIN_VALUE || key == Integer.MAX_VALUE){
			return false;
		}
		//TODO
		//throw new UnsupportedOperationException();
		while(true) {
			Window window = find(head, key);
			Node<T> pred = window.pred, curr = window.curr;
			Node<T> node = new Node<>(item);
			node.next.compareAndSet(null, curr, false, false);
			if (pred.next.compareAndSet(curr, node, false, false)) {
				return true;
			}
		}
	}	

	public boolean remove(T item) {
		int key = item.hashCode();
		if (key == Integer.MIN_VALUE || key == Integer.MAX_VALUE){
			return false;
		}
		//TODO
		//throw new UnsupportedOperationException();
		boolean snip;
		while (true) {
			Window window = find(head, key);
			Node<T> pred = window.pred, curr = window.curr;
			if (curr.key != key) {
				return false;
			} else {
				Node<T> succ = curr.next.getReference();
				snip = curr.next.compareAndSet(succ, succ, false, true);
				if(!snip) 
					continue;
				pred.next.compareAndSet(curr, succ, false, false);
				return true;
			}
		}
	}	

	public boolean contains(T item) {
		int key = item.hashCode();
		if (key == Integer.MIN_VALUE || key == Integer.MAX_VALUE){
			return true;
		}
		//TODO
		//throw new UnsupportedOperationException();
		Node<T> curr = this.head;
		while (curr.key <= key) {
			if (curr.key == key && curr.next.isMarked() == false) {
				return true;
			}
			curr = curr.next.getReference();
		}
		return false;
	}
}
