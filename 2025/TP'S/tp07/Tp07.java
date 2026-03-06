import java.util.*;
import java.io.*;

public class Tp07{

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

        Alvinegra arvore = new Alvinegra();

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
                System.out.print(encontrado.getName() + " (" + ano + ")=>");
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

    public String getName() { return name; }
    public String getReleaseDate() { return releaseDate; }
}

class NoAN {
    public boolean cor;
    public Game elemento;
    public NoAN esq, dir;

    public NoAN(Game elemento, boolean cor) {
        this.elemento = elemento;
        this.cor = cor;
        this.esq = this.dir = null;
    }
}

class Alvinegra {

    private NoAN raiz;

    private boolean isRed(NoAN n) {
        return (n != null && n.cor == true);
    }

    private NoAN rotacaoDir(NoAN h) {
        NoAN x = h.esq;
        h.esq = x.dir;
        x.dir = h;
        x.cor = h.cor;
        h.cor = true;
        return x;
    }

    private NoAN rotacaoEsq(NoAN h) {
        NoAN x = h.dir;
        h.dir = x.esq;
        x.esq = h;
        x.cor = h.cor;
        h.cor = true;
        return x;
    }

    private void flipColors(NoAN h) {
        h.cor = true;
        h.esq.cor = false;
        h.dir.cor = false;
    }

    public void inserir(Game g) {
        raiz = inserir(g, raiz);
        raiz.cor = false;
    }

    private NoAN inserir(Game g, NoAN h) {

        if (h == null) {
            return new NoAN(g, true);
        }

        int cmp = g.getName().compareTo(h.elemento.getName());
        if (cmp < 0) h.esq = inserir(g, h.esq);
        else if (cmp > 0) h.dir = inserir(g, h.dir);

        if (isRed(h.dir) && !isRed(h.esq)) h = rotacaoEsq(h);
        if (isRed(h.esq) && isRed(h.esq.esq)) h = rotacaoDir(h);
        if (isRed(h.esq) && isRed(h.dir)) flipColors(h);

        return h;
    }

    public boolean pesquisarComCaminho(String nome) {
        System.out.print("raiz ");
        return pesquisar(nome, raiz);
    }

    private boolean pesquisar(String nome, NoAN i) {

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
