#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

typedef struct {
    int ano, mes, dia;
} Data;

typedef struct {
    int hora, minuto;
} Hora;

typedef struct {
    int id, capacidade;
    char nome[100], cidade[100];
    double avaliacao;
    char tipos[10][50];
    int qtdTipos;
    int faixaPreco;
    Hora abertura, fechamento;
    Data data;
    bool aberto;
} Restaurante;

int contarPreco(char* s) {
    int c = 0;
    for (int i = 0; s[i]; i++) if (s[i] == '$') c++;
    return c;
}

Hora parseHora(char* s) {
    Hora h;
    sscanf(s, "%d:%d", &h.hora, &h.minuto);
    return h;
}

Data parseData(char* s) {
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}

Restaurante* parseRestaurante(char* linha) {
    Restaurante* r = malloc(sizeof(Restaurante));

    char tiposRaw[200], preco[10], horario[20], dataStr[20], abertoStr[10];

    sscanf(linha, "%d,%[^,],%[^,],%d,%lf,%[^,],%[^,],%[^,],%[^,],%s",
           &r->id, r->nome, r->cidade, &r->capacidade, &r->avaliacao,
           tiposRaw, preco, horario, dataStr, abertoStr);

    r->faixaPreco = contarPreco(preco);

    r->qtdTipos = 0;
    char* token = strtok(tiposRaw, ";");
    while (token != NULL) {
        strcpy(r->tipos[r->qtdTipos++], token);
        token = strtok(NULL, ";");
    }

    char h1[10], h2[10];
    sscanf(horario, "%[^-]-%s", h1, h2);
    r->abertura = parseHora(h1);
    r->fechamento = parseHora(h2);

    r->data = parseData(dataStr);
    r->aberto = strcmp(abertoStr, "true") == 0;

    return r;
}

void formatar(Restaurante* r) {
    char data[20], h1[10], h2[10];

    sprintf(data, "%02d/%02d/%04d", r->data.dia, r->data.mes, r->data.ano);
    sprintf(h1, "%02d:%02d", r->abertura.hora, r->abertura.minuto);
    sprintf(h2, "%02d:%02d", r->fechamento.hora, r->fechamento.minuto);

    printf("[%d ## %s ## %s ## %d ## %.1lf ## [", r->id, r->nome, r->cidade, r->capacidade, r->avaliacao);

    for (int i = 0; i < r->qtdTipos; i++) {
        printf("%s", r->tipos[i]);
        if (i < r->qtdTipos - 1) printf(",");
    }

    printf("] ## ");

    for (int i = 0; i < r->faixaPreco; i++) printf("$");

    printf(" ## %s-%s ## %s ## %s]\n",
           h1, h2, data,
           r->aberto ? "true" : "false");
}

typedef struct {
    Restaurante* array[1000];
    int n;
} Colecao;

Colecao lerCsv() {
    Colecao c;
    c.n = 0;

    FILE* f = fopen("/tmp/restaurantes.csv", "r");
    char linha[512];

    fgets(linha, sizeof(linha), f);

    while (fgets(linha, sizeof(linha), f)) {
        linha[strcspn(linha, "\n")] = 0;
        c.array[c.n++] = parseRestaurante(linha);
    }

    fclose(f);
    return c;
}

Restaurante* buscarPorId(Colecao* c, int id) {
    for (int i = 0; i < c->n; i++) {
        if (c->array[i]->id == id) return c->array[i];
    }
    return NULL;
}

void countingSort(Restaurante* arr[], int n) {
    int max = arr[0]->capacidade;

    for (int i = 1; i < n; i++) {
        if (arr[i]->capacidade > max) {
            max = arr[i]->capacidade;
        }
    }

    int* count = calloc(max + 1, sizeof(int));
    Restaurante** ordenado = malloc(n * sizeof(Restaurante*));

    for (int i = 0; i < n; i++) {
        count[arr[i]->capacidade]++;
    }

    for (int i = 1; i <= max; i++) {
        count[i] += count[i - 1];
    }

    for (int i = n - 1; i >= 0; i--) {
        ordenado[count[arr[i]->capacidade] - 1] = arr[i];
        count[arr[i]->capacidade]--;
    }

    for (int i = 0; i < n; i++) {
        arr[i] = ordenado[i];
    }

    free(count);
    free(ordenado);
}

int main() {
    Colecao base = lerCsv();

    Restaurante* lista[1000];
    int n = 0;

    int id;
    while (scanf("%d", &id) && id != -1) {
        lista[n++] = buscarPorId(&base, id);
    }

    countingSort(lista, n);

    for (int i = 0; i < n; i++) {
        formatar(lista[i]);
    }

    return 0;
}