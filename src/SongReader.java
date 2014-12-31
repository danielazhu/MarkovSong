import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class SongReader {

	private static final int KEYS = 128;

	public SongReader() {

	}

	public void loadSong(String fileName, MarkovChain<Integer, Note> noteChain) {
		File file = new File(fileName);
		try {
			Sequence sequence = MidiSystem.getSequence(file);
			Track[] tracks = sequence.getTracks();
			for (int i = 0; i < tracks.length; i++) {
				Note[] noteArray = new Note[(int) tracks[i].size()];
				int[] keys = new int[KEYS];
				for (int j = 0; j < tracks[i].size(); j++) {
					try {
						ShortMessage shortMessage = (ShortMessage) tracks[i]
								.get(j).getMessage();

						MidiEvent midiEvent = tracks[i].get(j);
						if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {

							keys[shortMessage.getData1()] = (int) midiEvent
									.getTick();
						} else if (shortMessage.getCommand() == ShortMessage.NOTE_OFF) {
							int length = (int) (midiEvent.getTick() - keys[shortMessage
									.getData1()]);
							noteArray[shortMessage.getData1()] = new Note(
									shortMessage.getData1(), length,
									shortMessage.getData2());
							noteArray[shortMessage.getData1()].print();
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				for (int j = 0; j < noteArray.length - 1; j++) {
					try {
						noteChain.add(noteArray[j].getKey(), noteArray[j + 1]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
