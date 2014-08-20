package src.language.symboltable;

import java.util.HashMap;
import java.util.Map;

import src.language.symboltable.PersistentTreeMap.Node;

public class PersistentTreeMap<K extends Comparable<K>,V> {
	
    private final boolean RED   = true;
    private final boolean BLACK = false;
    
	private Node<K,V> root;
	
	private final Map<K,V> map = new HashMap<K,V>();
	
	public class Node<K extends Comparable<K>,V>{
		private Node<K,V> left;
		private Node<K,V> right;
		private Node<K,V> parent;
		private K key;
		private V value;
		private boolean color;
			

		@SuppressWarnings("unchecked")
		public int compareTo(K o) {
				return ((Comparable<K>) this).compareTo(o);
		}
		
	}
	
	public PersistentTreeMap(){
	}
	
	public PersistentTreeMap(Map<K,V> m){
		this.map.putAll(m);
		insertAllNodes();
	}
	
	
	public PersistentTreeMap<K,V> insert(K key, V value){
		Map<K,V> copy = new HashMap<K,V>(this.map);
		copy.put(key, value);
		System.out.println(copy.toString());
		return new PersistentTreeMap<K,V>(copy);
	}
	
	public PersistentTreeMap<K,V> insertAll(Map<K,V> m){
		Map<K,V> copy = new HashMap<K,V>(this.map);
		copy.putAll(m);
		return new PersistentTreeMap<K,V>(copy);
	}

	public void insertAllNodes(){
		for(K k:this.map.keySet()){
			insertNode(k,this.map.get(k));
		}
	}
	
	public void insertNode(K key, V value){
		map.put(key, value);
		if(root!=null){
		System.out.println("root: "+root.key);
		}
		System.out.println("key: "+key);
		//create new node
		Node<K,V> n= new Node<K, V>();
		n.key = key;
		n.value = value;
		n.color = RED;
		
		//if root is null, make new node the root
		if(root==null){
			root=n;
			root.color = BLACK;
			return;
		}
		
		
		//find insertion point in tree
		Node<K,V> cur= root;
		Node<K,V> parent= cur.parent;
		
		int cmp=0;
		while(cur !=null){
			cmp= n.key.compareTo(cur.key);
			if(cmp>0){
				parent=cur;
				cur=cur.right;
			}
			else if(cmp<0){
				parent=cur;
				cur=cur.left;
			}
			else{
				//key already in tree
				return;
			}
		}
		System.out.println(n.key+" "+parent.key+" "+cmp);
		n.parent=parent;
		
		if (cmp>0){
			parent.right= n;
		}
		else{
			parent.left= n;
		}
		
		//Assume that the root is BLACK
		//If n is not null and its parent is RED, the grandparent must exist
		//Newly inserted nodes are RED. If both n and the parent are RED, the red-black tree rules are violated.

			fixInsertion(n);
	
		System.out.println(key);
		return;
	}
	
	public void fixInsertion(Node<K,V> n){
		while(n!=null && n.parent!=null && n.parent.color==RED){
			//the parent of n is on the left of the grandparent
			if(n.parent==n.parent.parent.left){
				Node<K,V> uncle= n.parent.parent.right;
				//If uncle is RED, grandparent must be black. Flip colors and push violation up tree
				if(uncle!=null && uncle.color==RED){
					if(n.parent.parent==root){
						n.color=BLACK;
						return;
					}
					System.out.println("case1");
					uncle.color=BLACK;
					n.parent.color=BLACK;
					n.parent.parent.color=RED;
					n=n.parent.parent;
				}
				//Do rotations to fix violation
				else{
					//make n on the left side of its parent
					if(n==n.parent.right){
						System.out.println("case2");
						n=n.parent;
						leftRotation(n);
					}
					System.out.println("case3");
					n.parent.color= BLACK;
					n.parent.parent.color=RED;
					rightRotation(n.parent.parent);
				}
			}
			
			//the parent of n is on the right of the grandparent
			else if(n.parent==n.parent.parent.right){
				Node<K,V> uncle= n.parent.parent.left;
				//If uncle is RED, grandparent must be black. Flip colors and push violation up tree
				if(uncle!=null && uncle.color==RED){
					if(n.parent.parent==root){
						n.color=BLACK;
						return;
					}
					System.out.println("case1");
					uncle.color=BLACK;
					n.parent.color=BLACK;
					n.parent.parent.color=RED;
					n=n.parent.parent;
				}
				//Do rotations to fix violation
				else{
					//make n on the right side of its parent
					if(n==n.parent.right){
						System.out.println("case2");
						n=n.parent;
						leftRotation(n);
					}
					System.out.println("case3");
					n.parent.color= BLACK;
					n.parent.parent.color=RED;
					rightRotation(n.parent.parent);
				}
			}

		}
	}
	
