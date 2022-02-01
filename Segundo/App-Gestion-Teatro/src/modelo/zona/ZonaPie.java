package modelo.zona;


/**
 * Clase ZonaPie
 *
 * @author Jaime Diaz, Javier Fraile e Ivan Fernandez
 */
public class ZonaPie extends Zona {

	/**
	 * 
	 */
	private static final long serialVersionUID = 187325081710486481L;

	/**
	 * Constructor de una zonaPie 
	 *
	 * @param identificador    Id de la zona a crear
	 * @param aforo 	n�mero total del aofor que tendr� la zona
	 * @param nombreZona	nombre que tendr� la zona
	 * 
	 */
	public ZonaPie(int identificador, int aforo, String nombreZona) {
		super(identificador, aforo, nombreZona);
	}

}
