
public class Note {

	private int length;
	private int key;
	private int volume;

	public Note(int key, int length, int volume) {
		this.key = key;
		this.length = length;
		this.volume = volume;
	}
	
	public void print() {
		System.out.println("Note with key "+key+", length "+length+", and volume "+volume+".");
	}
	
	public int getKey() {
		return key;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public int getLength() {
		return length;
	}
}
