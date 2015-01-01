import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class SongReader {

	private static final int KEYS = 128;
	private static final int MAX_NOTE_LENGTH = 1000;

	public SongReader() {

	}

	public void loadSong(String fileName, MarkovChain<Integer, Note[]> noteChain, int chainDepth) {
		File file = new File(fileName);
		try {
			Sequence sequence = MidiSystem.getSequence(file);
			Track[] tracks = sequence.getTracks();
			for (int i = 0; i < tracks.length; i++) {
				ArrayList<Note> noteList = new ArrayList<Note>();
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
							noteList.add(new Note(
									shortMessage.getData1(), length,
									shortMessage.getData2()));
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				loadMarkovChain(noteChain, noteList, chainDepth);
			}
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadMarkovChain(MarkovChain<Integer, Note[]> markovChain, ArrayList<Note> noteList, int chainDepth) {
		
		noteList = eliminateNonModeBeats(noteList);
		
		for (int j = 0; j < noteList.size() - chainDepth; j++) {
			try {
				Note[] tempNoteArray = new Note[chainDepth];
				for(int i = 0; i < chainDepth; i++)
					tempNoteArray[i] = noteList.get(j+i+1);
				markovChain.add(noteList.get(j).getKey(), tempNoteArray);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<Note> eliminateNonModeBeats(ArrayList<Note> noteList) {
		ArrayList<Note> newNoteList = new ArrayList<Note>();
		for(int i =0 ; i < noteList.size(); i++) {
			if(noteList.get(i).getLength() < 400)
				newNoteList.add(noteList.get(i));
		}
		
		return newNoteList;
	}
}
