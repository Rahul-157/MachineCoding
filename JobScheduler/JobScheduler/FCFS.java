package JobScheduler;

import java.util.*;

public class FCFS {
    List<CustomProcess> processes;
    // PriorityQueue<CustomProcess> pq;
    List<CustomThread> threads;
    FCFS(List<CustomProcess> processes,int n_threads){

        this.threads = new ArrayList<>();
       
        this.processes = processes;
        for(int i=0;i<n_threads;i++)
        this.threads.add(new CustomThread("Thread"+i));
        
    }

    void schedule(){
        int n_t = 0;
        for(int i=0;i<this.processes.size();i++){
            this.threads.get(n_t).processes.add(this.processes.get(i));
            n_t = (n_t+1)%this.threads.size();
        }
        this.printOrder();
    }


    public void printOrder(){
        for(CustomThread ct: this.threads){
            System.out.print(ct.name+" ");
            for(CustomProcess cp : ct.processes)
            System.out.print(cp.name+" ");
            System.out.println();
        }
    }
}
