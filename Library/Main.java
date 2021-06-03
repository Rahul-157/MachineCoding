package Library;

import java.util.*;

abstract class Matchers{
    static final String create_library =  "^create_library .*";
    static final String add_book =        "^add_book .*";
    static final String remove_book_copy= "^remove_book_copy .*";
    static final String borrow_book =     "^borrow_book .*";
    static final String return_book_copy= "^return_book_copy .*";
    static final String print_borrowed =  "^print_borrowed .*";
    static final String search =          "^search .*";
}


class Executor{
    Library library;

    Executor(){
        new User("user1");
        new User("user2");
    }

    private void _printResults(Set<Book> results){
        for(Book book:results){
            for(BookCopy book_copy:book.available_copies)
                book_copy.print_details();
            for(BookCopy book_copy:book.borrowed_copies)
                book_copy.print_details();
        }
    }

    void create_library(String []args){
        try{
            this.library = new Library(Integer.parseInt(args[1]));
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    void add_book(String []args){
        try{
        new Book(this.library,args);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void remove_book_copy(String []args){
        try{
            BookCopy.removeBookCopy(args[1],args[2]);
        } catch (Exception e){
                e.printStackTrace();
        }
    }

    void borrowBook(String []args){
        try{
            if(! User.users.containsKey(args[2])){
                System.err.println("Invalid User");
                return;
            }
            User user = User.users.get(args[2]);
            if(user.borrowed.size()>=5){
                System.out.println("Overlimit");
                return;
            }
            BookCopy borrowed_book = Book.isBookAvailable(args[1], user, args[3]);
            if(borrowed_book!=null){
                System.out.printf("Borrowed Book from rack:%d\n",borrowed_book.rack);
                user.borrowed.add(borrowed_book);
                borrowed_book.setRack(-1);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void return_book_copy(String []args){
        try{
            BookCopy.returnBookCopy(args[1],args[2]);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void print_borrowed(String []args){
        try{
            if(! User.users.containsKey(args[1])){
                System.err.println("Invalid User");
                return;
            }
            User user = User.users.get(args[1]);
            user.print_borrowed();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void search(String []args){
        assert(args.length%2==1);
        Set<Book> search_results = new HashSet<>();  
        for(int i=1;i<args.length;i=i+2){
            String key = args[i];
            String value = args[i+1];
            if(key.equals("book_id")){
                search_results = new HashSet<>();
                search_results.add(Book.books.get(value));
            }
            else if(key.equals("author_id")){
                if(search_results.size()==0)
                    search_results = Author.authors.get(value).books;
                else
                    search_results.retainAll(Author.authors.get(value).books);
            } else if(key.equals("publisher_id")){
                if(search_results.size()==0)
                    search_results = Publisher.publishers.get(value).books;
                else
                    search_results.retainAll(Publisher.publishers.get(value).books);
            } 
        }
        _printResults(search_results);
    }


    void start(){
        Scanner sc = new Scanner(System.in);
        String query = sc.nextLine();
        while(! query.equals("exit")){
            String []args = query.split(" ");
            if(query.matches(Matchers.create_library))
                this.create_library(args);
            else if(query.matches(Matchers.add_book))
                this.add_book(args);
            else if(query.matches(Matchers.borrow_book))
                this.borrowBook(args);
            else if(query.matches(Matchers.print_borrowed))
                this.print_borrowed(args);
            else if(query.matches(Matchers.remove_book_copy))
                this.remove_book_copy(args);
            else if(query.matches(Matchers.return_book_copy))
                this.return_book_copy(args);
            else if(query.matches(Matchers.search))
                this.search(args);
            query = sc.nextLine();
        }
    }

}


public class Main {
    
    public static void main(String[] args) {
        Executor executor = new Executor();
        executor.start();
    }
}