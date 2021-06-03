package Library;

import java.util.*;

class Rack{
    int id;
    Set<String> books;
    Rack(int id){
        this.id = id;
        this.books = new HashSet<String>();
    }
}

class Library {
    List<Rack> racks;
    Library(Integer num_racks){
        this.racks = new ArrayList<>();
        for(int i=0;i<num_racks;i++)
            this.racks.add(new Rack(i+1));
        System.out.println("Created Library with "+racks.size()+ " racks");
    }

}
