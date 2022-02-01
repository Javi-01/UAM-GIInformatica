package modelo.entrada;

import java.io.Serializable;
import java.time.*;
import es.uam.eps.padsof.tickets.*;
import modelo.evento.*;
import modelo.usuario.*;
import modelo.zona.*;

/**
 * Clase para realizar las operaciones relacionadas con las entradas
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public abstract class Entrada implements ITicketInfo, Serializable{
	
	private static final long serialVersionUID = -7671386568173301564L;
	
	private Usuario usuarioCompra;
	private Usuario usuarioReserva;
	private double precioEntrada;
	private int codigo;
	private LocalDateTime fechaValidezEntrada; 
	private Boolean validezEntrada;
	private Boolean ocupada = false;
	private Boolean habilitada = true;
	private Representacion representacion;
	private Zona zona;
	private boolean compraAbono = false;
	
	/**
	 * Constructor de Entrada
	 *
	 * @param codigo 	 codigo de la entrada
	 * @param representacion  	representacion que almacenar� la entrada
	 * @param zona 		zona a partir de la cual se genera la entrada
	 * @param precio  	precio de la entrada segun la zona
	 * @param fechaValidezEntrada 		fecha limite de validez de la entrada
	 */
	public Entrada(int codigo, Representacion representacion, Zona zona, double precio, LocalDateTime fechaValidezEntrada) {
		this.codigo = codigo;
		this.representacion = representacion;
		this.zona = zona;
		this.precioEntrada = precio;
		this.fechaValidezEntrada = fechaValidezEntrada;
	}
	
	/**
	 * Metodo abstracto encargado de comprar entradas con una tarjeta de cr�dito
	 *
	 * @param numeroTarjeta 	numero de la tarjeta con la que se comprar� la entrada
	 * @throws NonExistentFileException		excepcion si no existe la raiz indicada donde guardar el pdf de la entrada 
	 * @throws UnsupportedImageTypeException	excepcion si no encuentra la imagen de la entrada
	 * 
	 * @return true si se ha comprado correctamente la entrada o false en caso contrario 
	 */
	public abstract boolean comprarEntradaTarjeta(String numeroTarjeta) throws NonExistentFileException, UnsupportedImageTypeException;
	
	/**
	 * Metodo abstracto encargado de comprar entradas con un abono
	 * comprobando si el usuario posee un abono anual en dicha zana 
	 * o un abono de cilos que contine el evento indicado en la entrada 
	 * 
	 * @return true si se ha comprado correctamente la entrada o false en caso contrario 
	 */
	public abstract boolean comprarEntradaAbono();
	
	/**
	 * Metodo abstracto encargado de validar la compra 
	 * comprueba que el numero de la tarjeta sea correcto 
	 * 
	 * @return true si se ha podido tramitar la compra o false en caso contrario 
	 */
	protected abstract boolean tramitarEntradaCompra(String numeroTarjeta);
	
	/**
	 * Metodo abstracto encargado de generar el pdf 
	 * de la entrada comprada 
	 *  
	 */
	protected abstract void tramitarEntradaPDF();
	
	/**
	 * Metodo abstracto encargado de reservar la entrada 
	 * 
	 * @return true si se ha podido reservar correctamente la entrada o false en caso contrario 
	 */
	public abstract boolean reservarEntrada();
	
	/**
	 * Metodo abstracto encargado de cancelar la reserva de una entrada 
	 * 
	 * @return true si se ha podido cancelar la entrada o false en caso contrario 
	 */
	public abstract boolean cancelarEntradaReservada();
	
	
	/***GETTERS***/
	
	/**
	 * M�todo getter del precio que tiene la entrada
	 *
	 * @return precioEntrada 
	 * 
	 */
	public double getPrecioEntrada() {
		return precioEntrada;
	}
	
	/**
	 * M�todo getter del codigo que tiene la entrada
	 *
	 * @return codigo 
	 * 
	 */
	public int getCodigoValidacion() {
		return codigo;
	}
	
	/**
	 * M�todo getter de la validez de la entrada
	 *
	 * @return validezEntrada 
	 * 
	 */
	public Boolean getValidezEntrada() {
		return validezEntrada;
	}
	
	/**
	 * M�todo getter de la fecha de validez de la entrada
	 *
	 * @return fechaValidezEntrada 
	 * 
	 */
	public LocalDateTime getFechaValidezEntrada() {
		return fechaValidezEntrada;
	}
	
	/**
	 * M�todo getter del usaurio que compr� la entrada
	 *
	 * @return usuarioCompra 
	 * 
	 */
	public Usuario getEntradaUsuarioCompra() {
		return usuarioCompra;
	}
	
	/**
	 * M�todo getter del usaurio que reserv� la entrada
	 *
	 * @return usuarioCompra 
	 * 
	 */
	public Usuario getEntradaUsuarioReserva() {
		return usuarioReserva;
	}

	/**
	 * M�todo getter de si la entrada est� ocupada o no
	 *
	 * @return ocupada (si esta a true sigifica que est� ocupado y si est� a false significa que estar� libre) 
	 * 
	 */
	public Boolean getEntradaOcupada() {
		return ocupada;
	}
	
	/**
	 * M�todo abstracto getter de la butaca de la entrada
	 * si fuese una entradaNumerada
	 *
	 * @return butaca 
	 * 
	 */
	public abstract Butaca getEntradaButaca();

	/**
	 * M�todo getter de la representaci�n que almacena la entrada
	 *
	 * @return representacion 
	 * 
	 */
	public Representacion getEntradaRepresentacion() {
		return representacion;
	}
	
	/**
	 * M�todo getter de si la entrada est� habilitada o no
	 *
	 * @return habilitada (si esta a true sigifica que est� habilitada y si est� a false significa que no lo est�) 
	 * 
	 */
	public Boolean getEntradaHabilitada() {
		return habilitada;
	}
	
	/**
	 * M�todo getter de la zona sobre la que esta creada la entrada
	 *
	 * @return zona 
	 * 
	 */
	public Zona getEntradaZona() {
		return zona;
	}
	
	/**
	 * M�todo getter de la bandera de compraAbono
	 *
	 * @return boolean con el estado de la bandera 
	 * 
	 */
	public boolean isCompraAbono() {
		return compraAbono;
	}
	/***SETTERS***/
	
	/**
	 * M�todo setter del precio de la entrada
	 *
	 * @param precioEntrada		precio a establecer a la entrada 
	 * 
	 */
	public void setPrecioEntrada(double precioEntrada) {
		this.precioEntrada = precioEntrada;
	}

	/**
	 * M�todo setter de la validez de la entrada
	 *
	 * @param validezEntrada 	booleano que indica la validez que tendr� la entrada
	 * 
	 */
	public void setValidezEntrada(Boolean validezEntrada) {
		this.validezEntrada = validezEntrada;
	}
	
	/**
	 * M�todo setter de la fecha de validez de la entrada
	 *
	 * @param fechaValidezEntrada 		fecha limite de validez de la entrada  
	 * 
	 */
	public void setFechaValidezEntrada(LocalDateTime fechaValidezEntrada) {
		this.fechaValidezEntrada = fechaValidezEntrada;
	}

	/**
	 * M�todo setter del usaurio que compr� la entrada
	 *
	 * @param usuarioCompra		usuario que compra la entrada 
	 * 
	 */
	public void setEntradaUsuarioCompra(Usuario usuarioCompra) {
		this.usuarioCompra = usuarioCompra;
	}
	
	/**
	 * M�todo setter del usaurio que reserv� la entrada
	 *
	 * @param usuarioReserva 	usuario que reserva la entrada
	 * 
	 */
	public void setEntradaUsuarioReserva(Usuario usuarioReserva) {
		this.usuarioReserva = usuarioReserva;
	}

	/**
	 * M�todo setter de si la entrada est� ocupada o no
	 *
	 * @param ocupada	booleano que indica si est� o no ocupada la entrada
	 * 
	 */
	public void setEntradaOcupada(Boolean ocupada) {
		this.ocupada = ocupada;
	}

	/**
	 * M�todo setter de si la entrada est� habilitada o no
	 *
	 * @param habilitada 	booleano que indica si est� o no habilitada la entrada
	 * 
	 */
	public void setEntradaHabilitada(Boolean habilitada) {
		this.habilitada = habilitada;
	}

	/**
	 * M�todo setter de la zona sobre la que esta creada la entrada
	 *
	 * @param zona 		zona a la que pertenece la entrada
	 * 
	 */
	public void setEntradaZona(Zona zona) {
		this.zona = zona;
	}

	/**
	 * M�todo abstracto setter de la butaca de la entrada
	 * si fuese esta una entradaNumerada
	 *
	 * @param butaca 	butaca asignada a la entrada
	 * 
	 */
	public abstract void setEntradaButaca(Butaca butaca);

	/**
	 * M�todo setter de la representaci�n que almacena la entrada
	 *
	 * @param representacion 	representacion que posee la entrada
	 * 
	 */
	public void setEntradaRepresentacion(Representacion representacion) {
		this.representacion = representacion;
	}

	/**
	 * Metodo setter de la flag para la compra con abono
	 *
	 * @param compraAbono boolean con true si se compro con abono o false
	 * 
	 */
	public void setCompraAbono(boolean compraAbono) {
		this.compraAbono = compraAbono;
	}

}
