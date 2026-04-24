import java.util.Scanner;

class Data{
    private int dia;
    private int mes;
    private int ano;

    public Data(int ano, int mes, int dia){
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }

    public int getAno(){return ano;}
    public int getMes(){return mes;}
    public int getDia(){return dia;}

    public void setAno(int ano){this.ano = ano;}
    public void setMes(int mes){this.mes = mes;}
    public void setDia(int dia){this.dia = dia;}

    public static Hora parseData(String str){
        Scanner sc = new Scanner(str);/
        sc.useDelimiter("-"); // separa por :
        int ano = sc.nextInt(); 
        int mes = sc.nextInt();
        int dia = sc.nextInt();
        sc.close();
        Data data = new Data(ano,mes,dia); // criação do objeto data
        return data;// retorno da nova data
    }

    public String formatar(){
        return String.format("%02d/%02d/%04d", this.dia, this.mes, this.ano); // retorno da data formatada
    }
}

class Hora{
    private int hora;
    private int minuto;
    
    public Hora(int hora, int minuto){
        this.hora = hora;
        this.minuto = minuto;
    }

    public int getHora() {return hora;}
    public int getMinuto(){return minuto;}

    public void setHora(){this.hora = hora;}
    public void setMinito(){this.minuto = minuto;}

    public static Hora parseHora(String str){
        Scanner sc = new Scanner(str);
        sc.useDelimiter(":"); 
        int hora = sc.nextInt();
        int minuto = sc.nextInt();
        Hora hr = new Hora(hora,minuto);
        return hr;
    }

    public String formatar(){
        return String.format("%02d:%02d", this.hora, this.minuto);
    }

}

class Restaurante{
    private int id;
    private String nome;
    private String cidade;
    private int capacidade;
    private double avaliacao;
    private String[] tipos_cozinha = new String[];
    private int faixa_preco;
    private Hora horario_abertura;
    private Hora horario_fechamento;
    private Data data_abertura;
    private boolean aberto;

    public Restaurante(int id, String nome, String cidade, int capacidade, double avaliacao, String tipos_cozinha, int faixa_preco, Hora data_fechamento, Hora data_abertura, Data data_abertura, boolean aberto){
        this.id = id;
        this.nome = nome;
        this.cidade = cidade; 
        this.capacidade = capacidade;
        this.avaliacao = avaliacao;
        this.tipos_cozinha = tipos_cozinha;
        this.faixa_preco = faixa_preco;
        this.horario_abertura= horario_abertura;
        this.horario_fechamento = horario_fechamento;
        this.data_abertura = data_abertura;
        this.aberto = aberto;
    }

    public int getId(){return id;}
    public String getNome(){return nome;}
    public String getCidade(){return cidade;}
    public int getCapacidade(){return capacidade;}
    public double getAvalaicacao(){return avaliacao;}
    public String getTipos_Cozinha(){return tipos_cozinha}
    public int getFaixa_Preco(){return faixa_preco;}
    public Hora getHorario_Abertura(){return horario_abertura;}
    public Hora getHorario_Fechamento(){return horario_fechamento;}
    public Hora getData_Abertura(){return data_abertura;}
    public boolean getAberto(){return aberto;}

    public void setId(){this.id = id;}
    public void setNome(){this.nome = nome;}
    public void setCidade(){this.cidade = cidade;}
    public void setCapacidade(){this.capacidade = capacidade;}
    public void setAvaliacao(){this.avaliacao = avaliacao;}
    public void setTipos_Cozinha(){this.tipo_cozinha = tipo_cozinha;}
    public void setFaixa_Preco(){this.faixa_preco = faixa_preco;}
    public void setHorario_Abertura(){this.horario_abertura = horario_abertura;}
    public void setHorario_Fechamento(){this.horario_fechamento = horario_fechamento;}
    public void setData_Abertura(){this.data_abertura = data_abertura;}
    public void setAberto(){this.aberto = aberto;}


    public static int Preco_Converter( ){
        while(Faixa_preço != "\0"){

        }

    }

}