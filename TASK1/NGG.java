import java.util.*;
public class NGG{
    private static final int a = 1;
    private static final int b = 100;
    private static final int c = 7;
    public static void main(String[] d){
        Scanner e = new Scanner(System.in);
        Random f = new Random();
        System.out.printf("Guess the number between "+a+" and "+b+".");
        System.out.printf("You have "+c+" attempts.");
        int g =0;
        int h=0;
        boolean i=true;
        while(i){
            g++;
            int j=f.nextInt(b-a+1)+a;
            int k =0;
            boolean l = false;
            System.out.println("\nRound "+ g);
            while(k < c){
                    k++;
                    int m=0;
                    while(true){
                        System.out.print("Attempt "+k+":");
                        String n=e.nextLine().trim();
                        try{
                            m=Integer.parseInt(n);
                            if(m<a||m>b){
                                System.out.println("Number out of range.");
                            }
                            else{
                                break;
                            }
                        }catch(Exception o){
                            System.out.println("Invalid Input. ");
                        }
                    }
                    if(m==j){
                        System.out.println("Correct! Attempts: "+k);
                        l=true;
                        h++;
                        break;
                    }else if(m<j){
                        System.out.println("Higher.");
                    }else{
                        System.out.println("Lower. ");
                    }
            }
            if(!l){
                System.out.println("Failed. Number was "+j+".");
            }
            System.out.println("Rounds Played: "+ g +"| Won: "+h);
            while(true){
                System.out.println("Play again? (y/n): ");
                String p = e.nextLine().trim().toLowerCase();
                if(p.equals("y") || p.equals("yes")){
                    i=true;
                    break;
                }
                else if(p.equals("n") || p.equals("no")){
                    i=false;
                    break;
                }
            }
        }
        System.out.println("Bye.");
        e.close();
    }
}
