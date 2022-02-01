package modelo.usuario;

import java.io.Serializable;
import java.time.LocalDate;

import modelo.abono.*;

/**
 * Clase para realizar las operaciones relacionadas con las fechas de los Abonos Anuales
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */

public class AbonoAnualFecha implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 272009615625717616L;
	
	private LocalDate fechaCompra;
	private LocalDate fechaCaducidad;
	private Abono abono;
	
	/**
	 * Constructor de AbonoAnualFecha
	 *
	 * @param fechaCompra fecha de compra del abono
	 * @param abono 	abono al que se le establecera la fecha de compra
	 */
	public AbonoAnualFecha(LocalDate fechaCompra, Abono abono) {
		this.fechaCompra = fechaCompra;
		this.setAbono(abono);
		this.fechaCaducidad = this.getFechaCompra().plusYears(1);
	}

	/** GETTERS **/
	
	/**
	 * Metodo getter de la fecha de compra
	 * 
	 * @return LocalDate de la fecha
	 */
	public LocalDate getFechaCompra() {
		return fechaCompra;
	}

	/**
	 * Metodo getter de la fecha de caducidad
	 * 
	 * @return LocalDate de la fecha
	 */
	public LocalDate getFechaCaducidad() {
		return fechaCaducidad;
	}
	
	/**
	 * Metodo getter de la fecha de caducidad
	 * 
	 * @return LocalDate de la fecha
	 */
	public Abono getAbono() {
		return abono;
	}
	
	/** SETTERS **/
	
	/**
	 * Metodo para establecer la fecha de caducidad
	 * 
	 * @param fechaCaducidad LocalDate con fecha de caducidad del abono
	 */
	public void setFechaCaducidad(LocalDate fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}
	
	/**
	 * Metodo getter de la fecha de caducidad
	 * 
	 * @param abono 	abono al que se le establecera la fecha
	 */
	public void setAbono(Abono abono) {
		this.abono = abono;
	}
}