#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>

typedef struct {
    int id;
    char* name;
    char* releaseDate;
    int estimatedOwners;
    float price;
    char** supportedLanguages;
    int numSupportedLanguages;
    float mediaScore; 
    int achievements;
    float userScore;
    char** publishers;
    int numPublishers;
    char** developers;
    int numDevelopers;
    char** categories;
    int numCategories;
    char** genres;
    int numGenres;
    char** tags;
    int numTags;
} Game;

// converte string para int
int converteParaInt(const char* s){
    if(!s) return 0;
    while(isspace((unsigned char)*s)) s++;

    int sinalzinho = 1;
    if(*s=='+' || *s=='-'){
        if(*s=='-') sinalzinho = -1;
        s++;
    }

    int numero = 0;
    while(isdigit((unsigned char)*s)){
        numero = numero*10 + (*s - '0');
        s++;
    }

    return sinalzinho * numero;
}

// converte string para float (trocando vírgula por ponto)
float converteParaFloat(const char* s){
    if(!s) return 0.0f;
    char buff[128];
    int idx = 0;

    // limpando o texto pra evitar que lixo entre
    while(*s && idx < 120){
        if(*s == ',')
            buff[idx++] = '.';
        else if(!isspace((unsigned char)*s))
            buff[idx++] = *s;
        s++;
    }

    buff[idx] = '\0';
    return (float)atof(buff);
}

// duplico e tiro espaços laterais 
static char* strtrimdup(const char* str){
    if(!str) return strdup("");

    const char *ini = str;
    const char *fim = str + strlen(str);

    while(ini < fim && isspace((unsigned char)*ini)) ini++;
    while(fim > ini && isspace((unsigned char)fim[-1])) fim--;

    size_t t = (size_t)(fim - ini);
    char* novo = (char*)malloc(t + 1);

    if(!novo) return strdup("");

    memcpy(novo, ini, t);
    novo[t] = '\0';
    return novo;
}

// formata datas
char* formatarData(const char* in){
    if(!in || !*in) return strdup("01/01/0000");

    size_t L = strlen(in);
    char temp[256];

    // tira aspas se tiver
    if(in[0]=='"' && in[L-1]=='"' && L>=2){
        memcpy(temp, in+1, L-2);
        temp[L-2] = '\0';
    } else {
        strncpy(temp, in, sizeof(temp)-1);
        temp[sizeof(temp)-1] = '\0';
    }

    char* clean = strtrimdup(temp);
    if(*clean == '\0'){
        free(clean);
        return strdup("01/01/0000");
    }

    // descobrir mês
    const char* abvs[]    = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    const char* numeros[] = {"01","02","03","04","05","06","07","08","09","10","11","12"};

    char dia[3] = "01";
    char mes[3] = "01";
    char ano[5] = "0000";

    char *tok, *ctx = NULL;

    tok = strtok_r(clean, " ", &ctx);
    char* p1 = tok ? strdup(tok) : strdup("");

    tok = strtok_r(NULL, " ", &ctx);
    char* p2 = tok ? strdup(tok) : strdup("");

    tok = strtok_r(NULL, " ", &ctx);
    char* p3 = tok ? strdup(tok) : strdup("");

    if(*p1 && *p2 && *p3){
        for(int j=0;j<12;j++){
            if(strncmp(p1, abvs[j], 3) == 0){
                strcpy(mes, numeros[j]);
                break;
            }
        }

        char* virg = strchr(p2, ',');
        if(virg) *virg = '\0';

        if(*p2) snprintf(dia, sizeof(dia), "%02d", converteParaInt(p2));
        if(strlen(p3)==4) strcpy(ano, p3);
    }
    else if(*p1 && *p2){
        for(int j=0;j<12;j++){
            if(strncmp(p1, abvs[j], 3)==0){
                strcpy(mes, numeros[j]);
                break;
            }
        }
        if(strlen(p2)==4) strcpy(ano, p2);
    }
    else if(*p1){
        if(strlen(p1)==4) strcpy(ano, p1);
    }

    free(p1); free(p2); free(p3); free(clean);

    char* out = (char*)malloc(11);
    if(!out) return strdup("01/01/0000");

    snprintf(out, 11, "%s/%s/%s", dia, mes, ano);
    return out;
}

