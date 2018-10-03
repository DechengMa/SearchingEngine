import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class UserInterface {
    static Scanner sc = new Scanner(System.in);
    public static String mainPagePrint(){
        System.out.println("Hi! Welcome to use this search engine, what would you like to do ? ");
        System.out.println("1. Index Document");
        System.out.println("2. Search Document");
        System.out.println("3. Exit");
        String choice = sc.nextLine();
        return choice;
    }

    public static void printResult(Map<String,Double> finalResult){
        int count = 1;
        System.out.println("Here is your result !");
        for(Map.Entry<String,Double> entry:finalResult.entrySet()){
            if(count <= 5 && entry.getValue() != 0.0){
                System.out.println(count+ ". Document Name: "+entry.getKey());
                System.out.println("  Cos similarity: "+entry.getValue());
                count +=1;
            }else if(count < 2){
                count += 1;
                System.out.println("Ops! your query didn't match any documents! Can you try something else?");
            }
        }
    }

    public static void finishIndexing(){
        System.out.println("Done!");
    }

    public static void queryIsEmpty(){
        System.out.println("Sorry, query can not be empty!");
    }

    public static void invalidOption(){
        System.out.println("Sorry, this system doesn't have this option");
    }

    public static String getIndexDirectoryPath(){
        System.out.println("Hi, welcome to use index function");
        System.out.println("First, please input the path of the collection:");
        return sc.nextLine();
    }

    public static String getQuery(){
        System.out.println("What do you wish to search?");
        System.out.println("Please input the query: ");
        return sc.nextLine();
    }

    public static String getSearchingDirectoryPath(){
        System.out.println("Hi, welcome to use searching function");
        System.out.println("First, please input the path of the collection that you want to search:");
        return sc.nextLine();
    }

    public static String getDirectoryStorePath(){
        System.out.println("Where do you wish the index file to be stored?");
        System.out.println("Please input it's path (include filename and extension): ");
        return sc.nextLine();
    }

    public static void isDirectory(){
        System.out.println("is a Directory");
    }

    public static void notDirectory(){
        System.out.println("Sorry, the file path that you input is not a directory");
    }

    public static void fileDoesNotExist(){
        System.out.println("Sorry, file doesn't exist, please input again");
        System.out.println("Could you please create the file manually(end with extension of txt)?");
    }
}
