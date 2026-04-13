#include <stdio.h>
#include <stdbool.h>

int tamanho(char s[]) {
    int i = 0;
    while (s[i] != '\0') {
        i++;
    }
    return i;
}

char minusculo(char c) {
    if (c >= 'A' && c <= 'Z') {
        return c + 32;
    }
    return c;
}

bool Anagrama(char s1[], char s2[]) {
    int len1 = tamanho(s1);
    int len2 = tamanho(s2);

    if (len1 != len2) return false;

    int cont[256] = {0};

    for (int i = 0; i < len1; i++) {
        cont[(unsigned char)minusculo(s1[i])]++;
    }

    for (int i = 0; i < len2; i++) {
        cont[(unsigned char)minusculo(s2[i])]--;
    }

    for (int i = 0; i < 256; i++) {
        if (cont[i] != 0) return false;
    }

    return true;
}

int main() {
    char s1[100], s2[100];

    while (scanf("%s %s", s1, s2) != EOF) {
        if (Anagrama(s1, s2)) {
            printf("SIM\n");
        } else {
            printf("NAO\n");
        }
    }

    return 0;
}