// ['a','b'] vira vetor 
char** extrairLista(const char* entradaBruta, int* qItens){
    *qItens = 0;
    if(!entradaBruta || !*entradaBruta) return NULL;

    char temp[4096];
    int j = 0;

    // tiro colchetes e aspas
    for(size_t k=0;k<strlen(entradaBruta)&&j<4095;k++){
        char c = entradaBruta[k];
        if(c!='[' && c!=']' && c!='\'' && c!='"')
            temp[j++] = c;
    }
    temp[j]='\0';

    char* limpinho = strtrimdup(temp);
    if(*limpinho=='\0'){
        free(limpinho);
        return NULL;
    }

    int qtd = 1;
    for(int k=0; limpinho[k]; k++)
        if(limpinho[k]==',') qtd++;

    char** lista = (char**)malloc(qtd * sizeof(char*));
    if(!lista){
        free(limpinho);
        return NULL;
    }

    char *tok, *ctx = NULL;
    tok = strtok_r(limpinho, ",", &ctx);

    while(tok){
        lista[*qItens] = strtrimdup(tok);
        (*qItens)++;
        tok = strtok_r(NULL, ",", &ctx);
    }

    free(limpinho);
    return lista;
}

// quebra linha csv em campos
char** quebrarCSV(const char* linha, int* qtdCampos){
    *qtdCampos = 0;

    int max = 14;
    char** out = (char**)malloc(max*sizeof(char*));
    if(!out) return NULL;

    bool asp = false;
    int ini = 0;
    size_t n = strlen(linha);

    // percorrendo caractere por caractere
    for(size_t k=0;k<=n;k++){
        char ch = (k==n ? ',' : linha[k]);

        if(ch=='"'){
            asp = !asp; // alterno entre dentro/fora das aspas
        }
        else if(ch==',' && !asp){
            size_t tam = k - ini;

            char* tmp = (char*)malloc(tam+1);
            strncpy(tmp, linha+ini, tam);
            tmp[tam]='\0';

            // tiro aspas internas
            if(*qtdCampos==1 || *qtdCampos==9){
                size_t L = strlen(tmp);
                if(L>=2 && tmp[0]=='"' && tmp[L-1]=='"'){
                    tmp[L-1]='\0';
                    char* z = strdup(tmp+1);
                    free(tmp);
                    tmp = z;
                }
            }

            out[*qtdCampos] = tmp;
            (*qtdCampos)++;
            ini = (int)k+1;
        }
    }

    return out;
}

// imprime
void imprimirInfoJogoIdx(int idx, const Game* jogo){
    printf("[%d] => %d ## %s ## %s ## %d ## %.2f ## [",
           idx, jogo->id, jogo->name, jogo->releaseDate, jogo->estimatedOwners, jogo->price);

    for(int j=0;j<jogo->numSupportedLanguages;j++)
        printf("%s%s", jogo->supportedLanguages[j], (j==jogo->numSupportedLanguages-1)?"":", ");

    printf("] ## %d ## %.1f ## %d ## [",
           (int)jogo->mediaScore, (float)jogo->achievements, (int)jogo->userScore);

    for(int j=0;j<jogo->numPublishers;j++)
        printf("%s%s", jogo->publishers[j], (j==jogo->numPublishers-1)?"":", ");

    printf("] ## [");

    for(int j=0;j<jogo->numDevelopers;j++)
        printf("%s%s", jogo->developers[j], (j==jogo->numDevelopers-1)?"":", ");

    printf("] ## [");

    for(int j=0;j<jogo->numCategories;j++)
        printf("%s%s", jogo->categories[j], (j==jogo->numCategories-1)?"":", ");

    printf("] ## [");

    for(int j=0;j<jogo->numGenres;j++)
        printf("%s%s", jogo->genres[j], (j==jogo->numGenres-1)?"":", ");

    printf("] ## [");

    for(int j=0;j<jogo->numTags;j++)
        printf("%s%s", jogo->tags[j], (j==jogo->numTags-1)?"":", ");

    printf("] ##\n");
}

