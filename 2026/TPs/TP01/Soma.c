#include <stdio.h>

int somaDigitos(int n) {
    if (n < 10) {
        return n;
    }
    return (n % 10) + somaDigitos(n / 10);
}

int main() {
    int n;

    while (scanf("%d", &n) != EOF) {
        if (n < 0) n = -n;
        printf("%d\n", somaDigitos(n));
    }

    return 0;
}