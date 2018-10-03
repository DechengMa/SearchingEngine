import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DocumentReaderAndWriter {
    static ArrayList<String> filenameList = new ArrayList();

    public static Map readIndexFile(String fileName){
        Map indexFileMap = new HashMap();
        BufferedReader reader = null;
        try {
            File file = new File(fileName);
            reader = new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {
                indexFileMap.put(line.substring(0,line.indexOf(',')),line.substring(line.indexOf(',')+1,line.length()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return indexFileMap;
    }

    public static StringBuffer readFile(String fileName){
        BufferedReader reader = null;
        StringBuffer stringBuffer = new StringBuffer("");
        try {
            File file = new File(fileName);
            reader = new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append(" ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuffer;
    }

    public static void addFilename(Path filename){
        filenameList.add(filename.toString());
    }

    @Test
    public static ArrayList<String> readCollection(String directoryName){
        try {
            Files.walk(Paths.get(directoryName))
                    .filter(p -> p.toString().endsWith(".txt"))
                    .forEach(DocumentReaderAndWriter::addFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filenameList;
    }

    public static String getFileName(String filepath) {
        File f= new File(filepath);
        String filename = f.getName().substring(0,f.getName().lastIndexOf("."));
        return filename;
    }


    public static void writeFile(Map<String, String> wordList,String locationPath) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(locationPath));

            for (Map.Entry<String, String> entry : wordList.entrySet())
            {
                out.write(entry.getKey()+","+entry.getValue()+"\n");
            }
            System.out.println("Finish write!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(out != null){
                    out.close();
                } else {
                    System.out.println("Buffer has not been initialized!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
