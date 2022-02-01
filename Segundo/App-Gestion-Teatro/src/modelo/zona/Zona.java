package modelo.zona;

import java.io.Serializable;

/**
 * Clase Zona
 *
 * @author Jaime Diaz, Javier Fraile e Ivan Fernandez
 */
public abstract class Zona implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3236345835779538483L;
	
	private int aforoTotal;
	private int identificador;
	private String nombreZona;

	/**
	 * Constructor de una zona que no recibe aforo
	 *
	 * @param identificador    Id de la zona a crear
	 * @param nombreZona	nombre que tendrá la zona
	 * 
	 */
	public Zona(int identificador, String nombreZona) { 
		this.identificador = identificador;
		this.nombreZona = nombreZona;
	}

	/**
	 * Constructor de la zona que recibe aforo
	 *
	 * @param identificador    Id de la zona a crear
	 * @param aforo 	número total del aofor que tendrá la zona
	 * @param nombreZona	nombre que tendrá la zona
	 * 
	 */
	public Zona(int identificador, int aforo, String nombreZona) {
		this.aforoTotal = aforo;
		this.identificador = identificador;
		this.nombreZona = nombreZona;
	}

	/** GETTERS **/
	
	/**
	 * Método getter que devuelve el aforo que tiene una zona
	 *
	 * @return número de aforo
	 * 
	 */
	public int getZonaAforo() {
		return aforoTotal;
	}	
	
	/**
	 * Método getter que devuelve el id de la zona
	 *
	 * @return identificador de la zona
	 * 
	 */
	public int getZonaIdentificador() {
		return identificador;
	}

	/**
	 * Método getter que devuelve el nombre de la zona
	 *
	 * @return nombre de la zona
	 * 
	 */
	public String getZonaNombre() {
		return nombreZona;
	}
	
	/**
	 * Método setter que devuelve el nombre de la zona
	 *
	 * @param aforo total
	 * 
	 */
	public void setZonaAforo(int aforo) {
		this.aforoTotal = aforo;
	}

}