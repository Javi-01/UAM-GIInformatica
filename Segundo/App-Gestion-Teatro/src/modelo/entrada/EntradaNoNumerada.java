package modelo.entrada;

import java.time.*;
import es.uam.eps.padsof.tickets.*;
import modelo.evento.*;
import modelo.sistema.Sistema;
import modelo.zona.*;
import es.uam.eps.padsof.telecard.*;

/**
 * Clase para realizar las operaciones relacionadas con las entradas no numeradas
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class EntradaNoNumerada extends Entrada {

	/**
	 * 
	 */
	private static final long serialVersionUID = -110923708320520396L;
	
	/**
	 * Constructor de EntradaNoNumerada
	 *
	 * @param codigo 	 codigo de la entrada
	 * @param representacion  	representacion que almacenar� la entrada
	 * @param zona 		zona a partir de la cual se genera la entrada
	 * @param precio  	precio de la entrada segun la zona
	 * @param fechaValidezEntrada 		fecha limite de validez de la entrada
	 */
	public EntradaNoNumerada(int codigo, Representacion representacion, Zona zona, double precio,
			LocalDateTime fechaValidezEntrada) {
		super(codigo, representacion, zona, precio, fechaValidezEntrada);
	}

	/**
	 * Metodo abstracto encargado de comprar entradas con una tarjeta de cr�dito
	 *
	 * @param numeroTarjeta 	numero de la tarjeta con la que se comprar� la entrada
	 * 
	 * @return true si se ha comprado correctamente la entrada o false en caso contrario 
	 */
	@Override
	public boolean comprarEntradaTarjeta(String numeroTarjeta) throws NonExistentFileException, UnsupportedImageTypeException {
		
		/* Comprobar que el n�mero de la tarjeta es v�lido y con el se puede tramitar la compra */
		if (!this.tramitarEntradaCompra(numeroTarjeta)) {
			return false;
		}

		this.setEntradaOcupada(true);
		this.setValidezEntrada(true);
		this.getEntradaRepresentacion()
				.setRepresentacionAforoOcupado(this.getEntradaRepresentacion().getRepresentacionAforoOcupado() + 1);
		this.getEntradaRepresentacion().setRepresentacionAforoDisponible(
				this.getEntradaRepresentacion().getRepresentacionAforoDisponible() - 1);
		this.getEntradaRepresentacion().getRepresentacionEvento().updateEventoEstadisticas();
		Sistema.getInstance().getSistemaUsuarioLogeado().addUsuarioEntradasCompradas(this);
		this.setEntradaUsuarioCompra(Sistema.getInstance().getSistemaUsuarioLogeado());
		
		/* Generamos el pdf de la entrada */
		this.tramitarEntradaPDF();
		
		return true;
	}

	/**
	 * Metodo encargado de comprar entradas con un abono
	 * comprobando si el usuario posee un abono anual en dicha zana 
	 * o un abono de cilos que contine el evento indicado en la entrada 
	 * 
	 * @return true si se ha comprado correctamente la entrada o false en caso contrario 
	 */
	@Override
	public boolean comprarEntradaAbono() {
		if (Sistema.getInstance().getSistemaUsuarioLogeado().compraUsuarioAbonoCiclo(this)) {
			this.setEntradaOcupada(true);
			this.setValidezEntrada(true);
			this.getEntradaRepresentacion().setRepresentacionAforoOcupado(
					this.getEntradaRepresentacion().getRepresentacionAforoOcupado() + 1);
			this.getEntradaRepresentacion().setRepresentacionAforoDisponible(
					this.getEntradaRepresentacion().getRepresentacionAforoDisponible() - 1);
			this.getEntradaRepresentacion().getRepresentacionEvento().updateEventoEstadisticas();
			Sistema.getInstance().getSistemaUsuarioLogeado().addUsuarioEntradasCompradas(this);
			this.setEntradaUsuarioCompra(Sistema.getInstance().getSistemaUsuarioLogeado());
			this.setCompraAbono(true);
			
			return true;
		}
		return false;
	}


	/**
	 * Metodo encargado de generar el pdf 
	 * de la entrada comprada 
	 * 
	 */
	@Override
	protected void tramitarEntradaPDF() {
		
		try {
			TicketSystem.createTicket(this, "./entradas//EntradaNoNumerada");
		} catch (NonExistentFileException e) {
			System.out.println("NonExistentFileException");
			e.printStackTrace();
		} catch (UnsupportedImageTypeException e) {
			System.out.println("UnsupportedImageTypeException");
			e.printStackTrace(); 
		}
		;
	}
	
	/**
	 * Metodo encargado de validar la compra 
	 * comprueba que el numero de la tarjeta sea correcto 
	 * 
	 * @return true si se ha podido tramitar la compra o false en caso contrario 
	 */
	@Override
	protected boolean tramitarEntradaCompra(String numeroTarjeta) {
		
		try {
			if (!TeleChargeAndPaySystem.isValidCardNumber(numeroTarjeta)) {
				return false;
			}
			TeleChargeAndPaySystem.charge(numeroTarjeta, Sistema.getInstance().getSistemaUsuarioLogeado().getUsuarioNombre(),
					this.getPrecioEntrada());
		} catch (InvalidCardNumberException e) {
			e.printStackTrace();
		} catch (FailedInternetConnectionException e) {
			e.printStackTrace();
		} catch (OrderRejectedException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	/**
	 * Metodo encargado de reservar la entrada
	 * 
	 * @return true si se ha podido reservar correctamente la entrada o false en caso contrario 
	 */
	@Override
	public boolean reservarEntrada() {

		this.setEntradaOcupada(true);
		this.setValidezEntrada(false);
		this.setFechaValidezEntrada(this.getEntradaRepresentacion().getRepresentacionFecha().minusHours(1));
		this.getEntradaRepresentacion()
				.setRepresentacionAforoOcupado(this.getEntradaRepresentacion().getRepresentacionAforoOcupado() + 1);
		this.getEntradaRepresentacion().setRepresentacionAforoDisponible(
				this.getEntradaRepresentacion().getRepresentacionAforoDisponible() - 1);
		this.getEntradaRepresentacion().getRepresentacionEvento().updateEventoEstadisticas();
		Sistema.getInstance().getSistemaUsuarioLogeado().addUsuarioEntradasReservadas(this);
		this.setEntradaUsuarioReserva(Sistema.getInstance().getSistemaUsuarioLogeado());

		return true;
	}

	/**
	 * Metodo encargado de cancelar la reserva de una entrada
	 * 
	 * @return true si se ha podido cancelar la entrada o false en caso contrario 
	 */
	@Override
	public boolean cancelarEntradaReservada() {

		this.setEntradaOcupada(false);
		this.setValidezEntrada(true);
		this.getEntradaRepresentacion()
				.setRepresentacionAforoOcupado(this.getEntradaRepresentacion().getRepresentacionAforoOcupado() - 1);
		this.getEntradaRepresentacion().setRepresentacionAforoDisponible(
				this.getEntradaRepresentacion().getRepresentacionAforoDisponible() + 1);
		this.getEntradaRepresentacion().getRepresentacionEvento().updateEventoEstadisticas();
		this.getEntradaRepresentacion().getRepresentacionListaDeEsperaEnZona(this.getEntradaZona()).avisarEntradasDisponibles(); /* La lista de espera avisa a los usuarios que hay entradas disponibles */
		this.getEntradaUsuarioReserva().delUsuarioEntradasReservadas(this);
		this.setEntradaUsuarioReserva(null);

		return false;
	}
	

	/** GETTERS **/
	
	/**
	 * M�todo getter de la butaca de la entrada
	 *
	 * @return operacion no soportada al tratarse de una entrada no numerada que no tiene butaca 
	 * 
	 */
	@Override
	public Butaca getEntradaButaca() {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	/**
	 * M�todo getter del id de la etnrada
	 *
	 * @return codigo de validacion 
	 * 
	 */
	@Override
	public int getIdTicket() {
		return this.getCodigoValidacion();
	}
	
	/**
	 * M�todo getter del nombre del teatro propietario del evento que tiene la representacion de la entrada
	 *
	 * @return Cadena con el nombre del teatro 
	 * 
	 */
	public String getTheaterName(){
		return "Theater Administration";
	}
	
	/**
	 * M�todo getter de la fecha de la representacion
	 *
	 * @return string con informaci�n acerca de la fecha 
	 * 
	 */
	public String getEventDate(){
		return  this.getFechaValidezEntrada().getDayOfMonth() + "/" + this.getFechaValidezEntrada().getMonthValue() + ""
				+ "/" + this.getFechaValidezEntrada().getYear() + ", hora " + this.getFechaValidezEntrada().getHour() + ":" + this.getFechaValidezEntrada().getMinute();
				
	}
	
	/**
	 * M�todo getter del nombre del evento del que est� basada la representacion de la entrada
	 *
	 * @return Cadena con el nombre del evento 
	 * 
	 */
	public String getEventName(){
		return this.getEntradaRepresentacion().getRepresentacionEvento().getEventoTitulo();
	}
	
	/**
	 * M�todo getter del asiento de la entrada
	 *
	 * @return Cadena con el numero del asiento de la entrada 
	 * 
	 */
	public String getSeatNumber(){
		return "Zona : " + this.getEntradaZona().getZonaIdentificador() + " de pie";
	}
	
	/**
	 * M�todo getter de la imagen que tendra el pdf 
	 *
	 * @return Cadena con el nombre de la imagen a utilizar en la entrada 
	 * 
	 */
	public String getPicture() {
		return "./sources//ImagenEntradaTeatro.jpg";
	}
	
	/** SETTERS **/
	
	/**
	 * M�todo setter de la butaca de la entrada
	 *
	 * @param butaca 	butaca
	 * 
	 */
	@Override
	public void setEntradaButaca(Butaca butaca) {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

}