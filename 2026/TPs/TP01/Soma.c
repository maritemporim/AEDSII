#include <stdio.h>

int isFim(char str[]){
    return str[0] == 'F' && str[1] == 'I' && str[2] == 'M' && str[3] == '\0';
}

int tamanho(char str[]){
    int i = 0;
    while(str[i] != '\0'){
        i++;
    }
    return i;
}

int somaDigitos(char str[], int i){
    if(str[i] == '\0'){
        return 0;
    }
    return (str[i] - '0') + somaDigitos(str, i + 1);
}

int main(){
    char linha[1000];

    fgets(linha, 1000, stdin);

    while(1){
        int i = 0;

        while(linha[i] != '\0'){
            if(linha[i] == '\n'){
                linha[i] = '\0';
                break;
            }
            i++;
        }

        if(isFim(linha)){
            break;
        }

        printf("%d\n", somaDigitos(linha, 0));

        fgets(linha, 1000, stdin);
    }

    return 0;
}