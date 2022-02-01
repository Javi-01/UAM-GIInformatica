package modelo.estadistica;

import java.util.*;
import java.util.stream.*;

import modelo.evento.Evento;
import modelo.evento.PrecioZona;
import modelo.evento.Representacion;
import modelo.zona.Zona;

/**
 * Clase encargada de las estad�sticas por recaudaci�n de un evento
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
	 * @param evento    Evento del cual se generar�n las estad�sticas por recaudaci�n
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
	 * M�todo encargado de actualizar las estad�sticas por recaudaci�n de un evento
	 *
	 * @param evento    evento del que se va a ser a�adidas
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
	 * M�todo encargado de a�adir una representaci�n a las estadisticas de un evento por recaudaci�n
	 *
	 * @param reprepresentacion    reprepresentacion a a�adir
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
	 * M�todo encargado de eliminar una representaci�n de las estadisticas por recaudaci�n de un evento
	 * en caso de que alguna representaci�n sea cancelada
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
	 * M�todo getter del hashmap con las zonas y la recaudacion ya ordenado 
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
	 * M�todo getter de la recaudacion total del ejemplo 
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
