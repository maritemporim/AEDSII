#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>

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

#define MAX_TIPOS  15
#define MAX_STR    256

typedef struct {
    int    id;
    char   nome[MAX_STR];
    char   cidade[MAX_STR];
    int    capacidade;
    double avaliacao;
    int    n_tipos_cozinha;
    char   tipos_cozinha[MAX_TIPOS][MAX_STR];
    int    faixa_preco;
    Hora   horario_abertura;
    Hora   horario_fechamento;
    Data   data_abertura;
    bool   aberto;
} Restaurante;

int converter_preco(char* str) {
    int count = 0;
    for (int i = 0; str[i] != '\0'; i++)
        if (str[i] == '$') count++;
    return count;
}

void proximo_campo(char** p, char* dest) {
    char* inicio  = *p;
    char* virgula = strchr(inicio, ',');
    if (virgula == NULL) {
        strcpy(dest, inicio);
        *p = inicio + strlen(inicio);
    } else {
        int len = (int)(virgula - inicio);
        strncpy(dest, inicio, len);
        dest[len] = '\0';
        *p = virgula + 1;
    }
}

Restaurante* parse_restaurante(char* s) {
    Restaurante* r = (Restaurante*) malloc(sizeof(Restaurante));
    if (!r) return NULL;

    char  campo[1024];
    char* p = s;

    proximo_campo(&p, campo); r->id = atoi(campo);
    proximo_campo(&p, campo); strncpy(r->nome,   campo, MAX_STR - 1);
    proximo_campo(&p, campo); strncpy(r->cidade, campo, MAX_STR - 1);
    proximo_campo(&p, campo); r->capacidade = atoi(campo);
    proximo_campo(&p, campo); r->avaliacao  = atof(campo);

    proximo_campo(&p, campo);
    r->n_tipos_cozinha = 0;
    char* ini = campo;
    while (*ini != '\0' && r->n_tipos_cozinha < MAX_TIPOS) {
        char* ponto = strchr(ini, ';');
        if (ponto == NULL) {
            if (strlen(ini) > 0)
                strncpy(r->tipos_cozinha[r->n_tipos_cozinha++], ini, MAX_STR - 1);
            break;
        }
        int len = (int)(ponto - ini);
        if (len > 0) {
            strncpy(r->tipos_cozinha[r->n_tipos_cozinha], ini, len);
            r->tipos_cozinha[r->n_tipos_cozinha][len] = '\0';
            r->n_tipos_cozinha++;
        }
        ini = ponto + 1;
    }

    proximo_campo(&p, campo); r->faixa_preco = converter_preco(campo);

    proximo_campo(&p, campo);
    char hora_ab[10], hora_fe[10];
    sscanf(campo, "%[^-]-%s", hora_ab, hora_fe);
    r->horario_abertura   = parse_hora(hora_ab);
    r->horario_fechamento = parse_hora(hora_fe);

    proximo_campo(&p, campo); r->data_abertura = parse_data(campo);

    proximo_campo(&p, campo);
    int len = strlen(campo);
    while (len > 0 && (campo[len-1]=='\n' || campo[len-1]=='\r' || campo[len-1]==' '))
        campo[--len] = '\0';
    r->aberto = (strcmp(campo, "true") == 0);

    return r;
}

void formatar_restaurante(Restaurante* r, char* buffer) {
    char cozinhas[MAX_STR * MAX_TIPOS]; cozinhas[0] = '\0';
    for (int i = 0; i < r->n_tipos_cozinha; i++) {
        strcat(cozinhas, r->tipos_cozinha[i]);
        if (i < r->n_tipos_cozinha - 1) strcat(cozinhas, ",");
    }

    char preco[6]; preco[0] = '\0';
    for (int i = 0; i < r->faixa_preco; i++) strcat(preco, "$");

    char buf_ab[10], buf_fe[10], buf_data[12];
    formatar_hora(&r->horario_abertura,   buf_ab);
    formatar_hora(&r->horario_fechamento, buf_fe);
    formatar_data(&r->data_abertura,      buf_data);

    sprintf(buffer,
        "[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %s-%s ## %s ## %s]",
        r->id, r->nome, r->cidade, r->capacidade, r->avaliacao,
        cozinhas, preco, buf_ab, buf_fe, buf_data,
        r->aberto ? "true" : "false");
}

typedef struct {
    int           tamanho;
    Restaurante** restaurantes;
} Colecao_Restaurantes;

