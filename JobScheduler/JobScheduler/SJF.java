package JobScheduler;

import java.util.*;

public class SJF {
    
    List<CustomProcess> processes;
    PriorityQueue<CustomProcess> pq;
    List<CustomThread> threads;
    SJF(List<CustomProcess> processes,int n_threads){
        this.threads = new ArrayList<>();
        for(int i=0;i<n_threads;i++)
            this.threads.add(new CustomThread("Thread"+i));
        Comparator<CustomProcess> comparator = new Comparator<CustomProcess>(){
            @Override
            public int compare(CustomProcess p1, CustomProcess p2){
                if(p1.duration==p2.duration)
                    return p1.priority-p2.priority;
                return p1.duration-p2.duration;
            }
        };
        this.processes = processes;

        this.pq = new PriorityQueue<CustomProcess>(comparator);
        for(CustomProcess p : this.processes){
            pq.add(p);
        }
    }

    void schedule(){
        int n_t = 0;
        while(pq.size()!=0){
            this.threads.get(n_t).processes.add(pq.poll());
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
