import java.util.Scanner;

class Aleatorio{
    public static void main(String[] args){
    Random gerador = new Random();
    gerador.setSpeed(4);
    System.out.println((char)('a' + (Math.abs(gerador.nextInt()) % 26)));

}

}
