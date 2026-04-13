#include <stdio.h>

int isFim(char str[]){
    return str[0]=='F' && str[1]=='I' && str[2]=='M' && str[3]=='\0';
}

int tamanho(char str[], int i){
    if(str[i] == '\0'){
        return i;
    }
    return tamanho(str, i+1);
}

int vogal(char str[], int i){
    if(str[i] == '\0'){
        return i > 0;
    }

    char c = str[i];
    if(c!='a'&&c!='e'&&c!='i'&&c!='o'&&c!='u'&&
       c!='A'&&c!='E'&&c!='I'&&c!='O'&&c!='U'){
        return 0;
    }

    return vogal(str, i+1);
}

int consoante(char str[], int i){
    if(str[i] == '\0'){
        return i > 0;
    }

    char c = str[i];

    if(!((c>='a'&&c<='z') || (c>='A'&&c<='Z'))){
        return 0;
    }

    if(c=='a'||c=='e'||c=='i'||c=='o'||c=='u'||
       c=='A'||c=='E'||c=='I'||c=='O'||c=='U'){
        return 0;
    }

    return consoante(str, i+1);
}

int inteiro(char str[], int i){
    if(str[i] == '\0'){
        return i > 0;
    }

    char c = str[i];

    if(!(c >= '0' && c <= '9')){
        return 0;
    }

    return inteiro(str, i+1);
}

int real(char str[], int i, int ponto){
    if(str[i] == '\0'){
        return i > 0;
    }

    char c = str[i];

    if(c == '.' || c == ','){
        ponto++;
        if(ponto > 1){
            return 0;
        }
    } else if(!(c >= '0' && c <= '9')){
        return 0;
    }

    return real(str, i+1, ponto);
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

        printf("%s %s %s %s\n",
            vogal(str,0) ? "SIM" : "NAO",
            consoante(str,0) ? "SIM" : "NAO",
            inteiro(str,0) ? "SIM" : "NAO",
            real(str,0,0) ? "SIM" : "NAO"
        );
    }

    return 0;
}