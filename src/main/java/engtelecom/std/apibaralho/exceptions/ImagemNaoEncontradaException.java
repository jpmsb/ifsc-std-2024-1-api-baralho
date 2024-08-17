package engtelecom.std.apibaralho.exceptions;

public class ImagemNaoEncontradaException extends RuntimeException {
    public ImagemNaoEncontradaException(String carta){
        super("Não foi possível encontrar o arquivo de imagem " + carta);
    }
}
