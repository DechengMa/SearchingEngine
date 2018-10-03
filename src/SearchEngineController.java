public class SearchEngineController {
    //Starter of the searching system
    public static void main(String[] args) {
       while(true){
           mainPage();
           System.out.println(" ");
       }
    }

    public static void mainPage() {
        SearchEngine searchEngine = new SearchEngine();
        switch (UserInterface.mainPagePrint().trim()) {
            case "1":
                String indexDirectoryName = UserInterface.getIndexDirectoryPath();
                String locationPath = UserInterface.getDirectoryStorePath();
                searchEngine.indexing(indexDirectoryName,locationPath);
                break;
            case "2":
                String searchingDirectoryName = UserInterface.getSearchingDirectoryPath();
                String query = UserInterface.getQuery();
                searchEngine.searching(searchingDirectoryName,query);
                break;
            case "3":
                System.exit(0);
            default:
                UserInterface.invalidOption();
        }
    }
}
