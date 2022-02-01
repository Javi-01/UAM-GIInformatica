package modelo.evento;

import java.io.Serializable;

import modelo.sistema.Sistema;
import modelo.zona.*;

/**
 * Clase para realizar las operaciones relacionadas con el precio de las zonas
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PrecioZona implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1270134563351621126L;
	
	private Zona zona;
	private double precio; 

	/**
	 * Constructor de PrecioZona
	 *
	 * @param evento 		evento al cual ser� a�adido un objeto de tipo PrecioZona
	 * @param zona  		zona a la cual se establecer� el precio 
	 * @param precio			precio de la zona
	 */
	public PrecioZona(Evento evento, Zona zona, double precio) {
		this.zona = zona;
		this.setPrecioZonaEvento(evento, zona, precio);
	}

	/** GETTERS **/
	
	/**
	 * M�todo getter del precio que tiene una zona de un determinado evento
	 *
	 * @param evento 	evento del cual ser� consultado el precio de una zona
	 * @param zona 		zona a consultar
	 * @return double del precio de la zona  
	 * 
	 */
	public double getPrecioZonaEvento (Evento evento, Zona zona) {
		if (!Sistema.getInstance().getSistemaEventos().contains(evento)) {
			return 0;
		}

		if (!Sistema.getInstance().getSistemaZonas().contains(zona)) {
			return 0;
		}
		for (Zona z: evento.getEventoZonas()) {
			if (z.equals(zona)) {
				if (!z.getClass().equals(ZonaCompuesta.class))
					return this.precio;
				}else {
					
				}
		}
		return 0;
	}
	
	/**
	 * M�todo getter de la zona de PrecioZona
	 *
	 * @return zona objeto de tipo Zona  
	 * 
	 */
	public Zona getPrecioZonaZona() {
		return zona;
	}

	/**
	 * M�todo getter del precio de la zona guardada en PrecioZona en caso de ser 
	 * una zona del teatro que tiene el mismo precio que tiene el mismo precio para todos los eventos
	 *
	 * @return double del precio de la zona  
	 * 
	 */
	public double getPrecioZonaPrecio () {
		return this.precio;
	}
	
	/** SETTERS **/
	
	/**
	 * M�todo setter del precio que tiene una zona de un determinado evento
	 *
	 * @param evento 	evento del cual ser� consultado el precio de una zona
	 * @param zona 		zona a consultar
	 * @param precio 	precio que tendr� dicha zona del evento 
	 * @return true si se ha establecido correctamente el precio o false en caso contrario   
	 * 
	 */
	private Boolean setPrecioZonaEvento(Evento evento, Zona zona, double precio) {

		if (!Sistema.getInstance().getSistemaEventos().contains(evento)) {
			return false;
		}
		
		if (!Sistema.getInstance().getSistemaZonas().contains(zona)) {
			return false;
		}
		for (Zona z: evento.getEventoZonas()) {
			if (z.equals(zona)) {
				if (!z.getClass().equals(ZonaCompuesta.class))
					this.precio = precio;
					return true;
				}
		}
		return false;
	}
}
