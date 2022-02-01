package modelo.evento;

/**
 * Clase Danza para un tipo de Eventos
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class EventoDanza extends Evento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 61542714044385959L;
	
	private String danzaOrquesta;
	private String bailarines;
	private String directorOrquesta;

	/**
	 * Constructor de Danza
	 *
	 * @param duracion         duracion de la representacion
	 * @param titulo           titulo del evento
	 * @param descripcion      description del evento
	 * @param autor            autor del evento
	 * @param director         director del evento
	 * @param danzaOrquesta    orquesta de la danza
	 * @param bailarines       bailarines de la danza
	 * @param directorOrquesta director de la orquesta
	 */
	public EventoDanza(double duracion, String titulo,
			String descripcion, String autor, String director, String danzaOrquesta, String bailarines, String directorOrquesta) {
		super(duracion, titulo, descripcion, autor, director);
		this.danzaOrquesta = danzaOrquesta;
		this.directorOrquesta = directorOrquesta;
		this.bailarines = bailarines;
	}

	/** GETTERS **/

	/**
	 * Metodo para obtener la orquesta del evento de tipo danza
	 * 
	 * @return orquesta del evento
	 */
	public String getEventoDanzaDanzaOrquesta() {
		return danzaOrquesta;
	}

	/**
	 * Metodo para obtener los bailarines del evento de tipo danza
	 * 
	 * @return bailarines del evento
	 */
	public String getEventoDanzaBailarines() {
		return this.bailarines;
	}

	/**
	 * Metodo para obtener el director de orquesta del evento de tipo danza
	 * 
	 * @return director de orquesta del evento
	 */
	public String getEventoDanzaDirectorOrquesta() {
		return this.directorOrquesta;
	}
	
	/**
	 * Metodo para obtener el string del tipo de evento
	 * 
	 * @return pieza del evento
	 */
	@Override
	public String getEventoTipoString() {
		return "Danza";
	}
}

