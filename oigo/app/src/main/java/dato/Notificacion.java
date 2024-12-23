package dato;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import com.tecesind.oigo.TraducirLSB.modelo.SpeechRecognitionHelper;

/**
 * Entity mapped to table "FRASE".
 */
public class Notificacion {

    private Long id;
    private String mensaje;
    private Long idGrupo;
    private String complemento;

    public Notificacion() {
    }

    public Notificacion(Long id) {
        this.id = id;
    }

    public Notificacion(Long id, String mensaje, Long idGrupo, String complemento) {
        this.id = id;
        this.mensaje = mensaje;
        this.idGrupo = idGrupo;
        this.complemento = complemento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getComplemento() { return complemento; }

    public void setComplemento(String complemento) { this.complemento = complemento; }

}
