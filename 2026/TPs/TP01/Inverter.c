#include <stdio.h>

int tamanho(char str[]){
    int i = 0;
    while(str[i] != '\0'){
        i++;
    }
    return i;
}

int isFim(char str[]){
    return tamanho(str) == 3 &&
           str[0] == 'F' &&
           str[1] == 'I' &&
           str[2] == 'M';
}

void inverter(char str[]){
    int tam = tamanho(str);

    for(int i = tam - 1; i >= 0; i--){
        printf("%c", str[i]);
    }
    printf("\n");
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

        inverter(linha);

        fgets(linha, 1000, stdin);
    }

    return 0;
}