package parking_lot;


enum VType {
    TRUCK,
    BIKE,
    CAR
  }

class Vehicle {
    String reg_no;
    VType type;
    String color;
    static final int  TRUCK_LIMIT=1;
    static final int BIKE_LIMIT = 4;
    Vehicle(String reg_no, String type,String color) throws Exception  {
        
        if(type.equalsIgnoreCase("car"))
            this.type = VType.CAR;
        else if(type.equalsIgnoreCase("bike"))
            this.type = VType.BIKE;
        else if (type.equalsIgnoreCase("truck"))
            this.type = VType.TRUCK;
        else{
            throw new Exception("Invalid Vehicle Type");
        }
        this.reg_no = reg_no;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Registration Number: "+this.reg_no + " Color: " + this.color;
    }
}
