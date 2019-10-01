import java.util.HashMap;
import java.util.Map;

public class SessionInfo {

    public Map<String, Double> buyIns = new HashMap<>();
    public final String sessionDate;

    public SessionInfo(String sessionDate) {
        this.sessionDate = sessionDate;
    }
}
