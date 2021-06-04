package Splitwise;

import java.util.*;

class User {
    String id,name;
    static HashMap<String,User> users = new HashMap<>();
    HashMap<String,Transaction> transactions ;

    synchronized static private User addUser(User u){
        return users.put(u.id, u);
    }

    static public User getUser(String id){
        return users.get(id);
    }

    User(String id,String name){
        this.id = id;
        this.name = name;
        this.transactions = new HashMap<>();
        addUser(this);
    }

    static public void showTransactions(Set<String> users) throws Exception{
        boolean only_balances = false;
        if(users.size()==0){
            only_balances= true;
            users = User.users.keySet();
        }
        for(String user_id:users){
            User user = User.users.get(user_id);
            if(user == null)
                throw new Exception("User: "+user_id+" not found");
            user.printTransactions(only_balances);
        }
    }

    synchronized public void makeTransaction(String user_id, int money,int flag ) throws Exception{
        User user = getUser(user_id);
        if(user == null){
            throw new Exception("User "+user_id+" not found.");
        }
        Transaction transaction_with_user = this.transactions.get(user_id);
        if(transaction_with_user == null)
        {
            Transaction new_transaction  = new Transaction(user);
            new_transaction.giveMoney(money);
            this.transactions.put(user_id,new_transaction);   
        }else
            transaction_with_user.giveMoney(money);
        if(flag==1)
        user.makeTransaction(this.id, -1 * money,0);
    }

    public void printTransactions(boolean only_balances){
        for(String  user_id: this.transactions.keySet()){
            Transaction t = this.transactions.get(user_id);
            if(t.money==0)
                continue;
            
            if(t.money<0){
                System.out.printf("%s owes %s: %d\n",this.name,t.user.name,-1 *t.money);
            } else if(!only_balances){
                System.out.printf("%s owes %s: %d\n",t.user.name,this.name,t.money);
            }
            
        }
    }
}
