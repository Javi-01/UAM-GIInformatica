package modelo.abono;

import java.io.Serializable;

import es.uam.eps.padsof.telecard.FailedInternetConnectionException;
import es.uam.eps.padsof.telecard.InvalidCardNumberException;
import es.uam.eps.padsof.telecard.OrderRejectedException;
import es.uam.eps.padsof.telecard.TeleChargeAndPaySystem;
import modelo.sistema.Sistema;
import modelo.zona.Zona;

/**
 * Clase para realizar las operaciones relacionadas con los Ciclos
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public abstract class Ciclo implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 8493055071404632350L;

	private Zona zona;
	private String nombreCiclo;
	private double porcentage;
	protected double precio = 0;

	/**
	 * Constructor de Cilos
	 *
	 * @param porcentage  porcentage que se aplica al ciclo
	 * @param nombreCiclo nombre que va a tener el ciclo a crear
	 * @param zona        zona para la que el ciclo es valido
	 */
	public Ciclo(double porcentage, String nombreCiclo, Zona zona) {
		this.zona = zona;
		this.nombreCiclo = nombreCiclo;
		this.porcentage = porcentage;
	}

	/**
	 * Metodo para comprar el ciclo, llama a tramitar
	 *
	 * @param numeroTarjeta numero de la tarjeta
	 * @return true si se efectua la compra, false en caso contrario
	 */
	public boolean CompraCicloUsuario(String numeroTarjeta) {
		if (!tramitarCicloCompra(numeroTarjeta)) {
			return false;
		}
		Sistema.getInstance().getSistemaUsuarioLogeado().addUsuarioCiclo(this); // Se a�ade el abono al usuario
		return true;
	}

	/**
	 * Metodo para tramitar la compra del ciclo
	 *
	 * @param numeroTarjeta numero de la tarjeta
	 * @return true si se efectua la tramitacion, false en caso contrario
	 */
	@SuppressWarnings("static-access")
	protected boolean tramitarCicloCompra(String numeroTarjeta) {
		TeleChargeAndPaySystem compra = new TeleChargeAndPaySystem();
		try {
			if (!compra.isValidCardNumber(numeroTarjeta)) {
				return false;
			}
			compra.charge(numeroTarjeta, Sistema.getInstance().getSistemaUsuarioLogeado().getUsuarioNombre(),
					this.precio);
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
	 * Metodo abstracto para obtener el precio de un ciclo tras aplicar el
	 * porcentage indicado ciclo
	 * 
	 */
	public abstract void applyPorcentage();

	/**
	 * Metodo para obtener la zona del ciclo
	 * 
	 * @return Zona en la que el ciclo es valido
	 */
	public Zona getCicloZona() {
		return zona;
	}

	/**
	 * Metodo para obtener el nombre del ciclo
	 * 
	 * @return nombreCiclo String con el nombre
	 */
	public String getCicloNombre() {
		return nombreCiclo;
	}

	/**
	 * Metodo para establecer el precio de un ciclo
	 * 
	 * @param precio double con el precio del ciclo
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}

	/**
	 * Metodo para obtener el precio del ciclo
	 * 
	 * @return precio double con el precio
	 */
	public double getPrecio() {
		return precio;
	}

	/**
	 * Metodo para obtener el porcentage de reduccion de precio del ciclo
	 * 
	 * @return porcentage double con el porcentage
	 */
	public double getPorcentage() {
		return porcentage;
	}

}