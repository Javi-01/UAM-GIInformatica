package modelo.abono;
import java.io.Serializable;
import java.time.LocalDate;

import es.uam.eps.padsof.telecard.FailedInternetConnectionException;
import es.uam.eps.padsof.telecard.InvalidCardNumberException;
import es.uam.eps.padsof.telecard.OrderRejectedException;
import es.uam.eps.padsof.telecard.TeleChargeAndPaySystem;
import modelo.sistema.Sistema;
import modelo.usuario.AbonoAnualFecha;
import modelo.zona.Zona;

/**
 * Clase para realizar las operaciones relacionadas con los Abonos
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class Abono implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3781099038310951280L;
	
	private double precioAbono;
	private Zona zona;
	
	/**
	 * Constructor de Abono
	 *
	 * @param precio precio del abono
	 * @param zona donde se creara el abono
	 */
	public Abono(double precio, Zona zona) {
		this.precioAbono = precio;
		this.zona = zona;
	}
		
	/**
	 * Metodo para tramitar la compra del abono
	 *
	 * @param numeroTarjeta numero de la tarjeta 
	 * @return true si se efectua la tramitacion, false en caso contrario
	 */
	@SuppressWarnings("static-access")
	protected boolean tramitarAbonoCompra(String numeroTarjeta) {
		TeleChargeAndPaySystem compra = new TeleChargeAndPaySystem();
		try {
			if (!compra.isValidCardNumber(numeroTarjeta)) {
				return false;
			}
			compra.charge(numeroTarjeta, Sistema.getInstance().getSistemaUsuarioLogeado().getUsuarioNombre(),
					this.getAbonoPrecio());
		} catch (InvalidCardNumberException e) {
			e.printStackTrace();
			return false;
		} catch (FailedInternetConnectionException e) {
			e.printStackTrace();
			return false;
		} catch (OrderRejectedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Metodo para comprar el abono, llama a tramitar
	 *
	 * @param numeroTarjeta numero de la tarjeta
	 * @return true si se efectua la compra, false en caso contrario
	 */
	public boolean CompraAbonoUsuario(String numeroTarjeta) {

		if (Sistema.getInstance().getSistemaUsuarioLogeado().getAbonoAnualFecha(this) != null) {

			if (LocalDate.now().isBefore(Sistema.getInstance().getSistemaUsuarioLogeado().getAbonoAnualFecha(this)
					.getFechaCaducidad())) { /*Si la fecha actual es anterior a la fecha de caducidad no se puede comprar */
				return false;
			} else if (LocalDate.now().isAfter(Sistema.getInstance().getSistemaUsuarioLogeado().getAbonoAnualFecha(this)
					.getFechaCaducidad())) { /* Si esta caducado, se elimina para comprar otro */
				Sistema.getInstance().getSistemaUsuarioLogeado().delUsuarioAbono(this);
			}
		}
		if (!tramitarAbonoCompra(numeroTarjeta)) {
			return false;
		}
		Sistema.getInstance().getSistemaUsuarioLogeado().addUsuarioAbonoAnualFecha(new AbonoAnualFecha(LocalDate.now(), this));
		Sistema.getInstance().getSistemaUsuarioLogeado().addUsuarioAbono(this); // Se a�ade el abono al usuario

		return true;
	}

	/**
	 * Metodo para obtener la zona del abono
	 * 
	 * @return zona Zona del abono
	 */
	public Zona getAbonoZona() {
		return zona;
	}
	/**
	 * Metodo para obtener el precio del abono
	 * 
	 * @return double precioAbono con el precio del abono
	 */
	public double getAbonoPrecio() {
		return precioAbono;
	}
	/**
	 * Metodo para establecer el precio del abono
	 * 
	 * @param precioAbono double precio del abono
	 */
	public void setAbonoPrecio(double precioAbono) {
		this.precioAbono = precioAbono;
	}
}