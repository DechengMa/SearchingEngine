import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import java.util.stream.Collectors;

public class IdfAndCosCalculater {
    static int filenumber = 0;
    public int calculateNumofDoc(String docString){
        int numOfDoc = 0;
        for (char c : docString.toCharArray()){
            if (c == 'd'){
                numOfDoc += 1;
            }
        }
        return numOfDoc;
    }

    public static double calculateQueryDivisor(Map<String,Double> queryMap){
        double queryDivisor = 0.0;
        for (Map.Entry<String,Double> queryMapEntry:queryMap.entrySet()){
            queryDivisor = queryDivisor + Math.pow(queryMapEntry.getValue(),2);
        }
        return Math.sqrt(queryDivisor);
    }

    public static Map calculateCosSimi(Map<String,Double> queryMap,Map<String,Map<String,Integer>> docTermTfMap,Map<String,String> indexListMap){

        Map finalResult = new HashMap();

        double queryDivisor = 0.0;
//        double docDivisor = 0.0;
        // Cos similarity : cosSimiDividend/cosSimiDivisor
        queryDivisor = calculateQueryDivisor(queryMap);
        System.out.println(queryDivisor);
        for (Map.Entry<String,Map<String,Integer>> docTermTfMapEntry:docTermTfMap.entrySet()){
            double docDivisor = 0.0;
            double cosSimiDividend = 0.0;
            for (Map.Entry<String,Double> queryMapEntry:queryMap.entrySet()){
                for(Map.Entry<String,Integer> termAndTFEntry : docTermTfMapEntry.getValue().entrySet()){

                    if(queryMapEntry.getKey().equals(termAndTFEntry.getKey())){

                        cosSimiDividend = cosSimiDividend +  Math.pow(queryMapEntry.getValue(),termAndTFEntry.getValue());
//                        docDivisor = docDivisor + Math.pow(getTermIdf(termAndTFEntry.getKey(),indexListMap) * termAndTFEntry.getValue(),2);
                    }
                }
            }
            docDivisor = getDocDivsor(docTermTfMapEntry.getKey(),docTermTfMap,indexListMap);
            double cos = cosSimiDividend/docDivisor * queryDivisor;
            finalResult.put(docTermTfMapEntry.getKey(),cos);
        }

        finalResult = sortByValue(finalResult);
        return finalResult;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static double getDocDivsor(String docName,Map<String,Map<String,Integer>> docTermTfMap,Map<String,String> indexListMap ){
        double docDivisor = 0.0;
        for (Map.Entry<String,Map<String,Integer>> docTermTfEntry:docTermTfMap.entrySet()){
            if(docTermTfEntry.getKey().equals(docName)){
                for(Map.Entry<String,Integer> termAndTFEntry : docTermTfEntry.getValue().entrySet()){
                    docDivisor = docDivisor + Math.pow(getTermIdf(termAndTFEntry.getKey(), indexListMap) * termAndTFEntry.getValue(),2) ;
                }
            }

        }
        return Math.sqrt(docDivisor);
    }

    public static double getTermIdf(String term,Map<String,String> indexListMap){
        double termIdf = 0.0;
        for(Map.Entry<String,String> printMapEntry: indexListMap.entrySet()){
            if(printMapEntry.getKey().equals(term)){
                termIdf = Double.parseDouble(printMapEntry.getValue().substring(printMapEntry.getValue().length()-5,printMapEntry.getValue().length())) ;
            }
        }
        return termIdf;
    }

    public static Map calculateQueryIdf(Map<String,Double> queryMap,Map<String,String> indexListMap){
//        ArrayList<Double> queryTermIdfList = new ArrayList<>();
        for (Map.Entry<String,Double> queryMapEntry : queryMap.entrySet()){
            for(Map.Entry<String,String> printMapEntry: indexListMap.entrySet()){
                if(printMapEntry.getKey().equals(queryMapEntry.getKey())){
                    double QueryTermIdf = Double.parseDouble(printMapEntry.getValue().substring(printMapEntry.getValue().length()-5,printMapEntry.getValue().length())) ;
//                    queryTermIdfList.add(QueryTermIdf)

                    double value = queryMapEntry.getValue() ;
                    double multipleResult = QueryTermIdf * value;
                    queryMap.put(queryMapEntry.getKey(), multipleResult);
                }
            }
        }
        return queryMap;
    }

    public static void getfileNumberTool(Path filepath){
        filenumber = filenumber + 1;
    }

    public static int calculateN(String directoryName){
        try {
            Files.walk(Paths.get(directoryName))
                    .filter(p -> p.toString().endsWith(".txt"))
                    .forEach(IdfAndCosCalculater::getfileNumberTool);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filenumber;
    }

    public double calculateIdf (int N, int df){
        double idf = Math.log(N/df+1);
        idf = (double)Math.round(idf * 1000d) / 1000d;
        return idf;
    }
}
