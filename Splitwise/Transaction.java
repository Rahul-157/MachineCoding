package Splitwise;

class Transaction {
    User user;
    int money;
    Transaction(User user){
        this.user = user;
        this.money = 0;
    }

    public void giveMoney(int add_money){
        this.money = this.money + add_money;
    }
    
    public void takeMoney(int sub_money){
        this.money = this.money - sub_money;
    }
}
