package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class EvilHangman {

    public static void main(String[] args) throws EmptyDictionaryException, IOException, GuessAlreadyMadeException {
        EvilHangmanGame eh = new EvilHangmanGame();
        String dicString = args[0];
        File file = new File(dicString);
        int length = 8;
        eh.startGame(file, length);
        eh.theGame();
    }

}
