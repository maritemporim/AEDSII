#include <stdio.h>

int isFim(char str[]){
    return str[0]=='F' && str[1]=='I' && str[2]=='M' && str[3]=='\0';
}

int tamanho(char str[]){
    int i = 0;
    while(str[i] != '\0'){
        i++;
    }
    return i;
}

int maiorSub(char str[]){
    int usado[256];
    int max = 0;
    int inicio = 0;

    for(int i = 0; i < 256; i++){
        usado[i] = -1;
    }

    for(int i = 0; str[i] != '\0'; i++){
        if(usado[(unsigned char)str[i]] >= inicio){
            inicio = usado[(unsigned char)str[i]] + 1;
        }

        usado[(unsigned char)str[i]] = i;

        int tam = i - inicio + 1;
        if(tam > max){
            max = tam;
        }
    }

    return max;
}

int main(){
    char str[1000];

    while(fgets(str, 1000, stdin) != NULL){

        int i = 0;
        while(str[i] != '\0'){
            if(str[i] == '\n'){
                str[i] = '\0';
                break;
            }
            i++;
        }

        if(isFim(str)){
            break;
        }

        printf("%d\n", maiorSub(str));
    }

    return 0;
}