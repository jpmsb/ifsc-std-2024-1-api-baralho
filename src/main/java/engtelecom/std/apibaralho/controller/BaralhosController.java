package engtelecom.std.apibaralho.controller;

import java.util.Set;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import engtelecom.std.apibaralho.entities.Carta;
import engtelecom.std.apibaralho.exceptions.BaralhoNaoEncontradoException;
import engtelecom.std.apibaralho.service.BaralhoService;


@RestController
@RequestMapping({"/baralhos", "/baralhos/"})
public class BaralhosController {
    @Autowired
    private BaralhoService baralhoService;

    @GetMapping
    public Set<String> listarBaralhos(){
        return this.baralhoService.listarBaralhos();
    }

    @GetMapping("/{uuid}")
    public ArrayList<Map<String, String>> listaCartas(@PathVariable String uuid){
        ArrayList<Carta> cartas = this.baralhoService.listarCartas(uuid);
        ArrayList<Map<String, String>> cartasCompletas = new ArrayList<>();

        for (Carta carta : cartas) {
            String urlBase = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

            // Garante que a ordem permaneça a mesma de quando foi adicionada
            Map<String, String> cartaCompleta = new LinkedHashMap<>();
            cartaCompleta.put("codigo", carta.getCodigo());
            cartaCompleta.put("naipe", carta.getNaipe());
            cartaCompleta.put("valor", carta.getValor());
            cartaCompleta.put("url", urlBase + "/carta/" + carta.getCodigo() + ".png");

            cartasCompletas.add(cartaCompleta);
        }

        return cartasCompletas;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String criarBaralho() {
        String uuidNovoBaralho = this.baralhoService.criar();
        return "{ \"uuid\" : \"" + uuidNovoBaralho + "\" }";
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirBaralho(@PathVariable String uuid){
        if (! this.baralhoService.excluir(uuid)){
            throw new BaralhoNaoEncontradoException(uuid);
        }
    }

    @ControllerAdvice
    class BaralhoNaoEncontrado {
        @ResponseBody
        @ExceptionHandler(BaralhoNaoEncontradoException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        String baralhoNaoEncontrado(BaralhoNaoEncontradoException p){
            return p.getMessage();
        }
    }
}
