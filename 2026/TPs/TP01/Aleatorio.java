import java.util.Scanner;
import java.util.Random;

class Aleatorio{
    public static boolean isFim(String s){
        return s.length() == 3 &&
               s.charAt(0) == 'F' &&
               s.charAt(1) == 'I' &&
               s.charAt(2) == 'M';
    }

    public static String alterar(String str, Random gerador){
        char a = (char)('a' + (Math.abs(gerador.nextInt()) % 26));
        char b = (char)('a' + (Math.abs(gerador.nextInt()) % 26));

        String resp = "";

        for(int i = 0; i < str.length(); i++){
            char c = str.charAt(i);
            if(c == a){
                resp += b;
            } else {
                resp += c;
            }
        }

        return resp;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Random gerador = new Random();
        gerador.setSeed(4);

        String linha = sc.nextLine();

        while(!isFim(linha)){
            System.out.println(alterar(linha, gerador));
            linha = sc.nextLine();
        }

        sc.close();
    }
}