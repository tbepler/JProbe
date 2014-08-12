package language.symboltable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class PersistentQueue<E> {

	private Node<E> head;
	
	private final int size;

	private static class Node<E>{
		private Node<E> next;
		public E value;
	}
	
    private PersistentQueue(Node<E> n, int size){
    	head= n;
    	this.size = size;
    }
    
    public int size(){
    	return size;
    }
	
	public PersistentQueue(Collection<E> c){
		head= new Node<E>();
		Node cur= head;
		
		Iterator<E> itr= c.iterator();
		while(itr.hasNext()){
			if(head.value==null){
				head.value= itr.next();
			} else{
				Node n= new Node();
				n.value= itr.next();
				cur.next= n;
				cur= cur.next;
			}
		}
		size = c.size();
	}

	public E peek(){
		return head.value;
	}

	public PersistentQueue<E> remove(){
		return new PersistentQueue(head.next, size - 1);
	}
	

	public boolean isEmpty(){
		return head==null;
	}
	
	public String toString(){
		Node cur= head;
		String ret="";
		while(cur!=null){
			ret= ret+" "+cur.value;
			cur=cur.next;
		}
		return ret;
	}

}

