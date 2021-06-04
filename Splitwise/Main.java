package Splitwise;

import java.util.*;


abstract class Matchers{
    static final String   SHOW = "^SHOW ?.*";
    static final String   EXPENSE = "^EXPENSE .*";
}

class Executor{

    Executor(){
        new User("u1","User1");
        new User("u2","User2");
        new User("u3","User3");
        new User("u4","User4");
    }

    private void show(List<String> args) throws Exception{
        Set<String> users = new HashSet<>(args);
        User.showTransactions(users);
    }

    private void expense(String user_id,List<String> other_users,List<Integer> money) throws Exception{
     User user = User.getUser(user_id);
     for(int i=0;i<other_users.size();i++){
         user.makeTransaction(other_users.get(i), money.get(i),1);
     }
    }

    public void execute(String query) throws Exception{
        List<String> o_args = Arrays.asList(query.split(" "));
        ArrayList<String> args = new ArrayList<String>(o_args);
        // if(args.size()>1)
            args.remove(0);
        if(query.matches(Matchers.SHOW)){
            this.show(args);
        }
        else if(query.matches(Matchers.EXPENSE)){
            String g_user = args.get(0);
            int money = Integer.valueOf(args.get(1));
            int total_users = Integer.valueOf(args.get(2));
            List<String> t_users = new ArrayList<>();
            for(int i=0;i<total_users;i++)
                t_users.add(args.get(3+i));

            List<Integer> individual_money = new ArrayList<>();
            if(args.contains("EXACT")){
                int idx = args.indexOf("EXACT")+1;
                int total_money = 0;
                for(int i=0;i<total_users;i++){
                    total_money = total_money + Integer.valueOf(args.get(idx+i));
                    individual_money.add(Integer.valueOf(args.get(idx+i)));
                }
                if(total_money!=money){
                    throw new Exception("Money Sum not EQUAL");
                   
                }
            } else if(args.contains("EQUAL")){
                int i_money = money/total_users;
                for(int i=0;i<total_users;i++)
                    individual_money.add(Integer.valueOf(i_money));
                int remainder = money%total_users;
                while(remainder!=0){
                    individual_money.set(remainder, individual_money.get(remainder)+1);
                    remainder = remainder-1;
                }
            } else if(args.contains("PERCENT")){
                int idx = args.indexOf("PERCENT")+1;
                int percentage = 0;
                int remainder = 0;
                for(int i=0;i<total_users;i++){
                    percentage = percentage +  Integer.valueOf(args.get(idx+i));
                    int i_money = money * percentage / 100;
                    remainder = remainder+ (money * percentage)%100;
                    individual_money.add(i_money);
                }
                if(percentage!=100){
                    throw new Exception("Total Percentage not EQUAL to 100");
                }
                while(remainder!=0){
                    individual_money.set(remainder, individual_money.get(remainder)+1);
                    remainder = remainder-1;
                }
            }
            this.expense(g_user,t_users,individual_money);
        }
    }


}


public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Executor executor = new Executor();
        String query = sc.nextLine();
        
        while(! query.equals("exit")){
            try{
                executor.execute(query);
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
            query = sc.nextLine();
        }
    }
}