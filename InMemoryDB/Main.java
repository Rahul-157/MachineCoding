package InMemoryDB;

import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Executor executor = new Executor();
        Scanner sc = new Scanner(System.in);
        while(true){
            String query = sc.nextLine();
            if(query.equals("exit"))
                break;
            executor.execute(query);
        }
    }
}