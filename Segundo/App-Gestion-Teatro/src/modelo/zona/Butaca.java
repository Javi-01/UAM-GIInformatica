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
	 * @param fila    Fila donde se sitaur� la butaca
	 * @param columna    Columna donde se sitaur� la butaca
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
	 * M�todo encargado de comprobar si una entrada esta habilitada o no 
	 *
	 * @return true si la butaca esta habilitada o false en caso contrario 
	 * 
	 */
	public boolean isButacaHabilitada() {
		return habilitada;
	}

	/** GETTERS **/
	
	/**
	 * M�todo getter de la columna de la butaca
	 *
	 * @return n�mero de la columna donde est� situada la butaca
	 * 
	 */
	public int getButacaColumna() {
		return columna;
	}

	/**
	 * M�todo getter de la fila de la butaca
	 *
	 * @return n�mero de la fila donde est� situada la butaca
	 * 
	 */
	public int getButacaFila() {
		return fila;
	}
	
	/** SETTERS **/
	
	/**
	 * M�todo setter de la columna de la butaca
	 *
	 * @param columna n�mero de la columna donde se situar� la butaca
	 * 
	 */
	public void setButacaColumna(int columna) {
		this.columna = columna;
	}
	
	/**
	 * M�todo setter de la fila de la butaca
	 *
	 * @param fila n�mero de la fila donde se situar� la butaca
	 * 
	 */
	public void setButacaFila(int fila) {
		this.fila = fila;
	}

	/**
	 * M�todo setter acerca de la disponibilidad de la butaca
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