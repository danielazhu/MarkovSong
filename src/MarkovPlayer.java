import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;



public class MarkovPlayer extends Thread {

	private static final int PLAY_SPEED = 200;
	
	private int chainDepth;
	private MarkovChain<Integer, Note[]> noteChain;
	
	public MarkovPlayer(MarkovChain<Integer, Note[]> noteChain, int chainDepth) {
		this.noteChain = noteChain;
		this.chainDepth = chainDepth;
		
		start();
	}
	
	public void run() {
		try {
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
			MidiChannel[] channels = synth.getChannels();

			int key = 58;
			Note[] noteGroup;

			for (int i = 0; i < 50; i++) {
				noteGroup = noteChain.get(key);
				for (int j = 0; j < chainDepth; j++) {
					key = noteGroup[j].getKey();
					channels[0].noteOn(key, noteGroup[j].getVolume());
					Thread.sleep(PLAY_SPEED);
					channels[0].noteOff(key);
				}
			}
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
