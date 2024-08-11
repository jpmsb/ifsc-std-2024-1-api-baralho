package engtelecom.std.apibaralho.exceptions;

public class BaralhoNaoEncontradoException extends RuntimeException {
    public BaralhoNaoEncontradoException(String uuid){
        super("Não foi possível encontrar o baralho com o identificador: " + uuid);
    }
}
