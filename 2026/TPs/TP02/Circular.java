import java.io.*;
import java.util.*;

class Hora{
    private int hora;
    private int minuto;
    public Hora(int hora, int minuto){
        this.hora = hora;
        this.minuto = minuto;
    }

    public int getHora(){
        return hora;
    }

    public int getMinuto(){
        return minuto;
    }

    public void setHora(int hora){
        this.hora = hora;
    }

    public void setMinuto(int minuto){
        this.minuto = minuto;
    }

    public static Hora parseHora(String s){
        Scanner scan = new Scanner(s);
        scan.useDelimiter(":");
        int hora = scan.nextInt();
        int minuto = scan.nextInt();
        Hora h = new Hora(hora,minuto);
        return h;
    }

    public String formatar(){
        return String.format("%02d:%02d", this.hora, this.minuto); 
    }
}   

class Data{
    
    private int ano;
    private int mes;
    private int dia;
    public Data(int ano, int mes, int dia){
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }
    //gets e sets
    public int getAno(){
        return ano;
    }

    public int getMes(){
        return mes;
    }

    public int getDia(){
        return dia;
    }

    public void setAno(int ano){
        this.ano = ano;
    }

    public void setMes(int mes){
        this.mes = mes;
    }

    public void setDia(int dia){
        this.dia = dia;
    }
    public static Data parseData(String s){ 
        Scanner scan = new Scanner(s);
        scan.useDelimiter("-");
        int ano = scan.nextInt();
        int mes = scan.nextInt();
        int dia = scan.nextInt();
        scan.close();
        Data data = new Data(ano, mes, dia);
        return data;
    }
   
    public String formatar(){
        return String.format("%02d/%02d/%04d", this.dia, this.mes, this.ano);
    }
}
    
class Restaurante{
    private int idRestaurante;
    private String nome;
    private String cidade;
    private int capacidade;
    private double avaliacao;
    private String[] tiposCozinha;
    private int faixa_preco;
    private Hora horarioAbertura;
    private Hora horarioFechamento;
    private Data dataAbertura;
    private boolean aberto;
    public Restaurante(int idRestaurante, String nome, String cidade, int capacidade, double avaliacao,
            String[] tiposCozinha,int faixa_preco, Hora horarioAbertura, Hora horarioFechamento, Data dataAbertura, boolean aberto) {
        this.idRestaurante = idRestaurante;
        this.nome = nome;
        this.cidade = cidade;
        this.capacidade = capacidade;
        this.avaliacao = avaliacao;
        this.tiposCozinha = tiposCozinha;
        this.faixa_preco = faixa_preco;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
        this.dataAbertura = dataAbertura;
        this.aberto = aberto;
    }
    public int getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String[] getTiposCozinha() {
        return tiposCozinha;
    }

    public void setTiposCozinha(String[] tiposCozinha) {
        this.tiposCozinha = tiposCozinha;
    }

    public int getFaixa_Preco(){
        return faixa_preco;
    }

    public void setFaixa_Preco(int faixa_preco){
        this.faixa_preco = faixa_preco;
    }

    public Hora getHorarioAbertura() {
        return horarioAbertura;
    }

    public void setHorarioAbertura(Hora horarioAbertura) {
        this.horarioAbertura = horarioAbertura;
    }

    public Hora getHorarioFechamento() {
        return horarioFechamento;
    }

    public void setHorarioFechamento(Hora horarioFechamento) {
        this.horarioFechamento = horarioFechamento;
    }

    public Data getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Data dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }
    public static int pegarFaixa_Preco(String s){
        int cont = 0;    
        for(int i = 0; i < s.length(); i++)
            if(s.charAt(i) == '$') cont++;

        return cont;
    }

    public static Restaurante parseRestaurante(String s){
        Scanner scan = new Scanner(s);
        scan.useLocale(Locale.US);
        scan.useDelimiter(",");
        int id = scan.nextInt();
        String nome = scan.next();
        String cidade = scan.next();
        int capacidade = scan.nextInt();
        double avaliacao = scan.nextDouble();
        String tpCozinha = scan.next();  
        int faixa_preco = pegarFaixa_Preco(scan.next());
        String horarios = scan.next();
        Scanner scanHorarios = new Scanner(horarios); 
        scanHorarios.useDelimiter("-");
        Hora horaAbertura = Hora.parseHora(scanHorarios.next());
        Hora horaFechamento = Hora.parseHora(scanHorarios.next());
        scanHorarios.close(); 
        Data dataAbertura = Data.parseData(scan.next());
        String abertoStr = scan.next(); 
        boolean aberto = (abertoStr.compareTo("true") == 0);
        scan.close(); 

        String[] aux = new String[10]; 
        int cout = 0;
        Scanner scanTp = new Scanner(tpCozinha); 
        scanTp.useDelimiter(";");
        while(scanTp.hasNext()){
            String palavra = scanTp.next();
            if(palavra.length() > 0){
                aux[cout] = palavra; 
                cout++;
            }
        }
        scanTp.close();
    
        String[] tipoCozinha = new String[cout]; 
        for(int i = 0; i < cout; i++){
            tipoCozinha[i] = aux[i];
        }
        return new Restaurante(id, nome, cidade, capacidade, avaliacao, tipoCozinha, 
                               faixa_preco ,horaAbertura, horaFechamento, dataAbertura, aberto);
    }

    public String formatar(){
     
        String strCozinhas = "";
        for(int i = 0; i < tiposCozinha.length; i++){
            strCozinhas += tiposCozinha[i];
            if(i < tiposCozinha.length - 1){
                strCozinhas += ","; 
            }
        }
        int n = this.faixa_preco;
        String faixa_p = "";
        for(int i = 0; i < n; i++){
            faixa_p += '$';
        }
        
        double valor = this.avaliacao;
        String strAvaliacao = valor + ""; 

        String s = String.format("[%d ## %s ## %s ## %d ## %s ## [%s] ## %s ## %s-%s ## %s ## %b]", 
                idRestaurante, nome, cidade, capacidade, strAvaliacao, strCozinhas, 
                faixa_p, horarioAbertura.formatar(),horarioFechamento.formatar(), 
                dataAbertura.formatar(), aberto);
        return s; 
    }
}

