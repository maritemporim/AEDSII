import java.util.Scanner;

public class Maiusculo {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int bts; //contador
        bts = 0;
        String palavra;
        palavra = sc.nextLine();

        while(palavra != "FIM"){
            for(int i = 0; i < palavra.length(); i++){
                if(palavra.charAt(i) >= 'A' && palavra.charAt(i) <= 'Z'){
                        bts++;
                }
                
            }

            System.out.println(bts);
            bts = 0;
            palavra = sc.nextLine();
        }
        sc.close();
        }
        
}
