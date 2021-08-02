package com.example.menstrualproductlocator.ChatSearchAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatSearchAlgorithm {

    /**
     * Searches through chatMessage and looks for a textToFind to find
     *
     * @param textToFind the desired text to look for
     * @param chatMessage the whole chatMessage being searched
     * @return list of indexes of where the desired text to find begins in the chatMessage
     */
    public static List<Integer> searchInChat(CharSequence textToFind,
                                           CharSequence chatMessage) {

        List<Integer> startingIndexList = new ArrayList<>();

        if (textToFind.length() > chatMessage.length()) { //textToFind is longer than the section of chatMessage being searched, so it's guaranteed to not be in there
            return startingIndexList;
        }

        Map<Character, Integer> lastTable = createLastOccurrenceTable(textToFind);
        int start = 0;
        int end = 0;

        while (start <= chatMessage.length() - textToFind.length()) {
            end = textToFind.length() - 1;
            while (end >= 0 && chatMessage.charAt(start + end) == textToFind.charAt(end)) {
                end = end - 1;
            }
            if (end == -1) {
                startingIndexList.add(start);
                start++;
            } else {
                int shift = lastTable.getOrDefault(chatMessage.charAt(start + end), -1); //shift searching over a few characters to skip chatMessage
                if (shift < end) {
                    start = start + (end - shift);
                } else {
                    start = start + 1;
                }
            }
        }
        return startingIndexList;

    }

    /**
     * Preprocesses the desired text to find in order to skip sections of text for efficiency
     *
     * @param textToFind the desired text to look for
     * @return map with keys of the characters in the textToFind to their last occurrence index in the textToFind
     */
    public static Map<Character, Integer> createLastOccurrenceTable(CharSequence textToFind) {

        int textLength  = textToFind.length();
        HashMap<Character, Integer> lastOccurenceTable = new HashMap<>();
        for (int index = 0; index < textLength; index++) {
            lastOccurenceTable.put(textToFind.charAt(index), index);
        }
        return lastOccurenceTable;
    }
}
