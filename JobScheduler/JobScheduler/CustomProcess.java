package JobScheduler;


class CustomProcess {
    
    String name;
    int duration,priority,deadline,user;
    CustomProcess(String name,int duration,int priority,int deadline,int user ){
        this.name = name;
        this.duration= duration;
        this.priority = priority;
        this.deadline = deadline;
        this.user = user;
    }
}