void ler_csv_colecao(Colecao_Restaurantes* colecao, char* path) {
    FILE* f = fopen(path, "r");
    if (!f) {
        fprintf(stderr, "Erro ao abrir: %s\n", path);
        colecao->tamanho      = 0;
        colecao->restaurantes = NULL;
        return;
    }

    colecao->restaurantes = (Restaurante**) malloc(2000 * sizeof(Restaurante*));
    colecao->tamanho = 0;

    char linha[2048];
    bool primeira = true;
    while (fgets(linha, sizeof(linha), f)) {
        if (primeira) { primeira = false; continue; }
        linha[strcspn(linha, "\n")] = '\0';
        linha[strcspn(linha, "\r")] = '\0';
        if (strlen(linha) == 0) continue;
        colecao->restaurantes[colecao->tamanho++] = parse_restaurante(linha);
    }
    fclose(f);
}

Colecao_Restaurantes* ler_csv() {
    Colecao_Restaurantes* c = (Colecao_Restaurantes*) malloc(sizeof(Colecao_Restaurantes));
    ler_csv_colecao(c, "/tmp/restaurantes.csv");
    return c;
}

Restaurante* buscar_por_id(Colecao_Restaurantes* colecao, int id) {
    for (int i = 0; i < colecao->tamanho; i++)
        if (colecao->restaurantes[i]->id == id) return colecao->restaurantes[i];
    return NULL;
}

void liberar_colecao(Colecao_Restaurantes* colecao) {
    for (int i = 0; i < colecao->tamanho; i++) free(colecao->restaurantes[i]);
    free(colecao->restaurantes);
    free(colecao);
}

void selection_sort(Restaurante** arr, int n,
                    long long* comparacoes, long long* movimentacoes) {
    *comparacoes   = 0;
    *movimentacoes = 0;

    for (int i = 0; i < n - 1; i++) {
        int min = i;
        for (int j = i + 1; j < n; j++) {
            (*comparacoes)++;
            if (strcmp(arr[j]->nome, arr[min]->nome) < 0)
                min = j;
        }
        if (min != i) {
            Restaurante* tmp = arr[i];
            arr[i]   = arr[min];
            arr[min] = tmp;
            (*movimentacoes)++;
        }
    }
}

int busca_binaria(Restaurante** arr, int n,
                  char* nome, long long* comparacoes) {
    int esq = 0, dir = n - 1;

    while (esq <= dir) {
        int meio = (esq + dir) / 2;
        (*comparacoes)++;
        int cmp = strcmp(arr[meio]->nome, nome);

        if (cmp == 0) return 1;
        else if (cmp < 0) esq = meio + 1;
        else               dir = meio - 1;
    }
    return 0;
}

int main() {
    Colecao_Restaurantes* colecao = ler_csv();

    int ids[2000];
    int n_ids = 0;
    int id;
    while (scanf("%d", &id) == 1 && id != -1)
        ids[n_ids++] = id;

    Restaurante** base   = (Restaurante**) malloc(n_ids * sizeof(Restaurante*));
    int           n_base = 0;
    for (int i = 0; i < n_ids; i++) {
        Restaurante* r = buscar_por_id(colecao, ids[i]);
        if (r != NULL) base[n_base++] = r;
    }

    long long comp_ord = 0, mov_ord = 0;
    selection_sort(base, n_base, &comp_ord, &mov_ord);

    long long comparacoes = 0;
    char nome[MAX_STR];

    int c;
    while ((c = getchar()) != '\n' && c != EOF);

    struct timespec t_ini, t_fim;
    clock_gettime(CLOCK_MONOTONIC, &t_ini);

    while (fgets(nome, sizeof(nome), stdin)) {
        nome[strcspn(nome, "\n")] = '\0';
        nome[strcspn(nome, "\r")] = '\0';

        if (strcmp(nome, "FIM") == 0) break;
        if (strlen(nome) == 0) continue;

        int achou = busca_binaria(base, n_base, nome, &comparacoes);
        printf("%s\n", achou ? "SIM" : "NAO");
    }

    clock_gettime(CLOCK_MONOTONIC, &t_fim);
    double tempo_ms = (t_fim.tv_sec  - t_ini.tv_sec)  * 1000.0
                    + (t_fim.tv_nsec - t_ini.tv_nsec) / 1e6;

    FILE* log = fopen("885948_binaria.txt", "w");
    if (log) {
        fprintf(log, "885948\t%lld\t%.2f\n", comparacoes, tempo_ms);
        fclose(log);
    }

    free(base);
    liberar_colecao(colecao);
    return 0;
}