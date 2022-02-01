package modelo.estadistica;

import java.util.*;
import java.util.stream.*;

import modelo.evento.Evento;
import modelo.evento.Representacion;
import modelo.zona.Zona;

/**
 * Clase encargada de las estad�sticas por ocupaci�n de un evento
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
	 * @param evento    Evento del cual se generar�n las estad�sticas por ocupaci�n
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
	 * M�todo encargado de actualizar las estad�sticas por ocupaci�n de un evento
	 *
	 * @param evento    evento del que se va a ser a�adidas
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
	 * M�todo encargado de a�adir una representaci�n a las estadisticas de un evento por ocupaci�n
	 *
	 * @param reprepresentacion    reprepresentacion a a�adir
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
	 * M�todo encargado de eliminar una representaci�n de las estadisticas por ocupaci�n de un evento
	 * en caso de que alguna representaci�n sea cancelada
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
	 * M�todo getter del hashmap con las zonas y la ocupacion ya ordenado 
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
	 * M�todo getter de la ocupacion total del ejemplo 
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
