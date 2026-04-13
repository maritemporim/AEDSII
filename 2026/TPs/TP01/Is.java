import java.util.Scanner;

class Is{
    public static boolean isFim(String str){
        return str.length() == 3 &&
               str.charAt(0) == 'F' &&
               str.charAt(1) == 'I' &&
               str.charAt(2) == 'M';
    }

    public static boolean vogal(String str){
        for(int i = 0; i < str.length(); i++){
            char c = str.charAt(i);
            if(c != 'a'&& c!='e'&& c!='i' && c !='o' && c!= 'u'&&
               c != 'A'&& c!='E'&& c!='I' && c !='O' && c!= 'U'){
                return false;
            }
        }
        return str.length() > 0;
    }

    public static boolean consoante(String str){
        for(int i = 0; i < str.length(); i++){
            char c = str.charAt(i);

            if(!(c >= 'a'&&c <= 'z' || c >= 'A'&&c <= 'Z')){
                return false;
            }

            if( c =='a'||c =='e'||c =='i'||c =='o'||c =='u'||
               c =='A'||c =='E'||c =='I'||c =='O'||c =='U'){
                return false;
            }
        }
        return str.length() > 0;
    }

    public static boolean inteiro(String str){
        for(int i = 0; i < str.length(); i++){
            char c = str.charAt(i);

            if(!(c >= '0' && c <= '9')){
                return false;
            }
        }
        return str.length() > 0;
    }

    public static boolean real(String str){
        int ponto = 0;

        for(int i = 0; i < str.length(); i++){
            char c = str.charAt(i);

            if(c == '.' || c == ','){
                ponto++;
                if(ponto > 1){
                    return false;
                }
            } else if(!(c >= '0' && c <= '9')){
                return false;
            }
        }

        return str.length() > 0;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        String linha = sc.nextLine();

        while(true){
            if(isFim(linha)){
                break;
            }

            String x1 = vogal(linha) ? "SIM" : "NAO";
            String x2 = consoante(linha) ? "SIM" : "NAO";
            String x3 = inteiro(linha) ? "SIM" : "NAO";
            String x4 = real(linha) ? "SIM" : "NAO";

            System.out.println(x1 + " " + x2 + " " + x3 + " " + x4);

            linha = sc.nextLine();
        }

        sc.close();
    }
}