	public void inOrder(Node<K,V> n){
		if(n==null){
			return;
		}
		inOrder(n.left);
		System.out.println(n.key+" "+n.color);
		inOrder(n.right);
	}
	
	public void leftRotation(Node<K,V> x){
		//New root for tree after rotation
		Node<K,V> y = x.right;
		
		//Move y's left subtree to x's right subtree
		x.right= y.left;
		if(y.left!=null){
			y.left.parent= x;
		}
		
		//set parent of y to parent of x
		y.parent= x.parent;
		//if x is the root, set y as the new root
		if(x.parent==null){
			root=y;
		}
		
		//set left/right of grandparent to be y
		else if(x==x.parent.left){
			x.parent.left=y;
		}
		else{
			x.parent.right=y;
		}
		y.left=x;
		x.parent=y;
	}
	
	public void rightRotation(Node<K,V> y){
		//New root for tree after rotation
		Node<K,V> x = y.left;
		
		//Move x's right subtree to y's left subtree
		y.left= x.right;
		if(x.right!=null){
			x.right.parent= x;
		}
		
		//set parent of y to parent of x
		x.parent= y.parent;
		//if y is the root, set x as the new root
		if(y.parent==null){
			root=x;
		}
		
		//set left/right of grandparent to be x
		else if(y==y.parent.left){
			y.parent.left=x;
		}
		else{
			y.parent.right=x;
		}
		x.right=y;
		y.parent=x;
	}
	

	public void remove(K key){
 
		if(!contains(key)){
			return;
		}
		
		map.remove(key);
		
		//find node in tree
		Node<K,V> cur= root;
		while(cur !=null){
			int cmp= key.compareTo(cur.key);
			if(cmp>0){
				cur=cur.right;
			}
			else if(cmp<0){
				cur=cur.left;
			}
			else{
				break;
			}
		}
		
		Node<K,V> newNode= removeNode(cur);
		
		//if the removed node is RED, the rules aren't broken
		if(cur.color==RED){
			return;
		}
		
		while(newNode!=root && newNode.color==BLACK){
			fixRemove(newNode);
		}
	}
	
	public Node<K,V> removeNode(Node<K,V> n){
		
		//if n is leaf, delete node
		if(n.left==null && n.right==null){
			if(n==root){
				root=null;
			}
			else if(n==n.parent.left){
				n.parent.left=null;
			}
			else if(n==n.parent.right){
				n.parent.right=null;
			}
			return n.parent;
		}
		
		if(n==root && n.left!=null){
			root=n.left;
			return root;
		}
		
		if(n==root && n.right!=null){
			root=n.right;
			return root;
		}
		
		Node<K, V> parent= n.parent;
		int cmp= n.key.compareTo(parent.key);
		
		//if deleted node only has one subtree, move pointer
		if(n.left==null){
			if(cmp<0){
				parent.left= n.right;
				n.right.parent=parent;
				return parent;
			}
			else if(cmp>0){
				parent.right= n.right;
				n.right.parent=parent;
				return parent;
			}
		}
		else if(n.right==null){
			if(cmp<0){
				parent.left= n.left;
				n.left.parent= parent;
				return parent;
			}
			else if(cmp>0){
				parent.right= n.left;
				n.left.parent= parent;
				return parent;
			}
		}

		//Replace deleted node with smallest node in right subtree
		else{
			//Find smallest node in right subtree
			Node<K,V> cur= n.right;
			while(cur!=null){
				if(cur.left==null && cur.right==null){
					break;
				}
				else{
					cur=cur.left;
				}
			}

			//Replace n with cur's key and value
			n.key= cur.key;
			n.value=cur.value;

			//delete cur
			if(cur.key.compareTo(cur.parent.key)<0){
				cur.parent.left=null;
			}
			else if(cur.key.compareTo(cur.parent.key)>0){
				cur.parent.right=null;
			}
		}

		return n;

		
	}
	
