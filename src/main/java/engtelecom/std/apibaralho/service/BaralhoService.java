package engtelecom.std.apibaralho.service;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import engtelecom.std.apibaralho.entities.Baralho;
import engtelecom.std.apibaralho.entities.Carta;

@Component
public class BaralhoService {
    private HashMap<String, Baralho> baralhos;

    public BaralhoService(){
        baralhos = new HashMap<>();
    }

    public String criar(){
        String uuid = UUID.randomUUID().toString();
        Baralho novoBaralho = new Baralho();

        baralhos.put(uuid, novoBaralho);

        return uuid;
    }

    public Boolean excluir(String uuid){
        if (baralhos.get(uuid) != null) {
            baralhos.remove(uuid);
            return true;
        }
        return false;
    }

    public Set<String> listarBaralhos(){
        return baralhos.keySet();
    }

    public ArrayList<Carta> listarCartas(String uuid){
        return baralhos.get(uuid).getCartas();
    }
}
