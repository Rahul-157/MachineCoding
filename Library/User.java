package Library;

import java.util.*;

class User {
    String name;
    List<BookCopy> borrowed;
    static HashMap<String,User> users = new HashMap<>();
    User(String name){
        this.name = name;
        this.borrowed = new ArrayList<>();
        users.put(name,this);
    }

    void print_borrowed(){
       for(BookCopy bookCopy : borrowed){
           System.out.printf("Book Copy: %s %s\n",bookCopy.book_copy_id,bookCopy.order.due_date);
       }
    }
}
