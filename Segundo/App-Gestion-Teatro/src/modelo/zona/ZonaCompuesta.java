package modelo.zona;

import java.util.*;

/**
 * Clase ZonaCompuesta
 *
 * @author Jaime Diaz, Javier Fraile e Ivan Fernandez
 */
public class ZonaCompuesta extends Zona{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5012232237720667788L;
	
	private ArrayList<Zona> zonas;
	
	/**
	 * Constructor de ZonaCompuesta cuando solo quiere ser creada una zona compuesta pero no se indican subzonas
	 *
	 * @param identificador    id de la zona a crear
	 * @param nombreZona	nombre que tendr� la zona
	 * 
	 */
	public ZonaCompuesta(int identificador, String nombreZona) {
		super(identificador, nombreZona);
		this.zonas = new ArrayList<>();
	}
	
	/**
	 * Constructor de ZonaCompuesta cuando se indica las zonas que debe de contener 
	 *
	 * @param identificador    id de la zona a crear
	 * @param nombreZona	nombre que tendr� la zona
	 * @param zonas    arraylist de zonas simple que contendra la zona compuesta
	 * 
	 */
	public ZonaCompuesta(int identificador, String nombreZona, ArrayList <Zona> zonas) {
		super(identificador, 0, nombreZona);
		this.zonas = new ArrayList<>();
		this.zonas.addAll(zonas);
		setZonaCompuestaAforoTotal();
	}
	
	/**
	 * M�todo que se encargar� de obtener el aforo de la zona compuesta
	 *
	 * 
	 */
	public void setZonaCompuestaAforoTotal () {
		
		ArrayList <Zona> zonas = getZonaCompuestaZonas();
		int cont = 0;
		
		for (Zona z : zonas) {
			cont += z.getZonaAforo();
		}
		
		this.setZonaAforo(cont);
	}
	
	
	/**
	 * M�todo que se encargar� de a�adir una zona a otra zona (en caso de ser una zonaCompuesta)
	 *
	 * @param zona    objeto de tipo zona a a�adir
	 * 
	 */
	public void addZonaCompuestaZona(Zona zona) {
		this.zonas.add(zona);
	}

	/**
	 * M�todo que se encargar� de eliminar una zona de otra zona (en caso de ser una zonaCompuesta)
	 *
	 * @param zona    objeto de tipo zona a a�adir
	 * 
	 */
	public void delZonaCompuestaZona(Zona zona) {
		
		if(getZonaCompuestaZonas().contains(zona)) {
			this.zonas.remove(zona);
		}
		
	}
	
	/** GETTERS **/	
	
	/**
	 * M�todo getter abstracto que devuelve un arrayList de zonas que tiene una zona 
	 * (en caso de ser una zonaCompuesta), si la zona contiene m�s zonas compuestas dentro, 
	 * la funci�n se llama al mimso m�todo y a�ade todas las zonas que contenga
	 *
	 * @return arayList de zonas
	 * 
	 */
	public ArrayList<Zona> getZonaCompuestaZonas(){
		
		ArrayList <Zona> zonas = new ArrayList<>(); 
		
		for (Zona z1 : this.zonas) {

			if(z1.getClass().equals(ZonaCompuesta.class)) {
				for (Zona z2 : ((ZonaCompuesta)z1).getZonaCompuestaZonas()) {
					if (!zonas.contains(z2)) { /* Evitamos repetir zonas */
						zonas.add(z2);
					}
				}
			}
			
			else if(!zonas.contains(z1)) { /* Evitamos repetir zonas */
				zonas.add(z1);
			}
			
		}
		
		return zonas;
	}
	
}