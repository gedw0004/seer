package correlator;

import common.LoggerObject;

import io.github.crew102.rapidrake.RakeAlgorithm;
import io.github.crew102.rapidrake.data.SmartWords;
import io.github.crew102.rapidrake.model.RakeParams;
import io.github.crew102.rapidrake.model.Result;

import java.io.IOException;

/**
 * Class to calculate keyword correlations between two strings
 * @author Gareth Edwards
 * @version 0.0.1
 */
public class TextCorrelator extends LoggerObject {
    private String[] stopWords;
    private String[] stopPos;
    private String posTagURL;
    private String senDetURL;
    private String delims;

    private int minWord;
    boolean doStem;

    /**
     * Default constructor
     */
    public TextCorrelator() throws IOException {
        super();
        stopWords = new SmartWords().getSmartWords();
        stopPos = new String[]{"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
        posTagURL = "model-bin/en-pos-maxent.bin";
        senDetURL = "model-bin/en-sent.bin";
        minWord = 1;
        doStem = true;
        delims = "[-,.?():;\"!/]";
    }

    /**
     * Constructor
     * @param stopWords Stop words
     * @param stopPos   Stop pos
     * @param posTagURL URL for pos tagger library
     * @param senDetURL URL for sentence detection library
     * @param delims    Delimiters
     * @param minWord   Minimum word size
     * @param doStem    True if stemming required
     */
    public TextCorrelator(String[] stopWords,
                          String[] stopPos,
                          String posTagURL,
                          String senDetURL,
                          String delims,
                          int minWord,
                          boolean doStem) throws IOException {
        this();
        this.stopWords = stopWords;
        this.stopPos = stopPos;
        this.posTagURL = posTagURL;
        this.senDetURL = senDetURL;
        this.delims = delims;
        this.minWord = minWord;
        this.doStem = doStem;
    }

    /**
     * Compute the percentage keyword correlation for two strings
     * @param txtA  First text to be analysed
     * @param txtB  Second text to be analysed
     * @return Percentage keyword correlation
     */
    public double correlateKeywords(String txtA, String txtB) {
        double keyMatch = 0.0;
        double stemMatch = 0.0;
        double fullMatch = 0.0;

        Result resA;
        Result resB;

        resA = getKeyWords(txtA);
        resB = getKeyWords(txtB);

        if (resA != null && resB != null) {
            keyMatch = wordCompare(resA.getFullKeywords(), resB.getFullKeywords());
            logInfo("Keyword match (unscored) = " + keyMatch);

            if (doStem) {
                stemMatch = wordCompare(resA.getStemmedKeywords(), resB.getStemmedKeywords());
                logInfo("Stem match (unscored) = " + stemMatch);
            }
        }

        fullMatch = doStem ? (keyMatch + stemMatch) / 2 : keyMatch;
        logInfo("Combined match (unscored) = " + fullMatch);

        return fullMatch;
    }

    /**
     * Get the keywords for a string
     * @param txt  Text to be analysed
     * @return  Analysis result
     * @throws IOException
     */
    public Result getKeyWords(String txt) {
        Result result = null;
        RakeParams params = new RakeParams(stopWords, stopPos, minWord, doStem, delims);

        try {
            logInfo("Executing Rake algorithm for: '" + txt + "'");
            RakeAlgorithm algorithm = new RakeAlgorithm(params, posTagURL, senDetURL);
            result = algorithm.rake(txt);
        }
        catch (IOException e) {
            logErr("Failed to create Rake algorithm object: " + e.getMessage());
        }

        return result;
    }

    /**
     * Compute the percentage similarity between two lists of words
     * @param wordsA    Word list A
     * @param wordsB    Word list B
     * @return Percentage similarity
     */
    private double wordCompare(String[] wordsA, String[] wordsB) {
        int matches = 0;

        String[] wordsBig;
        String[] wordsSml;

        if (wordsA.length >= wordsB.length) {
            wordsBig = wordsA;
            wordsSml = wordsB;
        }
        else {
            wordsBig = wordsB;
            wordsSml = wordsA;
        }

        for (String wordBig : wordsBig)
            for (String wordSml : wordsSml)
                if (wordBig.equalsIgnoreCase(wordSml))
                    matches++;

        return wordsBig.length > 0 ? (double) matches / (double) wordsBig.length : 0.0;
    }

    public static void main(String[] args) {
        try {
            TextCorrelator tc = new TextCorrelator();
            tc.correlateKeywords("Users are unable to connect to WVD (Prod and QA) - Global",
                                 "Unable to connect to WVD - APAC");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //region Set and Get Methods
    public String[] getStopWords() {
        return stopWords;
    }

    public void setStopWords(String[] stopWords) {
        this.stopWords = stopWords;
    }

    public String[] getStopPos() {
        return stopPos;
    }

    public void setStopPos(String[] stopPos) {
        this.stopPos = stopPos;
    }

    public String getPosTagURL() {
        return posTagURL;
    }

    public void setPosTagURL(String posTagURL) {
        this.posTagURL = posTagURL;
    }

    public String getSenDetURL() {
        return senDetURL;
    }

    public void setSenDetURL(String senDetURL) {
        this.senDetURL = senDetURL;
    }

    public String getDelims() {
        return delims;
    }

    public void setDelims(String delims) {
        this.delims = delims;
    }

    public int getMinWord() {
        return minWord;
    }

    public void setMinWord(int minWord) {
        this.minWord = minWord;
    }
    //endregion
}
