#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <stdbool.h>

/* ==================== TIPOS ==================== */

typedef struct {
    int hora;
    int minuto;
} Hora;

typedef struct {
    int ano;
    int mes;
    int dia;
} Data;

typedef struct {
    int    id;
    char  *nome;
    char  *cidade;
    int    capacidade;
    double avaliacao;
    char **tipos_cozinha;
    int    n_tipos_cozinha;
    int    faixa_preco;
    Hora   horario_abertura;
    Hora   horario_fechamento;
    Data   data_abertura;
    bool   aberto;
} Restaurante;

typedef struct {
    int           tamanho;
    Restaurante **restaurantes;
} Colecao_Restaurantes;

/* ==================== AUX ==================== */

static char *dup_str(const char *s) {
    char *r = malloc(strlen(s) + 1);
    strcpy(r, s);
    return r;
}

/* SPLIT SEM STRTOK */
void split_sem_strtok(char *str, char delim, char ***out, int *count) {
    int n = 1;
    for (int i = 0; str[i]; i++)
        if (str[i] == delim) n++;

    char **result = malloc(n * sizeof(char*));

    int idx = 0;
    char *inicio = str;

    for (int i = 0; ; i++) {
        if (str[i] == delim || str[i] == '\0') {
            int tam = &str[i] - inicio;

            char *palavra = malloc(tam + 1);
            strncpy(palavra, inicio, tam);
            palavra[tam] = '\0';

            result[idx++] = palavra;

            if (str[i] == '\0') break;
            inicio = &str[i + 1];
        }
    }

    *out = result;
    *count = idx;
}

/* ==================== DATA ==================== */

Data parse_data(char *s) {
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}

void formatar_data(Data *data, char *buffer) {
    sprintf(buffer, "%02d/%02d/%04d", data->dia, data->mes, data->ano);
}

/* ==================== HORA ==================== */

Hora parse_hora(char *s) {
    Hora h;
    sscanf(s, "%d:%d", &h.hora, &h.minuto);
    return h;
}

void formatar_hora(Hora *hora, char *buffer) {
    sprintf(buffer, "%02d:%02d", hora->hora, hora->minuto);
}

/* ==================== CSV ==================== */

static char *next_csv_field(char **ptr) {
    char *start = *ptr;
    char *out   = malloc(strlen(start) + 1);
    int   i     = 0;

    if (*start == '"') {
        start++;
        while (*start && *start != '"') out[i++] = *start++;
        if (*start == '"') start++;
        if (*start == ',') start++;
    } else {
        while (*start && *start != ',') out[i++] = *start++;
        if (*start == ',') start++;
    }

    out[i] = '\0';

    while (i > 0 && (out[i-1] == '\r' || out[i-1] == '\n'))
        out[--i] = '\0';

    *ptr = start;
    return out;
}

/* ==================== PARSE ==================== */

Restaurante *parse_restaurante(char *s) {
    char *ptr = s;
    Restaurante *r = malloc(sizeof(Restaurante));
    char *f;

    f = next_csv_field(&ptr);
    r->id = atoi(f); free(f);

    r->nome = next_csv_field(&ptr);
    r->cidade = next_csv_field(&ptr);

    f = next_csv_field(&ptr);
    r->capacidade = atoi(f); free(f);

    f = next_csv_field(&ptr);
    r->avaliacao = atof(f); free(f);

    /* SEM STRTOK */
    f = next_csv_field(&ptr);
    split_sem_strtok(f, ';', &r->tipos_cozinha, &r->n_tipos_cozinha);
    free(f);

    f = next_csv_field(&ptr);
    int fp = 0;
    for (int i = 0; f[i]; i++) if (f[i] == '$') fp++;
    r->faixa_preco = fp;
    free(f);

    f = next_csv_field(&ptr);
    char ab[6], fe[6];
    sscanf(f, "%5[^-]-%5s", ab, fe);
    r->horario_abertura   = parse_hora(ab);
    r->horario_fechamento = parse_hora(fe);
    free(f);

    f = next_csv_field(&ptr);
    r->data_abertura = parse_data(f);
    free(f);

    f = next_csv_field(&ptr);
    r->aberto = (strcmp(f, "true") == 0);
    free(f);

    return r;
}

/* ==================== FORMAT ==================== */

