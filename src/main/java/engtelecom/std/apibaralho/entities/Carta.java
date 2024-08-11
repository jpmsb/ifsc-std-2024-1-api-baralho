package engtelecom.std.apibaralho.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Carta {
    @NonNull
    private String naipe;

    @NonNull
    private String valor;

    @NonNull
    private String codigo;
}
