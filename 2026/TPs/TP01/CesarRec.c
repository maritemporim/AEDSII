#include <stdio.h>

int isFim(char str[]){
    return str[0]=='F' && str[1]=='I' && str[2]=='M' && str[3]=='\0';
}

void cifrar(char str[], int i){
    if(str[i] == '\0'){
        return;
    }

    printf("%c", str[i] + 3);
    cifrar(str, i + 1);
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

        cifrar(str, 0);
        printf("\n");
    }

    return 0;
}