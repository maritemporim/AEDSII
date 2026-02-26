import java.util.*;

class Dados{
    String nome;
    String regiao;
    int custo;

    public Dados(String nome, String regiao, int custo){
        this.nome = nome;
        this.regiao = regiao;
        this.custo = custo;
    }
}

public class Van{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        List<Dados> lista = new ArrayList<>();
        int linha = sc.nextInt();
        sc.nextLine();

        for(int i = 0; i < linha; i++){
            String nome = sc.next();
            String regiao = sc.next();
            int custo = sc.nextInt();

            Dados novo = new Dados(nome,regiao,custo);
            lista.add(novo);
        }


       // sc.close();
       
       

        Dados tmp = lista.get(i);
        lista.set(i,lista.get(menor));
        lista.set(menor,tmp);


       }



        for( Dados d : lista){
            System.out.println(d.nome + " ");
        }
    }
}