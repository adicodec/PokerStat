import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private final static String fileName = "/Users/adinayak/Downloads/sept_poker_summary.txt";
    private final static String HEADER = "PLAYER\tTOTAL WINNINGS\tMATCHES PLAYED\tMATCHES +VE\tMATCHES -VE\tAVG GAIN/LOSS";
    private static Set<String> allPlayers = new HashSet<>();
    private static Map<String, String> alias = new HashMap<>();

    static {
        alias.put("voldy","monty");
        alias.put("voldi","monty");
        alias.put("volddigger","monty");
        alias.put("srinibash","prusty");
        alias.put("adi","aditya");
        alias.put("aadi","aditya");
        alias.put("fucka", "amit");
        alias.put("virat", "amit");
        alias.put("ricky", "amit");
        alias.put("fcka", "amit");
        alias.put("mota", "amit");
        alias.put("faka", "amit");
        alias.put("sisu","shashank");
        alias.put("siku", "deka");
        alias.put("kaushik", "deka");
        alias.put("ani", "aniket");
        /*alias.put("bhag", "bhagyesh");
        alias.put("bhaghesh", "bhagyesh");
        alias.put("bhaygesh", "bhagyesh");
        alias.put("bhagesh", "bhagyesh");
        alias.put("niranjan loser", "niranjan");
        alias.put("niranjan juari", "niranjan");
        alias.put("nit", "nitin");
        alias.put("nik", "nikhil");
        alias.put("suhaag", "nikhil");
        alias.put("suhag", "nikhil");
        alias.put("dan", "danish");
        alias.put("desu", "desai");
        alias.put("neer", "niranjan");
        alias.put("rah", "rahul");
        alias.put("preeth", "preet");
        alias.put("prit", "preet");
        alias.put("dhwani", "dhavani");*/

    }

    public static void main(String[] args) throws IOException{
        List<SessionInfo> sl = getAllSessions(fileName);
        Map<String, List<Double>> summary = getSummary(sl);
        printSummary(summary);
    }

    private static void printSummary(Map<String, List<Double>> summary) {
        System.out.println(HEADER);
        summary.entrySet().forEach( e -> {
            String line = e.getKey() + "\t";
            Double tot = 0.0;
            int matches = 0, positive = 0, negative = 0;
            for(Double d: e.getValue()) {
                matches++;
                if (d >= 0.0) {
                    positive++;
                } else {
                    negative++;
                }
                tot += d;
            }
            List<Object> statList = Arrays.asList(tot, matches, positive, negative, String.format("%.2f",tot/matches));
            line += statList.stream().map(Object::toString).collect(Collectors.joining("\t"));
            System.out.println(line);
        });
    }

    private static Map<String, List<Double>> getSummary(List<SessionInfo> sl) {
        Map<String, List<Double>> summary = new HashMap<>();
        allPlayers.stream().forEach( p -> {
            summary.put(p, new ArrayList<>());
        });
        sl.stream().forEach( si -> {
            si.buyIns.entrySet().stream().forEach( e -> {
                summary.get(e.getKey()).add(e.getValue());
            });
        });
        return summary;
    }

    private static List<SessionInfo> getAllSessions(String fileName) throws IOException {
        List<SessionInfo> sl = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine();
        while (line != null) {
            if(line.contains("SESSION SUMMARY")) {
                SessionInfo session = getSessionInfo(br, line);
                sl.add(session);
            }
            line = br.readLine();
        }
        return sl;
    }

    private static SessionInfo getSessionInfo(BufferedReader br, String title) throws IOException{
        String curr;
        SessionInfo sessionInfo = new SessionInfo(getSessionDate(title));
        while((curr = br.readLine()) != null) {
            if(curr.trim().equals("") || curr.trim().equals("#### Buying Details ####")){
                continue;
            }
            if(curr.trim().equals("###################") || curr.trim().equals("###########")){
                break;
            }
            String name = curr.split(":")[0].trim().toLowerCase();
            name = alias.get(name) == null ? name : alias.get(name);
            sessionInfo.buyIns.put(name, Double.parseDouble(curr.split(":")[1].trim()));
            allPlayers.add(name);
        }
        return sessionInfo;
    }

    private static String getSessionDate(String title) {
        String ar[] = title.split(" ");
        return ar[ar.length-1];
    }
}
