MarkovSong
==============
This application creates a Markov Chain-based MIDI song.
Users can load sample MIDI songs in MarkovController.java.
The program then creates a Markov Chain that maps a subsequent pitch to a first pitch, creating an ArrayList of all the notes in the loaded songs that follow a pitch. Users can define how many pitches are played based on a seed pitch, which means that an ArrayList of the subsequent notes following a pitch will be saved.
