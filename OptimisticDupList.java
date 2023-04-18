/*
 * Class implementing our OptimisticDuplLists
 */
public class OptimisticDupList<T> implements OptimisticInterface<T> {
	/* TODO: Add the necessary fields and implement all the methods given
	 * in this file to create an Optimistic List that supports duplicate 
	 * elements 
	 */	
	private Node<T> head;
	private Node<T> tail;
	OptimisticDupList() {
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
			pred.lock();
			try {
				curr.lock();
				try {
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
						if (curr.key == key) {
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
						return (curr.key == key);
					}
				} finally {
					curr.unlock();
				}
			} finally {
				pred.unlock();
			}
		}
	}

	/* Validate is unique only to the Optimistic and Lazy Lists */
	public boolean validate(Node<T> pred, Node<T> curr) {
		//TODO
		//throw new UnsupportedOperationException();
		Node<T> node = head;
		while (node.key <= pred.key) {
			if (node == pred)
				return pred.next == curr;
			node = node.next;
		}
		return false;
	}
}
