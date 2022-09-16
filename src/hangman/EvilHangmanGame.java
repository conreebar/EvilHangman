package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{
    //set of words left that are possible to guess, initally all words in the dictionary
    private Set<String> wordSet = new TreeSet<>();
    private Set<String> lettersUsed = new TreeSet<>();
    private String word;

    public String getFakeWord(){
        for(String word: wordSet){
            return word.toString();
        }
        return null;
    }
    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        //set all words in wordSet to the ones in the dictionary that are the current length
        Scanner sc = new Scanner(dictionary);
        String str = "";
        wordSet.clear();
        lettersUsed.clear();

        StringBuilder sb = new StringBuilder();
        for (int iter = 0; iter < wordLength; iter++){
            sb.append('_');
        }
        word = sb.toString();

        if(wordLength == 0){
            System.out.println("Throw error");
            throw new EmptyDictionaryException("Wordlength is Zero");
        }
        if(!sc.hasNext()){
            throw new EmptyDictionaryException("Empty dictionary");
        }
        while(sc.hasNext()){
            str = sc.nextLine();
            if(str.length() == wordLength){
                wordSet.add(str);
            }
        }
        if(wordSet.isEmpty()){
            throw new EmptyDictionaryException("No valid words in length");
        }
        sc.close();
        //System.out.println(wordSet.toString());
    }

    private String getSubsetKey(String str, char guess){
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
        if(partition.get(bestSubsetKey).size() > partition.get(ob).size()){
            return bestSubsetKey;
        }
        //case equal
        if(partition.get(bestSubsetKey).size() == partition.get(ob).size()){
            //case same (for how i have loop set up)
            if(bestSubsetKey == ob){
                return ob;
            }
            //start counting for equal cases
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
            if(bestCount == 0){
                return bestSubsetKey;
            }
            if(obCount == 0){
                return ob;
            }
            if(bestCount < obCount){
                return bestSubsetKey;
            }
            if(bestCount > obCount){
                return ob;
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

        //first check if guess already made
        guess = Character.toLowerCase(guess);
        for(String ob: lettersUsed){
            if(guess == ob.charAt(0)){
                throw new GuessAlreadyMadeException("Already used letter");
            }
        }
        lettersUsed.add(String.valueOf(guess));

        //map of sets of strings making up partition
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
            //System.out.println("Comparing" + bestSubsetKey + " AND " + ob);
            bestSubsetKey = compareSubsets(bestSubsetKey, ob, partition, guess);
        }
        wordSet = partition.get(bestSubsetKey);
        System.out.println("All possible are:" + wordSet.toString());
        updateWord(bestSubsetKey, guess);
        return partition.get(bestSubsetKey);
    }
    //takes the subset key and changes all the characters to the new one
    private void updateWord(String bestSubsetKey, char guess){
        StringBuilder sb = new StringBuilder(word);
        int count = 0;
        for(int iter = 0; iter < bestSubsetKey.length(); iter++){
            if(bestSubsetKey.charAt(iter) != '_'){
                sb.replace(iter, iter+1, bestSubsetKey.substring(iter, iter+1));
                count++;
            }
        }
        if(count == 0){
            System.out.println("Sorry, there are no " + guess + "'s");
        }
        else{
            System.out.println("Yes, there is " + count + " " + guess);
        }

        word = sb.toString();
    }
    public String getWord(){
        return word;
    }
    @Override
    public SortedSet<Character> getGuessedLetters() {
        SortedSet<Character> temp = new TreeSet<>();
        for(String ob: lettersUsed){
            temp.add(ob.charAt(0));
        }
        return temp;
    }

}