// limpa memoria de tudo que foi mallocado dentro do Game
void liberarMemoriaJogo(Game* g){
    if(!g) return;

    free(g->name);
    free(g->releaseDate);

    for(int j=0;j<g->numSupportedLanguages;j++) free(g->supportedLanguages[j]);
    free(g->supportedLanguages);

    for(int j=0;j<g->numPublishers;j++) free(g->publishers[j]);
    free(g->publishers);

    for(int j=0;j<g->numDevelopers;j++) free(g->developers[j]);
    free(g->developers);

    for(int j=0;j<g->numCategories;j++) free(g->categories[j]);
    free(g->categories);

    for(int j=0;j<g->numGenres;j++) free(g->genres[j]);
    free(g->genres);

    for(int j=0;j<g->numTags;j++) free(g->tags[j]);
    free(g->tags);
}

// carrega csv inteiro 
Game* carregarCSV(const char* caminho, int* totalJogos){
    FILE* arq = fopen(caminho, "r");
    if(!arq) return NULL;

    int capMax = 1024;
    *totalJogos = 0;

    Game* meusJogos = (Game*)malloc(capMax * sizeof(Game));
    if(!meusJogos){
        fclose(arq);
        return NULL;
    }

    char linhaCSV[8192];

    // ignora a primeira linha
    if(!fgets(linhaCSV, sizeof(linhaCSV), arq)){
        fclose(arq);
        free(meusJogos);
        return NULL;
    }

    // leio jogo por jogo
    while(fgets(linhaCSV, sizeof(linhaCSV), arq)){
        size_t L = strlen(linhaCSV);
        if(L && linhaCSV[L-1]=='\n') linhaCSV[L-1]='\0';

        int qtdCampos = 0;
        char** campos = quebrarCSV(linhaCSV, &qtdCampos);

        if(!campos) continue;

        if(qtdCampos >= 14){
            // aumento a capacidade se precisar
            if(*totalJogos >= capMax){
                capMax *= 2;
                Game* novo = (Game*)realloc(meusJogos, capMax*sizeof(Game));
                if(!novo){
                    for(int i=0;i<qtdCampos;i++) free(campos[i]);
                    free(campos);
                    break;
                }
                meusJogos = novo;
            }

            Game* jogoQueAchei = &meusJogos[*totalJogos];

            jogoQueAchei->id = converteParaInt(campos[0]);
            jogoQueAchei->name = strtrimdup(campos[1]);
            jogoQueAchei->releaseDate = formatarData(campos[2]);
            jogoQueAchei->estimatedOwners = converteParaInt(campos[3]);
            jogoQueAchei->price = converteParaFloat(campos[4]);

            jogoQueAchei->supportedLanguages = extrairLista(campos[5], &jogoQueAchei->numSupportedLanguages);

            jogoQueAchei->mediaScore = converteParaFloat(campos[6]);
            jogoQueAchei->achievements = converteParaInt(campos[7]);
            jogoQueAchei->userScore  = converteParaFloat(campos[8]);

            jogoQueAchei->publishers = extrairLista(campos[9],  &jogoQueAchei->numPublishers);
            jogoQueAchei->developers = extrairLista(campos[10], &jogoQueAchei->numDevelopers);

            jogoQueAchei->categories  = extrairLista(campos[11], &jogoQueAchei->numCategories);
            jogoQueAchei->genres = extrairLista(campos[12], &jogoQueAchei->numGenres);
            jogoQueAchei->tags = extrairLista(campos[13], &jogoQueAchei->numTags);

            (*totalJogos)++;
        }

        for(int i=0;i<qtdCampos;i++) free(campos[i]);
        free(campos);
    }

    fclose(arq);
    return meusJogos;
}

// busca jogo pelo id dentro do array
Game* buscarPorId(Game* lista, int total, int id){
    for(int i=0;i<total;i++){
        if(lista[i].id == id)
            return &lista[i];
    }
    return NULL;
}

