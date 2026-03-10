import java.util.Scanner;

class Sort {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m;
        int n;

        n = sc.nextInt();
        m = sc.nextInt();

        int[] numeros = new int[n];
        int maior;

        for (int i = 0; i < n; i++) {
            numeros[i] = sc.nextInt();
        }

        maior = numeros[0];

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                if ((numeros[j] % m > numeros[j + 1] % m) && (numeros[j] % m != numeros[j + 1] % m)) {
                    maior = numeros[j];
                    swap(numeros, j);
                } else {
                    if (numeros[j] % 2 == 0 && numeros[j + 1] % 3 == 0 && numeros[j] % m == numeros[j + 1] % m) {
                        maior = numeros[j];
                        swap(numeros, j);
                    }

                    if (numeros[j] % 3 == 0 && numeros[j + 1] % 2 == 0 && numeros[j] % m == numeros[j + 1] % m) {
                        maior = numeros[j + 1];
                        swap(numeros, j + 1);
                    }
                }

            }
        }

        for(int i = 0; i < n; i++){
            System.out.println(numeros[i]);
        }
    }

    static void swap(int[] numeros, int j) {
        int tmp = numeros[j];
        numeros[j] = numeros[j + 1];
        numeros[j + 1] = tmp;
    }
}
