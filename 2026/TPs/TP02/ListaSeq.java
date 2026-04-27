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
        return String.format("%02d/%02d/%04d", dia, mes, ano);
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

    public static Hora parseHora(String s) {
        Scanner sc = new Scanner(s);
        sc.useDelimiter(":");
        int h = sc.nextInt();
        int m = sc.nextInt();
        sc.close();
        return new Hora(h, m);
    }

    public String formatar() {
        return String.format("%02d:%02d", hora, minuto);
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
    private Hora abertura;
    private Hora fechamento;
    private Data data;
    private boolean aberto;

    public Restaurante(int id, String nome, String cidade, int capacidade, double avaliacao, String[] tipos, int faixaPreco, Hora abertura, Hora fechamento, Data data, boolean aberto) {
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
        this.capacidade = capacidade;
        this.avaliacao = avaliacao;
        this.tiposCozinha = tipos;
        this.faixaPreco = faixaPreco;
        this.abertura = abertura;
        this.fechamento = fechamento;
        this.data = data;
        this.aberto = aberto;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }

    public static int converterPreco(String s) {
        int c = 0;
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) == '$') c++;
        return c;
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

        String tiposRaw = sc.next();
        String precoStr = sc.next();
        int faixa = converterPreco(precoStr);

        String horario = sc.next();
        Scanner scHora = new Scanner(horario);
        scHora.useDelimiter("-");
        Hora abertura = Hora.parseHora(scHora.next());
        Hora fechamento = Hora.parseHora(scHora.next());
        scHora.close();

        Data data = Data.parseData(sc.next());
        boolean aberto = sc.next().trim().equals("true");

        sc.close();

        String[] temp = new String[20];
        int n = 0;
        Scanner scTipos = new Scanner(tiposRaw);
        scTipos.useDelimiter(";");
        while (scTipos.hasNext()) {
            temp[n++] = scTipos.next();
        }
        scTipos.close();

        String[] tipos = new String[n];
        for (int i = 0; i < n; i++) tipos[i] = temp[i];

        return new Restaurante(id, nome, cidade, capacidade, avaliacao, tipos, faixa, abertura, fechamento, data, aberto);
    }

    public String formatar() {
        StringBuilder t = new StringBuilder();
        for (int i = 0; i < tiposCozinha.length; i++) {
            t.append(tiposCozinha[i]);
            if (i < tiposCozinha.length - 1) t.append(",");
        }

        StringBuilder p = new StringBuilder();
        for (int i = 0; i < faixaPreco; i++) p.append("$");

        return String.format("[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %s-%s ## %s ## %b]",
                id, nome, cidade, capacidade, avaliacao,
                t.toString(), p.toString(),
                abertura.formatar(), fechamento.formatar(),
                data.formatar(), aberto);
    }
}

class ColecaoRestaurantes {
    private Restaurante[] array;
    private int n;

    public ColecaoRestaurantes() {
        array = new Restaurante[1000];
        n = 0;
    }

    public void lerCsv(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            String linha;
            while ((linha = br.readLine()) != null) {
                array[n++] = Restaurante.parseRestaurante(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro");
        }
    }

    public static ColecaoRestaurantes lerCsv() {
        ColecaoRestaurantes c = new ColecaoRestaurantes();
        c.lerCsv("/tmp/restaurantes.csv");
        return c;
    }

    public Restaurante buscarPorId(int id) {
        for (int i = 0; i < n; i++) {
            if (array[i].getId() == id) return array[i];
        }
        return null;
    }
}

class Lista {
    private Restaurante[] array;
    private int n;

    public Lista(int tamanho) {
        array = new Restaurante[tamanho];
        n = 0;
    }

    public void inserirInicio(Restaurante x) throws Exception {
        for (int i = n; i > 0; i--) array[i] = array[i - 1];
        array[0] = x;
        n++;
    }

    public void inserirFim(Restaurante x) {
        array[n++] = x;
    }

    public void inserir(Restaurante x, int pos) {
        for (int i = n; i > pos; i--) array[i] = array[i - 1];
        array[pos] = x;
        n++;
    }

    public Restaurante removerInicio() {
        Restaurante r = array[0];
        n--;
        for (int i = 0; i < n; i++) array[i] = array[i + 1];
        return r;
    }

    public Restaurante removerFim() {
        return array[--n];
    }

    public Restaurante remover(int pos) {
        Restaurante r = array[pos];
        n--;
        for (int i = pos; i < n; i++) array[i] = array[i + 1];
        return r;
    }

    public void mostrar() {
        for (int i = 0; i < n; i++) {
            System.out.println(array[i].formatar());
        }
    }
}

public class ListaSeq {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        ColecaoRestaurantes base = ColecaoRestaurantes.lerCsv();
        Lista lista = new Lista(1000);

        while (true) {
            int id = sc.nextInt();
            if (id == -1) break;
            lista.inserirFim(base.buscarPorId(id));
        }

        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            String linha = sc.nextLine();
            Scanner cmd = new Scanner(linha);

            String op = cmd.next();

            if (op.equals("II")) {
                lista.inserirInicio(base.buscarPorId(cmd.nextInt()));
            } else if (op.equals("IF")) {
                lista.inserirFim(base.buscarPorId(cmd.nextInt()));
            } else if (op.equals("I*")) {
                int pos = cmd.nextInt();
                lista.inserir(base.buscarPorId(cmd.nextInt()), pos);
            } else if (op.equals("RI")) {
                System.out.println("(R)" + lista.removerInicio().getNome());
            } else if (op.equals("RF")) {
                System.out.println("(R)" + lista.removerFim().getNome());
            } else if (op.equals("R*")) {
                System.out.println("(R)" + lista.remover(cmd.nextInt()).getNome());
            }

            cmd.close();
        }

        lista.mostrar();
        sc.close();
    }
}