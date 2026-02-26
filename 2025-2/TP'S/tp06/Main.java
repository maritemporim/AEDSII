import java.util.*;
import java.io.*;

public class Main {

    public static String extrairAno(String data) {
        if (data == null) return "";
        data = data.replace("\"", "").trim();
        String[] partes = data.split(" ");
        return partes[partes.length - 1].replace(",", "");
    }

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String caminhoCSV = "/tmp/games.csv";
        HashMap<Integer, Game> mapa = new HashMap<>();

        BufferedReader csv = new BufferedReader(new FileReader(caminhoCSV));
        String linha;

        csv.readLine();

        while ((linha = csv.readLine()) != null) {

            linha = linha.replace("\"\"", "\"");

            String[] partes = linha.split(",(?=(?:[^\\[]*\\[[^\\]]*\\])*(?![^\\[]*\\]))");

            int appId = Integer.parseInt(partes[0]);
            String name = partes[1];
            String release = partes[2];

            mapa.put(appId, new Game(name, release));
        }

        csv.close();

        ArvoreBinaria arvore = new ArvoreBinaria();

        while (true) {
            linha = br.readLine();
            if (linha.equals("FIM")) break;

            int id = Integer.parseInt(linha);

            if (mapa.containsKey(id)) {
                arvore.inserir(mapa.get(id));
            }
        }

        while (true) {
            linha = br.readLine();
            if (linha.equals("FIM")) break;

            String nomeBuscado = linha;
            Game encontrado = null;

            for (Game g : mapa.values()) {
                if (g.getName().equals(nomeBuscado)) {
                    encontrado = g;
                    break;
                }
            }

            if (encontrado != null) {
                String ano = extrairAno(encontrado.getReleaseDate());
                System.out.print(encontrado.getName() + " (" + ano + ")" + "=>");
            } else {
                System.out.print(nomeBuscado + ":");
            }

            arvore.pesquisarComCaminho(nomeBuscado);
        }
    }
}

class Game {

    private String name;
    private String releaseDate;

    public Game(String name, String releaseDate) {
        this.name = name;
        this.releaseDate = releaseDate;
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}

class No {
    public Game elemento;
    public No esq, dir;

    public No(Game g) {
        this.elemento = g;
        this.esq = this.dir = null;
    }
}

class ArvoreBinaria {

    private No raiz;

    public void inserir(Game g) {
        raiz = inserir(g, raiz);
    }

    private No inserir(Game g, No i) {
        if (i == null) return new No(g);

        int cmp = g.getName().compareTo(i.elemento.getName());

        if (cmp < 0) i.esq = inserir(g, i.esq);
        else if (cmp > 0) i.dir = inserir(g, i.dir);

        return i;
    }

    public boolean pesquisarComCaminho(String nome) {
        System.out.print("raiz  ");
        return pesquisar(nome, raiz);
    }

    private boolean pesquisar(String nome, No i) {

        if (i == null) {
            System.out.print("NAO\n");
            return false;
        }

        int cmp = nome.compareTo(i.elemento.getName());

        if (cmp == 0) {
            System.out.print("SIM\n");
            return true;
        }

        if (cmp < 0) {
            System.out.print("esq ");
            return pesquisar(nome, i.esq);
        } else {
            System.out.print("dir ");
            return pesquisar(nome, i.dir);
        }
    }
}
