#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_NAME 300
#define MAX_LINE 1000

typedef struct {
    char name[MAX_NAME];
    char release[50];
} Game;

typedef struct {
    int id;
    Game g;
} Mapa;

Mapa mapa[30000];
int mapaCount = 0;

typedef struct No {
    Game elemento;
    struct No *esq, *dir;
    int altura;
} No;

int altura(No* n) {
    return (n == NULL ? 0 : n->altura);
}

int max(int a, int b) {
    return (a > b ? a : b);
}

No* novoNo(Game g) {
    No* n = (No*) malloc(sizeof(No));
    n->elemento = g;
    n->esq = n->dir = NULL;
    n->altura = 1;
    return n;
}

No* rotacaoDireita(No* y) {
    No* x = y->esq;
    No* T2 = x->dir;

    x->dir = y;
    y->esq = T2;

    y->altura = max(altura(y->esq), altura(y->dir)) + 1;
    x->altura = max(altura(x->esq), altura(x->dir)) + 1;

    return x;
}

No* rotacaoEsquerda(No* x) {
    No* y = x->dir;
    No* T2 = y->esq;

    y->esq = x;
    x->dir = T2;

    x->altura = max(altura(x->esq), altura(x->dir)) + 1;
    y->altura = max(altura(y->esq), altura(y->dir)) + 1;

    return y;
}

int fatorBalanceamento(No* n) {
    return (n == NULL ? 0 : altura(n->esq) - altura(n->dir));
}

No* inserirAVL(No* raiz, Game g) {
    if (raiz == NULL) return novoNo(g);

    int cmp = strcmp(g.name, raiz->elemento.name);

    if (cmp < 0)
        raiz->esq = inserirAVL(raiz->esq, g);
    else if (cmp > 0)
        raiz->dir = inserirAVL(raiz->dir, g);
    else
        return raiz;

    raiz->altura = 1 + max(altura(raiz->esq), altura(raiz->dir));

    int fb = fatorBalanceamento(raiz);

    if (fb > 1 && strcmp(g.name, raiz->esq->elemento.name) < 0)
        return rotacaoDireita(raiz);

    if (fb < -1 && strcmp(g.name, raiz->dir->elemento.name) > 0)
        return rotacaoEsquerda(raiz);

    if (fb > 1 && strcmp(g.name, raiz->esq->elemento.name) > 0) {
        raiz->esq = rotacaoEsquerda(raiz->esq);
        return rotacaoDireita(raiz);
    }

    if (fb < -1 && strcmp(g.name, raiz->dir->elemento.name) < 0) {
        raiz->dir = rotacaoDireita(raiz->dir);
        return rotacaoEsquerda(raiz);
    }

    return raiz;
}

int pesquisarAVL(No* raiz, char nome[]) {
    if (raiz == NULL) {
        printf("NAO\n");
        return 0;
    }

    int cmp = strcmp(nome, raiz->elemento.name);

    if (cmp == 0) {
        printf("SIM\n");
        return 1;
    }

    if (cmp < 0) {
        printf("esq ");
        return pesquisarAVL(raiz->esq, nome);
    } else {
        printf("dir ");
        return pesquisarAVL(raiz->dir, nome);
    }
}

void pesquisarComCaminho(No* raiz, char nome[]) {
    printf("raiz ");
    pesquisarAVL(raiz, nome);
}

void extrairAno(char data[], char ano[]) {
    int len = strlen(data);
    int j = 0;

    for (int i = len - 1; i >= 0; i--) {
        if (data[i] >= '0' && data[i] <= '9') {
            ano[j++] = data[i];
            if (j == 4) break;
        }
    }
    ano[j] = '\0';

    for (int i = 0; i < j/2; i++) {
        char c = ano[i];
        ano[i] = ano[j - i - 1];
        ano[j - i - 1] = c;
    }
}

void parseCSVLine(char* linha, int* id, char nome[], char data[]) {
    char* token;

    token = strtok(linha, ",");
    *id = atoi(token);

    token = strtok(NULL, ",");
    if (token) strcpy(nome, token);
    else nome[0] = '\0';

    token = strtok(NULL, ",");
    if (token) strcpy(data, token);
    else data[0] = '\0';
}

int main() {
    FILE* f = fopen("/tmp/games.csv", "r");
    if (!f) return 0;

    char linha[MAX_LINE];
    if (!fgets(linha, MAX_LINE, f)) {
        fclose(f);
        return 0;
    }

    while (fgets(linha, MAX_LINE, f) != NULL) {
        int id;
        char nome[MAX_NAME] = {0}, data[50] = {0};

        parseCSVLine(linha, &id, nome, data);

        Game g;
        strncpy(g.name, nome, MAX_NAME-1);
        g.name[MAX_NAME-1] = '\0';
        strncpy(g.release, data, sizeof(g.release)-1);
        g.release[sizeof(g.release)-1] = '\0';

        mapa[mapaCount].id = id;
        mapa[mapaCount].g = g;
        mapaCount++;
        if (mapaCount >= (int)(sizeof(mapa)/sizeof(mapa[0]))) break;
    }

    fclose(f);

    No* raiz = NULL;

    while (1) {
        char entrada[50];
        if (scanf("%49s", entrada) != 1) break;
        if (strcmp(entrada, "FIM") == 0) break;

        int id = atoi(entrada);

        for (int i = 0; i < mapaCount; i++) {
            if (mapa[i].id == id) {
                raiz = inserirAVL(raiz, mapa[i].g);
                break;
            }
        }
    }

    getchar();

    while (1) {
        char nomeBusca[MAX_NAME];
        if (!fgets(nomeBusca, MAX_NAME, stdin)) break;
        nomeBusca[strcspn(nomeBusca, "\n")] = '\0';

        if (strcmp(nomeBusca, "FIM") == 0) break;

        int achei = 0;
        Game g;

        for (int i = 0; i < mapaCount; i++) {
            if (strcmp(mapa[i].g.name, nomeBusca) == 0) {
                achei = 1;
                g = mapa[i].g;
                break;
            }
        }

        if (achei) {
            char ano[10];
            extrairAno(g.release, ano);
            printf("%s (%s): ", g.name, ano);
        } else {
            printf("%s:", nomeBusca);
        }

        pesquisarComCaminho(raiz, nomeBusca);
    }

    return 0;
}
