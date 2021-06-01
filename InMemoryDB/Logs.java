package InMemoryDB;

import java.time.Instant;
import java.util.*;

abstract class Logs {
    static ArrayList<String> logs;
    
    synchronized static void print(String query){
        logs.add(query+" " +Instant.now().toString());
    }
    
}
