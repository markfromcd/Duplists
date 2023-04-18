/*
 * Class implementing our LazyDupList
 */
public class LazyDupList<T> implements LazyInterface<T> {
	/* TODO: Add the necessary fields and implement all the
	 * methods given in this file to create a Lazy List that
	 * supports duplicate elements 
	 */
	Node<T> head;
	Node<T> tail;

	LazyDupList() {
		head = new Node(null, Integer.MIN_VALUE);
		tail = new Node(null, Integer.MAX_VALUE);
		head.next = tail;
	}

	public boolean isEmpty() {
		return head.next.key == tail.key;
	}

	public boolean add(T item) {
		int key = item.hashCode();
		if (key == Integer.MIN_VALUE || key == Integer.MAX_VALUE){
			return false;
		}
		//TODO
		//throw new UnsupportedOperationException();
		while (true) {
			Node<T> pred = head;
			Node<T> curr = pred.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			try {
				pred.lock();
				try {
					curr.lock();
					if (validate(pred, curr)) {
						Node<T> node = new Node<>(item);
						node.next = curr;
						pred.next = node;
						return true;
					}
				} finally {
					curr.unlock();
				}
			} finally {
				pred.unlock();
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
		while (true) {
			Node<T> pred = head;
			Node<T> curr = head.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			pred.lock();
			try {
				curr.lock();
				try {
					if (validate(pred, curr)) {
						if (curr.key == key) {
							curr.marked = true;
							pred.next = curr.next;
							return true;
						} else {
							return false;
						}

					}
				} finally {
					curr.unlock();
				}
			} finally {
				pred.unlock();
			}
		}
	}	

	public boolean contains(T item) {
		int key = item.hashCode();
		if(key == Integer.MIN_VALUE || key == Integer.MAX_VALUE){
			return true;
		}
		//TODO
		//throw new UnsupportedOperationException();
		Node<T> curr = this.head;
		while (curr.key < key) {
			curr = curr.next;
		}
		return curr.key == key && !curr.marked;
	}

	/* Validate is unique only to the Optimistic and Lazy Lists */
	private boolean validate(Node<T> pred, Node<T> curr) {
		//TODO
		//throw new UnsupportedOperationException();
		return !pred.marked && !curr.marked && pred.next == curr;
	}
}
