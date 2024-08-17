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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import engtelecom.std.apibaralho.entities.Carta;
import engtelecom.std.apibaralho.exceptions.BaralhoEmbaralhadoException;
import engtelecom.std.apibaralho.exceptions.BaralhoNaoEncontradoException;
import engtelecom.std.apibaralho.exceptions.BaralhoVazioException;
import engtelecom.std.apibaralho.service.BaralhoService;
import org.springframework.web.bind.annotation.RequestBody;



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
        } else if (this.baralhoService.quantidadeCartas(uuid) == 0) {
            throw new BaralhoVazioException(uuid);
        }

        Map<String, ArrayList<Map<String, String>>> cartasCompletasChave = new HashMap<>();
        cartasCompletasChave.put("cartas", montaCartaCompleta(this.baralhoService.listarCartas(uuid)));
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
    public String embaralha(@PathVariable String uuid){
        if (! this.baralhoService.existe(uuid)) {
            throw new BaralhoNaoEncontradoException(uuid);
        }

        if (this.baralhoService.embaralha(uuid)){
             return "{ \"status\" : \"sucesso\", \"quantidadeCartas\" : " + this.baralhoService.quantidadeCartas(uuid) + " }";
        }
        return "{ \"status\" : \"falha\", \"quantidadeCartas\" : " + this.baralhoService.quantidadeCartas(uuid) + " }";
    }

    @PutMapping(path = "/{uuid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> removeNCartas(@PathVariable String uuid, @RequestBody String quantidade) {
        if (! this.baralhoService.existe(uuid)) {
            throw new BaralhoNaoEncontradoException(uuid);
        } else if (this.baralhoService.quantidadeCartas(uuid) == 0) {
            throw new BaralhoVazioException(uuid);
        }

        Map<String, Object> cartasRemovidasEQuantidadeRestante = new HashMap<>();
        JsonObject jsonProcessado = new Gson().fromJson(quantidade, JsonObject.class);
        int quantidadeDoJson = Integer.parseInt(jsonProcessado.get("quantidade").toString());

        ArrayList<Carta> cartasRemovidas =  this.baralhoService.removeNCartas(uuid, quantidadeDoJson);
        cartasRemovidasEQuantidadeRestante.put("cartas", montaCartaCompleta(cartasRemovidas));
        cartasRemovidasEQuantidadeRestante.put("quantidadeRestante", this.baralhoService.quantidadeCartas(uuid));

        return cartasRemovidasEQuantidadeRestante;
    }

    private ArrayList<Map<String, String>> montaCartaCompleta(ArrayList<Carta> cartas){
        ArrayList<Map<String, String>> cartasCompletas = new ArrayList<>();
        String urlBase = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        for (Carta carta : cartas) {
            // LinkedHashMap garante que a ordem permaneça a mesma de quando foi adicionada
            Map<String, String> cartaCompleta = new LinkedHashMap<>();
            cartaCompleta.put("codigo", carta.getCodigo());
            cartaCompleta.put("naipe", carta.getNaipe());
            cartaCompleta.put("valor", carta.getValor());
            cartaCompleta.put("url", urlBase + "/baralhos/carta/" + carta.getCodigo() + ".png");

            cartasCompletas.add(cartaCompleta);
        }

        return cartasCompletas;
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
        @ResponseStatus(HttpStatus.FORBIDDEN)
        String baralhoEmbaralhado(BaralhoEmbaralhadoException p){
            return "{ \"erro\" : \"" + p.getMessage() + "\" }";
        }
    }

    @ControllerAdvice
    class BaralhoVazio {
        @ResponseBody
        @ExceptionHandler(BaralhoVazioException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        String baralhoVazio(BaralhoVazioException p){
            return "{ \"erro\" : \"" + p.getMessage() + "\" }";
        }
    }
}
