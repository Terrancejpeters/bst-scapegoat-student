package structures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<T extends Comparable<T>> implements BSTInterface<T> {
	protected BSTNode<T> root;
	private int size;

	public boolean isEmpty() {
		return root == null;
	}

	public int size() {
		return size;
	}

	public boolean contains(T t) {
		// TODO
		if (get(t).equals(t)) {
			return true;
		}
		return false;
	}

	public boolean remove(T t) {
		if (t == null) {
			throw new NullPointerException();
		}
		Iterator<T> toTraverse = inorderIterator();
		while (toTraverse.hasNext()) {
			T temp = toTraverse.next();
			if (temp.equals(t)) {
				size--;
				return true;
			}
		}
		return false;
	}

	public T get(T t) throws NullPointerException {
		if (t == null) {
			throw new NullPointerException();
		}
		Iterator<T> toTraverse = inorderIterator();
		while (toTraverse.hasNext()) {
			T temp = toTraverse.next();
			if (temp.equals(t)) {
				return temp;
			}
		}
		return null;
	}

	public void add(T t) {
		root = addToSubtree(t, root);
		size++;
	}

	private BSTNode<T> addToSubtree(T t, BSTNode<T> node) {
		if (node == null) {
			return new BSTNode<T>(t, null, null);
		}
		if (t.compareTo(node.getData()) <= 0) {
			node.setLeft(addToSubtree(t, node.getLeft()));
		} else {
			node.setRight(addToSubtree(t, node.getRight()));
		}
		return node;
	}

	@Override
	public T getMinimum() {
		Iterator<T> toGet = preorderIterator();
		T rval = null;
		if (toGet.hasNext())
			rval = toGet.next();
		return rval;
	}

	@Override
	public T getMaximum() {
		Iterator<T> toGet = preorderIterator();
		T temp = null;
		while (toGet.hasNext()) {
			temp = toGet.next();
		}
		return temp;
	}

	@Override
	public int height() {
		return heightHelper(root);
	}

	public int heightHelper(BSTNode<T> node) {
		if (node == null) {
			return -1;
		}
		if (node.getLeft() == null & node.getRight() == null) {
			return 0;
		}
		if (node.getRight() == null) {
			return 1 + heightHelper(node.getLeft());
		}
		if (node.getLeft() == null) {
			return 1 + heightHelper(node.getRight());
		}
		int value = node.getLeft().getData().compareTo(node.getRight().getData());
		if (value > 0) {
			return 1 + heightHelper(node.getLeft());
		} else {
			return 1 + heightHelper(node.getRight());
		}
	}

	@Override
	public Iterator<T> preorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		preorderTraverse(queue, root);
		return queue.iterator();
	}

	public void preorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			queue.add(node.getData());
			preorderTraverse(queue, node.getLeft());
			preorderTraverse(queue, node.getRight());
		}
	}

	@Override
	public Iterator<T> inorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		inorderTraverse(queue, root);
		return queue.iterator();
	}

	private void inorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			inorderTraverse(queue, node.getLeft());
			queue.add(node.getData());
			inorderTraverse(queue, node.getRight());
		}
	}

	@Override
	public Iterator<T> postorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		postorderTraverse(queue, root);
		return queue.iterator();
	}

	public void postorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			postorderTraverse(queue, node.getLeft());
			inorderTraverse(queue, node.getRight());
			queue.add(node.getData());
		}

	}

	// Need to figure this out
	@Override
	public boolean equals(BSTInterface<T> other) {
		return equaHelper(root,other.getRoot());
	}
	
	public boolean equaHelper(BSTNode<T> thisNode, BSTNode<T> otherNode){
		
		if (thisNode == otherNode) {
	        return true;
	    }
	    if (thisNode == null || otherNode == null) {
	        return false;
	    }
	    return thisNode.getData().equals(otherNode.getData()) &&
	           equaHelper(thisNode.getLeft(), otherNode.getLeft()) &&
	           equaHelper(thisNode.getRight(), otherNode.getRight());
	}

	@Override
	public boolean sameValues(BSTInterface<T> other) {
		// TODO
		if (equals(other)) {
			return true;
		}
		LinkedList<T> otherVals = new LinkedList<T>();
		LinkedList<T> thisVals = new LinkedList<T>();
		Iterator<T> thisIt = inorderIterator();
		Iterator<T> otherIt = other.inorderIterator();
		while (thisIt.hasNext()) {
			thisVals.add(thisIt.next());
		}
		while (otherIt.hasNext()) {
			otherVals.add(otherIt.next());
		}
		for (int i = 0; i < thisVals.size(); i++) {
			T toFind = thisVals.get(i);
			if (!otherVals.contains(toFind)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isBalanced() {
		if (size == 0) {
			return true;
		}
		double lowBound = Math.pow(2, height());
		double highBound = Math.pow(2, height() + 1);
		//System.out.println("Low is " + lowBound + " High is " + highBound + " size is " + size);
		if (lowBound <= size && size < highBound) {
			return true;
		}
		return false;
	}

	@Override
	public void balance() {

		LinkedList<T> nodes = getAsLL();
		for (int i = 0; i < nodes.size(); i++){
			remove(nodes.get(i));
		}
		insertTree(0, nodes.size() - 1, nodes);
	}

	public void insertTree(int lower, int upper, LinkedList<T> nodes) {
		
		if (lower == upper)
			add(nodes.get(lower));
		else if (lower + 1 == upper) {
			add(nodes.get(lower));
			add(nodes.get(upper));
		} else {
			int mid = (lower + upper) / 2;
			add(nodes.get(mid));
			insertTree(lower, mid - 1, nodes);
			insertTree(mid+1,upper,nodes);
		}
	}

	public LinkedList<T> getAsLL() {
		LinkedList<T> thisVals = new LinkedList<T>();
		Iterator<T> thisIt = inorderIterator();
		while (thisIt.hasNext()) {
			thisVals.add(thisIt.next());
		}
		return thisVals;

	}

	@Override
	public BSTNode<T> getRoot() {
		// DO NOT MODIFY
		return root;
	}

	public static <T extends Comparable<T>> String toDotFormat(BSTNode<T> root) {
		// DO NOT MODIFY
		// see project description for explanation

		// header
		int count = 0;
		String dot = "digraph G { \n";
		dot += "graph [ordering=\"out\"]; \n";
		// iterative traversal
		Queue<BSTNode<T>> queue = new LinkedList<BSTNode<T>>();
		queue.add(root);
		BSTNode<T> cursor;
		while (!queue.isEmpty()) {
			cursor = queue.remove();
			if (cursor.getLeft() != null) {
				// add edge from cursor to left child
				dot += cursor.getData().toString() + " -> " + cursor.getLeft().getData().toString() + ";\n";
				queue.add(cursor.getLeft());
			} else {
				// add dummy node
				dot += "node" + count + " [shape=point];\n";
				dot += cursor.getData().toString() + " -> " + "node" + count + ";\n";
				count++;
			}
			if (cursor.getRight() != null) {
				// add edge from cursor to right child
				dot += cursor.getData().toString() + " -> " + cursor.getRight().getData().toString() + ";\n";
				queue.add(cursor.getRight());
			} else {
				// add dummy node
				dot += "node" + count + " [shape=point];\n";
				dot += cursor.getData().toString() + " -> " + "node" + count + ";\n";
				count++;
			}

		}
		dot += "};";
		return dot;
	}
}