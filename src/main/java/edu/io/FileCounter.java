package edu.io;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileCounter {

    private String[] fileTypeDisplay;
    private String[] fileRegexes;
    private int[] fileCount;
    private long[] lineCount;
    private ArrayList<ArrayList<String>> filesFound;

    public FileCounter(){
        fileTypeDisplay = new String[] {
                "Java source files",
                "Text files",
                "Gradle files"
        };
        fileRegexes = new String[] {
                ".+\\.java$",
                ".+\\.txt$|.+\\.md$",
                ".*gradle.*"
        };
        fileCount = new int[fileRegexes.length];
        lineCount = new long[fileRegexes.length];
        filesFound = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < fileRegexes.length; i++) {
            filesFound.add(new ArrayList<String>());
        }

        if(fileRegexes.length != fileTypeDisplay.length)
            throw new RuntimeException("Wrong configuration of file counting");
    }

    public boolean matchesAnyType(IFileComposite fileComposite){
        String fileName = fileComposite.fileName();
        for (int i = 0; i < fileRegexes.length; i++) {
            Pattern pattern = Pattern.compile(fileRegexes[i], Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(fileName);
            if(matcher.find()) {
                return true;
            }
        }
        return false;
    }

    // counting file types
    public void checkAndCountFile(IFileComposite fileComposite){
        String fileName = fileComposite.fileName();

        for (int i = 0; i < fileRegexes.length; i++) {
            Pattern pattern = Pattern.compile(fileRegexes[i], Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(fileName);
            if(matcher.find()) {
                fileCount[i]++;
                lineCount[i] += fileComposite.lineLength();
                filesFound.get(i).add(fileName);
            }
        }
    }

    public int countedTypes() {
        return fileTypeDisplay.length;
    }

    public String getDisplay(int index){
        return fileTypeDisplay[index];
    }

    public ArrayList<String> getFilesOfType(int index){
        return filesFound.get(index);
    }

    public long getLineCount(int index) {
        return lineCount[index];
    }

    public int getFileCount(int index){
        return fileCount[index];
    }
}
