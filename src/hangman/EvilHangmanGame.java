package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{
    //set of words left that are possible to guess, initally all words in the dictionary
    private Set<String> wordSet = new TreeSet<>();

    public void theGame() throws GuessAlreadyMadeException {
        wordSet = makeGuess('a');
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        //set all words in wordSet to the ones in the dictionary that are the current length
        Scanner sc = new Scanner(dictionary);
        String str = "";
        while(sc.hasNext()){
            str = sc.nextLine();
            if(str.length() == wordLength){
                wordSet.add(str);
            }
        }
        System.out.println(wordSet.toString());
    }

    String getSubsetKey(String str, char guess){
        StringBuilder sb = new StringBuilder();
        sb.delete(0,sb.length());

        for(int iter = 0; iter < str.length(); iter++){
            if(str.charAt(iter) == guess){
                sb.append(guess);
            }
            else{
                sb.append("_");
            }
        }

        return sb.toString();
    }

    private String compareSubsets(String bestSubsetKey, String ob, Map<String,Set<String>> partition, char guess){
        //case current ob is a bigger partition:
        if(partition.get(bestSubsetKey).size() < partition.get(ob).size()){
            return ob;
        }
        //case equal
        if(partition.get(bestSubsetKey).size() == partition.get(ob).size()){
            //case same (for how i have loop set up
            if(bestSubsetKey == ob){
                return ob;
            }
            //case one or the other doesn't have letters
            else if(!bestSubsetKey.contains(""+guess)){
                return bestSubsetKey;
            }
            else if(!ob.contains(""+guess)){
                return ob;
            }

            //case one has one letter the other has two
            int bestCount = 0;
            int obCount = 0;
            for(int iter = 0; iter < ob.length(); iter++){
                if(bestSubsetKey.charAt(iter) == guess){
                    bestCount++;
                }
                if(ob.charAt(iter) == guess){
                    obCount++;
                }
            }
            if(bestCount > obCount){
                return bestSubsetKey;
            }
            if(bestCount < obCount){
                return bestSubsetKey;
            }
            if(bestCount == obCount){
                for(int iter = 0; iter < ob.length(); iter++){
                    //if ob is rightmost, use best
                    if((ob.charAt(iter) == guess) && (bestSubsetKey.charAt(iter) != guess)){
                        return bestSubsetKey;
                    }
                    //if best is rightmost, use other
                    if((ob.charAt(iter) != guess) && (bestSubsetKey.charAt(iter) == guess)){
                        return ob;
                    }
                }
            }
        }
        System.out.println("Compare didn;t work");
        return null;
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        //map of sets of strings making up partition, String is a
        Map<String,Set<String>> partition = new TreeMap<>();
        Set<String> subsetStrings = new TreeSet<>();
        //for each String in wordSet, find where it needs to go in the map
        for(String ob: wordSet){
            String subsetKey = getSubsetKey(ob, guess);
            //System.out.println("Current word is " + ob + " , and current subset key is " + subsetKey);
            //add ob to the partition based on the subsetKey
            if(partition.get(subsetKey) == null){
                partition.put(subsetKey,new TreeSet<String>());
                //System.out.println("made new partition");

                //add it to a set so you can iterate through them later
                subsetStrings.add(subsetKey);
            }
            Set<String> currentPartition = partition.get(subsetKey);
            currentPartition.add(ob);
            //System.out.println(currentPartition.toString());
        }
        //then find the largest partition and make that the new wordSet.
        String bestSubsetKey = subsetStrings.toArray()[0].toString();
        for(String ob: subsetStrings){ //account for all empty maybe??
            bestSubsetKey = compareSubsets(bestSubsetKey, ob, partition, guess);
        }
        System.out.println("Best SubsetKey is " + bestSubsetKey);
        return partition.get(bestSubsetKey);
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return null;
    }

    private void hangman(){

    }
}
