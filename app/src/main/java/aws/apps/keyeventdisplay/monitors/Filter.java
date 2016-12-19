package aws.apps.keyeventdisplay.monitors;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/*package*/ class Filter {
    private static final String TAG = Filter.class.getSimpleName();

    private final Set<String> filteredWords;

    public Filter(final String[] wordlist) {
        filteredWords = new HashSet<String>();
        loadWordList(wordlist);
    }

    public String getFilterWords() {
        final StringBuilder sb = new StringBuilder();

        for (final String s : filteredWords) {
            sb.append(s);
            sb.append('\n');
        }

        return sb.toString();
    }

    public boolean isValidLine(String line) {
        for (final String s : filteredWords) {
            if (line.toLowerCase().contains(s)) {
                return true;
            }
        }
        return false;
    }

    private void loadWordList(String[] wordArray) {
        Log.i(TAG, "Loading word list. Length: " + wordArray.length);

        for (String s : wordArray) {
            filteredWords.add(s.toLowerCase());
        }

        Log.i(TAG, "Set length: " + filteredWords.size());
    }
}
