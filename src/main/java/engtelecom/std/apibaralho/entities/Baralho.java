package engtelecom.std.apibaralho.entities;

import java.util.ArrayList;
import java.util.Collections;

public class Baralho {
    private final String[] NAIPES = {"copas", "espadas", "ouros", "paus"};
    private final String[] VALORES = {"As", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Valete", "Dama", "Rei"};
    private final String[] VALORES_ABREVIADOS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "j", "q", "k"};
    private ArrayList<Carta> cartas;
    private boolean embaralhado = false;

    public Baralho(){
        cartas = new ArrayList<>();

        for (String naipe : NAIPES) {
            int contador = 0;
            for (String valor : VALORES) {
                cartas.add(new Carta(naipe, valor, VALORES_ABREVIADOS[contador] + naipe.charAt(0)));
                contador++;
            }
        }
    }

    public Boolean embaralhar(){
        Collections.shuffle(cartas);
        embaralhado = true;
        return embaralhado;
    }

    public Boolean getEmbaralhado(){
        return embaralhado;
    }

    public ArrayList<Carta> getCartas(){
        return this.cartas;
    }

    public int quantidadeCartas(){
        return cartas.size();
    }
}
