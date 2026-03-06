import java.util.Scanner;

public class Espelho {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        int num1;
        num1 = sc.nextInt();
        int num2;
        num2 = sc.nextInt();

        int bts; //contador

        bts = 0;

        for(int i = num1; i <= num2; i++){
            bts = i;
            System.out.print(bts);
        }

        int c;
        c = num2;
        while(c != (num1-1)){
            System.out.print(c);
            c--;
        }

        sc.close();
    }
}
