import java.util.ArrayList;
import java.util.HashMap;


public class MarkovChain<E, T> {
	
	private HashMap<E, ArrayList<T>> hashMap;
	
	public MarkovChain() {
		hashMap = new HashMap<E, ArrayList<T>>();
	}
	
	public void add(E e1, T t2) {
		if(hashMap.get(e1) != null) {
			hashMap.get(e1).add(t2);
		} else if(hashMap.get(e1) == null) {
			ArrayList<T> temp = new ArrayList<T>();
			temp.add(t2);
			hashMap.put(e1, temp);
		}
	}
	
	public T get(E e1) {
		ArrayList<T> temp = hashMap.get(e1);
		int woah = (int) (Math.random() * temp.size());
		return temp.get(woah);
	}
}
