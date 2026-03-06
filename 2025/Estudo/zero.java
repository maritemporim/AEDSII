//criação da arvore alvinegra do zero

import java.util.*;

class NoAlv{
    public boolean cor;
    public int elemento;
    public NoAlv esq, dir;

    public NoAlv(int elemento, boolean cor, NOAlv esq, NoAlv dir){
        this.elemento = elemento;
        this.cor = false;
        this.esq = esq;
        this.dir = dir;
    }
}

class Alvinegra{    
    private NoAlv raiz;

    public Alvinegra(){
        raiz = null;
    }

    public boolean inserir(int elemento){
        if(raiz == null){
            raiz = new NoAlv(elemento);
        }else if(raiz.esq == null && raiz.dir == null){
            if(raiz < elemento){
                raiz.dir = new NoAlv(elemento);
            }else{
                raiz.esq = new NoAlv(elemento);
            }

            else if(raiz.dir == null){
                if(raiz.dir > elemento){
                    raiz.dir = new NoAlv(elemento);
                }
            }
        }
    }

}

class zero {
    public class static void mainn(String[] args){
        inserir(x);
    }
}
