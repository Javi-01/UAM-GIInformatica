package modelo.estadistica;

import java.io.Serializable;
import java.util.*;

import modelo.evento.*;

/**
 * Clase encargada de las estad�sticas de los eventos
 *
 * @author Jaime Diaz, Javier Fraile e Ivan Fernandez
 */
public abstract class Estadistica implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4416368474446702140L;
	
	private ArrayList <Evento> eventos = new ArrayList<>(); 
	
	/**
	 * Constructor de Estadisticas
	 *
	 * @param evento    Evento del cual se generar�n las estad�sticas
	 * 
	 */
	public Estadistica(Evento evento) {
		if(!this.getEventoConEstadisticas().contains(evento)) {
			this.eventos.add(evento);
		}
	}

	/**
	 * M�todo getter de los eventos que tienes representaciones generadas
	 *
	 * @return eventos    ArrayList de tipo Evento con todos los eventos que tienen estad�sticas generadas
	 * 
	 */
	public ArrayList <Evento> getEventoConEstadisticas() {
		return eventos;
	}
}
