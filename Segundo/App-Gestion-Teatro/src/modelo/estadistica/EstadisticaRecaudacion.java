package modelo.estadistica;

import java.util.*;
import java.util.stream.*;

import modelo.evento.Evento;
import modelo.evento.PrecioZona;
import modelo.evento.Representacion;
import modelo.zona.Zona;

/**
 * Clase encargada de las estadísticas por recaudación de un evento
 *
 * @author Jaime Diaz, Javier Fraile e Ivan Fernandez
 */
public class EstadisticaRecaudacion extends Estadistica {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6548998610889867312L;
	
	private ArrayList<Zona> zonas;
	private HashMap <String, Double> zonasRecaudacion;
	
	/**
	 * Constructor de EstadisticasRecaudacion
	 *
	 * @param evento    Evento del cual se generarán las estadísticas por recaudación
	 * 
	 */
	public EstadisticaRecaudacion (Evento evento) {
		super(evento);
		this.zonas = new ArrayList<>();
		this.zonasRecaudacion = new HashMap<>();
		
		zonas.addAll(evento.getEventoZonas());
		for(Zona z : zonas) {
			zonasRecaudacion.put(z.getZonaNombre(), 0.0);
		}
	}
	
	/**
	 * Método encargado de actualizar las estadísticas por recaudación de un evento
	 *
	 * @param evento    evento del que se va a ser añadidas
	 * 
	 */
	public void updateEstadisticasRecaudacion(Evento evento) {
		double dinero = 0.0;
		int ocupacion = 0;
		
		for(Zona z : zonas) {
			for(Representacion r : evento.getEventoRepresentaciones()) {
				ocupacion += r.getRepresentacionEntradasVendidasEnZona(z).size(); 
			}
			
			for(PrecioZona pz : evento.getEventoPrecioZonas()) {
				if(pz.getPrecioZonaZona().getZonaNombre().equals(z.getZonaNombre())) {
					dinero = ocupacion * pz.getPrecioZonaPrecio(); 
				}
			}
			
			zonasRecaudacion.put(z.getZonaNombre(), dinero);
			ocupacion = 0;
			dinero = 0.0;
		}
	}
	
	/**
	 * Método encargado de añadir una representación a las estadisticas de un evento por recaudación
	 *
	 * @param reprepresentacion    reprepresentacion a añadir
	 * 
	 */
	public void addReperesetacionRecaudacion(Representacion reprepresentacion) {
		double dinero = 0.0;
		int ocupacion = 0; 
		
		for(Zona z : zonas) {
			dinero = zonasRecaudacion.get(z.getZonaNombre());
			ocupacion = reprepresentacion.getRepresentacionEntradasVendidasEnZona(z).size();
			
			for(PrecioZona pz : reprepresentacion.getRepresentacionEvento().getEventoPrecioZonas()) {
				if(pz.getPrecioZonaZona().getZonaNombre().equals(z.getZonaNombre())) {
					dinero += ocupacion * pz.getPrecioZonaPrecio(); 
				}
			}
			
			zonasRecaudacion.put(z.getZonaNombre(), dinero);
		}
	}
	
	/**
	 * Método encargado de eliminar una representación de las estadisticas por recaudación de un evento
	 * en caso de que alguna representación sea cancelada
	 *
	 * @param reprepresentacion    reprepresentacion a eliminar 
	 * 
	 */
	public void removeReperesetacionRecaudacion(Representacion reprepresentacion) {
		double dinero = 0.0;
		int ocupacion = 0; 
		
		for(Zona z : zonas) {
			dinero = zonasRecaudacion.get(z.getZonaNombre());
			ocupacion = reprepresentacion.getRepresentacionEntradasVendidasEnZona(z).size();
			
			for(PrecioZona pz : reprepresentacion.getRepresentacionEvento().getEventoPrecioZonas()) {
				if(pz.getPrecioZonaZona().getZonaNombre().equals(z.getZonaNombre())) {
					dinero -= ocupacion * pz.getPrecioZonaPrecio(); 
				}
			}
			
			zonasRecaudacion.put(z.getZonaNombre(), dinero);
		}
	}
	
	/*** GETTER ***/
	
	/**
	 * Método getter del hashmap con las zonas y la recaudacion ya ordenado 
	 *
	 * @return hashMap con clave (id de la zona), valor (recaudacion)
	 * 
	 */
	public HashMap <String, Double> getEstadisticasRecaudacion() { 		
		return zonasRecaudacion.entrySet().stream()
                						.sorted((Map.Entry.<String, Double>comparingByValue().reversed()))
                						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
	/**
	 * Método getter de la recaudacion total del ejemplo 
	 *
	 * @return double de la recaudacion total
	 * 
	 */
	public double getEstadisticasRecaudacionTotal() {
		double recaudacion = 0.0; 
		
		for(Zona z : zonas) {
			recaudacion += zonasRecaudacion.get(z.getZonaNombre());
		}
		
		return recaudacion;
	}

}
