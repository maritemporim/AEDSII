public import java.util.Scanner;

class SomasRec{
    public static boolean isFim(String str){
        return str.length() == 3 &&
               str.charAt(0) == 'F' &&
               str.charAt(1) == 'I' &&
               str.charAt(2) == 'M';
    }

    public static int soma(String str, int i){
        if(i >= str.length()){
            return 0;
        }
        return (str.charAt(i) - '0') + soma(str, i + 1);
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        String linha = sc.nextLine();

        while(true){
            if(isFim(linha)){
                break;
            }

            System.out.println(soma(linha, 0));

            linha = sc.nextLine();
        }

        sc.close();
    }
} {
    
}
