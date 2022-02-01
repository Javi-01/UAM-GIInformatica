package modelo.zona;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Clase Butaca
 *
 * @author Jaime Diaz, Javier Fraile e Ivan Fernandez
 */
public class Butaca implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2329558318003254674L;
	private int fila;
	private int columna;
	private boolean habilitada = true;
	private String motivoDeshabilitacion; 
	private LocalDateTime inicioDeshabilitacion;
	private LocalDateTime finDeshabilitacion;

	/**
	 * Constructor de Butaca
	 *
	 * @param fila    Fila donde se sitaurá la butaca
	 * @param columna    Columna donde se sitaurá la butaca
	 * 
	 */
	public Butaca(int fila, int columna) {
		this.columna = columna;
		this.fila = fila;
		this.motivoDeshabilitacion = " ";
		this.setButacaInicioDeshabilitacion(null);
		this.setButacaFinDeshabilitacion(null);
	}
	
	/**
	 * Método encargado de comprobar si una entrada esta habilitada o no 
	 *
	 * @return true si la butaca esta habilitada o false en caso contrario 
	 * 
	 */
	public boolean isButacaHabilitada() {
		return habilitada;
	}

	/** GETTERS **/
	
	/**
	 * Método getter de la columna de la butaca
	 *
	 * @return número de la columna donde está situada la butaca
	 * 
	 */
	public int getButacaColumna() {
		return columna;
	}

	/**
	 * Método getter de la fila de la butaca
	 *
	 * @return número de la fila donde está situada la butaca
	 * 
	 */
	public int getButacaFila() {
		return fila;
	}
	
	/** SETTERS **/
	
	/**
	 * Método setter de la columna de la butaca
	 *
	 * @param columna número de la columna donde se situará la butaca
	 * 
	 */
	public void setButacaColumna(int columna) {
		this.columna = columna;
	}
	
	/**
	 * Método setter de la fila de la butaca
	 *
	 * @param fila número de la fila donde se situará la butaca
	 * 
	 */
	public void setButacaFila(int fila) {
		this.fila = fila;
	}

	/**
	 * Método setter acerca de la disponibilidad de la butaca
	 *
	 * @param habilitada booleano acerca de la dispnibilidad de la butaca
	 * 
	 */
	public void setButacaHabilitada(boolean habilitada) {
		this.habilitada = habilitada;
	}

	public LocalDateTime getButacaInicioDeshabilitacion() {
		return inicioDeshabilitacion;
	}

	public void setButacaInicioDeshabilitacion(LocalDateTime inicioDeshabilitacion) {
		this.inicioDeshabilitacion = inicioDeshabilitacion;
	}

	public LocalDateTime getButacaFinDeshabilitacion() {
		return finDeshabilitacion;
	}

	public void setButacaFinDeshabilitacion(LocalDateTime finDeshabilitacion) {
		this.finDeshabilitacion = finDeshabilitacion;
	}

	public String getButacaMotivoDeshabilitacion() {
		return motivoDeshabilitacion;
	}

	public void setButacaMotivoDeshabilitacion(String motivoDeshabilitacion) {
		this.motivoDeshabilitacion = motivoDeshabilitacion;
	}
}