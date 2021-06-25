package JobScheduler;

import java.util.*;

public class CustomThread {
    
    List<CustomProcess> processes;
    int occupied;
    String name;
    CustomThread(String name){
        this.processes = new ArrayList<>();
        this.occupied= 0;
        this.name = name;
    }

}
