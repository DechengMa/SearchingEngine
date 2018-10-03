import org.junit.jupiter.api.Test;

import javax.print.Doc;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private final String URL = "http(s)?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
    private final String EMAIL = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    private final String EDUEMAIL = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.+-]+\\.edu";
    private final String HYPHENATEDWORD = "(?<!-)\\b(\\w+\\-\\w+)\\b(?!-)";
    private final String IPADDRESS = "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))";
    private final String QUOTATION = "\\s\\'[[A-Za-z0-9]+\\s?]+\\'\\s";
    private final String ACRONYM = "\\b[A-Z](\\.[A-Z])+\\.?";
    private final String SPLIT = "[ {.,:;”’'\"()?!}]";

    @Test
    public void testRegex(){
        int x = 1;
        double y = 2.555;
        double mult = y * x;
        System.out.println(mult);
    }

    public Map tokenQuery(Map<String,Double> tokenList){
        ArrayList<String> regexList = new ArrayList<>();
        Map<String,Double> newtokenList = new HashMap<>();
        regexList.add(URL);
        regexList.add(EMAIL);
        regexList.add(EDUEMAIL);
        regexList.add(HYPHENATEDWORD);
        regexList.add(IPADDRESS);
        regexList.add(QUOTATION);
        regexList.add(ACRONYM);

        Iterator iterator = tokenList.entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry<String,Integer> pair = (Map.Entry)iterator.next();
            for (String regex : regexList) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(pair.getKey().toString());
                while(matcher.find()){
                    newtokenList.put(matcher.group(0),(double)pair.getValue());
                }
            }
        }

        tokenList.putAll(newtokenList);
        return tokenList;
    }

    @Test
    public Map tokenize(String filepath,Map<String,Integer> tokenList){
        HashMap<String,Integer> docAndTfMap = new HashMap<>();
        StringBuffer stringBuffer = DocumentReaderAndWriter.readFile(filepath);
        String filename = DocumentReaderAndWriter.getFileName(filepath);
        String fileString = stringBuffer.toString();

        ArrayList<String> regexList = new ArrayList<>();
        regexList.add(URL);
        regexList.add(EMAIL);
        regexList.add(EDUEMAIL);
        regexList.add(HYPHENATEDWORD);
        regexList.add(IPADDRESS);
        regexList.add(QUOTATION);
        regexList.add(ACRONYM);

        for (String regex : regexList) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(stringBuffer);

            while(matcher.find()){
                if (tokenList.get(matcher.group(0)) != null){
                    String key = matcher.group(0);
                    tokenList.put(key,tokenList.get(key)+1);
                }else {
                    tokenList.put(matcher.group(0),1);
                }
                fileString = fileString.replace(matcher.group(0),"");
            }
        }
        System.out.println("finish remove special characters");

        System.out.println("Start split");
        String[] stringArray = fileString.split(SPLIT);
        for (String string : stringArray) {
            if(tokenList.get(string) != null && !string.trim().isEmpty()){
                tokenList.put(string,tokenList.get(string)+1);
            }else if(!string.trim().isEmpty()){
                tokenList.put(string, 1);
            }
        }
        return tokenList;
    }
}
