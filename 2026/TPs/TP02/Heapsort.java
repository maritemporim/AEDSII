import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

class Data {
    private int ano;
    private int mes;
    private int dia;

    public Data(int ano, int mes, int dia) {
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }

    public int getAno() { return ano; }
    public int getMes() { return mes; }
    public int getDia() { return dia; }

    public static Data parseData(String s) {
        Scanner sc = new Scanner(s);
        sc.useDelimiter("-");
        int ano = sc.nextInt();
        int mes = sc.nextInt();
        int dia = sc.nextInt();
        sc.close();
        return new Data(ano, mes, dia);
    }

    public String formatar() {
        return String.format("%02d/%02d/%04d", this.dia, this.mes, this.ano);
    }
}

class Hora {
    private int hora;
    private int minuto;

    public Hora(int hora, int minuto) {
        this.hora = hora;
        this.minuto = minuto;
    }

    public int getHora() { return hora; }
    public int getMinuto() { return minuto; }

    public void setHora(int hora) { this.hora = hora; }
    public void setMinuto(int minuto) { this.minuto = minuto; }

    public static Hora parseHora(String s) {
        Scanner sc = new Scanner(s);
        sc.useDelimiter(":");
        int hora = sc.nextInt();
        int minuto = sc.nextInt();
        sc.close();
        return new Hora(hora, minuto);
    }

    public String formatar() {
        return String.format("%02d:%02d", this.hora, this.minuto);
    }
}

class Restaurante {
    private int id;
    private String nome;
    private String cidade;
    private int capacidade;
    private double avaliacao;
    private String[] tiposCozinha;
    private int faixaPreco;
    private Hora horarioAbertura;
    private Hora horarioFechamento;
    private Data  dataAbertura;
    private boolean aberto;

    public Restaurante(int id, String nome, String cidade, int capacidade,
                       double avaliacao, String[] tiposCozinha, int faixaPreco,
                       Hora horarioAbertura, Hora horarioFechamento,
                       Data dataAbertura, boolean aberto) {
        this.id  = id;
        this.nome = nome;
        this.cidade = cidade;
        this.capacidade = capacidade;
        this.avaliacao = avaliacao;
        this.tiposCozinha = tiposCozinha;
        this.faixaPreco = faixaPreco;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
        this.dataAbertura = dataAbertura;
        this.aberto = aberto;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCidade() { return cidade; }
    public int getCapacidade() { return capacidade; }
    public double  getAvaliacao()  { return avaliacao; }
    public String[] getTiposCozinha() { return tiposCozinha; }
    public int getFaixaPreco() { return faixaPreco; }
    public Hora getHorarioAbertura() { return horarioAbertura; }
    public Hora getHorarioFechamento() { return horarioFechamento; }
    public Data  getDataAbertura() { return dataAbertura; }
    public boolean  getAberto() { return aberto; }

    public void setId(int id)  { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public void setCapacidade(int capacidade) { this.capacidade = capacidade; }
    public void setAvaliacao(double avaliacao){ this.avaliacao = avaliacao; }
    public void setTiposCozinha(String[] tiposCozinha) { this.tiposCozinha = tiposCozinha; }
    public void setFaixaPreco(int faixaPreco) { this.faixaPreco = faixaPreco; }
    public void setHorarioAbertura(Hora h) { this.horarioAbertura = h; }
    public void setHorarioFechamento(Hora h) { this.horarioFechamento = h; }
    public void setDataAbertura(Data d) { this.dataAbertura = d; }
    public void setAberto(boolean aberto) { this.aberto = aberto; }

    public static int converterPreco(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) == '$') count++;
        return count;
    }

    public static Restaurante parseRestaurante(String s) {
        Scanner sc = new Scanner(s);
        sc.useLocale(Locale.US);
        sc.useDelimiter(",");

        int id = sc.nextInt();
        String nome = sc.next();
        String cidade  sc.next();
        int capacidade = sc.nextInt();
        double avaliacao  = sc.nextDouble();
        String tipoCozinhaRaw = sc.next();
        int faixaPreco = converterPreco(sc.next());
        String horarios = sc.next();

        Scanner scHora = new Scanner(horarios);
        scHora.useDelimiter("-");
        Hora horarioAbertura = Hora.parseHora(scHora.next());
        Hora horarioFechamento = Hora.parseHora(scHora.next());
        scHora.close();

        Data dataAbertura = Data.parseData(sc.next());
        boolean aberto = sc.next().trim().compareTo("true") == 0;
        sc.close();

        String[] temp = new String[15];
        int c = 0;
        Scanner scTc = new Scanner(tipoCozinhaRaw);
        scTc.useDelimiter(";");
        while (scTc.hasNext()) {
            String p = scTc.next();
            if (p.length() > 0) temp[c++] = p;
        }
        scTc.close();

        String[] tiposCozinha = new String[c];
        for (int i = 0; i < c; i++) tiposCozinha[i] = temp[i];

        return new Restaurante(id, nome, cidade, capacidade, avaliacao,
                               tiposCozinha, faixaPreco,
                               horarioAbertura, horarioFechamento,
                               dataAbertura, aberto);
    }