	public void fixRemove(Node<K,V> n){

		if(n==n.parent.left){
			Node<K,V> sibling= n.parent.right;
			//case #1
			if(sibling.color==RED){
				sibling.color=BLACK;
				n.parent.color=RED;
				leftRotation(n.parent);
				sibling= n.parent.right;
			}
			//case #2
			if(sibling.left.color==BLACK && sibling.right.color==BLACK){
				sibling.color=RED;
				n=n.parent;
			}
			//case #3
			else if(sibling.right.color==BLACK){
				sibling.left.color=BLACK;
				sibling.color=RED;
				rightRotation(sibling);
				sibling=n.parent.right;
			}
			//case #4
			sibling.color=n.parent.color;
			n.parent.color=BLACK;
			sibling.right.color=BLACK;
			leftRotation(n.parent);
			n=root;
		}


		if(n==n.parent.right){
			Node<K,V> sibling= n.parent.left;
			//case#1
			if(sibling.color==RED){
				sibling.color=BLACK;
				n.parent.color=RED;
				rightRotation(n.parent);
				sibling= n.parent.left;
			}
			//case #2
			if(sibling.right.color==BLACK && sibling.left.color==BLACK){
				sibling.color=RED;
				n=n.parent;
			}
			//case #3
			else if(sibling.left.color==BLACK){
				sibling.right.color=BLACK;
				sibling.color=RED;
				leftRotation(sibling);
				sibling=n.parent.left;
			}
			//case #4
			sibling.color=n.parent.color;
			n.parent.color=BLACK;
			sibling.right.color=BLACK;
			rightRotation(n.parent);
			n=root;
		}
	}
	

	public int depth(Node<K,V> n){
		if(n==null){
			return 0;
		}
		else{
			return 1+ Math.max(depth(n.left), depth(n.right));
		}
	}
	
	public V get(K key){
		Node<K,V> cur= root;
		
		while(cur != null){
			int cmp = key.compareTo(cur.key);
			if (cmp<0){
				cur=cur.left;
			}
			else if (cmp>0){
				cur=cur.right;
			}
			else{
				return cur.value;
			}
		}
		//key not in tree; return null
		return cur.value;
	}

	public boolean contains(K key){
		return (get(key) != null);
	}
	
	public V lookup(K key){
		return map.get(key);
	}
	@Override
	public int hashCode(){
		int hash=0;
		for(K key:this.map.keySet()){
			hash=hash*13+key.hashCode()*17+this.map.get(key).hashCode()*23;
		}
		return hash;
	}
	@Override
	public boolean equals(Object o){
		if(this==o){
			return true;
		}
		if(!(o instanceof PersistentTreeMap)){
			return false;
		}
		
		PersistentTreeMap obj = (PersistentTreeMap) o;
		if(!this.map.keySet().containsAll(obj.map.keySet())){
			return false;
		}
		if(!obj.map.keySet().containsAll(this.map.keySet())){
			return false;
		}
		for(K key:map.keySet()){
			if(this.map.get(key)!=obj.map.get(key)){
				return false;
			}
		}
		return true;
	}
	

}
