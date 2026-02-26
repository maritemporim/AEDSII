import java.io.*;
import java.util.*;

class Game {
    public int id;
    public String name;
    public int owners;

    public Game(int id, String name, int owners) {
        this.id = id;
        this.name = name;
        this.owners = owners;
    }

    public int getMod() {
        return owners % 15;
    }
}

class NoSegundo {
    public String chave;
    public NoSegundo esq, dir;

    public NoSegundo(String chave) {
        this.chave = chave;
    }
}

class NoPrimeiro {
    public int chave;
    public NoPrimeiro esq, dir;
    public NoSegundo segundo;

    public NoPrimeiro(int chave) {
        this.chave = chave;
    }
}

class ArvorePrimeira {

    private NoPrimeiro raiz;

    public void inserirPrimeiro(int x) {
        raiz = inserirPrimeiro(x, raiz);
    }

    private NoPrimeiro inserirPrimeiro(int x, NoPrimeiro i) {
        if (i == null) return new NoPrimeiro(x);
        if (x < i.chave) i.esq = inserirPrimeiro(x, i.esq);
        else if (x > i.chave) i.dir = inserirPrimeiro(x, i.dir);
        return i;
    }

    public void inserirSegundo(Game g) {
        int m = g.getMod();
        NoPrimeiro no = buscarPrimeiro(m, raiz);
        if (no != null) no.segundo = inserirSegundo(g.name, no.segundo);
    }

    private NoPrimeiro buscarPrimeiro(int x, NoPrimeiro i) {
        if (i == null) return null;
        if (x == i.chave) return i;
        if (x < i.chave) return buscarPrimeiro(x, i.esq);
        return buscarPrimeiro(x, i.dir);
    }

    private NoSegundo inserirSegundo(String s, NoSegundo i) {
        if (i == null) return new NoSegundo(s);
        if (s.compareTo(i.chave) < 0) i.esq = inserirSegundo(s, i.esq);
        else if (s.compareTo(i.chave) > 0) i.dir = inserirSegundo(s, i.dir);
        return i;
    }

    public void pesquisar(String nome) {
        System.out.print(nome + " => ");
        boolean achou = pesquisarPrimeira(raiz, nome);
        if (!achou) System.out.print("NAO");
        System.out.println();
    }

    private boolean pesquisarPrimeira(NoPrimeiro i, String nome) {
        if (i == null) return false;

        System.out.print("raiz ");

        boolean achou = pesquisarSegunda(i.segundo, nome, "");

        if (!achou) achou = pesquisarPrimeiraAux(i.esq, nome, "ESQ ");

        if (!achou) achou = pesquisarPrimeiraAux(i.dir, nome, "DIR ");

        return achou;
    }

    private boolean pesquisarPrimeiraAux(NoPrimeiro i, String nome, String mov) {
        if (i == null) return false;

        System.out.print(mov);

        boolean achou = pesquisarSegunda(i.segundo, nome, "");

        if (!achou) achou = pesquisarPrimeiraAux(i.esq, nome, "ESQ ");
        if (!achou) achou = pesquisarPrimeiraAux(i.dir, nome, "DIR ");

        return achou;
    }

    private boolean pesquisarSegunda(NoSegundo i, String nome, String mov) {
        if (i == null) return false;

        if (pesquisarSegunda(i.esq, nome, "esq ")) {
            System.out.print("esq ");
            return true;
        }

        if (i.chave.equals(nome)) {
            System.out.print("SIM ");
            return true;
        }

        if (pesquisarSegunda(i.dir, nome, "dir ")) {
            System.out.print("dir ");
            return true;
        }

        return false;
    }
}

public class Tp07_02 {

    public static int parseOwners(String s) {
        if (s == null) return 0;
        s = s.replace("\"", "").trim();
        if (s.isEmpty()) return 0;
        String[] p = s.split(" ");
        String num = p[0].replace(",", "");
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static List<String> splitCSV(String linha) {
        List<String> out = new ArrayList<>();
        if (linha == null) return out;
        boolean dentroAspas = false;
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == '"') {
                dentroAspas = !dentroAspas;
                cur.append(c);
            } else if (c == ',' && !dentroAspas) {
                out.add(cur.toString().trim());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString().trim());
        return out;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        ArvorePrimeira arvore = new ArvorePrimeira();
        int[] ordem = {7,3,11,1,5,9,13,0,2,4,6,8,10,12,14};
        for (int x : ordem) arvore.inserirPrimeiro(x);

        HashMap<Integer, Game> mapa = new HashMap<>();
        BufferedReader csv = null;
        try {
            csv = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/games.csv"), "UTF-8"));
            String header = csv.readLine();
            String linha;
            while ((linha = csv.readLine()) != null) {
                linha = linha.replace("\"\"", "\"");
                List<String> partes = splitCSV(linha);
                if (partes.size() < 4) continue;
                String idStr = partes.get(0).replace("\"", "").trim();
                String name = partes.get(1).replaceAll("^\"|\"$", "").trim();
                String ownersField = partes.get(3).trim();
                int id;
                try {
                    id = Integer.parseInt(idStr);
                } catch (NumberFormatException e) {
                    continue;
                }
                int owners = parseOwners(ownersField);
                mapa.put(id, new Game(id, name, owners));
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (csv != null) csv.close();
        }

        String linha;
        while (true) {
            linha = br.readLine();
            if (linha == null) return;
            linha = linha.trim();
            if (linha.equals("FIM")) break;
            if (linha.isEmpty()) continue;
            int id;
            try {
                id = Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                continue;
            }
            if (mapa.containsKey(id)) arvore.inserirSegundo(mapa.get(id));
        }

        while (true) {
            linha = br.readLine();
            if (linha == null) return;
            linha = linha.trim();
            if (linha.equals("FIM")) break;
            if (linha.isEmpty()) continue;
            arvore.pesquisar(linha);
        }
    }
}