    public String formatar() {
        StringBuilder cozinhas = new StringBuilder();
        for (int i = 0; i < tiposCozinha.length; i++) {
            cozinhas.append(tiposCozinha[i]);
            if (i < tiposCozinha.length - 1) cozinhas.append(",");
        }

        StringBuilder preco = new StringBuilder();
        for (int i = 0; i < faixaPreco; i++) preco.append('$');

        return String.format(
            "[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %s-%s ## %s ## %b]",
            id, nome, cidade, capacidade, avaliacao,
            cozinhas.toString(),
            preco.toString(),
            horarioAbertura.formatar(),
            horarioFechamento.formatar(),
            dataAbertura.formatar(),
            aberto
        );
    }
}

class ColecaoRestaurantes {
    private int           tamanho;
    private Restaurante[] restaurantes;

    public ColecaoRestaurantes() {
        this.tamanho      = 0;
        this.restaurantes = new Restaurante[1000];
    }

    public int           getTamanho()      { return tamanho; }
    public Restaurante[] getRestaurantes() { return restaurantes; }

    public void lerCsv(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) { primeiraLinha = false; continue; }
                if (linha.trim().isEmpty()) continue;
                restaurantes[tamanho++] = Restaurante.parseRestaurante(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public static ColecaoRestaurantes lerCsv() {
        ColecaoRestaurantes colecao = new ColecaoRestaurantes();
        colecao.lerCsv("/tmp/restaurantes.csv");
        return colecao;
    }

    public Restaurante buscarPorId(int id) {
        for (int i = 0; i < tamanho; i++)
            if (restaurantes[i].getId() == id) return restaurantes[i];
        return null;
    }
}

class HeapSort {

    private static long comparacoes;
    private static long movimentacoes;

    private static int dataParaInt(Data d) {
        return d.getAno() * 10000 + d.getMes() * 100 + d.getDia();
    }

    private static int comparar(Restaurante a, Restaurante b) {
        comparacoes++;
        int da = dataParaInt(a.getDataAbertura());
        int db = dataParaInt(b.getDataAbertura());
        if (da != db) return da - db;
        comparacoes++;
        return a.getNome().compareTo(b.getNome());
    }

    private static void trocar(Restaurante[] arr, int i, int j) {
        Restaurante tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
        movimentacoes++;
    }

    private static void heapify(Restaurante[] arr, int n, int i) {
        int maior = i;
        int esq = 2 * i + 1;
        int dir   = 2 * i + 2;

        if (esq < n && comparar(arr[esq], arr[maior]) > 0)
            maior = esq;

        if (dir < n && comparar(arr[dir], arr[maior]) > 0)
            maior = dir;

        if (maior != i) {
            trocar(arr, i, maior);
            heapify(arr, n, maior);
        }
    }

    public static void sort(Restaurante[] arr, int n,
                            long[] retComp, long[] retMov) {
        comparacoes   = 0;
        movimentacoes = 0;

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        for (int i = n - 1; i > 0; i--) {
            trocar(arr, 0, i);
            heapify(arr, i, 0);
        }

        retComp[0] = comparacoes;
        retMov[0]  = movimentacoes;
    }
}

public class Heapsort {

    public static void main(String[] args) throws IOException {
        ColecaoRestaurantes colecao = ColecaoRestaurantes.lerCsv();

        Scanner sc   = new Scanner(System.in);
        int[]   ids  = new int[2000];
        int     nIds = 0;
        while (sc.hasNextInt()) {
            int id = sc.nextInt();
            if (id == -1) break;
            ids[nIds++] = id;
        }
        sc.close();

        Restaurante[] lista  = new Restaurante[nIds];
        int           nLista = 0;
        for (int i = 0; i < nIds; i++) {
            Restaurante r = colecao.buscarPorId(ids[i]);
            if (r != null) lista[nLista++] = r;
        }

        long[] comparacoes   = {0};
        long[] movimentacoes = {0};

        long inicio = System.nanoTime();
        HeapSort.sort(lista, nLista, comparacoes, movimentacoes);
        long fim = System.nanoTime();

        double tempoMs = (fim - inicio) / 1e6;

        for (int i = 0; i < nLista; i++)
            System.out.println(lista[i].formatar());

        FileWriter log = new FileWriter("885948_heapsort.txt");
        log.write(String.format("884948\t%d\t%d\t%.2f%n",
                  comparacoes[0], movimentacoes[0], tempoMs));
        log.close();
    }
}