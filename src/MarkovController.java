
public class MarkovController {

	private static final int CHAIN_DEPTH = 10;

	public static void main(String[] args) {
		MarkovChain<Integer, Note[]> noteChain = new MarkovChain<Integer, Note[]>();
		SongReader songReader = new SongReader();
		
		songReader.loadSong("bin/music/bwv772.mid", noteChain, CHAIN_DEPTH);
		songReader.loadSong("bin/music/bwv773.mid", noteChain, CHAIN_DEPTH);
		songReader.loadSong("bin/music/bwv774.mid", noteChain, CHAIN_DEPTH);
		songReader.loadSong("bin/music/bwv775.mid", noteChain, CHAIN_DEPTH);
		songReader.loadSong("bin/music/bwv776.mid", noteChain, CHAIN_DEPTH);

		new MarkovPlayer(noteChain, CHAIN_DEPTH);
	}
}
