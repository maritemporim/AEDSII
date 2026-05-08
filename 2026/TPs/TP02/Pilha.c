#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>

typedef struct Data{
    int dia;
    int mes;
    int ano;
}Data;

typedef struct Hora{
    int hora;
    int minuto;
}Hora;

typedef struct Restaurante{
    int id_restaurante;
    char* nome;
    char* cidade;
    int capacidade;
    double avaliacao;
    char** tipo_cozinha;
    int faixa_preco;
    Hora hora_abertura;
    Hora hora_fechamento;
    Data data_abertura;
    bool aberto;
}Restaurante;

typedef struct ColecaoRestaurante{
    int tamanho;
    Restaurante* restaurante;
}Colecao_Restaurante;

typedef struct pilha{
    int n;
    Restaurante* restaurante;
}Pilha;

Data parse_data(char *s){
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}

void formatar_data(Data* data, char* buffer){
    sprintf(buffer,"%02d/%02d/%04d", data->dia, data->mes, data->ano);
}

Hora parse_hora(char *s){
    Hora h;
    sscanf(s,"%d:%d", &h.hora, &h.minuto);
    return h;
}

void formatar_hora(Hora* hora, char* buffer){
    sprintf(buffer, "%02d:%02d", hora->hora, hora->minuto);
}

void liberar_restaurante(Restaurante* r) {
    free(r->nome);
    free(r->cidade);
    free(r->tipo_cozinha[0]);
    free(r->tipo_cozinha);
}

Restaurante* parse_restaurante(char *s){
    Restaurante* r = (Restaurante*)malloc(sizeof(Restaurante));
    if(r == NULL){
        printf("Nao foi possivel criar restaurante!");
        return NULL;
    }
    char hora_a[6], hora_f[6], data_a[11], nome[100], cidade[100], preco[10], tipo[40], aberto[10];

    sscanf(s, "%d,%[^,],%[^,],%d,%lf,%[^,],%[^,],%[^-]-%[^,],%[^,],%[^\n]",
           &r->id_restaurante, nome, cidade, &r->capacidade,
           &r->avaliacao, tipo, preco, hora_a, hora_f,
           data_a, aberto);
    
    for(int i = 0; aberto[i] != '\0'; i++){
        if(aberto[i] == '\r' || aberto[i] == '\n' || aberto[i] == ' ')
            aberto[i] = '\0';
    }
    r->aberto = (strcmp(aberto, "true") == 0);
    r->hora_abertura = parse_hora(hora_a);
    r->hora_fechamento = parse_hora(hora_f);
    r->data_abertura = parse_data(data_a);

    int tam = 0;
    while(nome[tam] != '\0') tam++;
    r->nome = (char*)malloc((tam + 1) * sizeof(char));
    sprintf(r->nome,"%s", nome);
    
    tam = 0;
    while(cidade[tam] != '\0') tam++;
    r->cidade = (char*)malloc((tam + 1) * sizeof(char));
    sprintf(r->cidade,"%s", cidade);

    tam = 0;
    while(preco[tam] != '\0') tam++;
    r->faixa_preco = tam;

    tam = 0;
    while(tipo[tam] != '\0') tam++;
    for(int i = 0; tipo[i] != '\0'; i++)
        if(tipo[i] == ';') tipo[i] = ',';

    r->tipo_cozinha = (char**)malloc(1 * sizeof(char*));
    r->tipo_cozinha[0] = (char*)malloc((tam + 1) * sizeof(char));
    sprintf(r->tipo_cozinha[0],"%s",tipo);

    return r;
}

void formatar_restaurante(Restaurante* restaurante, char* buffer){
    char hora_fechamento[7], hora_abertura[7], data_abertura[12], str_aberto[6];

    formatar_hora(&restaurante->hora_abertura, hora_abertura);
    formatar_hora(&restaurante->hora_fechamento, hora_fechamento);
    formatar_data(&restaurante->data_abertura, data_abertura);
  
    char f_preco[5];
    int i;
    for(i = 0; i < restaurante->faixa_preco; i++){
        f_preco[i] = '$';
    }
    f_preco[i] = '\0';

    if(restaurante->aberto == true) {
        sprintf(str_aberto, "true");
    }else{
        sprintf(str_aberto, "false");
    }
    sprintf(buffer,"[%d ## %s ## %s ## %d ## %.1lf ## [%s] ## %s ## %s-%s ## %s ## %s]",
        restaurante->id_restaurante, restaurante->nome, restaurante->cidade,
        restaurante->capacidade, restaurante->avaliacao, restaurante->tipo_cozinha[0],
        f_preco, hora_abertura, hora_fechamento, data_abertura, str_aberto);
}

