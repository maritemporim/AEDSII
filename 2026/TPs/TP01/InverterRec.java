import java.util.Scanner;

class InverterRec{
    public static boolean isFim(String str){
        return str.length() == 3 &&
               str.charAt(0) == 'F' &&
               str.charAt(1) == 'I' &&
               str.charAt(2) == 'M';
    }

    public static String inverter(String str, int i){
        if(i < 0){
            return "";
        }
        return str.charAt(i) + inverter(str, i - 1);
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        String linha = sc.nextLine();

        while(true){
            if(isFim(linha)){
                break;
            }

            System.out.println(inverter(linha, linha.length() - 1));

            linha = sc.nextLine();
        }

        sc.close();
    }
}