package SnakeAndLadder;

import java.util.*;


class SnakeAndLadder {
    int n_ladders,n_snakes,n_players;
    Map<Integer,Integer> ladders,snakes;
    List<String> players;
    Map<String,Integer> positions;
    Random dice;
    SnakeAndLadder(){
        this.ladders = new HashMap<>();
        this.snakes = new HashMap<>();
        this.positions = new HashMap<>();
        this.players = new ArrayList<>();
        this.dice = new Random();
        Scanner sc = new Scanner(System.in);
        this.n_snakes = sc.nextInt();

        for(int i=0;i<this.n_snakes;i++)
        {
            int s = sc.nextInt();
            int d = sc.nextInt();
            this.snakes.put(s,d);
        }

        this.n_ladders = sc.nextInt();
        for(int i=0;i<this.n_ladders;i++)
        {
            int s = sc.nextInt();
            int d = sc.nextInt();
            this.ladders.put(s,d);
        }
        this.n_players = sc.nextInt();
        sc.nextLine();
        for(int i=0;i<this.n_players;i++){
            String name = sc.nextLine();
            this.players.add(name);
            this.positions.put(name,0);
        }
    }

    void start(){
        int steps,source,destination,turn=0;
        String current_player;
        while(true){
            current_player = this.players.get(turn);
            steps = this.dice.nextInt(6) + 1;
            source = this.positions.get(current_player);
            destination = source+steps;
            while(this.ladders.containsKey(destination) || this.snakes.containsKey(destination)){
                if(this.ladders.containsKey(destination))
                    destination = this.ladders.get(destination);
                else
                    destination = this.snakes.get(destination);
            }
            if(destination > 100)
                destination = source;
            System.out.printf("%s rolled %d and moves from %d to %d \n",current_player,steps,source,destination);

            if(destination==100){
                System.out.printf("%s wins the game\n",current_player);
                return;
            }
            this.positions.put(current_player,destination);
            turn = (turn+1)%(this.n_players);
        }
    }
}
