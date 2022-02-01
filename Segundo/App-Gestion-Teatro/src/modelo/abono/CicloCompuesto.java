package modelo.abono;

import java.util.*;

import modelo.evento.Evento;
import modelo.evento.PrecioZona;
import modelo.zona.Zona;

/**
 * Clase para realizar las operaciones relacionadas con los Ciclos Compuestos
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CicloCompuesto extends Ciclo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6157315521198983169L;

	private ArrayList<Ciclo> ciclos;
	private ArrayList<Evento> eventos;

	/**
	 * Constructor de CicloCompuesto
	 *
	 * @param porcentage  porcentage del abono
	 * @param nombreCiclo nombre del ciclo
	 * @param eventos     para los que el ciclo es valido
	 * @param zona        zona para la que el ciclo es valido
	 * @param ciclo       conjunto de ciclos que recoge el ciclo compuesto
	 */
	public CicloCompuesto(double porcentage, String nombreCiclo, ArrayList<Evento> eventos, Zona zona, ArrayList <Ciclo>ciclo) {
		super(porcentage, nombreCiclo, zona);
		ciclos = new ArrayList<Ciclo>();
		for (Ciclo c : ciclo) {

			ciclos.add(c);
		}

		this.eventos = eventos;
		this.applyPorcentage();
	}

	/**
	 * Metodo para obtener los ciclos
	 * 
	 * @return ciclos ArrayList de ciclos
	 */
	public ArrayList<Ciclo> getCicloCompuestoCiclos() {
		return ciclos;
	}

	/**
	 * Metodo para obtener los eventos del ciclo compuesto
	 *
	 * @return eventos ArrayList de eventos
	 */
	public ArrayList<Evento> getCicloCompuestoEventos() {

		ArrayList<Evento> eventos = new ArrayList<>();

		for (Evento e : this.eventos) {
			if (!eventos.contains(e)) {
				eventos.add(e);
			}
		}

		for (Ciclo c : getCicloCompuestoCiclos()) {
			if (c.getClass().equals(CicloCompuesto.class)) {
				for (Evento e : ((CicloCompuesto) c).getCicloCompuestoEventos()) {
					if (!eventos.contains(e)) {
						eventos.add(e);
					}
				}
			} else if (c.getClass().equals(CicloEvento.class)) {
				for (Evento e : ((CicloEvento) c).getCicloEventoEventos()) {
					if (!eventos.contains(e)) {
						eventos.add(e);
					}
				}
			}
		}

		return eventos;
	}

	@Override
	/**
	 * Metodo para establecer el precio de un ciclo tras aplicar el porcentage
	 * indicado ciclo
	 * 
	 */
	public void applyPorcentage() {
		for (Evento v : this.eventos) {
			for (PrecioZona p : v.getEventoPrecioZonas())
				if (p.getPrecioZonaZona().equals(this.getCicloZona())) {
					precio += p.getPrecioZonaPrecio() * (this.getPorcentage() / 100);
				}
		}
		for (Ciclo c : this.ciclos) {
			precio += c.getPrecio() * (this.getPorcentage() / 100);
		}
		this.setPrecio(precio);
	}

}