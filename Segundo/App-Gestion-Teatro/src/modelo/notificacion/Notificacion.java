package modelo.notificacion;

import java.io.Serializable;

import modelo.usuario.Usuario;

/**
 * Clase encargada de las notificacione
 *
 * @author Jaime Diaz, Javier Fraile e Ivan Fernandez
 */
public class Notificacion implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5022939957996756782L;
	
	private Usuario usuario;
	private Integer id;
	private String tituloNotificacion;
	private String asuntoNotificacion;
	private boolean leido = false;

	/**
	 * Constructor de Notificacion
	 *
	 * @param id 	identificador de la notificacion
	 * @param tituloNotificacion    Título que tendrá la notifiación a crear
	 * @param asuntoNotificacion    Asunto que tendrá la notifiación a crear
	 * @param usuario    usuario que se le asociará la notifiación
	 * 
	 */
	public Notificacion(Integer id, String tituloNotificacion, String asuntoNotificacion, Usuario usuario) {
		this.tituloNotificacion = tituloNotificacion;
		this.asuntoNotificacion = asuntoNotificacion;
		this.usuario = usuario;
		this.id = id;
	}

	/**
	 * Método encargado de indicar si la notifiación ha sido leida o no 
	 *
	 * @return true si ya ha sido leida por el usuario o false en caso contrario 
	 * 
	 */
	public boolean isNotificacionLeida() {
		return leido;
	}

	/**
	 * Método encargado de indicar que la notificacion ha sido leida
	 * 
	 */
	public void notificacionLeida() {
		this.leido = true;
	}
	
	/**
	 * Método encargado de notifiacr al usuario propietario de la notificacion
	 * 
	 */
	public void notificarUsuario() {
		usuario.getUsuarioNotificaciones().add(this);
	}
	
	/**
	 * Método toString que indica la información de la notificacion
	 * 
	 * @return cadena con la información de la notificación
	 * 
	 */
	public String notificacionMostrarAusuario () {
		 return "Titulo: "+ this.tituloNotificacion + " Asunto: " + this.asuntoNotificacion;
	}
	
	/** GETTERS **/
	
	/**
	 * Método getter del usuario al que le pertenece la notificacion
	 * 
	 * @return usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * Método getter del titulo de la notificacion
	 * 
	 * @return cadena con el titulo de la notificacion
	 */
	public String getTituloNotificacion() {
		return tituloNotificacion;
	}

	/**
	 * Método getter del asunto de la notificacion
	 * 
	 * @return cadena con el asunto de la notificacion
	 */
	public String getAsuntoNotificacion() {
		return asuntoNotificacion;
	}

	/**
	 * Método getter del identificador
	 * 
	 * @return id de la notificacion
	 */
	public Integer getIdentificadorNotificacion() {
		return id;
	}
	
}
