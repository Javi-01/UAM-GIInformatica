package modelo.evento;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import modelo.entrada.*;
import modelo.estadistica.*;
import modelo.sistema.Sistema;
import modelo.zona.*;

/**
 * Clase para realizar las operaciones relacionadas con los Eventos
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public abstract class Evento implements Serializable{

	private static final long serialVersionUID = 1518671254300708799L;
	
	private double duracion;
	private String titulo;
	private String descripcion;
	private String autor;
	private String director;
	private EstadisticaOcupacion estadisticasOcupacion;
	private EstadisticaRecaudacion estadisticasRecaudacion;
	
	private ArrayList<Representacion> representacion = new ArrayList<>();
	private ArrayList<PrecioZona> precioZonas = new ArrayList<>();
	private ArrayList<Zona> zonas;

	/**
	 * Constructor de Evento
	 *
	 * @param duracion    duracion de la representacion
	 * @param titulo      titulo del evento
	 * @param descripcion description del evento
	 * @param autor       autor del evento
	 * @param director    director del evento
	 */
	public Evento(double duracion, String titulo, String descripcion, String autor, String director) {
		this.duracion = duracion;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.autor = autor;
		this.director = director;
	}

	/**
	 * Método para añadir una represenatcion a un evento
	 * 
	 * @param repre representacion a añadir
	 * @return true si se ha podido añadir y false en caso contrario
	 */
	public Boolean addEventoRepresentacion(Representacion repre) {

		if(!Sistema.getInstance().getSistemaAdministradorFlag()) {
			return false;
		}
		
		/* El sistema comprueba si la representacion se solapa con otra y en caso de ser asi devuelve true y se sale de este método */
		if(Sistema.getInstance().sistemaSolapeEntreRepresentaciones(repre)) {
			return false;
		}
		
		this.representacion.add(repre); // En cunalquier otro caso si se podra añadir
		this.estadisticasOcupacion.addReperesetacionOcupacion(repre);
		this.estadisticasRecaudacion.addReperesetacionRecaudacion(repre);

		return true;
	}

	/**
	 * Método para cancelar una representacion de un Evento
	 * 
	 * @param repre representacion a posponer
	 * @return true si se ha podido cancelar y false en caso contrario
	 */
	public Boolean cancelarEventoRepresentacion(Representacion repre) {

		if(!Sistema.getInstance().getSistemaAdministradorFlag()) {
			return false;
		}
		
		for (int i = 0; i < Sistema.getInstance().getSistemaEventos().size(); i++) {
			for (int j = 0; j < Sistema.getInstance().getSistemaEventos().get(i).getEventoRepresentaciones().size(); j++) {
				if (Sistema.getInstance().getSistemaEventos().get(i).getEventoRepresentaciones().get(j).equals(repre)) {
					this.representacion.remove(repre);
					this.estadisticasOcupacion.removeReperesetacionOcupacion(repre);
					this.estadisticasRecaudacion.removeReperesetacionRecaudacion(repre);
					repre.represetacionNotificarCancelacion();
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Método para posponer una representacion de un Evento
	 * 
	 * @param repre representacion a posponer
	 * @param newDate nueva fecha para la representacion
	 * @return true si se ha podido posponer a dicha fecha, y false en caso de que
	 *         ya hubiera una representacion en dicha fecha
	 */
	public Boolean posponerEventoRepresentacion(Representacion repre, LocalDateTime newDate) {

		if(!Sistema.getInstance().getSistemaAdministradorFlag()) {
			return false;
		}
		
		if (!Sistema.getInstance().getSistemaEventos().contains(repre.getRepresentacionEvento())) { 
			return false;
		}

		for (int i = 0; i < Sistema.getInstance().getSistemaEventos().size(); i++) { 
			for (int j = 0; j < Sistema.getInstance().getSistemaEventos().get(i).getEventoRepresentaciones().size(); j++) {
				if (Sistema.getInstance().getSistemaEventos().get(i).getEventoRepresentaciones().get(j)
						.getRepresentacionFecha().equals(newDate)) {
																
					return false;
				} else if (Sistema.getInstance().getSistemaEventos().get(i).getEventoRepresentaciones().get(j)
						.getRepresentacionFecha()
											
						.plusMinutes((long) Sistema.getInstance().getSistemaEventos().get(i).getEventoDuracion())
						.isAfter(newDate)
						&& (newDate.plusMinutes((long) this.duracion) 
																	
								.isAfter(Sistema.getInstance().getSistemaEventos().get(i).getEventoRepresentaciones()
										.get(j).getRepresentacionFecha()))) {
					return false;
				}
			}
		}
		this.representacion.remove(repre); 
		repre.setRepresentacionFecha(newDate);
		repre.represetacionNotificarPospuesta();
		this.representacion.add(repre);
		this.estadisticasOcupacion.addReperesetacionOcupacion(repre);
		this.estadisticasRecaudacion.addReperesetacionRecaudacion(repre);
		return true;
	}
	
	/**
	 * Metodo encargado de reducir el aforo de un evento, recorriendo todas las representaciones de dicho evento
	 * y reduciendo el porcentaje de aforo indicado en cada zona del teatro
	 * 
	 * @param porcentaje porcentaje de aforo a reducir de las zonas que tiene el
	 *                   teatro
	 */
	public void reducirEventoAforo(int porcentaje) {

		if(Sistema.getInstance().getSistemaAdministradorFlag() != true) {
			return;
		}
		
		int entradasZona = 0, entradasAlReducir =  0, entradasDisponiblesAlReducir = 0;
		int entradasOcupadasZona = 0, entradasAEliminar = 0, entradasDisponiblesZona = 0, i, j;
		for (Representacion r : getEventoRepresentaciones()) {
			for (Zona z : getEventoZonas()) {
				i = j = 0;
				if (!z.getClass().equals(ZonaCompuesta.class)) {
					entradasZona = r.getRepresentacionEntradasEnZona(z).size();
					entradasAlReducir = entradasZona - ((entradasZona * porcentaje)/100);
					entradasOcupadasZona = r.getRepresentacionEntradasOcupadasEnZona(z).size();
					entradasDisponiblesZona = r.getRepresentacionEntradasDisponiblesEnZona(z).size();
					
						if (z.getClass().equals(ZonaPie.class)) {
							i = entradasAEliminar = entradasOcupadasZona - entradasAlReducir;
								if (entradasAEliminar >= 0) { 
									while (i > 0) {
										if (r.getRepresentacionEntradasReservadasEnZona(z).size() > 0) {
											r.getRepresentacionEntradasReservadasEnZona(z).get(
													r.getRepresentacionEntradasReservadasEnZona(z).size() - 1).setEntradaHabilitada(false);
											
										}else if (r.getRepresentacionEntradasVendidasEnZona(z).size() > 0) {
											r.eliminarRepresentacionEntrada(r.getRepresentacionEntradasVendidasEnZona(z).get(
													r.getRepresentacionEntradasVendidasEnZona(z).size() - 1));
										}
										i--;
									}
									for (Entrada e : r.getRepresentacionEntradasDisponiblesEnZona(z)) { 
										e.setEntradaHabilitada(false);
									}
									r.setRepresentacionAforoDisponible(r.getRepresentacionAforoDisponible() - entradasDisponiblesZona);
									r.setRepresentacionAforoOcupado(r.getRepresentacionAforoOcupado() - entradasAEliminar);
									
								}else if (entradasAEliminar < 0){
									entradasDisponiblesAlReducir =entradasAlReducir - entradasOcupadasZona;
									entradasDisponiblesAlReducir = i = entradasDisponiblesZona - entradasDisponiblesAlReducir;
									
									for (Entrada e : r.getRepresentacionEntradasDisponiblesEnZona(z)) {
										if (i > 0) {
											e.setEntradaHabilitada(false);
											i--;
										}
									}
									r.setRepresentacionAforoDisponible(r.getRepresentacionAforoDisponible() - entradasDisponiblesAlReducir);
								}
								r.setRepresentacionAforoTotal(r.getRepresentacionAforoTotal() - (entradasZona - entradasAlReducir));
						}else if (z.getClass().equals(ZonaSentado.class)) {
							j = entradasAEliminar = entradasOcupadasZona - entradasAlReducir -  r.getRepresentacionEntradasDeshabilitadasEnZona(z).size();
							if (entradasAEliminar >= 0) {
								int mayorCapacidad,  aforoFila;
								while (j > 0) { 
									if (r.getRepresentacionEntradasReservadasEnZona(z).size() > 0) {
										int contFila = 0;
										mayorCapacidad = 0;
										for (i = 0; i < ((ZonaSentado)z).getFilasMax(); i++) {		
											aforoFila = r.getRepresentacionEntradasReservadasFila(z, i).size();
											if (aforoFila > mayorCapacidad) {
												mayorCapacidad = aforoFila;
												contFila = i;
											}
										}
										r.getRepresentacionEntradasReservadasFila(z, contFila).get(
												r.getRepresentacionEntradasReservadasFila(z, contFila).size() - 1).setEntradaHabilitada(false);
										
									}else if (r.getRepresentacionEntradasVendidasEnZona(z).size() > 0) {
										int contFila = 0;
										mayorCapacidad = 0;
										for (i = 0; i < ((ZonaSentado)z).getFilasMax(); i++) {		
											aforoFila = r.getRepresentacionEntradasVendidasFila(z, i).size();
											if (aforoFila > mayorCapacidad) {
												mayorCapacidad = aforoFila;
												contFila = i;
											}
										}
										r.eliminarRepresentacionEntrada(r.getRepresentacionEntradasVendidasFila(z, contFila).get(
												r.getRepresentacionEntradasVendidasFila(z, contFila).size() - 1));
									}
									j--;
								}
								for (Entrada e : r.getRepresentacionEntradasDisponiblesEnZona(z)) { 
									e.setEntradaHabilitada(false);
								}
							
								r.setRepresentacionAforoDisponible(r.getRepresentacionAforoDisponible() - entradasDisponiblesZona);
								r.setRepresentacionAforoOcupado(r.getRepresentacionAforoOcupado() - entradasAEliminar);
								
							}else if (entradasAEliminar < 0){
								entradasDisponiblesAlReducir =entradasAlReducir - entradasOcupadasZona;
								entradasDisponiblesAlReducir = i = entradasDisponiblesZona - entradasDisponiblesAlReducir;
								
								for (Entrada e : r.getRepresentacionEntradasDisponiblesEnZona(z)) {
									if (i > 0) {
										e.setEntradaHabilitada(false);
										i--;
									}
								}
								r.setRepresentacionAforoDisponible(r.getRepresentacionAforoDisponible() - entradasDisponiblesAlReducir);
						}
							r.setRepresentacionAforoTotal(r.getRepresentacionAforoTotal() - (entradasZona - entradasAlReducir));
					}
				}	
			}
		}
	}
	
	/**
	 * Metodo para actualizar las estadisiticas del evento
	 **/
	public void updateEventoEstadisticas() {
		this.estadisticasOcupacion.updateEstadisticasOcupacion(this);
		this.estadisticasRecaudacion.updateEstadisticasRecaudacion(this);
	}
	
	/**
	 * Metodo que se encarga de añadir un precio para una zona de un evento
	 * 
	 * @param precioZona del evento
	 */
	public void addEventoPrecioZona(PrecioZona precioZona) {
		
		if(!Sistema.getInstance().getSistemaAdministradorFlag()) {
			return;
		}
		
		this.precioZonas.add(precioZona);
	}
	
	
	/** GETTERS **/
		
	/**
	 * Metodo para obtener la duracion del evento
	 * 
	 * @return titulo del evento
	 */
	public double getEventoDuracion() {
		return duracion;
	}

	/**
	 * Metodo para obtener el titulo del evento
	 * 
	 * @return titulo del evento
	 */
	public String getEventoTitulo() {
		return titulo;
	}
	
	/**
	 * Metodo para obtener la descripcion del evento
	 * 
	 * @return descripcion del evento
	 */
	public String getEventoDescripcion() {
		return descripcion;
	}
	
	/**
	 * Metodo para obtener el autor del evento
	 * 
	 * @return autor del evento
	 */
	public String getEventoAutor() {
		return autor;
	}
	
	/**
	 * Metodo para obtener el director del evento
	 * 
	 * @return director del evento
	 */
	public String getEventoDirector() {
		return director;
	}
	
	/**
	 * Metodo para obtener todas las representaciones de un evento
	 * 
	 * @return ArrayList de representaciones
	 */
	public ArrayList<Representacion> getEventoRepresentaciones() {
		return this.representacion;
	}
	
	/**
	 * Metodo para obtener todas las zonas de un evento
	 * 
	 * @return ArrayList de zonas 
	 */
	public ArrayList<Zona> getEventoZonas() {
		return this.zonas;
	}

	/**
	 * Metodo para obtener las estadisticas por ocupacion del evento, 
	 * solo accesible si el admin esta registrado
	 * 
	 * @return hashMap con clave (id de la zona), valor (ocupacion)
	 */
	public HashMap <String, Integer> getEventoEstadisticaOcupacion() {
		
		if(!Sistema.getInstance().getSistemaAdministradorFlag()) {
			return null;
		}
		
		return this.estadisticasOcupacion.getEstadisticasOcupacion();
	}
	
	/**
	 * Metodo para obtener las estadisticas totales por ocupacion del evento, 
	 * solo accesible si el admin esta registrado
	 * 
	 * @return int con la ocupacion total del evento
	 */
	public int getEventoEstadisticaOcupacionTotal() {
		
		if(!Sistema.getInstance().getSistemaAdministradorFlag()) {
			return 0;
		}
		
		return this.estadisticasOcupacion.getEstadisticasOcupacionTotal();
	}

	/**
	 * Metodo para obtener las estadisticas por recaudacion del evento, 
	 * solo accesible si el admin esta registrado 
	 * 
	 * @return hashMap con clave (id de la zona), valor (Recaudacion)
	 */
	public HashMap <String, Double> getEventoEstadisticaRecaudacion() {
		
		if(!Sistema.getInstance().getSistemaAdministradorFlag()) {
			return null;
		}
		
		return this.estadisticasRecaudacion.getEstadisticasRecaudacion();
	}
	
	/**
	 * Metodo para obtener las estadisticas totales por recaudacion del evento, 
	 * solo accesible si el admin esta registrado
	 * 
	 * @return double con la recaudacion total del evento
	 */
	public double getEventoEstadisticaRecaudacionTotal() {
		
		if(!Sistema.getInstance().getSistemaAdministradorFlag()) {
			return 0.0;
		}
		
		return this.estadisticasRecaudacion.getEstadisticasRecaudacionTotal();
	}
	
	/**
	 * Metodo para obtener los precios de las zonas de un evento
	 * 
	 * @return ArrayList de precioZonas
	 */
	public ArrayList<PrecioZona> getEventoPrecioZonas() {
		return precioZonas;
	}
	
	/**
	 * Metodo para obtener el string del tipo de evento
	 * 
	 * @return String del evento
	 */
	public abstract String getEventoTipoString();
	
	/** SETTERS **/

	/**
	 * Método para establecer las zonas de un Evento
	 * 
	 * @param zonas zonas del evento
	 */
	public void setEventoZonas(ArrayList<Zona> zonas) {
		this.zonas = zonas;
		
		this.estadisticasOcupacion = new EstadisticaOcupacion(this);
		this.estadisticasRecaudacion = new EstadisticaRecaudacion(this);
	}

	/**
	 * Método para establecer el precio de las zonas de un Evento
	 * 
	 * @param precioZonas precioZonas del evento
	 */
	public void setEventoPrecioZonas(ArrayList<PrecioZona> precioZonas) {
		this.precioZonas = precioZonas;
	}
	
}
