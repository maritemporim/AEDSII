import java.util.Scanner;
import java.util.*;

public class TcpIp{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String[] pack = new String[100];
        String linha = sc.nextLine();
        int i = 1;

        pack[0] = sc.nextLine();

        while(pack[i].compareTo("0") > 0){
            pack[i] = sc.nextLine();
            i++;
        }

        for(int j = 0; j < i; i++){
            for(int k = 0; i-1 < 99; k++){
                if(pack[k].compareTo(pack[k+1]) > 0){
                    String tmp = pack[k];
                    pack[k] = pack[k+1];
                    pack[k+1] = tmp;
                }
            }
        }

        for(int k = 0; k < 100; k++){
            System.out.println(pack[k]);
        }

        }        
}   

