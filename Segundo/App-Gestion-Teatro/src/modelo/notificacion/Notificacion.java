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
	 * @param tituloNotificacion    T�tulo que tendr� la notifiaci�n a crear
	 * @param asuntoNotificacion    Asunto que tendr� la notifiaci�n a crear
	 * @param usuario    usuario que se le asociar� la notifiaci�n
	 * 
	 */
	public Notificacion(Integer id, String tituloNotificacion, String asuntoNotificacion, Usuario usuario) {
		this.tituloNotificacion = tituloNotificacion;
		this.asuntoNotificacion = asuntoNotificacion;
		this.usuario = usuario;
		this.id = id;
	}

	/**
	 * M�todo encargado de indicar si la notifiaci�n ha sido leida o no 
	 *
	 * @return true si ya ha sido leida por el usuario o false en caso contrario 
	 * 
	 */
	public boolean isNotificacionLeida() {
		return leido;
	}

	/**
	 * M�todo encargado de indicar que la notificacion ha sido leida
	 * 
	 */
	public void notificacionLeida() {
		this.leido = true;
	}
	
	/**
	 * M�todo encargado de notifiacr al usuario propietario de la notificacion
	 * 
	 */
	public void notificarUsuario() {
		usuario.getUsuarioNotificaciones().add(this);
	}
	
	/**
	 * M�todo toString que indica la informaci�n de la notificacion
	 * 
	 * @return cadena con la informaci�n de la notificaci�n
	 * 
	 */
	public String notificacionMostrarAusuario () {
		 return "Titulo: "+ this.tituloNotificacion + " Asunto: " + this.asuntoNotificacion;
	}
	
	/** GETTERS **/
	
	/**
	 * M�todo getter del usuario al que le pertenece la notificacion
	 * 
	 * @return usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * M�todo getter del titulo de la notificacion
	 * 
	 * @return cadena con el titulo de la notificacion
	 */
	public String getTituloNotificacion() {
		return tituloNotificacion;
	}

	/**
	 * M�todo getter del asunto de la notificacion
	 * 
	 * @return cadena con el asunto de la notificacion
	 */
	public String getAsuntoNotificacion() {
		return asuntoNotificacion;
	}

	/**
	 * M�todo getter del identificador
	 * 
	 * @return id de la notificacion
	 */
	public Integer getIdentificadorNotificacion() {
		return id;
	}
	
}
