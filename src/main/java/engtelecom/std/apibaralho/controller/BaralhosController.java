package engtelecom.std.apibaralho.controller;

import java.util.Set;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import engtelecom.std.apibaralho.entities.Carta;
import engtelecom.std.apibaralho.exceptions.BaralhoEmbaralhadoException;
import engtelecom.std.apibaralho.exceptions.BaralhoNaoEncontradoException;
import engtelecom.std.apibaralho.service.BaralhoService;


@RestController
@RequestMapping({"/baralhos", "/baralhos/"})
public class BaralhosController {
    @Autowired
    private BaralhoService baralhoService;

    @GetMapping
    public Map<String, Set<String>> listarBaralhos(){
        Map<String, Set<String>> baralhos = new HashMap<>();
        baralhos.put("baralhos", this.baralhoService.listarBaralhos());
        return baralhos;
    }

    @GetMapping("/{uuid}")
    public Map<String, ArrayList<Map<String, String>>> listaCartas(@PathVariable String uuid){
        if (! this.baralhoService.existe(uuid)) {
            throw new BaralhoNaoEncontradoException(uuid);
        } else if (this.baralhoService.embaralhado(uuid)) {
            throw new BaralhoEmbaralhadoException(uuid);
        }

        ArrayList<Carta> cartas = this.baralhoService.listarCartas(uuid);
        ArrayList<Map<String, String>> cartasCompletas = new ArrayList<>();

        for (Carta carta : cartas) {
            String urlBase = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

            // LinkedHashMap garante que a ordem permaneça a mesma de quando foi adicionada
            Map<String, String> cartaCompleta = new LinkedHashMap<>();
            cartaCompleta.put("codigo", carta.getCodigo());
            cartaCompleta.put("naipe", carta.getNaipe());
            cartaCompleta.put("valor", carta.getValor());
            cartaCompleta.put("url", urlBase + "/baralhos/carta/" + carta.getCodigo() + ".png");

            cartasCompletas.add(cartaCompleta);
        }

        Map<String, ArrayList<Map<String, String>>> cartasCompletasChave = new HashMap<>();
        cartasCompletasChave.put("cartas", cartasCompletas);
        return cartasCompletasChave;
    }

    @GetMapping(value = "/carta/{carta}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String carta){
        InputStream is = BaralhoService.class.getClassLoader().getResourceAsStream("static/cartas/" + carta);
        return ResponseEntity.ok().body(new InputStreamResource(is));
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

    @PutMapping("/{uuid}")
    public String embaralhar(@PathVariable String uuid){
        if (this.baralhoService.embaralhar(uuid)){
             return "{ \"status\" : \"sucesso\", \"quantidadeCartas\" : " + this.baralhoService.quantidadeCartas(uuid) + " }";
        }
        return "{ \"status\" : \"falha\", \"quantidadeCartas\" : " + this.baralhoService.quantidadeCartas(uuid) + " }";
    }

    @ControllerAdvice
    class BaralhoNaoEncontrado {
        @ResponseBody
        @ExceptionHandler(BaralhoNaoEncontradoException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        String baralhoNaoEncontrado(BaralhoNaoEncontradoException p){
            return "{ \"erro\" : \"" + p.getMessage() + "\" }";
        }
    }

    @ControllerAdvice
    class BaralhoEmbaralhado {
        @ResponseBody
        @ExceptionHandler(BaralhoEmbaralhadoException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        String baralhoEmbaralhado(BaralhoEmbaralhadoException p){
            return "{ \"erro\" : \"" + p.getMessage() + "\" }";
        }
    }
}
