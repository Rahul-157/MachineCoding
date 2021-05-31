package parking_lot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
 
class Slot extends Object{
    VType v_type;
    int number;
    int floor;
    Slot( int floor,int number){
        if(number == 1)
            this.v_type = VType.TRUCK;
        else if (number < 4 )
            this.v_type = VType.BIKE;
        else
            this.v_type = VType.CAR;
        this.number= number;
        this.floor = floor;
    }
}


class Floor{
    int num_slots;
    int f_number;
    final HashMap<VType,PriorityQueue<Slot>> free_slots = new HashMap<>();
    final HashMap<VType,HashSet<Integer>> occupied_slots = new HashMap<>();

    Comparator<Slot> myComparator = new Comparator<Slot>() {
        @Override
        public int compare(Slot o1, Slot o2) {
            return o1.number - o2.number;
        }
    };

    Floor(int number,int n_s){
        this.f_number =  number;
        this.num_slots = n_s;

        for (VType vType :  VType.values()){
            this.occupied_slots.put(vType, new HashSet<Integer>());
        }

        PriorityQueue<Slot> pq = new PriorityQueue<Slot>(myComparator);
        for( int i =1;i<=Vehicle.TRUCK_LIMIT && i<num_slots;i++)
            pq.add(new Slot(this.f_number,i));
        free_slots.put(VType.TRUCK,pq);
        pq = new PriorityQueue<Slot>(myComparator);
        for( int i =Vehicle.TRUCK_LIMIT+1;i<=Vehicle.BIKE_LIMIT && i<num_slots;i++)
            pq.add(new Slot(this.f_number,i));
        free_slots.put(VType.BIKE,pq);
        pq = new PriorityQueue<Slot>(myComparator);
        for( int i = Vehicle.BIKE_LIMIT+1;i<=num_slots;i++)
            pq.add(new Slot(this.f_number,i));
        free_slots.put(VType.CAR,pq);
    }

    public  int getSlotsCount(VType v_type,boolean vacant){
        if(vacant)
            return free_slots.get(v_type).size();
        else 
            return occupied_slots.get(v_type).size();
    }

    synchronized  Slot get_free_slot(VType v_type){
        Slot s = this.free_slots.get(v_type).poll();    
        this.occupied_slots.get(v_type).add(s.number);
        return s;
    }

    synchronized boolean return_slot(VType v_type,int slot){
            boolean res = this.occupied_slots.get(v_type).contains(slot);
            if(res)
                res = this.occupied_slots.get(v_type).remove(slot);
            if(res)
                res = this.free_slots.get(v_type).add(new Slot(this.f_number,slot));
            return res;
    }

}


class Ticket{
    String ticket_id;
    VType v_type;
    static HashMap<String,Vehicle> tickets = new HashMap<String,Vehicle>();


    Ticket(String p_id,Vehicle vehicle,Slot slot){
        this.ticket_id = p_id + "_" +String.valueOf(slot.floor) + "_" + String.valueOf(slot.number);
        synchronized(tickets){
            tickets.put(this.ticket_id,vehicle);
        }
    }

    synchronized static  Vehicle removeTicket(String ticket){
       return tickets.remove(ticket);
    }

    @Override
    public String toString() {        
        return this.ticket_id;
    }
}

class ParkingLot{
    List<Floor> floors;
    String p_id;
    int num_floors,num_slots;
    HashSet<String> parked_vehicles = new HashSet<String>();

    ParkingLot(String p_id,int n_f,int n_s){
        this.floors = new ArrayList<>();
        this.p_id = p_id;
        this.num_floors=n_f;
        this.num_slots=n_s;
        for(int i=0;i<this.num_floors;i++){
            this.floors.add( new Floor(i,n_s));
        }
        System.out.printf("Created parking lot with %s floors and %s slots per floor \n",num_floors,num_slots);
    }

    public Ticket parkVehicle(Vehicle vehicle){
        if(parked_vehicles.contains(vehicle.reg_no)){
            System.err.printf("Duplicate Vehicles with Regsitration Number : %s found\n",vehicle.reg_no);
            return null;
        }
        for(Floor floor : this.floors){
            if(floor.getSlotsCount(vehicle.type,true) > 0)
            {
                Slot free_slot = floor.get_free_slot(vehicle.type);
                Ticket ticket =  new Ticket(this.p_id,vehicle,free_slot);
                parked_vehicles.add(vehicle.reg_no);
                System.out.printf("Parked Vehicle. Ticket ID: %s \n",ticket);
                return ticket;
            }
        }

        System.out.printf("Parking Full for %s\n",vehicle.type);
        return null;
    }


    public void unParkVehicle(String ticket_id){
        
        String []tokens = ticket_id.split("_", 3);
        if( ! tokens[0].equals(this.p_id))
        System.err.println("Invalid Ticket");    
        try{
            int floor  = Integer.valueOf(tokens[1]);
            int slot = Integer.valueOf(tokens[2]);
            Vehicle vehicle = Ticket.removeTicket(ticket_id);
            if(vehicle ==null){
                System.err.println("Invalid Ticket");
                return;
            }
            boolean res =  this.floors.get(floor).return_slot(vehicle.type, slot);
            if(!res ){
                System.err.println("Error Unparking Vehicle");
            }
            parked_vehicles.remove(vehicle.reg_no);
            System.out.printf("Unparked Vehicle | %s\n",vehicle);
            
        } catch(NumberFormatException nfe){
            nfe.printStackTrace();
            
        }
    }

    public void displaySlots(VType v_type){
        for(Floor floor : floors){
            System.out.printf("Free Slots for Vehicle %s on Floor %s : ",v_type,floor.f_number);
            for(Slot s : floor.free_slots.get(v_type))
                System.out.print(String.valueOf(s.number) + " ");
            System.out.println();
        }
    }

    public void displaySlotsCount(VType v_type){
        for(Floor floor : floors){
            System.out.printf("Free Slots for Vehicle %s on Floor %s : %s\n",v_type,floor.f_number,floor.getSlotsCount(v_type, true));
        }
    }
}