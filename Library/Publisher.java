package Library;


import java.util.*;

class Publisher {
    String name;
    Set<Book> books;
    static HashMap<String,Publisher> publishers = new HashMap<>();
    Publisher(String name){
        this.name = name;
        this.books = new HashSet<>();
        publishers.put(name,this);
    }
   
    public boolean addBook(Book book){
        return this.books.add(book);
    }

    @Override
    public String toString(){
        return this.name;
    }
}
