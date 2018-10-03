import java.util.*;

public class StemmerTool {

    public List stemTermList(List termList){
        List newTermList = new ArrayList();
        Stemmer stemmer =  new Stemmer();
        Iterator iterator = termList.iterator();
        while(iterator.hasNext()){
            char[] charArray = iterator.next().toString().toCharArray();
            for (char a : charArray){
                stemmer.add(a);
            }
            stemmer.stem();
            String stemmedTerm = stemmer.toString();
            newTermList.add(stemmedTerm);
        }

        return newTermList;
    }

    public HashMap stemHashMapDouble(Map wordList){
        HashMap<String,Double> newWordList = new HashMap();

        Stemmer stemmer = new Stemmer();
        Iterator iterator = wordList.entrySet().iterator();
        while(iterator.hasNext())
        {
            HashMap.Entry pair = (HashMap.Entry) iterator.next();
            char[] charArray = pair.getKey().toString().toCharArray();

            for (char a : charArray) {
                stemmer.add(a);
            }
            stemmer.stem();
            String key = stemmer.toString();

            newWordList.merge(key, (double)pair.getValue(), (a, b) -> a + b);
        }
        return newWordList;
    }

    public HashMap stemHashMap(Map wordList){
        HashMap<String,Integer> newWordList = new HashMap();

        Stemmer stemmer = new Stemmer();
        Iterator iterator = wordList.entrySet().iterator();
        while(iterator.hasNext())
        {
            HashMap.Entry pair = (HashMap.Entry) iterator.next();
            char[] charArray = pair.getKey().toString().toCharArray();

            for (char a : charArray) {
                stemmer.add(a);
            }
            stemmer.stem();
            String key = stemmer.toString();

            newWordList.merge(key, (int)pair.getValue(), (a, b) -> a + b);
        }
        return newWordList;
    }
}
