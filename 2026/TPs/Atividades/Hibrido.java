import java.util.Scanner;

public class Contato{
    public String nome;
    public String telefone;
    public String email;
    public int cpf;

    public contato(String nome, String telefone, String email,int cpf){
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.cpf = cpf;
    }
}

public class Celula{
    public Contato contato;
    public Celula prox;

    public Celula(Contato contato, Celula prox){
        this.contato = contato;
        this.prox = prox;
    }
}
public class No{
    public char letra;
    public No esq;
    public No dir;
    Celula primeiro;
    Celula ultimo;

    public No(char letra, No esq, No dir, Celula primeiro, Celula ultimo){
        this.letra = letra;
        this.esq = esq;
        this.dir = dir;
        this.primeiro = primeiro;
        this.ultimo = ultimo;
    }
}

public class Agenda{
    public No raiz;

    public Agenda{
        raiz = null;
        String alfabeto = "MNOPQRSTUVWYZABCDEFGHIJKL"
    }

    

    public int remover(String nome){
        if(primeiro == ultimo){
            throw new Excepition("ERRO");
        }
        Celula i;
        for(i = primeiro; i.prox != ultimo; i = i.prox);
        int resp = ultimo.contato;
        ultimo = i;
        i = ultimo.prox = null;

        return resp;
        
    }

    public boolean pesquisarNome(string nome){
        boolean resp = false;
        for(Celula i = primeiro.prox;i != null; i = i.prox){
            if(i.elemento == nome){
                resp = true;
                i = ultimo;
            }
        }

        return resp;
    }

    public boolean pesquisarCPF(int cpf){
        boolean resp = false;
        for(Celula i = primeiro.prox; i != null; i = i.prox){
                if(i.elemento == cpf){
                    resp = true;
                    i = ultimo;
                }  
        }

        return resp;
    }

}

public class Hibrido {
    
}
