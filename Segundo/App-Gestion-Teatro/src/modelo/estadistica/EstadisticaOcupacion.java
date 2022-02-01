package modelo.estadistica;

import java.util.*;
import java.util.stream.*;

import modelo.evento.Evento;
import modelo.evento.Representacion;
import modelo.zona.Zona;

/**
 * Clase encargada de las estadísticas por ocupación de un evento
 *
 * @author Jaime Diaz, Javier Fraile e Ivan Fernandez
 */
public class EstadisticaOcupacion extends Estadistica {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6587095758795031756L;
	
	private ArrayList<Zona> zonas;
	private HashMap <String, Integer> zonasOcupacion;
	
	/**
	 * Constructor de EstadisticasOcupacion
	 *
	 * @param evento    Evento del cual se generarán las estadísticas por ocupación
	 * 
	 */
	public EstadisticaOcupacion(Evento evento) {
		super(evento);
		this.zonas = new ArrayList<>();
		this.zonasOcupacion = new HashMap<>();
		
		zonas.addAll(evento.getEventoZonas());
		for(Zona z : zonas) {
			zonasOcupacion.put(z.getZonaNombre(), 0);
		}
		
	}
	
	/**
	 * Método encargado de actualizar las estadísticas por ocupación de un evento
	 *
	 * @param evento    evento del que se va a ser añadidas
	 * 
	 */
	public void updateEstadisticasOcupacion(Evento evento) {
		int ocupacion = 0;
		
		for(Zona z : zonas) {
			for(Representacion r : evento.getEventoRepresentaciones()) {
				ocupacion += r.getRepresentacionEntradasVendidasEnZona(z).size();
			}
			zonasOcupacion.put(z.getZonaNombre(), ocupacion);
			ocupacion = 0;
		}
		
	}
	
	/**
	 * Método encargado de añadir una representación a las estadisticas de un evento por ocupación
	 *
	 * @param reprepresentacion    reprepresentacion a añadir
	 * 
	 */
	public void addReperesetacionOcupacion(Representacion reprepresentacion) {
		int ocupacion = 0; 
		
		for(Zona z : zonas) {
			ocupacion = zonasOcupacion.get(z.getZonaNombre());
			ocupacion += reprepresentacion.getRepresentacionEntradasVendidasEnZona(z).size();
			zonasOcupacion.put(z.getZonaNombre(), ocupacion);
		}
		
	}
	
	/**
	 * Método encargado de eliminar una representación de las estadisticas por ocupación de un evento
	 * en caso de que alguna representación sea cancelada
	 *
	 * @param reprepresentacion    reprepresentacion a eliminar 
	 * 
	 */
	public void removeReperesetacionOcupacion(Representacion reprepresentacion) {
		int ocupacion = 0; 
		
		for(Zona z : zonas) {
			ocupacion = zonasOcupacion.get(z.getZonaNombre());
			ocupacion -= reprepresentacion.getRepresentacionEntradasVendidasEnZona(z).size();
			zonasOcupacion.put(z.getZonaNombre(), ocupacion);
		}
	} 
	
	/*** GETTER ***/
	
	/**
	 * Método getter del hashmap con las zonas y la ocupacion ya ordenado 
	 *
	 * @return hashMap con clave (id de la zona), valor (ocupacion)
	 * 
	 */
	public HashMap <String, Integer> getEstadisticasOcupacion() { 		
		return zonasOcupacion.entrySet().stream()
                						.sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
	/**
	 * Método getter de la ocupacion total del ejemplo 
	 *
	 * @return int de la ocupacion total
	 * 
	 */
	public int getEstadisticasOcupacionTotal() {
		int ocupacion = 0; 
		
		for(Zona z : zonas) {
			ocupacion += zonasOcupacion.get(z.getZonaNombre());
		}
		
		return ocupacion;
	}
    
}
