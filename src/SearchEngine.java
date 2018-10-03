import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

public class SearchEngine {
    Map<String, String> printlist = new HashMap();

    //Main function of indexing
    @Test
    public void indexing(String directoryName,String locationPath,String stopwordsPath) {
        File directory = new File(directoryName);
        File storeFile = new File(locationPath);
        ArrayList filenameList;
        if (directory.isDirectory()) {
            if(storeFile.isFile()){
                UserInterface.isDirectory();
                filenameList = DocumentReaderAndWriter.readCollection(directoryName);
                Iterator iterator = filenameList.iterator();
                while (iterator.hasNext()) {
                    indexFile(iterator.next().toString());
                }
                IdfAndCosCalculater idfAndCosCalculater = new IdfAndCosCalculater();
                int N = IdfAndCosCalculater.calculateN(directoryName);
                for (Map.Entry<String,String> printlistEntry:printlist.entrySet()){
                    int df = idfAndCosCalculater.calculateNumofDoc(printlistEntry.getValue());
                    printlistEntry.setValue(printlistEntry.getValue()+","+idfAndCosCalculater.calculateIdf(N,df));
                }
                DocumentReaderAndWriter.writeFile(printlist,locationPath);
                UserInterface.finishIndexing();
            }else{
                UserInterface.fileDoesNotExist();
            }
        }else{
            UserInterface.notDirectory();
        }
    }

    //Indexing single file
    public void indexFile(String filepath) {
        Map wordlist = new HashMap();
        //token first
        Tokenizer tokenizer = new Tokenizer();
        wordlist = tokenizer.tokenize(filepath,wordlist);

        //then remove stop words
        Stopwords stopwords = new Stopwords();
        wordlist = stopwords.removeStopwords(wordlist);

        //then stem
        StemmerTool stemmer = new StemmerTool();
        wordlist = stemmer.stemHashMap(wordlist);

        //then normalize
        wordlist = Normalizer.normalize(wordlist);
        if (printlist.isEmpty()){
            printlist = immigrateHashMap(wordlist,printlist,filepath);
        }else {
            printlist = appendHashMap(wordlist,printlist,filepath);
        }
        System.out.println(printlist);
    }

    //process the map
    public static Map appendHashMap(Map<String, Double> wordlist,Map<String,String> printlist,String filepath){
        for (Map.Entry<String, Double> wordlistEntry : wordlist.entrySet()) {
            HashMap tempHashmap = new HashMap();
            Boolean hasOrNot = false;
            for(Map.Entry<String,String> printlistEntry: printlist.entrySet()){
                if(wordlistEntry.getKey().equals(printlistEntry.getKey())) {
                    hasOrNot = true;
                    printlistEntry.setValue(printlistEntry.getValue() + ",d" + DocumentReaderAndWriter.getFileName(filepath) + "," + wordlistEntry.getValue());
                }
            }
            if(hasOrNot == false){
                tempHashmap.put(wordlistEntry.getKey(),"d"+DocumentReaderAndWriter.getFileName(filepath)+","+wordlistEntry.getValue());
            }
            printlist.putAll(tempHashmap);
        }
        return printlist;
    }

    //process the map
    public static Map immigrateHashMap(Map<String, Double> wordlist, Map<String,String> printlist, String filepath){
        for (Map.Entry<String, Double> entry : wordlist.entrySet()) {
            printlist.put(entry.getKey(),"d"+DocumentReaderAndWriter.getFileName(filepath)+","+entry.getValue());
        }
        return printlist;
    }

    //main function of searching
    @Test
    public void searching(String directoryName,String query) {
        File directory = new File(directoryName);
        if (directory.isDirectory()){
            if (!query.trim().isEmpty()){
                Map doctermTfMap;
                //Process query
                Map<String,Double> queryMap  = changeQuerytoMap(query);
                queryMap = processMap(queryMap);

                //read text file generate map list
                Map indexFileMap = DocumentReaderAndWriter.readIndexFile("/Users/decheng/Desktop/test.txt");
                //Get the query map
                queryMap = IdfAndCosCalculater.calculateQueryIdf(queryMap,indexFileMap);

                //Get the Map:{docid:{term:tf}}
                doctermTfMap = getFileTermAndTF(directoryName);

                Map searchResult = IdfAndCosCalculater.calculateCosSimi(queryMap,doctermTfMap,indexFileMap);
                UserInterface.printResult(searchResult);
            }else{
                UserInterface.queryIsEmpty();
            }
        }else{
            UserInterface.notDirectory();
        }

    }

    //get term and term frequency
    public Map getFileTermAndTF(String directoryName){
        Map doctermTfMap = new HashMap();
        ArrayList<String> filenameList = DocumentReaderAndWriter.readCollection(directoryName);
        Iterator iterator = filenameList.iterator();
        while (iterator.hasNext()) {
            String filename = iterator.next().toString();
            Map wordlist = new HashMap();
            //token first
            Tokenizer tokenizer = new Tokenizer();
            wordlist = tokenizer.tokenize(filename,wordlist);
            // then remove stop words
            Stopwords stopwords = new Stopwords();
            wordlist = stopwords.removeStopwords(wordlist);

            //then stem
            StemmerTool stemmer = new StemmerTool();
            wordlist = stemmer.stemHashMap(wordlist);

            //then normalize
            wordlist = Normalizer.normalize(wordlist);
            doctermTfMap.put(DocumentReaderAndWriter.getFileName(filename),wordlist);
        }
        return doctermTfMap;
    }

    //process the query and change the format from String to Map
    public static Map changeQuerytoMap(String query){
        String[] queryArray = query.split(" ");
        Map<String,Double> queryMap = new HashMap();
        for(String term : queryArray){
            double count = queryMap.containsKey(term) ? queryMap.get(term) : 0;
            queryMap.put(term,(double)count+1);
        }
        return queryMap;
    }

    // Token, remove stopwords ,stem and normalize
    public Map processMap(Map<String,Double> wordlist){
        Tokenizer tokenizer = new Tokenizer();
        wordlist = tokenizer.tokenQuery(wordlist);

        // then remove stop words
        Stopwords stopwords = new Stopwords();
        wordlist = stopwords.removeStopwords(wordlist);

        //then stem
        StemmerTool stemmer = new StemmerTool();
        wordlist = stemmer.stemHashMapDouble(wordlist);

        //then normalize
        wordlist = Normalizer.normalize(wordlist);
        return wordlist;
    }
}
