package JobScheduler;

import java.util.*;

public class FPS {
    List<CustomProcess> processes;
    PriorityQueue<CustomProcess> pq;
    List<CustomThread> threads;
    FPS(List<CustomProcess> processes,int n_threads){
        this.threads = new ArrayList<>();
        for(int i=0;i<n_threads;i++)
            this.threads.add(new CustomThread("Thread"+i));
        Comparator<CustomProcess> comparator = new Comparator<CustomProcess>(){
            @Override
            public int compare(CustomProcess p1, CustomProcess p2){
                if(p1.priority==p2.priority){
                    if(p1.user==p2.user){
                        return -1* (p1.duration - p2.duration);
                    }else   
                        return p1.user-p2.user; 
                }else
                    return p1.priority-p2.priority;
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
        int t=0;
        while(true){
            {
                if(pq.size()==0) break;
                CustomProcess cp =  pq.poll();
                while(true){
                    CustomThread ct = this.threads.get(n_t);
                    if(ct.occupied==0){
                        ct.processes.add(cp);
                        ct.occupied = cp.duration;
                        break;
                    }
                    ct.occupied = ct.occupied -1;
                    n_t = (n_t+1)%this.threads.size();
                }
                t=t+1;
            
            }
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
