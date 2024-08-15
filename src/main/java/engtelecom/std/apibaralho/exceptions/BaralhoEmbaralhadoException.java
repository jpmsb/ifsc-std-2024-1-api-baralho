package engtelecom.std.apibaralho.exceptions;

public class BaralhoEmbaralhadoException extends RuntimeException {
    public BaralhoEmbaralhadoException(String uuid){
        super("OOPS! O baralho com o identificador: \'" + uuid + "\' já está embaralhado!");
    }
}
