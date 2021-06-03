package Library;

import java.util.*;

class Order{
    String user_id,due_date;
    Order(String user_id,String due_date){
        this.user_id = user_id;
        this.due_date = due_date;
    }
}

class Book {
    String book_id,title;
    List<Author> authors;
    List<Publisher> publishers;
    Library library;
    Comparator<BookCopy> myComparator = new Comparator<BookCopy>() {
        @Override
        public int compare(BookCopy b1, BookCopy b2) {
            return b1.rack - b2.rack;
        }
    };

    PriorityQueue<BookCopy> available_copies = new PriorityQueue<BookCopy>(myComparator);
    Set<BookCopy> borrowed_copies = new HashSet<>();
    static HashMap<String,Book> books = new HashMap<>();
    Book(Library lib,String []args){
        this.book_id = args[1];
        this.title = args[2];
        this.library = lib;
        this.authors = new ArrayList<>();
        this.publishers = new ArrayList<>();
        String []authorStrings = args[3].split(",");
        String []publishersStrings = args[4].split(",");
        String []copiesStrings = args[5].split(",");
        for(String author : authorStrings){
            Author auth;
            if(Author.authors.containsKey(author))
                auth = Author.authors.get(author);
            else
                auth = new Author(author);
            auth.addBook(this);
            this.authors.add(auth);
        }

        for(String publisher : publishersStrings){
            Publisher pub;
            if(Publisher.publishers.containsKey(publisher))
                pub = Publisher.publishers.get(publisher);
            else
                pub = new Publisher(publisher);
            pub.addBook(this);
            this.publishers.add(pub);
        }
        System.out.print("Added book to racks ");
        for(String copy : copiesStrings){
            System.out.print(new BookCopy(this,copy).rack+" ");
        }
        System.out.println();
        synchronized(books){
            books.put(this.book_id,this);
        }
    }

    static BookCopy isBookAvailable(String book_id,User user,String due_date){
        Book book = books.get(book_id);
        if(book == null ){
            System.err.println("Invalid Book");
            return null;
        }
        if(book.available_copies.size()>0){
            BookCopy book_copy = book.available_copies.poll();
            return BookCopy.isBookCopyAvailable(book_id,book_copy.book_copy_id, user, due_date);
        }  
        System.err.println("No BookCopies available for Book : "+book.book_id);
        return null;
    }
    
}

class BookCopy {
    Book book;
    String book_copy_id;
    Order order;
    int rack;
    
    static HashMap<String,BookCopy> book_copies = new HashMap<>();
    BookCopy(Book book,String id){
        this.book = book;
        this.book_copy_id = id;
        this.order = null;
        int i;
        for(i=0;i<this.book.library.racks.size();i++){
            Rack lib_rack = this.book.library.racks.get(i);
            if(! lib_rack.books.contains(book.book_id)){
                lib_rack.books.add(book.book_id);
                this.rack = lib_rack.id;
                this.book.available_copies.add(this);
                break;
            }
        }
        if(i==this.book.library.racks.size()) // throw new Exception
            System.err.println("No Racks available for Book : "+book.book_id);
        else{
            synchronized(book_copies){
                book_copies.put(this.book.book_id+id, this);
            }
        }
    }

    static BookCopy isBookCopyAvailable(String book_id,String book_copy_id,User user,String due_date){
        BookCopy book_copy = book_copies.get(book_id+book_copy_id);
        if(book_copy!=null){
            if(book_copy.order!=null){
                System.err.println("Not available : "+ book_copy.book_copy_id);
                return null;
            }
            book_copy.order = new Order(user.name,due_date);
            book_copy.book.borrowed_copies.add(book_copy);
            book_copy.book.library.racks.get(book_copy.rack-1).books.remove(book_copy.book.book_id);
            return book_copy;
        }else{
            System.err.println("Invalid Book Copy ID");
            return null;
        }
    }

    void setRack(int val){
        this.rack = val;
    }

    static boolean returnBookCopy(String book_id,String book_copy_id){
        BookCopy book_copy = BookCopy.book_copies.get(book_id+book_copy_id);
        if(book_copy!= null){
            if(! book_copy.book.borrowed_copies.contains(book_copy)){
                System.err.println("Book Copy not borrowed yet");
                return false;
            }
            boolean ret_value = book_copy.book.borrowed_copies.remove(book_copy);
            BookCopy.book_copies.remove(book_id+book_copy_id);
            BookCopy new_book_copy = new BookCopy(book_copy.book, book_copy_id);
            System.out.printf("Returned book copy %s and added to rack %d\n",new_book_copy.book_copy_id,new_book_copy.rack);
            return ret_value && new_book_copy!=null;
        }else{
            System.err.println("Invalid Book Copy ID");
            return false;
        }
        
    }

    static void removeBookCopy(String book_id,String book_copy_id){
        BookCopy book_copy = BookCopy.book_copies.get(book_id+book_copy_id);
        if(book_copy!= null){
            boolean ret_value = book_copy.book.available_copies.remove(book_copy);
            if(book_copy.book.borrowed_copies.contains(book_copy)){
                System.err.println("Book Copy is Borrowed");
                return ;
            }
            else  if(ret_value && BookCopy.book_copies.remove(book_id+book_copy_id)!=null){
                System.out.printf("Removed book copy: %s from rack %d\n",book_copy.book_copy_id,book_copy.rack);
                return ;
            }
        }else{
            System.err.println("Invalid Book Copy ID");
            return ;
        }
    }

    public void print_details(){
        System.out.printf("Book Copy: %s %s %s %s %s %d %s %s\n", 
        this.book_copy_id,
        this.book.book_id,
        this.book.title,
        String.join(",", this.book.authors.toString()),
        String.join(",",this.book.publishers.toString()),
        this.rack,
        this.order!=null?order.user_id:"",
        this.order!=null?order.due_date:""
        );
    }
    
}


