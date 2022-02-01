package modelo.entrada;

import java.io.Serializable;
import java.util.*;

import modelo.evento.Representacion;
import modelo.notificacion.Notificacion;
import modelo.sistema.Sistema;
import modelo.usuario.*;
import modelo.zona.Zona;

/**
 * Clase encargada de la lista de espera por cada zona y representacion
 *
 * @author Jaime Diaz, Javier Fraile e Ivan Fernandez
 */
public class ListaEspera implements Serializable{

	private static final long serialVersionUID = 5084818877357221497L;
	
	private ArrayList<Usuario> UsuariosEnEspera;
	private Representacion representacion;
	private Zona zona;

	/**
	 * Constructor de ListaEspera
	 *
	 * @param representacion Representacion de la cual se generará la lista de
	 *                       espera
	 * @param zona           zona a partir de la cual será generada la lista de
	 *                       espera
	 * 
	 */
	public ListaEspera(Representacion representacion, Zona zona) {
		this.UsuariosEnEspera = new ArrayList<>();
		this.setListaEsperaZona(zona);
		this.setRepresentacion(representacion);
	}

	/**
	 * Método encargado de añadir a un usuario a la lista de espera
	 *
	 * @param usuario usuario a añadir a la lista de espera
	 * 
	 */
	public void addUsuariosEnEspera(Usuario usuario) {

		/*
		 * Si el usuario ya ha sido registreado en la lista de espera no se añade de
		 * nuevo
		 */
		if (!this.getUsuariosEnEspera().contains(usuario)) {
			this.getUsuariosEnEspera().add(usuario);
		}
	}

	/**
	 * Método encargado de avisar a los usuarios almacenados en la lista de espera
	 * de que hay entradas disponibles
	 * 
	 */
	public void avisarEntradasDisponibles() {
		for (Usuario usuario : this.getUsuariosEnEspera()) {
			Notificacion notificacion = new Notificacion(Sistema.getInstance().getContadorNotificaciones(),
					"Entrada para " + representacion.getRepresentacionEvento().getEventoTitulo() + " disponible",
					"¡Entradas disponibles en la zona" + this.getListaEsperaZona() + "¡Corre que vuelan!", usuario);
			notificacion.notificarUsuario();
			Sistema.getInstance().setContadorNotificaciones();
		}
		
		this.UsuariosEnEspera.removeAll(UsuariosEnEspera); /* Una vez avisados a todos los usuarios se limpia la lista de espera */
	}

	/** GETTERS **/

	/**
	 * Método getter de la zona a partir de la cual se ha generado la lista de
	 * espera
	 *
	 * @return Objeto de tipo Zona
	 * 
	 */
	public Zona getListaEsperaZona() {
		return zona;
	}

	/**
	 * Método getter de un ArrayList de usuarios que están en la lista de espera
	 *
	 * @return ArrayList de usuarios
	 * 
	 */
	public ArrayList<Usuario> getUsuariosEnEspera() {
		return UsuariosEnEspera;
	}

	/**
	 * Método getter de la represetnacion a partir de la cual se ha generado la
	 * lista de espera
	 *
	 * @return Objeto de tipo Representacion
	 * 
	 */
	public Representacion getRepresentacion() {
		return representacion;
	}

	/** SETTERS **/

	/**
	 * Método setter de la zona de la lista de espera
	 *
	 * @param zona zona a settear
	 * 
	 */
	public void setListaEsperaZona(Zona zona) {
		this.zona = zona;
	}

	/**
	 * Método setter de la representacion de la lista de espera
	 *
	 * @param representacion representacion a settear
	 * 
	 */
	public void setRepresentacion(Representacion representacion) {
		this.representacion = representacion;
	}

}