// estrutura da pilha
typedef struct Celula{
    Game* jogo;
    struct Celula* prox;
} Celula;

typedef struct {
    Celula* topo;
    int tamanho;
} PilhaGame;

// inicializa a pilha
void pilhaInit(PilhaGame* p){
    p->topo = NULL;
    p->tamanho = 0;
}

// empilha jogo (coloco no topo)
void empilhar(PilhaGame* p, Game* jogoQueAchei){
    Celula* novoNo = (Celula*)malloc(sizeof(Celula));

    // ligo o novo nó na pilha
    novoNo->jogo = jogoQueAchei;
    novoNo->prox = p->topo;

    // atualizo topo
    p->topo = novoNo;
    p->tamanho++;
}

// remove topo da pilha e devolve o jogo
Game* desempilhar(PilhaGame* p){
    if(!p->topo) return NULL;

    Celula* noTopo = p->topo;
    Game* jogoRemovido = noTopo->jogo;

    p->topo = noTopo->prox;
    free(noTopo);

    p->tamanho--;
    return jogoRemovido;
}

int main(){
    const char* caminho = "/tmp/games.csv";

    int totalJogos = 0;
    Game* todosJogos = carregarCSV(caminho, &totalJogos);

    if(!todosJogos) return 1;

    PilhaGame minhaPilha;
    pilhaInit(&minhaPilha);

    char entradaUser[256];

    // leio ids até encontrar "FIM"
    while(fgets(entradaUser, sizeof(entradaUser), stdin)){
        size_t L = strlen(entradaUser);
        if(L && entradaUser[L-1]=='\n') entradaUser[L-1]='\0';

        if(strcmp(entradaUser, "FIM") == 0)
            break;

        int id = converteParaInt(entradaUser);
        Game* jogo = buscarPorId(todosJogos, totalJogos, id);

        if(jogo) 
            empilhar(&minhaPilha, jogo); // empilho o jogo encontrado
    }

    if(!fgets(entradaUser, sizeof(entradaUser), stdin)){
        Celula* no = minhaPilha.topo;

        while(no){
            Celula* prox = no->prox;
            free(no);
            no = prox;
        }

        for(int i=0;i<totalJogos;i++)
            liberarMemoriaJogo(&todosJogos[i]);

        free(todosJogos);
        return 0;
    }

    int qtdCmd = converteParaInt(entradaUser);

    for(int k=0;k<qtdCmd;k++){
        if(!fgets(entradaUser, sizeof(entradaUser), stdin))
            break;

        size_t L = strlen(entradaUser);
        if(L && entradaUser[L-1]=='\n') entradaUser[L-1]='\0';

        // comando de inserir
        if(entradaUser[0] == 'I'){
            int id = converteParaInt(entradaUser+1);
            Game* jogo = buscarPorId(todosJogos, totalJogos, id);

            if(jogo)
                empilhar(&minhaPilha, jogo); // empilho mais um
        }
        // comando de remover
        else if(entradaUser[0] == 'R'){
            Game* removido = desempilhar(&minhaPilha);

            if(removido)
                printf("(R) %s\n", removido->name);
        }
    }

    int qtd = minhaPilha.tamanho;
    Game** arrInvertido = (Game**)malloc(qtd*sizeof(Game*));

    int idx = 0;
    for(Celula* no = minhaPilha.topo; no; no = no->prox)
        arrInvertido[idx++] = no->jogo;

    // imprimir na ordem certa
    for(int k = qtd-1, pos = 0; k >= 0; k--, pos++)
        imprimirInfoJogoIdx(pos, arrInvertido[k]);

    free(arrInvertido);

    // limpando toda a pilha
    Celula* no = minhaPilha.topo;
    while(no){
        Celula* prox = no->prox;
        free(no);
        no = prox;
    }

    // libero os jogos do CSV
    for(int i=0;i<totalJogos;i++)
        liberarMemoriaJogo(&todosJogos[i]);

    free(todosJogos);

    return 0;
}