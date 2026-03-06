import java.io.*;
import java.util.*;

class Game { 
    private int id;
    private String name;
    private String releaseDate;
    private int estimatedOwners;
    private float price;
    private String[] supportedLanguages;
    private float mediaScore;
    private int achievements;
    private float userScore;
    private String[] publishers;
    private String[] developers;
    private String[] categories;
    private String[] genres;
    private String[] tags;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public int getEstimatedOwners() { return estimatedOwners; }
    public void setEstimatedOwners(int estimatedOwners) { this.estimatedOwners = estimatedOwners; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public String[] getSupportedLanguages() { return supportedLanguages; }
    public void setSupportedLanguages(String[] supportedLanguages) { this.supportedLanguages = supportedLanguages; }

    public float getMediaScore() { return mediaScore; }
    public void setMediaScore(float mediaScore) { this.mediaScore = mediaScore; }

    public int getAchievements() { return achievements; }
    public void setAchievements(int achievements) { this.achievements = achievements; }

    public float getUserScore() { return userScore; }
    public void setUserScore(float userScore) { this.userScore = userScore; }

    public String[] getPublishers() { return publishers; }
    public void setPublishers(String[] publishers) { this.publishers = publishers; }

    public String[] getDevelopers() { return developers; }
    public void setDevelopers(String[] developers) { this.developers = developers; }

    public String[] getCategories() { return categories; }
    public void setCategories(String[] categories) { this.categories = categories; }

    public String[] getGenres() { return genres; }
    public void setGenres(String[] genres) { this.genres = genres; }

    public String[] getTags() { return tags; }
    public void setTags(String[] tags) { this.tags = tags; }

    public String toString() {
        return "=> " + id + " ## " + name + " ## " + releaseDate + " ## " + estimatedOwners + " ## " +
               String.format("%.2f", price) + " ## " + Arrays.toString(supportedLanguages).replaceAll("^\\[|\\]$", "") +
               " ## " + (int) mediaScore + " ## " + (float) achievements + " ## " + (int) userScore + " ## " +
               Arrays.toString(publishers) + " ## " + Arrays.toString(developers) + " ## " +
               Arrays.toString(categories) + " ## " + Arrays.toString(genres) + " ## " + Arrays.toString(tags) + " ##";
    }
}

class No {
    Game dadoDoNo;
    No proximoNo;

    No(Game dadoDoNo) { 
        this.dadoDoNo = dadoDoNo; 
        this.proximoNo = null; 
    }
}

class FilaGame {

    private No inicioFila;  
    private No fimFila;
    private int quantidade;

    public FilaGame() {
        // iniciando a fila vazia
        inicioFila = fimFila = null;
        quantidade = 0;
    }

    public void enfileirar(Game jogo) {
        // criando um novo nó com o jogo que quero colocar na fila
        No novoNo = new No(jogo);

        // se estiver vazio, o primeiro e o último são o mesmo nó
        if (fimFila == null) {
            inicioFila = fimFila = novoNo;
        } else {
            // senão, jogo no final e atualizo o fim
            fimFila.proximoNo = novoNo;
            fimFila = novoNo;
        }

        quantidade++; 
    }

    public Game desenfileirar() throws Exception {
        // tentando remover de uma fila vazia = erro
        if (inicioFila == null) throw new Exception("Fila vazia");

        
        Game jogoRemovido = inicioFila.dadoDoNo;
        inicioFila = inicioFila.proximoNo;

        // se esvaziou, o fim também vira nulo
        if (inicioFila == null) fimFila = null;

        quantidade--;
        return jogoRemovido;
    }

    public int tamanho() {
        return quantidade;
    }

