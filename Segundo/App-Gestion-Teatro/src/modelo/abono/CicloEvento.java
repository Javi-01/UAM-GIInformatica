package modelo.abono;

import java.util.ArrayList;

import modelo.evento.*;
import modelo.zona.Zona;

/**
 * Clase para realizar las operaciones relacionadas con los Ciclos para eventos
 * simples
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CicloEvento extends Ciclo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1932315300306477266L;

	private ArrayList<Evento> eventos;

	/**
	 * Constructor de CicloEvento
	 *
	 * @param porcentage porcentage del abono
	 * @param nombre     nombre del ciclo evento
	 * @param eventos    para los que el ciclo es valido
	 * @param zona       zona para la que el ciclo es valido
	 */
	public CicloEvento(double porcentage, String nombre, ArrayList<Evento> eventos, Zona zona) {
		super(porcentage, nombre, zona);
		this.eventos = eventos;
		this.applyPorcentage();
	}

	/**
	 * Metodo para obtener los eventos
	 * 
	 * @return eventos ArrayList de eventos
	 */
	public ArrayList<Evento> getCicloEventoEventos() {
		return eventos;
	}

	/**
	 * Metodo para establecer el precio de un ciclo tras aplicar el porcentage
	 * indicado ciclo
	 * 
	 */
	@Override
	public void applyPorcentage() {
		double precio = 0;
		for (Evento v : eventos) {
			for (PrecioZona p : v.getEventoPrecioZonas())
				if (p.getPrecioZonaZona().equals(this.getCicloZona())) {
					precio += p.getPrecioZonaPrecio() * (this.getPorcentage() / 100);
				}
		}
		this.setPrecio(precio);
	}

}