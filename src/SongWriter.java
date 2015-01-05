/**
 * This class writes the markov chain song into a .mid file.
 * Thanks to http://www.automatic-pilot.com/midifile.html for an encoding example.
 * @author Daniel Zhu
 */

import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

public class SongWriter {

	File file;
	private Note[] noteArray;
	private int noteArrayIndex = 0;
	private int playSpeed;

	private static byte[] midiSoundset = { (byte) 0xF0, 0x7E, 0x7F, 0x09, 0x01,
			(byte) 0xF7 };
	private static byte[] midiTempo = { 0x00, (byte) 0x60, 0x00 };

	public SongWriter(String fileName) {
		if (!fileName.toLowerCase().equals("no")) {
			file = new File(fileName);
		}
	}

	public void prepare(int length, int playSpeed) {
		noteArray = new Note[length];
		this.playSpeed = playSpeed;
	}

	public void send(Note note) {
		noteArray[noteArrayIndex++] = note;
	}

	public void write() {
		if (file != null) {
			try {
				Sequence sequence = new Sequence(Sequence.PPQ, 24);

				Track track = sequence.createTrack();

				// Turn on the MIDI sound set
				SysexMessage sysexMessage = new SysexMessage();
				sysexMessage.setMessage(midiSoundset, midiSoundset.length);
				track.add(new MidiEvent(sysexMessage, 0));

				// Set the tempo
				MetaMessage tempoMessage = new MetaMessage();
				tempoMessage.setMessage(0x51, midiTempo, midiTempo.length);
				track.add(new MidiEvent(tempoMessage, 0));

				// Set the track name
				MetaMessage nameMessage = new MetaMessage();
				String trackName = new String("wut");
				nameMessage.setMessage(0x03, trackName.getBytes(),
						trackName.length());
				track.add(new MidiEvent(nameMessage, 0));

				// Turn on omni
				ShortMessage omniMessage = new ShortMessage();
				omniMessage.setMessage(0xB0, 0x7D, 0x00);
				track.add(new MidiEvent(omniMessage, 0));

				// Turn on poly
				ShortMessage polyMessage = new ShortMessage();
				polyMessage.setMessage(0xB0, 0x7F, 0x00);
				track.add(new MidiEvent(polyMessage, 0));

				// Set instrument to piano
				ShortMessage instrumentMessage = new ShortMessage();
				instrumentMessage.setMessage(0xC0, 0x00, 0x00);
				track.add(new MidiEvent(instrumentMessage, 0));

				// Add all the notes
				int time = 1;
				ShortMessage noteMessage;
				for (int i = 0; i < noteArray.length; i++) {

					// Turn on the note
					noteMessage = new ShortMessage();
					noteMessage.setMessage(0x90, noteArray[i].getKey(), 0x60);
					track.add(new MidiEvent(noteMessage, time));
					time += playSpeed;

					// Now turn off the note
					noteMessage = new ShortMessage();
					noteMessage.setMessage(0x80, noteArray[i].getKey(), 0x40);
					track.add(new MidiEvent(noteMessage, time));
				}

				// **** set end of track (meta event) 19 ticks later ****
				MetaMessage endMessage = new MetaMessage();
				byte[] empty = {};
				endMessage.setMessage(0x2F, empty, 0);
				track.add(new MidiEvent(endMessage, time));

				MidiSystem.write(sequence, 1, file);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
