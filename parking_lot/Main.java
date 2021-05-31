package parking_lot;


/**
 * javac parking_lot/* -d output
 * java -cp output/ parking_lot.Main
 */

import java.util.Scanner;

public class Main{
    static  ParkingLot parkingLot; 
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String command = sc.nextLine();
        while(! command.equals("exit")){
            String []tokens = command.split(" ");
            if(command.contains("create_parking_lot")){
                
                try{
                parkingLot = new ParkingLot(tokens[1], Integer.valueOf(tokens[2]), Integer.valueOf(tokens[3]));
                } catch (NumberFormatException nfe){
                    nfe.printStackTrace();
                }
            }
            else if(command.contains("display free_count")){
                VType v_type = tokens[2].equals("CAR") ? VType.CAR: tokens[2].equals("TRUCK")? VType.TRUCK:tokens[2].equals("BIKE")?VType.BIKE:null;
                if(v_type==null)
                    System.err.println("Invalid Vehicle Type");
                parkingLot.displaySlotsCount(v_type);
            }
            else if(command.contains("display free_slots")){
                VType v_type = tokens[2].equals("CAR") ? VType.CAR: tokens[2].equals("TRUCK")? VType.TRUCK:tokens[2].equals("BIKE")?VType.BIKE:null;
                if(v_type==null)
                    System.err.println("Invalid Vehicle Type");
                parkingLot.displaySlots(v_type);
            }
            else if(command.contains("unpark_vehicle"))
                    parkingLot.unParkVehicle(tokens[1]);
            else if (command.contains("park_vehicle")){
                try{
                Vehicle vehicle = new Vehicle(tokens[2], tokens[1], tokens[3]);
                parkingLot.parkVehicle(vehicle);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
            command = sc.nextLine();
        }

    }
}