package src.language.symboltable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class PersistentQueue<E> {

	private Node<E> head;

	private static class Node<E>{
		private Node<E> next;
		private E value;
	}
	
    private PersistentQueue(Node<E> n){
    	head= n;
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
	}

	public E peek(){
		return head.value;
	}

	public PersistentQueue<E> remove(){
		return new PersistentQueue(head.next);
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