class ColecaoRestaurantes{
    private int tamanho;
    private Restaurante[] restaurantes;
    public ColecaoRestaurantes(){
        this.tamanho = 0;
        this.restaurantes = null;
    }
    public ColecaoRestaurantes(int tamanho){
        this.tamanho = tamanho;
        this.restaurantes = new Restaurante[tamanho];
    }

    public int getTamanho(){
        return tamanho;       
    }

    public void setTamanho(int tam){
        this.tamanho = tam;
    }
        
    public Restaurante[] getRestaurantes(){
        return restaurantes;
    }

    public void setRestaurantes(Restaurante[] restaurantes){
        this.restaurantes = restaurantes;
    }

    public void lerCsv(String path) throws Exception{
        File arquivo = new File(path);
        Scanner scan = new Scanner(arquivo);
        
        if(scan.hasNextLine())
            scan.nextLine();

        int i = 0;
        while(scan.hasNextLine()){
           String linha = scan.nextLine();
           restaurantes[i] = Restaurante.parseRestaurante(linha);
           i++;
        }
        
    }

    public static ColecaoRestaurantes lerCsv() throws Exception{
        File arquivo = new File("/tmp/restaurantes.csv");
        Scanner scan = new Scanner(arquivo);

        int tam = 0;
        while(scan.hasNext()){
            scan.nextLine();
            tam++; 
        }

        scan.close();
        ColecaoRestaurantes novaCol = new ColecaoRestaurantes(tam - 1); 
        novaCol.lerCsv("/tmp/restaurantes.csv");
        
        return novaCol; 
    }

    public Restaurante buscarPorId(int id){
         for(int i = 0; i < tamanho; i++){
            if(restaurantes[i].getIdRestaurante() == id){
                return restaurantes[i];
            }
        }
        return null;
     }
}

class Fila{
    int primeiro, ultimo;
    Restaurante[] array;

    public Fila(int tamMax){
        primeiro = ultimo = 0;
        this.array = new Restaurante[tamMax + 1];
    }


  public void inserir(Restaurante x) throws Exception {

      //validar insercao
      if(((ultimo + 1) % array.length) == primeiro){
         System.out.println("(R)" + remover().getNome());
      }
      array[ultimo] = x;
      ultimo = (ultimo + 1) % array.length;
      System.out.println("(I)" + calcularMediaAno());
   }

   public Restaurante remover() throws Exception {


      if (primeiro == ultimo) {
         throw new Exception("Erro ao remover!");
      }

      Restaurante resp = array[primeiro];
      primeiro = (primeiro + 1) % array.length;
      return resp;
   }

   private int calcularMediaAno(){
        int soma = 0, count = 0;
        for(int i = primeiro; i != ultimo; i = (i+1) % array.length){
            soma += array[i].getDataAbertura().getAno();
            count++;
        }
        return (int) Math.round((double) soma / count);
    }
   
    public void imprimir() {
        for(int i = primeiro; i != ultimo; i = ((i+1)%array.length)) {
            System.out.println(array[i].formatar());
        }
    }
}

public class Circular{

     public static void main(String[] args) throws Exception{
            Scanner scan = new Scanner(System.in);
            ColecaoRestaurantes cr = ColecaoRestaurantes.lerCsv();
            Fila r = new Fila(5);
            String linha = scan.next();
                
            while(linha.compareTo("-1") != 0){
               int id = Integer.parseInt(linha);
               
               Restaurante tmp = cr.buscarPorId(id);
               if(tmp != null){
                  r.inserir(tmp);
               }

               linha = scan.next();
            }

            scan.nextLine();
            

            int n = Integer.parseInt(scan.nextLine());
            scan.useDelimiter("\\s+");
            for(int i = 0; i < n; i++){
                String entrada = scan.next();
                if(entrada.charAt(0) == 'I'){
                    int id = scan.nextInt();
                    Restaurante aux = cr.buscarPorId(id);
                    if (aux != null) r.inserir(aux);
                }else if(entrada.charAt(0) == 'R'){
                     System.out.println("(R)" + r.remover().getNome());
                }
            }
            scan.close();
            r.imprimir();       
        }
 }