package engtelecom.std.apibaralho.exceptions;

public class BaralhoVazioException extends RuntimeException {
    public BaralhoVazioException(String uuid){
        super("OOPS! O baralho com o identificador: \'" + uuid + "\' está vazio!");
    }
}
