import java.util.Scanner;

class Inverter{
    public static boolean isFim(String str){
        return str.length() == 3 &&
               str.charAt(0) == 'F' &&
               str.charAt(1) == 'I' &&
               str.charAt(2) == 'M';
    }

    public static String inverter(String str){
        String resp = "";

        for(int i = str.length() - 1; i >= 0; i--){
            resp += str.charAt(i);
        }

        return resp;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        String linha = sc.nextLine();

        while(true){
            if(isFim(linha)){
                break;
            }

            System.out.println(inverter(linha));

            linha = sc.nextLine();
        }

        sc.close();
    }
}
