import java.util.*;

public class Stopwords {
    final private String STOPWORDSFILENAME = "lib/Stopwords.txt";
    private String[] stopwordsArray;

    public Stopwords() {
        this.stopwordsArray = DocumentReaderAndWriter.readFile(STOPWORDSFILENAME).toString().split(" ");

    }

    public String[] getStopwordsArray() {
        for (int i = 0; i < stopwordsArray.length; i++) {
            System.out.println(stopwordsArray[i]);
        }
        return stopwordsArray;
    }

    public String[] getStopwordsArray(String filename){
        StringBuffer stopwordsBuffer  = DocumentReaderAndWriter.readFile(filename);
        stopwordsArray = stopwordsBuffer.toString().split(" ");
        return stopwordsArray;
    }

    public String[] getStopwordsArray(StringBuffer stringBuffer){
        stopwordsArray = stringBuffer.toString().split(" ");
        return stopwordsArray;
    }


    public Map removeStopwords(Map tokenList){
        Iterator iterator = tokenList.entrySet().iterator();
        while (iterator.hasNext()){
            HashMap.Entry pair = (HashMap.Entry) iterator.next();
            for(int i = 0;i < stopwordsArray.length; i++){
                if(pair.getKey().equals(stopwordsArray[i])){
                    iterator.remove();
                    break;
                }
            }
        }
        return tokenList;
    }

    public List removeStopwords(List tokenList){
        Iterator iterator = tokenList.iterator();
        while (iterator.hasNext()){
            String term = iterator.next().toString();
            for(int i = 0;i < stopwordsArray.length; i++){
                if(term.equals(stopwordsArray[i])){
                    iterator.remove();
                    break;
                }
            }
        }
        return tokenList;
    }
}
