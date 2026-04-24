import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
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
    private Data dataAbertura;
    private boolean aberto;

    public Restaurante(int id, String nome, String cidade, int capacidade,double avaliacao, String[] tiposCozinha, int faixaPreco, Hora horarioAbertura, Hora horarioFechamento, Data dataAbertura, boolean aberto) {
        this.id = id;
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

    // ── getters ──────────────────────────────
    public int      getId()                { return id; }
    public String   getNome()              { return nome; }
    public String   getCidade()            { return cidade; }
    public int      getCapacidade()        { return capacidade; }
    public double   getAvaliacao()         { return avaliacao; } 
    public String[] getTiposCozinha()      { return tiposCozinha; } 
    public int      getFaixaPreco()        { return faixaPreco; }
    public Hora     getHorarioAbertura()   { return horarioAbertura; }
    public Hora     getHorarioFechamento() { return horarioFechamento; }
    public Data     getDataAbertura()      { return dataAbertura; }
    public boolean  getAberto()            { return aberto; }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public void setCapacidade(int capacidade) { this.capacidade = capacidade; }
    public void setAvaliacao(double avaliacao) { this.avaliacao = avaliacao; }
    public void setTiposCozinha(String[] tiposCozinha) { this.tiposCozinha = tiposCozinha; }
    public void setFaixaPreco(int faixaPreco) { this.faixaPreco = faixaPreco; }
    public void setHorarioAbertura(Hora h) { this.horarioAbertura = h; }
    public void setHorarioFechamento(Hora h) { this.horarioFechamento = h; }
    public void setDataAbertura(Data d){ this.dataAbertura = d; }
    public void setAberto(boolean aberto){ this.aberto = aberto; }

    public static int converterPreco(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '$') count++;
        }
        return count;
    }

    public static Restaurante parseRestaurante(String s) {
        Scanner sc = new Scanner(s);
        sc.useLocale(Locale.US);
        sc.useDelimiter(",");

        int id = sc.nextInt();
        String nome = sc.next();
        String cidade = sc.next();
        int capacidade = sc.nextInt();
        double avaliacao = sc.nextDouble();
        String tipoCozinhaRaw = sc.next();
        int faixaPreco = converterPreco(sc.next());
        String horarios = sc.next();

        Scanner scHora = new Scanner(horarios);
        scHora.useDelimiter("-");
        Hora horarioAbertura = Hora.parseHora(scHora.next());
        Hora horarioFechamento = Hora.parseHora(scHora.next());
        scHora.close();
        Data dataAbertura = Data.parseData(sc.next());
        String abertoBts = sc.next().trim();
        boolean aberto = abertoBts.compareTo("true") == 0;

        sc.close();

        // parse tipos de cozinha separados por ";"
        String[] temp = new String[15];
        int c = 0;
        Scanner scTc = new Scanner(tipoCozinhaRaw);
        scTc.useDelimiter(";");
        while (scTc.hasNext()) {
            String p = scTc.next();
            if (p.length() > 0) {
                temp[c] = p;
                c++;
            }
        }
        scTc.close();

        String[] tiposCozinha = new String[c];
        for (int i = 0; i < c; i++) {
            tiposCozinha[i] = temp[i];
        }

        return new Restaurante(id, nome, cidade, capacidade, avaliacao,tiposCozinha, faixaPreco,horarioAbertura, horarioFechamento, dataAbertura, aberto);
    }

    public String formatar() {
        // monta string dos tipos cozinha separados por virgula
        StringBuilder cozinhas = new StringBuilder();
        for (int i = 0; i < tiposCozinha.length; i++) {
            cozinhas.append(tiposCozinha[i]);
            if (i < tiposCozinha.length - 1) cozinhas.append(",");
        }

        // monta string de preço
        StringBuilder preco = new StringBuilder();
        for (int i = 0; i < faixaPreco; i++) preco.append('$');

        String str = String.format(
            "[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %s-%s ## %s ## %b]",
            id, nome, cidade, capacidade, avaliacao,
            cozinhas.toString(),
            preco.toString(),
            horarioAbertura.formatar(),
            horarioFechamento.formatar(),
            dataAbertura.formatar(),
            aberto
        );
        return str;
    }
}

class ColecaoRestaurantes {
    private int tamanho;
    private Restaurante[] restaurantes;

    public ColecaoRestaurantes() {
        this.tamanho      = 0;
        this.restaurantes = new Restaurante[1000]; // capacidade inicial
    }

    public int getTamanho() { return tamanho; }

    public Restaurante[] getRestaurantes() { return restaurantes; }

    public void lerCsv(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) { // pula cabeçalho
                    primeiraLinha = false;
                    continue;
                }
                if (linha.trim().isEmpty()) continue;
                restaurantes[tamanho] = Restaurante.parseRestaurante(linha);
                tamanho++;
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    // retorna a coleção 
    public static ColecaoRestaurantes lerCsv() {
        ColecaoRestaurantes colecao = new ColecaoRestaurantes();
        colecao.lerCsv("/tmp/restaurantes.csv");
        return colecao;
    }

    // busca restaurante pelo id sendo que retorna null se nao encontrar
    public Restaurante buscarPorId(int id) {
        for (int i = 0; i < tamanho; i++) {
            if (restaurantes[i].getId() == id) return restaurantes[i];
        }
        return null;
    }
}


public class Restaurantes {

    public static void main(String[] args) {
        // Carrega o dataset
        ColecaoRestaurantes colecao = ColecaoRestaurantes.lerCsv();

        // Lê ids da entrada padrão até -1
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextInt()) {
            int id = sc.nextInt();
            if (id == -1) break;

            Restaurante r = colecao.buscarPorId(id);
            if (r != null) {
                System.out.println(r.formatar());
            }
        }
        sc.close();
    }
}
