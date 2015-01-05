import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;



public class MarkovPlayer extends Thread {

	private static final int PLAY_SPEED = 200;
	private static final int SONG_LENGTH_CYCLES = 50;
	
	private int chainDepth;
	private MarkovChain<Integer, Note[]> noteChain;
	private SongWriter songWriter;
	
	public MarkovPlayer(MarkovChain<Integer, Note[]> noteChain, int chainDepth, SongWriter songWriter) {
		this.noteChain = noteChain;
		this.chainDepth = chainDepth;
		this.songWriter = songWriter;
		
		songWriter.prepare(SONG_LENGTH_CYCLES * chainDepth, PLAY_SPEED);
		
		start();
	}
	
	public void run() {
		try {
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
			MidiChannel[] channels = synth.getChannels();

			int key = 58;
			Note[] noteGroup;

			for (int i = 0; i < SONG_LENGTH_CYCLES; i++) {
				noteGroup = noteChain.get(key);
				for (int j = 0; j < chainDepth; j++) {
					key = noteGroup[j].getKey();
					channels[0].noteOn(key, noteGroup[j].getVolume());
					songWriter.send(noteGroup[j]);
					Thread.sleep(PLAY_SPEED);
					channels[0].noteOff(key);
				}
			}
			
			songWriter.write();	
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
