package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class EvilHangman {

    public static void main(String[] args) throws EmptyDictionaryException, IOException, GuessAlreadyMadeException {
        EvilHangmanGame eh = new EvilHangmanGame();
        String dicString = args[0];
        int length = Integer.parseInt(args[1]);
        int guessesLeft = Integer.parseInt(args[2]);
        File file = new File(dicString);
        Scanner input = new Scanner(System.in);

        eh.startGame(file, length);
        while(guessesLeft > 0){
            System.out.println("You have " + guessesLeft + " guesses left");
            System.out.printf("Used letters:");
            for(int iter = 0; iter < eh.getGuessedLetters().size(); iter++){
                System.out.printf(eh.getGuessedLetters().toArray()[iter].toString()+ " ");
            }
            System.out.println("\nYour word is: "+ eh.getWord());
            System.out.println("Enter a single char as a guess");
            String guessedChar = input.next();

            try {
                char c = guessedChar.charAt(0);
                if (guessedChar.length()!=1) {
                    throw new GuessAlreadyMadeException("too many inputs");
                }
                else if (!Character.isLetter(c)){
                    throw new GuessAlreadyMadeException("Wrong kind of character");
                }
                else if(Character.isLetter(c)){
                    int count = 0;
                    for(int iter = 0; iter < eh.getWord().length(); iter++){
                        if(eh.getWord().charAt(iter) == '_'){
                            count++;
                        }
                    }
                    eh.makeGuess(guessedChar.charAt(0));
                    int count2 = 0;
                    for(int iter = 0; iter < eh.getWord().length(); iter++){
                        if(eh.getWord().charAt(iter) == '_'){
                            count2++;
                        }
                    }
                    if(count == count2){
                        guessesLeft--;
                    }
                    System.out.println("You guessed: " + guessedChar.charAt(0));
                }
            }
            catch(GuessAlreadyMadeException ex) {
                System.out.println(ex.getMessage());
            }

            if(!eh.getWord().contains("_")){
                System.out.println("YOU WIN!!!!!! Word is "+ eh.getWord());
                String wait = input.next();
                return;
            }
        }
        System.out.println("Ran out of Guesses, GAME OVER!!!!!");
        System.out.println("the word was " + eh.getFakeWord());
        String wait = input.next();
    }
}
