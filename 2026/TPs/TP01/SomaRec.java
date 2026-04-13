import java.util.Scanner;

class SomaRec {

    public static int somaDigitos(int n) {
        if (n < 10) {
            return n;
        }
        return (n % 10) + somaDigitos(n / 10);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextInt()) {
            int n = sc.nextInt();
            if (n < 0) n = -n;
            System.out.println(somaDigitos(n));
        }

        sc.close();
    }
}