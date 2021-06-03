package Library;


import java.util.*;

class Author {
    String name;
    Set<Book> books;
    static HashMap<String,Author> authors = new HashMap<>();
    Author(String name){
        this.name = name;
        this.books = new HashSet<>();
        authors.put(name, this);
    }

    public boolean addBook(Book book){
        return this.books.add(book);
    }

    @Override
    public String toString(){
        return this.name;
    }
}
