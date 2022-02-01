package modelo.evento;

/**
 * Clase Teatro para un tipo de Eventos
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class EventoTeatro extends Evento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3975524522952973258L;
	
	private String actores;

	/**
	 * Constructor de EventoTeatro
	 *
	 * @param actores     actores del evento de teatro
	 * @param duracion    duracion de la representacion
	 * @param titulo      titulo del evento
	 * @param descripcion description del evento
	 * @param autor       autor del evento
	 * @param director    director del evento
	 */
	public EventoTeatro(double duracion, String titulo, String descripcion, String autor, String director, String actores) {
		super(duracion, titulo, descripcion, autor, director);
		this.actores = actores;
	}

	/**
	 * M�todo toString que indica la informaci�n de eventoTeatro
	 * 
	 */
	@Override
	public String toString() {
		return super.toString() + "Teatro{" + "actores=" + actores + "\n}";
	}
	
	/** GETTERS **/
	
	/**
	 * M�todo getter de los atributo actores del eventoTeatro
	 * 
	 * @return actores
	 */
	public String getEventoTeatroActores() {
		return actores;
	}
	
	/**
	 * Metodo para obtener el string del tipo de evento
	 * 
	 * @return pieza del evento
	 */
	@Override
	public String getEventoTipoString() {
		return "Teatro";
	}

}