    public Game get(int indice) {
        // percorro ate achar o indice que quero
        No ponteiro = inicioFila;
        for (int k = 0; k < indice; k++) ponteiro = ponteiro.proximoNo;
        return ponteiro.dadoDoNo;
    }
}

public class Fila {
    public static void main(String[] args) {
        
        String arquivoCSV = "/tmp/games.csv";

        // carregando tudo num mapa
        Map<Integer, Game> mapaDeJogos = carregarCSV(arquivoCSV);

        // fila que vou usar
        FilaGame filaDeJogos = new FilaGame();

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(System.in))) {

            // comparação com "FIM"
            while (true) {
                String linhaDigitada = leitor.readLine();
                if (linhaDigitada == null) return;

                linhaDigitada = linhaDigitada.trim();

                if (linhaDigitada.equals("FIM")) break;

                // converto pro ID e pego o jogo do mapa
                int idLido = converterInt(linhaDigitada);
                Game jogo = mapaDeJogos.get(idLido);

                if (jogo != null) filaDeJogos.enfileirar(jogo);
            }

            String qtdStr = leitor.readLine();
            int qtdComandos = converterInt(qtdStr);

            for (int i = 0; i < qtdComandos; i++) {

                String comandoLido = leitor.readLine();
                if (comandoLido == null) break;

                comandoLido = comandoLido.trim();

                if (comandoLido.startsWith("I")) {
                    // comando de inserir
                    int idInserir = converterInt(comandoLido.substring(1).trim());
                    Game jogo = mapaDeJogos.get(idInserir);
                    if (jogo != null) filaDeJogos.enfileirar(jogo);

                } else if (comandoLido.equals("R")) {
                    // comando de remover
                    try {
                        Game removido = filaDeJogos.desenfileirar();
                        System.out.println("(R) " + removido.getName());
                    } catch (Exception e) {}
                }
            }

            // print final da fila
            int posicao = 0;
            for (int i = 0; i < filaDeJogos.tamanho(); i++) {
                System.out.print("[" + posicao + "] ");
                System.out.println(filaDeJogos.get(i));
                posicao++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Game> carregarCSV(String caminhoArquivo) {
        // criando o mapa que vai armazenar os jogos pelo id
        Map<Integer, Game> mapa = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            br.readLine(); // ignorando o cabeçalho
            String linha;

            while ((linha = br.readLine()) != null) {

                String[] partes = quebrarCSV(linha);
                if (partes.length < 14) continue; 

                // criando um jogo e preenchendo tudo
                Game g = new Game();
                g.setId(converterInt(partes[0]));
                g.setName(partes[1]);
                g.setReleaseDate(formatarData(partes[2]));
                g.setEstimatedOwners(converterInt(partes[3]));
                g.setPrice(converterFloat(partes[4]));
                g.setSupportedLanguages(separarLista(partes[5]));
                g.setMediaScore(converterFloat(partes[6]));
                g.setAchievements(converterInt(partes[7]));
                g.setUserScore(converterFloat(partes[8]));
                g.setPublishers(separarLista(partes[9]));
                g.setDevelopers(separarLista(partes[10]));
                g.setCategories(separarLista(partes[11]));
                g.setGenres(separarLista(partes[12]));
                g.setTags(separarLista(partes[13]));

                // boto no mapa usando id como chave
                mapa.put(g.getId(), g);
            }

        } catch (Exception e) {}

        return mapa;
    }

    public static String[] quebrarCSV(String linha) {
        List<String> campos = new ArrayList<>();
        boolean dentroAspas = false;
        StringBuilder acumulado = new StringBuilder();

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);

            if (c == '"') dentroAspas = !dentroAspas;
            else if (c == ',' && !dentroAspas) {
                campos.add(acumulado.toString().trim());
                acumulado.setLength(0);
            } else {
                acumulado.append(c);
            }
        }

        campos.add(acumulado.toString().trim());
        return campos.toArray(new String[0]);
    }

    public static String formatarData(String s) {
        if (s == null || s.isEmpty()) return "01/01/0000";

        Map<String, String> meses = new HashMap<>();
        meses.put("Jan", "01"); meses.put("Feb", "02"); meses.put("Mar", "03"); meses.put("Apr", "04");
        meses.put("May", "05"); meses.put("Jun", "06"); meses.put("Jul", "07"); meses.put("Aug", "08");
        meses.put("Sep", "09"); meses.put("Oct", "10"); meses.put("Nov", "11"); meses.put("Dec", "12");

        try {
            s = s.replace("\"", "").trim();
            String[] partes = s.split(" ");

            String dia = "01", mes = "01", ano = "0000";

            if (partes.length == 3) {
                dia = partes[1].replace(",", "");
                mes = meses.getOrDefault(partes[0], "01");
                ano = partes[2];
            } else if (partes.length == 2) {
                mes = meses.getOrDefault(partes[0], "01");
                ano = partes[1];
            } else if (partes.length == 1) {
                ano = partes[0];
            }

            return String.format("%02d/%s/%s", Integer.parseInt(dia), mes, ano);

        } catch (Exception e) {
            return "01/01/0000";
        }
    }

    public static String[] separarLista(String s) {
        if (s == null || s.isEmpty()) return new String[0];

        s = s.replace("[", "").replace("]", "").replace("\"", "").replace("'", "");

        String[] partes = s.split(",");

        for (int i = 0; i < partes.length; i++) partes[i] = partes[i].trim();

        return partes;
    }

    public static int converterInt(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return 0; }
    }

    public static float converterFloat(String s) {
        try { return Float.parseFloat(s.trim()); }
        catch (Exception e) { return 0f; }
    }
}