void formatar_restaurante(Restaurante *r, char *buffer) {
    char tipos[512] = "";
    for (int i = 0; i < r->n_tipos_cozinha; i++) {
        if (i > 0) strcat(tipos, ",");
        strcat(tipos, r->tipos_cozinha[i]);
    }

    char fp[6] = "";
    for (int i = 0; i < r->faixa_preco; i++) strcat(fp, "$");

    char ha[6], hf[6];
    formatar_hora(&r->horario_abertura, ha);
    formatar_hora(&r->horario_fechamento, hf);

    char da[11];
    formatar_data(&r->data_abertura, da);

    sprintf(buffer,
        "[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %s-%s ## %s ## %s]",
        r->id, r->nome, r->cidade, r->capacidade, r->avaliacao,
        tipos, fp, ha, hf, da, r->aberto ? "true" : "false");
}

/* ==================== COLECAO ==================== */

void ler_csv_colecao(Colecao_Restaurantes *col, char *path) {
    FILE *fp = fopen(path, "r");
    if (!fp) { fprintf(stderr, "Erro ao abrir %s\n", path); exit(1); }

    char line[4096];
    int n = 0;

    fgets(line, sizeof(line), fp);
    while (fgets(line, sizeof(line), fp)) n++;

    rewind(fp);
    fgets(line, sizeof(line), fp);

    col->tamanho = n;
    col->restaurantes = malloc(n * sizeof(Restaurante *));

    for (int i = 0; i < n; i++) {
        fgets(line, sizeof(line), fp);
        line[strcspn(line, "\n")] = '\0';
        col->restaurantes[i] = parse_restaurante(line);
    }

    fclose(fp);
}

Colecao_Restaurantes *ler_csv(void) {
    Colecao_Restaurantes *col = malloc(sizeof(Colecao_Restaurantes));
    ler_csv_colecao(col, "/tmp/restaurantes.csv");
    return col;
}

/* ==================== COUNTING SORT ==================== */

static long long comparacoes  = 0;
static long long movimentacoes = 0;

void counting_sort(Restaurante **arr, int n) {
    if (n <= 0) return;

    int min_cap = arr[0]->capacidade;
    int max_cap = arr[0]->capacidade;

    for (int i = 1; i < n; i++) {
        comparacoes++;
        if (arr[i]->capacidade < min_cap) min_cap = arr[i]->capacidade;
        comparacoes++;
        if (arr[i]->capacidade > max_cap) max_cap = arr[i]->capacidade;
    }

    int range = max_cap - min_cap + 1;

    int *count = calloc(range, sizeof(int));
    for (int i = 0; i < n; i++)
        count[arr[i]->capacidade - min_cap]++;

    for (int i = 1; i < range; i++)
        count[i] += count[i - 1];

    Restaurante **output = malloc(n * sizeof(Restaurante *));
    for (int i = n - 1; i >= 0; i--) {
        int idx = count[arr[i]->capacidade - min_cap] - 1;
        output[idx] = arr[i];
        count[arr[i]->capacidade - min_cap]--;
        movimentacoes++;
    }

    for (int i = 0; i < n; i++) {
        arr[i] = output[i];
        movimentacoes++;
    }

    free(output);
    free(count);
}

/* ==================== MAIN ==================== */

int main(void) {
    Colecao_Restaurantes *col = ler_csv();

    int n = col->tamanho;
    Restaurante **arr = malloc(n * sizeof(Restaurante *));
    for (int i = 0; i < n; i++) arr[i] = col->restaurantes[i];

    struct timespec t0, t1;
    clock_gettime(CLOCK_MONOTONIC, &t0);
    counting_sort(arr, n);
    clock_gettime(CLOCK_MONOTONIC, &t1);

    double tempo = (t1.tv_sec - t0.tv_sec) * 1e9 + (t1.tv_nsec - t0.tv_nsec);

    int id;
    char buf[2048];

    while (scanf("%d", &id) == 1 && id != -1) {
        for (int i = 0; i < n; i++) {
            if (col->restaurantes[i]->id == id) {
                formatar_restaurante(col->restaurantes[i], buf);
                printf("%s\n", buf);
                break;
            }
        }
    }

    FILE *log = fopen("matricula_countingsort.txt", "w");
    if (log) {
        fprintf(log, "matricula\t%lld\t%lld\t%.0f\n",
                comparacoes, movimentacoes, tempo);
        fclose(log);
    }

    free(arr);

    for (int i = 0; i < n; i++) {
        Restaurante *r = col->restaurantes[i];
        free(r->nome);
        free(r->cidade);
        for (int j = 0; j < r->n_tipos_cozinha; j++)
            free(r->tipos_cozinha[j]);
        free(r->tipos_cozinha);
        free(r);
    }

    free(col->restaurantes);
    free(col);

    return 0;
}