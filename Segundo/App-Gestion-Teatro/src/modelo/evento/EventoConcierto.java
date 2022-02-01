package modelo.evento;

/**
 * Clase Concierto para un tipo de Eventos
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class EventoConcierto extends Evento {

	private static final long serialVersionUID = 2835877537787572642L;
	
	private String orquesta;
	private String solista;
	private String pieza;

	/**
	 * Constructor de evento concierto
	 *
	 * @param orquesta    orquesta del Concierto
	 * @param solista     solista del Concierto
	 * @param pieza      pieza del Concierto
	 * @param duracion    duracion de la representacion
	 * @param titulo      titulo del evento
	 * @param descripcion description del evento
	 * @param autor       autor del evento
	 * @param director    director del evento
	 */
	public EventoConcierto(double duracion, String titulo, String descripcion,
			String autor, String director, String orquesta, String solista, String pieza) {
		super(duracion, titulo, descripcion, autor, director);
		this.orquesta = orquesta;
		this.solista = solista;
		this.pieza = pieza;
	}


	/** GETTERS **/

	/**
	 * Metodo para obtener la orquesta del evento de tipo concierto
	 * 
	 * @return orquesta del evento
	 */
	public String getEventoConciertoOrquesta() {
		return orquesta;
	}

	/**
	 * Metodo para obtener el solista del evento de tipo concierto
	 * 
	 * @return solista del evento
	 */
	public String getEventoConciertoSolista() {
		return this.solista;
	}
	
	/**
	 * Metodo para obtener la pieza del evento de tipo concierto
	 * 
	 * @return pieza del evento
	 */
	public String getEventoConciertoPieza() {
		return pieza;
	}

	/**
	 * Metodo para obtener el string del tipo de evento
	 * 
	 * @return pieza del evento
	 */
	@Override
	public String getEventoTipoString() {
		return "Concierto";
	}
}