void ler_csv_colecao(Colecao_Restaurante* colecao, char* path){
    
    FILE *arq = fopen(path, "r");
    if(arq == NULL){
        printf("Erro ao abrir arquivo!");
        return;
    }
    char linha[200];

    fgets(linha, sizeof(linha), arq);
   
    int i = 0;
    while(fgets(linha, sizeof(linha), arq) != NULL){
        Restaurante* aux = parse_restaurante(linha);

        colecao->restaurante[i] = *aux;
        i++;
        free(aux);
    }

    fclose(arq);
}

Colecao_Restaurante* ler_csv(){
        FILE *arq = fopen("/tmp/restaurantes.csv", "r");

        if(arq == NULL){
            printf("Erro ao abrir arquivo!");
            return NULL;
        }

        int tam = 0;
        char linha[200];

        while(fgets(linha, sizeof(linha), arq) != NULL){
            tam++;
        }

        fclose(arq);

        Colecao_Restaurante* novaCole = (Colecao_Restaurante*) malloc(sizeof(Colecao_Restaurante));
        if(novaCole == NULL){
            printf("Erro ao alocar Colecao!");
            return NULL;
        }
        novaCole->tamanho = tam - 1;
        novaCole->restaurante = (Restaurante*)malloc((tam - 1) * sizeof(Restaurante));
        if(novaCole->restaurante == NULL){
            printf("Erro ao alocar restaurante!");
            return NULL;
        }
        ler_csv_colecao(novaCole,"/tmp/restaurantes.csv");

      return novaCole;
}

int buscarId(Colecao_Restaurante* colecao, int id_buscado) {
    for (int i = 0; i < colecao->tamanho; i++) {
        if (colecao->restaurante[i].id_restaurante == id_buscado) {
            return i;
        }
    }
    return -1;
}

int transformarInt(char *s){
    int qtdCaracteres;
    for(qtdCaracteres=0; s[qtdCaracteres] != '\0' && s[qtdCaracteres] != '\n'; qtdCaracteres++);

    int contador = 1;
    int resposta = 0;
    for(int i=qtdCaracteres-1; i>=0; i--) {
        resposta += (s[i] - '0') * contador;
        contador *= 10;
    }
    return resposta;
}

void inserirFim(Pilha* r, Restaurante* x, int tam) {
   if(r->n >= tam){
      printf("Erro ao inserir!");
      exit(1);
   }

   r->restaurante[r->n] = *x;
   r->n++;
}

Restaurante removerFim(Pilha* r) {
   if (r->n == 0) {
      printf("Erro ao remover!");
      exit(1);
   }

   return r->restaurante[--r->n];
}

int main(){
    Colecao_Restaurante* cr = ler_csv();

    int tam = 500;

    Pilha *r = (Pilha*)malloc(sizeof(Pilha));
    r->n = 0;
    r->restaurante = (Restaurante*)malloc(tam * sizeof(Restaurante));

    char linha[50];
    scanf("%s", linha);
    while(strcmp(linha, "-1") != 0){
        int id = transformarInt(linha);

        int idBuscado = buscarId(cr, id);
        if(idBuscado != -1){
            inserirFim(r, &cr->restaurante[idBuscado], tam);
        }
        scanf("%s", linha);
    }
    scanf("%s", linha);

    int n = transformarInt(linha);
    while(getchar() != '\n');
    int id;
    char entrada[2];
    for(int i = 0; i < n; i++){
        fgets(linha, sizeof(linha), stdin);
      
        sscanf(linha,"%s %d", entrada, &id);
        if(entrada[0] == 'I'){
            int idx = buscarId(cr, id);
            if(idx != -1)
                inserirFim(r, &cr->restaurante[idx], tam);
        }

        if(entrada[0] == 'R'){
           Restaurante removido = removerFim(r);
           printf("(R)%s\n", removido.nome);
        }
    }
    
    for(int i = r->n - 1; i >= 0; i--){
        char linha[300];
        formatar_restaurante(&r->restaurante[i], linha);
        printf("%s\n", linha);
    }

    for (int i = 0; i < cr->tamanho; i++) {
        liberar_restaurante(&cr->restaurante[i]);
    }
    free(cr->restaurante);
    free(r->restaurante);
    free(r);
    free(cr);
}