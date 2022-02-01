package modelo.zona;

import java.util.ArrayList;

/**
 * Clase ZonaSentado
 *
 * @author Jaime Diaz, Javier Fraile e Ivan Fernandez
 */
public class ZonaSentado extends Zona {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6441341098911940299L;
	private  int filas = 0;
	private int columnas = 0;
	private int columnasMaximas;
	private int filasMaximas;
	private ArrayList<Butaca> butacas = new ArrayList<>();

	/**
	 * Constructor de ZonaSentado 
	 *
	 * @param identificador    id de la zona a crear
	 * @param filasMax    número máximo de filas que tendrá la zona
	 * @param columnasMax    número máximo de columnmas que tendrá la zona
	 * @param nombreZona	nombre que tendrá la zona
	 * 
	 */
	public ZonaSentado(int identificador, int filasMax, int columnasMax, String nombreZona) {

		super(identificador, filasMax * columnasMax, nombreZona); /* multiplicando el numero maximo de filas y columnas, calculamos e aforo que tendra la zona */
		this.columnasMaximas = columnasMax;
		this.filasMaximas = filasMax;

		while (filas < filasMaximas) {
			while (columnas < columnasMaximas) {
				this.butacas.add(new Butaca(filas, columnas));
				columnas++;
			}
			columnas = 0;
			filas++;
		}
	}
	
	/** GETERS **/ 
	
	/**
	 * Método getter que devuelve las de butacas de la zona
	 *
	 * @return ArrayList de butacas que tiene una zona sentado
	 * 
	 */
	public ArrayList<Butaca> getZonaSentadoButacas(){
		return this.butacas;
	}
	
	/**
	 * Método getter que devuelve el numero maximo de filas que tiene la zona
	 *
	 * @return numero de filas
	 * 
	 */
	public int getFilasMax() {
		return this.filasMaximas;
	}
	
	/**
	 * Método getter que devuelve el numero maximo de columnas que tiene la zona
	 *
	 * @return numero de columnas
	 * 
	 */
	public int getColumnasMax() {
		return this.columnasMaximas;
	}

}