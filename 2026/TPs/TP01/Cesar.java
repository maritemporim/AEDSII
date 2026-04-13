import java.util.Scanner;

class Cesar {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        while(sc.hasNextLine()){
            String palavra = sc.nextLine();
            String bts = "";

            for(int i = 0; i < palavra.length(); i++){
                char jk = palavra.charAt(i);
                bts += (char) (jk + 3);
            }

            System.out.println(bts);
        }

        sc.close();
    }
}