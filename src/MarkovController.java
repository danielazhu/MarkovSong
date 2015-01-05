/*
 * This program imports MIDI songs, which are loaded into a Markov Chain.
 * It then plays a randomly generated song based on note patterns.
 * The song is written into a .mid file so it can be listened to later.
 * 
 * @author Daniel Zhu
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MarkovController {

	private static final int CHAIN_DEPTH = 8;

	public static void main(String[] args) {
		MarkovChain<Integer, Note[]> noteChain = new MarkovChain<Integer, Note[]>();
		SongReader songReader = new SongReader();
		
		songReader.loadSong("bin/music/bwv772.mid", noteChain, CHAIN_DEPTH);
		songReader.loadSong("bin/music/bwv773.mid", noteChain, CHAIN_DEPTH);
		songReader.loadSong("bin/music/bwv774.mid", noteChain, CHAIN_DEPTH);
		songReader.loadSong("bin/music/bwv775.mid", noteChain, CHAIN_DEPTH);
		songReader.loadSong("bin/music/bwv776.mid", noteChain, CHAIN_DEPTH);

		System.out.println("Do you want to record? Enter a file name or type \"no\".");
		InputStreamReader inputReader = new InputStreamReader(System.in);
		BufferedReader bufferedReader = new BufferedReader(inputReader);
		try {
			String fileOutputName = bufferedReader.readLine();
			

			SongWriter songWriter = new SongWriter(fileOutputName);
			new MarkovPlayer(noteChain, CHAIN_DEPTH, songWriter);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
