package modelo.usuario;

import java.io.*;
import java.time.*;
import java.util.*;

import es.uam.eps.padsof.tickets.NonExistentFileException;
import es.uam.eps.padsof.tickets.UnsupportedImageTypeException;
import modelo.abono.*;
import modelo.entrada.*;
import modelo.evento.*;
import modelo.notificacion.*;
import modelo.sistema.Sistema;

/**
 * Clase para realizar las operaciones relacionadas con los Usuarios
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class Usuario implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2815488513600561140L;

	private String nombreUsuario;
	private String contrasenaUsuario;
	private ArrayList<Entrada> entradasCompradas;
	private ArrayList<Entrada> entradasReservadas;
	private ArrayList<Notificacion> notificaciones;
	private ArrayList<Abono> abonos;
	private ArrayList<Ciclo> ciclos;
	private ArrayList<AbonoAnualFecha> anualFechas;

	/**
	 * Constructor de Usuario
	 *
	 * @param nombreUsuario nombre del Usuario
	 * @param contrasena    contrase�a del usuario
	 */
	public Usuario(String nombreUsuario, String contrasena) {
		this.nombreUsuario = nombreUsuario;
		this.contrasenaUsuario = contrasena;

		this.entradasCompradas = new ArrayList<>();
		this.entradasReservadas = new ArrayList<>();
		this.notificaciones = new ArrayList<>();
		this.abonos = new ArrayList<>();
		this.anualFechas = new ArrayList<>();
		this.ciclos = new ArrayList<>();
	}

	/**
	 * Metodo para comprobar que el nombre de usuario y la contrase�a se
	 * corresponden con el usuario en cuesti�n
	 *
	 * @param nombreUsuario nombre del Usuario
	 * @param contrasena    contrase�a del usuario
	 * @return true si se corresponde con el usuario o false en caso contrario
	 */
	public boolean validarUsuario(String nombreUsuario, String contrasena) {
		if ((nombreUsuario.equals(this.nombreUsuario)) && (contrasena.equals(this.contrasenaUsuario))) {
			return true;
		}
		return false;
	}

	/**
	 * Metodo para consultar las notificaciones que tiene el usuario sin leer
	 *
	 * @return true si ha sido leida alguna notificacion o false en caso de no tener
	 *         notifiaciones sin leer
	 */
	public ArrayList<Notificacion> usuarioConsultarNotificacionesSinLeer() {

		 ArrayList<Notificacion> notificaciones = new ArrayList<>();
		 
		for (Notificacion n : this.getUsuarioNotificaciones()) {
			if (!n.isNotificacionLeida()) {
				notificaciones.add(n);
			}
		}

		return notificaciones;
	}
	
	/**
	 * Metodo para leer la notificacion pasada por parametros
	 *
	 * @param notificacion	notifiacion a leer por el usuario
	 * @return boolean de si se ha leido 
	 *
	 */
	public Boolean usuarioLeerNotificacion(Notificacion notificacion) {

		if (this.notificaciones.contains(notificacion)) {
			if (!notificacion.isNotificacionLeida()) {
				notificacion.notificacionLeida();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * metodo para obtener una notificacion a partir de un id
	 * 
	 * @param id indentificador de la notificacion
	 * @return notificacion a la que corresponde el id
	 *
	 */
	public Notificacion getUsuarioNotificacionPorIdentificador(Integer id) {

		for (Notificacion n : this.notificaciones) {
			if(n.getIdentificadorNotificacion() != null) {
				if (n.getIdentificadorNotificacion().equals(id)) {
					return n;
				}
			}
		}
		return null;
	}

	/**
	 * Metodo para consultar el n�mero de notificaciones que tiene el usuario sin
	 * leer
	 *
	 * @return integer que indica el n�mero de entradas
	 *
	 */
	public int usuarioNumNotificacionSinLeer() {
		int i = 0;

		for (Notificacion n : this.getUsuarioNotificaciones()) {
			if (!n.isNotificacionLeida()) {
				i++;
			}
		}

		return i;
	}

	/**
	 * Metodo que actualiza la validez de las entradas, comprobando que la fecha de
	 * validez de las mismas no sea despu�s de la fecha en la que son consultadas
	 *
	 */
	public void updateUsuarioValidezEntradasCompradas() {

		for (Entrada e : getUsuarioEntradasCompradas()) {
			if (e.getFechaValidezEntrada().isBefore(LocalDateTime.now())) {
				e.setValidezEntrada(false); /*
											 * Si la fecha de validez es posterior a la fecha actual, establece que
											 * dicha entrada no es v�lida
											 */
			}
		}
	}

	/**
	 * Metodo para saber si un usuario puede comprar una entrada con un ciclo o
	 * abono
	 *
	 *@param entrada con la entrada a comprobar si se puede comprar con ciclo o abono
	 * @return true si puede comprar, false en caso sontrario
	 */
	public boolean compraUsuarioAbonoCiclo(Entrada entrada) {
		if (compracompruebaEntradaAbono(entrada)) {
			return false;
		}

		for (Abono a : this.getUsuarioAbonos()) {
			if (Sistema.getInstance().getSistemaUsuarioLogeado().getAbonoAnualFecha(a).getFechaCaducidad()
					.isAfter(LocalDate.now()) && a.getAbonoZona().equals(entrada.getEntradaZona())) {
				return true;
			}
		}
		for (Ciclo c : this.getUsuarioCiclos()) {
			if (c.getClass().equals(CicloEvento.class)
					&& ((CicloEvento) c).getCicloZona().equals(entrada.getEntradaZona())) {
				if (((CicloEvento) c).getCicloEventoEventos()
						.contains(entrada.getEntradaRepresentacion().getRepresentacionEvento())) {
					return true;
				}
			} else if (c.getClass().equals(CicloCompuesto.class)
					&& ((CicloCompuesto) c).getCicloZona().equals(entrada.getEntradaZona())) {
				if (((CicloCompuesto) c).getCicloCompuestoEventos()
						.contains(entrada.getEntradaRepresentacion().getRepresentacionEvento())) {
					return true;
				} else {
					for (Ciclo c1 : ((CicloCompuesto) c).getCicloCompuestoCiclos()) {
						if (((CicloEvento) c1).getCicloEventoEventos()
								.contains(entrada.getEntradaRepresentacion().getRepresentacionEvento())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Metodo que comprueba si ya se ha comprado alguna entrada igual con abono
	 * 
	 * @param entrada con la entrada a comprobar si se puede comprar con abono
	 * @return true si no se compro ninguna, false en caso sontrario
	 */
	public boolean compracompruebaEntradaAbono(Entrada entrada) {
		for (Entrada e : this.entradasCompradas) {
			if (e.getEntradaRepresentacion().equals(entrada.getEntradaRepresentacion())) {
				if (e.getEntradaZona().equals(entrada.getEntradaZona()) && e.isCompraAbono()) {
					return true;
				}
			}
		}

		return false;
	}

	/** METODOS QUE A�ADEN ENTRADAS **/

	/**
	 * Metodo que a�ade una entrada al conjunto de entradas que tiene compradas el
	 * usuario
	 *
	 * @param entrada objeto de tipo Entrada que va a ser a�adido
	 *
	 */
	public void addUsuarioEntradasCompradas(Entrada entrada) {
		this.entradasCompradas.add(entrada);
	}

	/**
	 * Metodo que a�ade una entrada al conjunto de entradas que tiene reservadas el
	 * usuario
	 *
	 * @param entrada objeto de tipo Entrada que va a ser a�adido
	 *
	 */
	public void addUsuarioEntradasReservadas(Entrada entrada) {
		this.entradasReservadas.add(entrada);
	}

	/**
	 * Metodo que a�ade un abono al conjunto de abonos que tiene comprados el
	 * usuario
	 *
	 * @param abono objeto de tipo Abono que va a ser a�adido
	 *
	 */
	public void addUsuarioAbono(Abono abono) {
		this.abonos.add(abono);
	}

	/**
	 * Metodo que añade un ciclo al conjunto de ciclos que tiene comprados el
	 * usuario
	 *
	 * @param ciclo objeto de tipo Ciclo que va a ser a�adido
	 *
	 */
	public void addUsuarioCiclo(Ciclo ciclo) {
		if (!this.ciclos.contains(ciclo)) {
			this.ciclos.add(ciclo);
		}
	}
	
	/**
	* Metodo para saber el abono con el que se puede comprar una entrada
	*
	*@param entrada con la entrada a comprar con abono
	* @return abono que puede comprar la entrada
	*/
	public Abono compraUsuarioAbono(Entrada entrada) {
		if (compracompruebaEntradaAbono(entrada)) {
		return null;
	}
	for (Abono a : this.getUsuarioAbonos()) {
		if (Sistema.getInstance().getSistemaUsuarioLogeado().getAbonoAnualFecha(a).getFechaCaducidad()
				.isAfter(LocalDate.now()) && a.getAbonoZona().equals(entrada.getEntradaZona())) {
			return a;
		}
	}
		return null;
	}

	/**
	* Metodo para saber el ciclo con el que se puede comprar una entrada
	*
	*@param entrada con la entrada a comprar con ciclo
	* @return ciclo que puede comprar la entrada
	*/
	public Ciclo compraUsuarioCiclo(Entrada entrada) {
		if (compracompruebaEntradaAbono(entrada)) {
			return null;
	}



	for (Ciclo c : this.getUsuarioCiclos()) {
		if (c.getClass().equals(CicloEvento.class)
				&& ((CicloEvento) c).getCicloZona().equals(entrada.getEntradaZona())) {
			if (((CicloEvento) c).getCicloEventoEventos()
					.contains(entrada.getEntradaRepresentacion().getRepresentacionEvento())) {
				return c;
		}
		} else if (c.getClass().equals(CicloCompuesto.class)
				&& ((CicloCompuesto) c).getCicloZona().equals(entrada.getEntradaZona())) {
			if (((CicloCompuesto) c).getCicloCompuestoEventos()
					.contains(entrada.getEntradaRepresentacion().getRepresentacionEvento())) {
				return c;
			} else {
				for (Ciclo c1 : ((CicloCompuesto) c).getCicloCompuestoCiclos()) {
					if (((CicloEvento) c1).getCicloEventoEventos()
							.contains(entrada.getEntradaRepresentacion().getRepresentacionEvento())) {
						return c1;
					}
				}
			}
		}
	}

	return null;
	}

	/**
	 * Metodo add de AbonoAnualFecha en el array
	 *
	 * @param anualFecha AbonoAnualFecha
	 *
	 */
	public void addUsuarioAbonoAnualFecha(AbonoAnualFecha anualFecha) {
		this.anualFechas.add(anualFecha);
	}

	/** METODOS QUE ELIMINAN ENTRADAS **/

	/**
	 * Metodo que elimina una entrada del conjunto de entradas que tiene reservadas
	 * el usuario, esto ocurrira si el usuario decide cancelar las entradas
	 * reservadas de una representacion o si el propio sistema cancela las entradas
	 * reservadas de una representacion
	 *
	 * @param entrada objeto de tipo Entrada que va a ser eliminada
	 *
	 */
	public void delUsuarioEntradasReservadas(Entrada entrada) {
		this.entradasReservadas.remove(getUsuarioEntradasReservadas().get(getIndiceEntradaEnArrayPorIdentificador(entrada)));
	}

	/**
	 * Metodo que elimina un abono del conjunto de abonos que tiene reservadas el
	 * usuario, esto ocurrira si el abono en cuestion ha expirado
	 *
	 * @param abono objeto de tipo Abono que va a ser eliminado
	 *
	 */
	public void delUsuarioAbono(Abono abono) {
		this.abonos.remove(abono);
	}

	/**
	* Metodo encargado de cancelar una entrtada reservada por el usaurio
	*
	* @param e entrada a cancelar
	* @return true, si se ha podido cancelar la entrada o false en caso
	* contrario
	*
	*/
	public Boolean cancelarUsuarioEntradasReservadasRepresentacion(Entrada e) {

	/* Se podr� cancelar hasta 1h antes de la fecha de la representacion o esa entrada  o es reservada*/
		if (LocalDateTime.now().isAfter(
				e.getEntradaRepresentacion().getRepresentacionFecha().minusHours(Sistema.getInstance().getHorasConfirmacionReserva())) || e.getEntradaUsuarioReserva() == null) {
				return false;
				}
		e.cancelarEntradaReservada();

		return true;
	}

	/**
	 * Metodo encargado de comprar una entrada reservada con tarjeta
	 *
	 * @param numTarjeta numero de la tarheta
	 * @param entrada    entrada a comprar
	 * 
	 * @throws NonExistentFileException      excepcion lanzada cuando no se
	 *                                       encuentra la raiz donde guardar el pdf
	 *                                       de las entradas generadas
	 * @throws UnsupportedImageTypeException excepcion lanzada cuando no se
	 *                                       encuentra la imagen de la entrada
	 * 
	 * @return true, si se ha podido comprar la entrada o false en caso
	 *         contrario
	 *
	 */
	public Boolean comprarTarjetaRepresentacionEntradaReservada(String numTarjeta, Entrada entrada)
			throws NonExistentFileException, UnsupportedImageTypeException {

		Boolean entradaComprada = false;

		if (entrada.getEntradaRepresentacion().getRepresentacionFecha().minusHours(
				Sistema.getInstance().getHorasConfirmacionReserva()).isAfter(LocalDateTime.now())) {
			entrada.comprarEntradaTarjeta(numTarjeta); 
			delUsuarioEntradasReservadas(entrada);
			entradaComprada = true;
		}

		return entradaComprada;
	}
	
	/**
	 * Metodo encargado de comprar una entrada reservada con abono
	 *
	 * @param entrada    entrada a comprar
	 * 
	 * @throws NonExistentFileException      excepcion lanzada cuando no se
	 *                                       encuentra la raiz donde guardar el pdf
	 *                                       de las entradas generadas
	 * @throws UnsupportedImageTypeException excepcion lanzada cuando no se
	 *                                       encuentra la imagen de la entrada
	 * 
	 * @return true, si se ha podido comprar la entrada o false en caso
	 *         contrario
	 *
	 */
	public Boolean comprarAbonoRepresentacionEntradaReservada(Entrada entrada)
			throws NonExistentFileException, UnsupportedImageTypeException {
		
		for (Abono abono :this.abonos) {
			if (abono.getAbonoZona().equals(entrada.getEntradaZona())) {
				if (entrada.getEntradaRepresentacion().getRepresentacionFecha().minusHours(
						Sistema.getInstance().getHorasConfirmacionReserva()).isAfter(LocalDateTime.now())) {
					if (entrada.comprarEntradaAbono()) {
						delUsuarioEntradasReservadas(entrada);
							return true;
					}			
				}
			}
		}
		
		for (Ciclo ciclo :this.ciclos) {
			if (ciclo.getCicloZona().equals(entrada.getEntradaZona())) {
				if (entrada.getEntradaRepresentacion().getRepresentacionFecha().minusHours(
						Sistema.getInstance().getHorasConfirmacionReserva()).isAfter(LocalDateTime.now())) {
					if (entrada.comprarEntradaAbono()) {
						delUsuarioEntradasReservadas(entrada);
							return true;
					}			
				}
			}
		}

		return false;
	}

	/** GETTERS **/

	/**
	 * M�todo getter del nombre de usuario
	 *
	 * @return cadena con el nombre de usuario
	 * 
	 */
	public String getUsuarioNombre() {
		return this.nombreUsuario;
	}

	/**
	 * M�todo getter de la contrase�a de usuario
	 *
	 * @return cadena con la contrase�a de usuario
	 * 
	 */
	public String getUsuarioContrasena() {
		return this.contrasenaUsuario;
	}

	/**
	 * M�todo getter de las entradas compradas por el usuario y que son v�lidas
	 *
	 * @return ArrayList de entradas
	 * 
	 */
	public ArrayList<Entrada> getUsuarioEntradasValidas() {
		ArrayList<Entrada> entradasValidas = new ArrayList<>();

		this.updateUsuarioValidezEntradasCompradas();
		for (Entrada e : entradasCompradas) {
			if (e.getValidezEntrada() == true) {
				entradasValidas.add(e);
			}
		}

		return entradasValidas;
	}

	/**
	 * M�todo getter de las entradas reservadas por el usuario
	 *
	 * @return ArrayList de entradas
	 * 
	 */
	public ArrayList<Entrada> getUsuarioEntradasReservadas() {
		return this.entradasReservadas;
	}

	/**
	 * M�todo getter de las entradas compradas por el usuario
	 *
	 * @return ArrayList de entradas
	 * 
	 */
	public ArrayList<Entrada> getUsuarioEntradasCompradas() {
		return this.entradasCompradas;
	}

	/**
	 * M�todo getter de las entradas de una representacion compradas por el usuario
	 *
	 * @param representacion representacion de la que se quieren obtener las
	 *                       entradas compradas
	 * @return ArrayList de entradas
	 * 
	 */
	public ArrayList<Entrada> getUsuarioEntradasRepresentacionCompradas(Representacion representacion) {

		ArrayList<Entrada> entradasRepresentacion = new ArrayList<>();

		for (Entrada e : getUsuarioEntradasCompradas()) {
			if (e.getEntradaRepresentacion().equals(representacion)) {
				entradasRepresentacion.add(e);
			}
		}

		return entradasRepresentacion;
	}

	/**
	 * M�todo getter de las entradas de una representacion reservadas por el usuario
	 *
	 * @param representacion represetnacion de la que se buscan entradas reservadas
	 *                       por el usuario
	 * @return ArrayList de entradas
	 * 
	 */
	public ArrayList<Entrada> getUsuarioEntradasRepresentacionReservadas(Representacion representacion) {

		ArrayList<Entrada> entradasRepresentacion = new ArrayList<>();

		for (Entrada e : getUsuarioEntradasReservadas()) {
			if (e.getEntradaRepresentacion().equals(representacion)) {
				entradasRepresentacion.add(e);
			}
		}

		return entradasRepresentacion;
	}

	/**
	 * M�todo getter de las notificaciones del usuario
	 *
	 * @return ArrayList de notificaciones
	 * 
	 */
	public ArrayList<Notificacion> getUsuarioNotificaciones() {
		return notificaciones;
	}

	/**
	 * M�todo getter de los abonos que posee del usuario
	 *
	 * @return ArrayList de abonos
	 * 
	 */
	public ArrayList<Abono> getUsuarioAbonos() {
		return abonos;
	}

	/**
	 * M�todo getter de los ciclos que posee del usuario
	 *
	 * @return ArrayList de ciclos
	 * 
	 */
	public ArrayList<Ciclo> getUsuarioCiclos() {
		return ciclos;
	}

	/**
	 * Metodo getter de AbonoAnualFecha de un abono especifico
	 *
	 * @param abono abonoAnual a consultar su fecha
	 * @return AbonoAnualFecha de un abono o null
	 *
	 */
	public AbonoAnualFecha getAbonoAnualFecha(Abono abono) {

		if (this.anualFechas.isEmpty()) {
			return null;
		}

		for (AbonoAnualFecha f : anualFechas) {
			if (f.getAbono().equals(abono)) {
				return f;
			}
		}
		return null;
	}
	
	/**
     * Metodo getter de la posicion de la de le entrada en el
     * array de entradas reservadas
     *
     * @param e     entrada a buscar en el array de entradas
     * @return posicon de la entrada en el array o -1 en caso de no estar
     *
     */
    public int getIndiceEntradaEnArrayPorIdentificador(Entrada e) {
        for(int i = 0; i < getUsuarioEntradasReservadas().size(); i++) {
            if(getUsuarioEntradasReservadas().get(i).getCodigoValidacion() == e.getCodigoValidacion()) {
                return i;
            }
        }
        return -1;
    }

}