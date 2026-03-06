#include <stdio.h>
#include <string.h>

int main(void){
    char palavra[100];
    int bts = 0; // contador

    scanf("%s",palavra);

    while(palavra != "FIM"){
        for(int i = 0; i < strlen(palavra); i++ ){
            if(palavra(i) >= 'A' && palavra(i) <= 'Z'){
                bts++;
            }
        }
        printf("%d",bts);
        bts = 0;
        scanf("%s",palavra);
    }

    return 0;
}