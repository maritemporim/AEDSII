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

int anagrama(char a[], char b[]){
    int A1[256] = {0};
    int B1[256] = {0};

    int tamA = tamanho(a);
    int tamB = tamanho(b);

    if(tamA != tamB){
        return 0;
    }

    for(int i = 0; i < tamA; i++){
        A1[(unsigned char)a[i]]++;
        B1[(unsigned char)b[i]]++;
    }

    for(int i = 0; i < 256; i++){
        if(A1[i] != B1[i]){
            return 0;
        }
    }

    return 1;
}

int main(){
    char str1[1000], str2[1000];

    while(fgets(str1, 1000, stdin) != NULL){

        int i = 0;
        while(str1[i] != '\0'){
            if(str1[i] == '\n'){
                str1[i] = '\0';
                break;
            }
            i++;
        }

        if(isFim(str1)){
            break;
        }

        if(fgets(str2, 1000, stdin) == NULL){
            break;
        }

        i = 0;
        while(str2[i] != '\0'){
            if(str2[i] == '\n'){
                str2[i] = '\0';
                break;
            }
            i++;
        }

        if(anagrama(str1, str2)){
            printf("SIM\n");
        } else {
            printf("NAO\n");
        }
    }

    return 0;
}