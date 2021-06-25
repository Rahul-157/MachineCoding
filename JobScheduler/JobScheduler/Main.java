package JobScheduler;

import java.util.*;

class Scheduler{
    List<CustomProcess> processes;
 
    int n_threads;
    Scheduler(int threads){
        this.n_threads = threads;
        this.processes = new ArrayList<>();
       
        processes.add(new CustomProcess("J1",10,0,10,0));
        processes.add(new CustomProcess("J2",20,0,40,1));
        processes.add(new CustomProcess("J3",15,2,40,0));
        processes.add(new CustomProcess("J4",30,1,40,2));
        processes.add(new CustomProcess("J5",10,2,30,2));
    
    }


    void Sjf(){
        SJF sjf = new SJF(this.processes,n_threads);
        sjf.schedule();
    }


    void Fcfs(){
        FCFS fcfs = new FCFS(this.processes,n_threads);
        fcfs.schedule();
    }


     void Edf(){
        EDF edf = new EDF(this.processes,n_threads);
        edf.schedule();
    }

    void Fps(){
        FPS fps = new FPS(this.processes,n_threads);
        fps.schedule();
    }
}

public class Main {
    

    public static void main(String[] args) {
        Scheduler sc = new Scheduler(2);
        sc.Sjf();
        System.out.println();
        sc.Fcfs();
        System.out.println();
      
        sc.Fps();
        System.out.println();
        sc.Edf();
    }
}
