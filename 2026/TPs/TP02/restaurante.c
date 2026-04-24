#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

typedef struct {
    int ano;
    int mes;
    int dia;
} Data;

Data parse_data(char* s) {
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}

void formatar_data(Data* data, char* buffer) {
    sprintf(buffer, "%02d/%02d/%04d", data->dia, data->mes, data->ano);
}

typedef struct {
    int hora;
    int minuto;
} Hora;

Hora parse_hora(char* s) {
    Hora h;
    sscanf(s, "%d:%d", &h.hora, &h.minuto);
    return h;
}

void formatar_hora(Hora* hora, char* buffer) {
    sprintf(buffer, "%02d:%02d", hora->hora, hora->minuto);
}

#define MAX_TIPOS_COZINHA 15
#define MAX_STR 256

typedef struct {
    int id;
    char nome[MAX_STR];
    char cidade[MAX_STR];
    int capacidade;
    double avaliacao;
    int n_tipos_cozinha;          // quantidade de tipos
    char tipos_cozinha[MAX_TIPOS_COZINHA][MAX_STR];
    int faixa_preco;              // 1 a 4
    Hora horario_abertura;
    Hora horario_fechamento;
    Data data_abertura;
    bool aberto;
} Restaurante;

int converter_preco(char* str) {
    int count = 0;
    for (int i = 0; str[i] != '\0'; i++) {
        if (str[i] == '$') count++;
    }
    return count;
}

// retorna ponteiro para restaurante alocado
Restaurante* parse_restaurante(char* s) {
    Restaurante* r = (Restaurante*) malloc(sizeof(Restaurante));
    if (!r) return NULL;

    char nome[MAX_STR], cidade[MAX_STR];
    char tipos[MAX_STR], preco[10];
    char hora_ab[10], hora_fe[10];
    char data[12], aberto[10];

    sscanf(s, "%d,%[^,],%[^,],%d,%lf,%[^,],%[^,],%[^-]-%[^,],%[^,],%[^\n]", &r->id, nome, cidade, &r->capacidade, &r->avaliacao, tipos, preco, hora_ab, hora_fe, data, aberto
    );

    strncpy(r->nome, nome, MAX_STR - 1);
    r->nome[MAX_STR - 1] = '\0';

    strncpy(r->cidade, cidade, MAX_STR - 1);
    r->cidade[MAX_STR - 1] = '\0';

    // troca ; por ,
    r->n_tipos_cozinha = 0;
    for (int i = 0; tipos[i] != '\0'; i++) {
        if (tipos[i] == ';') tipos[i] = ',';
    }

    strncpy(r->tipos_cozinha[0], tipos, MAX_STR - 1);
    r->tipos_cozinha[0][MAX_STR - 1] = '\0';
    r->n_tipos_cozinha = 1;
    r->faixa_preco = converter_preco(preco);
    r->horario_abertura = parse_hora(hora_ab);
    r->horario_fechamento = parse_hora(hora_fe);
    r->data_abertura = parse_data(data);

    for (int i = 0; aberto[i] != '\0'; i++) {
        if (aberto[i] == '\r' || aberto[i] == '\n' || aberto[i] == ' ')
            aberto[i] = '\0';
    }
    r->aberto = (strcmp(aberto, "true") == 0);

    return r;
}

void formatar_restaurante(Restaurante* r, char* buffer) {
    // monta tipos de cozinha separados por virgula
    char cozinhas[MAX_STR * MAX_TIPOS_COZINHA] = "";
    for (int i = 0; i < r->n_tipos_cozinha; i++) {
        strcat(cozinhas, r->tipos_cozinha[i]);
        if (i < r->n_tipos_cozinha - 1) strcat(cozinhas, ",");
    }

    char preco[6] = "";
    for (int i = 0; i < r->faixa_preco; i++) strcat(preco, "$");

    char buf_ab[10], buf_fe[10], buf_data[12];
    formatar_hora(&r->horario_abertura,   buf_ab);
    formatar_hora(&r->horario_fechamento, buf_fe);
    formatar_data(&r->data_abertura,      buf_data);

    sprintf(buffer, "[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %s-%s ## %s ## %s]", r->id, r->nome, r->cidade, r->capacidade, r->avaliacao, cozinhas, preco, buf_ab, buf_fe, buf_data, r->aberto ? "true" : "false"
    );
}

typedef struct {
    int tamanho;
    Restaurante** restaurantes;  // arranjo de ponteiros
} Colecao_Restaurantes;

// le o CSV aloca e preenche a coleçao
void ler_csv_colecao(Colecao_Restaurantes* colecao, char* path) {
    FILE* f = fopen(path, "r");
    if (f == NULL) {
        fprintf(stderr, "Erro ao abrir o arquivo: %s\n", path);
        return;
    }

    // capacidade inicial
    int capacidade = 1000;
    colecao->restaurantes = (Restaurante**) malloc(capacidade * sizeof(Restaurante*));
    colecao->tamanho = 0;

    char linha[2048];
    bool primeira_linha = true;

    while (fgets(linha, sizeof(linha), f)) {
        if (primeira_linha) { // pula cabeçalho
            primeira_linha = false;
            continue;
        }
        // remove '\n' do final
        linha[strcspn(linha, "\n")] = '\0';
        if (strlen(linha) == 0) continue;

        colecao->restaurantes[colecao->tamanho] = parse_restaurante(linha);
        colecao->tamanho++;
    }

    fclose(f);
}

Colecao_Restaurantes* ler_csv() {
    Colecao_Restaurantes* colecao =
        (Colecao_Restaurantes*) malloc(sizeof(Colecao_Restaurantes));
    ler_csv_colecao(colecao, "/tmp/restaurantes.csv");
    return colecao;
}

// libera toda a memoria alocada
void liberar_colecao(Colecao_Restaurantes* colecao) {
    for (int i = 0; i < colecao->tamanho; i++) {
        free(colecao->restaurantes[i]);
    }
    free(colecao->restaurantes);
    free(colecao);
}

void printar_restaurante(Restaurante* r) {
    char cozinhas[1024] = "";
    for (int i = 0; i < r->n_tipos_cozinha; i++) {
        strcat(cozinhas, r->tipos_cozinha[i]);
        if (i < r->n_tipos_cozinha - 1) strcat(cozinhas, ",");
    }

    char preco[6] = "";
    for (int i = 0; i < r->faixa_preco; i++) strcat(preco, "$");

    char h1[10], h2[10], data[12];
    formatar_hora(&r->horario_abertura, h1);
    formatar_hora(&r->horario_fechamento, h2);
    formatar_data(&r->data_abertura, data);

    printf("[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %s-%s ## %s ## %s]\n",
        r->id, r->nome, r->cidade, r->capacidade, r->avaliacao,
        cozinhas, preco, h1, h2, data,
        r->aberto ? "true" : "false"
    );
}

int main() {
    Colecao_Restaurantes* colecao = ler_csv();

    int id;

    while (scanf("%d", &id) == 1 && id != -1) {
        for (int i = 0; i < colecao->tamanho; i++) {
            if (colecao->restaurantes[i]->id == id) {
                printar_restaurante(colecao->restaurantes[i]);
                break;
            }
        }
    }

    liberar_colecao(colecao);
    return 0;
}