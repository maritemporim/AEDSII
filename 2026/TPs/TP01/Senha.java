import java.util.Scanner;

class Senha{
    public static boolean isFim(String str){
        return str.length() == 3 &&
               str.charAt(0) == 'F' &&
               str.charAt(1) == 'I' &&
               str.charAt(2) == 'M';
    }

    public static boolean valida(String str){
        if(str.length() < 8){
            return false;
        }

        boolean maiuscula = false;
        boolean minuscula = false;
        boolean numero = false;
        boolean especial = false;

        for(int i = 0; i < str.length(); i++){
            char c = str.charAt(i);

            if(c >= 'A' && c <= 'Z'){
                maiuscula = true;
            } else if(c >= 'a' && c <= 'z'){
                minuscula = true;
            } else if(c >= '0' && c <= '9'){
                numero = true;
            } else {
                especial = true;
            }
        }

        return maiuscula && minuscula && numero && especial;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        String linha = sc.nextLine();

        while(true){
            if(isFim(linha)){
                break;
            }

            if(valida(linha)){
                System.out.println("SIM");
            } else {
                System.out.println("NAO");
            }

            linha = sc.nextLine();
        }

        sc.close();
    }
}