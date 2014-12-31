import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class MarkovController {

	public static void main(String[] args) {
		MarkovChain<Integer, Note> noteChain = new MarkovChain<Integer, Note>();
		(new SongReader()).loadSong("bin/bwv772.mid", noteChain);

		try {
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
			MidiChannel[] channels = synth.getChannels();

			int key = 67;
			Note note;
			
			for (int i = 0; i < 50; i++) {
				note = noteChain.get(key);
				channels[0].noteOn(note.getKey(), note.getVolume());
				Thread.sleep(note.getLength());
				channels[0].noteOff(note.getKey());
				note = noteChain.get(note.getKey());
			}
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
