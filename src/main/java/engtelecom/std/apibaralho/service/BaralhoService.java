package engtelecom.std.apibaralho.service;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import engtelecom.std.apibaralho.entities.Baralho;

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

    public Boolean excluir(String id){
        if (baralhos.get(id) != null) {
            baralhos.remove(id);
            return true;
        }
        return false;
    }

    public Set<String> listarBaralhos(){
        return baralhos.keySet();
    }
}
