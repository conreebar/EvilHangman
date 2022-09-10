package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class EvilHangman {

    public static void main(String[] args) throws EmptyDictionaryException, IOException, GuessAlreadyMadeException {
        EvilHangmanGame eh = new EvilHangmanGame();
        String dicString = args[0];
        File file = new File(dicString);
        Scanner input = new Scanner(System.in);

        int length;
        int guessesLeft;
        try{
            System.out.println("Enter the word length: (Int only)");
            length = Integer.parseInt(input.next());

            System.out.println("Enter the amount of guesses:");
            guessesLeft = Integer.parseInt(input.next());

        }catch (NumberFormatException ex) {
            System.out.println("Number of guesses or word length is incorrect. Enter an appropriate number of guesses");
            return;
        }

        eh.startGame(file, length);
        while(guessesLeft > 0){
            System.out.println("You have " + guessesLeft + " guesses left");
            System.out.printf("Used letters:");
            for(int iter = 0; iter < eh.getGuessedLetters().size(); iter++){
                System.out.printf(eh.getGuessedLetters().toArray()[iter].toString()+ " ");
            }
            System.out.println("\nYour word is: "+ eh.getWord());
            guessesLeft--;
            System.out.println("Enter a single char as a guess");
            String guessedChar = input.next();

            try {
                if (guessedChar.length()!=1) {
                    throw new GuessAlreadyMadeException();
                }
                eh.makeGuess(guessedChar.charAt(0));
            }
            catch(GuessAlreadyMadeException ex) {
                System.out.println("You already guessed this letter");
            }

            System.out.println("You guessed: " + guessedChar.charAt(0));
            if(!eh.getWord().contains("_")){
                System.out.println("YOU WIN!!!!!!");
                String wait = input.next();
                return;
            }
        }
        System.out.println("Ran out of Guesses, GAME OVER!!!!!");
        String wait = input.next();
    }
}
