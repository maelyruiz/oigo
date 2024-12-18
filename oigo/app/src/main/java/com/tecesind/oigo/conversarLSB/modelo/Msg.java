package com.tecesind.oigo.conversarLSB.modelo;


import dato.Usuario;

public class Msg {

	
	private Usuario emisor;
    private Usuario destino;
    private String mensaje;
    private String mensajeOriginal;
    private boolean tipo;
    private String fecha;
    private String hora;

    public Msg() {
    }

    public Msg(Usuario emisor, Usuario destino, String mensaje, String mensajeOriginal, boolean tipo, String fecha, String hora) {
        this.emisor = emisor;
        this.destino = destino;
        this.mensaje = mensaje;
        this.mensajeOriginal = mensajeOriginal;
        this.tipo = tipo;
        this.fecha = fecha;
        this.hora = hora;
    }

	public Usuario getEmisor() {
		return emisor;
	}

	public void setEmisor(Usuario emisor) {
		this.emisor = emisor;
	}

	public Usuario getDestino() {
		return destino;
	}

	public void setDestino(Usuario destino) {
		this.destino = destino;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensajeOriginal() {
		return mensajeOriginal;
	}

	public void setMensajeOriginal(String mensajeOriginal) {
		this.mensajeOriginal = mensajeOriginal;
	}

	public boolean isTipo() {
		return tipo;
	}

	public void setTipo(boolean tipo) {
		this.tipo = tipo;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

   
}
