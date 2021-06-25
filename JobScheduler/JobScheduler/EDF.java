package JobScheduler;

import java.util.*;

public class EDF {
    
    List<CustomProcess> processes;
    PriorityQueue<CustomProcess> pq;
    List<CustomThread> threads;
    EDF(List<CustomProcess> processes,int n_threads){
        this.threads = new ArrayList<>();
        for(int i=0;i<n_threads;i++)
            this.threads.add(new CustomThread("Thread"+i));
        Comparator<CustomProcess> comparator = new Comparator<CustomProcess>(){
            @Override
            public int compare(CustomProcess p1, CustomProcess p2){
                if(p1.deadline==p2.deadline){
                    if(p1.priority==p2.priority){
                        return p1.duration - p2.duration;
                    }else   
                        return (p1.priority - p2.priority); 
                }else
                    return p1.deadline-p2.deadline;
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
                // while(true){
                   
                    if(t+cp.duration>cp.deadline)
                        break;
                    t=t+1;
                    boolean assigned = false;
                    //t 10
                    //t1 4 
                    //t2 2
                    for(int i=0;i<this.threads.size();i++){
                        CustomThread ct = this.threads.get(n_t);
                        ct.occupied =ct.occupied -1;
                        if(ct.occupied <= 0 && !assigned){
                            ct.processes.add(cp);
                            ct.occupied = t+ cp.duration; 
                            assigned = true;
                        }
                       
                    }
                    
                    
                // }
                
            
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
