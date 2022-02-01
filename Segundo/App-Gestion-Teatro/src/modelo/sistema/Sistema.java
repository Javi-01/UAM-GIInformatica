package modelo.sistema;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import modelo.abono.*;
import modelo.entrada.Entrada;
import modelo.evento.*;
import modelo.notificacion.Notificacion;
import modelo.usuario.*;
import modelo.zona.*;

/**
 * Clase para realizar que almacena todos los datos de la gestion del teatro
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class Sistema implements Serializable {

	private static final long serialVersionUID = -8690746843141299376L;

	private String nombreAdministrador = "Administrador";
	private String contrasenaAdministrador = "teatro";
	private Usuario usuarioLogeado;
	private boolean administrador = false;
	private String fichero = "./sources//dataAppTeatro";
	private int horasConfirmacionReserva;
	private int numMaxEntradas; /* Por defecto, solo se puede comprar una entrada */
	private int contadorEntradas;
	private int contadorZonas;
	private int contadorNotificaciones;
	
	private static Sistema instancia = null;

	private ArrayList<Zona> zonas;
	private ArrayList<Evento> eventos;
	private ArrayList<Abono> abonos;
	private ArrayList<Ciclo> ciclos;
	private ArrayList<Usuario> usuarios;

	/**
	 * Constructor privado de la clase Sistema
	 *
	 * @param abonos   ArrayList de abonos
	 * @param zonas    ArrayList de zonas
	 * @param eventos  ArrayList de eventos
	 * @param usuarios ArrayList de usuarios
	 */
	private Sistema() {
		zonas = new ArrayList<>();
		abonos = new ArrayList<>();
		eventos = new ArrayList<>();
		usuarios = new ArrayList<>();
		ciclos = new ArrayList<>();
		numMaxEntradas = 5;
		contadorEntradas = 0;
		contadorEntradas = 0;
		contadorNotificaciones = 0;
		horasConfirmacionReserva = 1; /* Por defecto se pone a 0, se debe setear despues */

		/*
		 * Se leer los datos del fichero, se comprueba si hay entradas inválidas y en
		 * caso de haberlas son eliminadas y si hay butacas que han expirado su plazo de
		 * deshabilitacion
		 */
		sistemaLeerDatos();

	}

	/**
	 * Metodo para escribir los datos del fichero a la clase de instancia unica
	 * sistema
	 *
	 *
	 */
	public void sistemaEscribirDatos() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.getSistemaFicheroString()));

			oos.writeObject(Sistema.instancia);
			oos.flush();
			oos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para cargar los datos del fichero a la clase de instancia unica
	 * sistema
	 *
	 */
	public void sistemaLeerDatos() {

		File fichero = new File(this.getSistemaFicheroString());

		if (fichero.isFile()) {
			try {
				FileInputStream fis = new FileInputStream(fichero);
				ObjectInputStream ois = new ObjectInputStream(fis);

				Sistema sist = (Sistema) ois.readObject();

				this.zonas = sist.getSistemaZonas();
				this.eventos = sist.getSistemaEventos();
				this.abonos = sist.getSistemaAbonos();
				this.usuarios = sist.getSistemaUsuarios();
				this.ciclos = sist.getSistemaCiclos();
				this.contadorNotificaciones = sist.getContadorNotificaciones();
				this.numMaxEntradas = sist.getNumMaxEntradas();
				this.contadorEntradas = sist.getSistemaContadorEntradas();
				this.contadorZonas = sist.getContadorZonas();
				this.horasConfirmacionReserva = sist.getHorasConfirmacionReserva();
				this.nombreAdministrador = sist.getNombreAdministrador();
				this.contrasenaAdministrador = sist.getContrasenaAdministrador();

				ois.close();

			} catch (EOFException e1) {
				System.out.println("Fichero vacio, anadiendo data...");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public String getNombreAdministrador() {
		return nombreAdministrador;
	}

	public void setNombreAdministrador(String nombreAdministrador) {
		this.nombreAdministrador = nombreAdministrador;
	}

	public String getContrasenaAdministrador() {
		return contrasenaAdministrador;
	}

	public void setContrasenaAdministrador(String contrasenaAdministrador) {
		this.contrasenaAdministrador = contrasenaAdministrador;
	}

	/**
	 * Metodo para comprobar si el usuario introducido es el adminstrador del
	 * sistema
	 *
	 * @param nombre     nombre de usuario
	 * @param contrasena contraseï¿½a
	 * @return true si es el adminstrador, y flase en caso de que no lo sea
	 */
	public boolean sistemaComprobarAdministrador(String nombre, String contrasena) {
		if (this.nombreAdministrador.equals(nombre) && this.contrasenaAdministrador.equals(contrasena)) {
			return true;
		}
		return false;
	}

	/**
	 * Metodo para logear a un usuario en el sistema comprobando que la cuenta
	 * estaba creada con anterioridad
	 * 
	 * @param nombreUsuario     nombre del usuario a verificar
	 * @param contrasenaUsuario contraseï¿½a del usuario a verificar
	 * @return false si no se han encontrado coincidencias o true si se ha
	 *         encontrado con lo cual se establece en sesion activa
	 */
	public boolean sistemaIniciarSesion(String nombreUsuario, String contrasenaUsuario) {

		if (nombreUsuario == null || contrasenaUsuario == null || (getSistemaUsuarioLogeado() != null)) {
			return false;
		}

		if (this.sistemaComprobarAdministrador(nombreUsuario, contrasenaUsuario)) {
			setSistemaAdministradorFlag(true);
			sistemaCancelarEntradasInvalidas();
			sistemaHabilitarButacasTeatro();
			return true;
		} else {
			for (Usuario u : this.getSistemaUsuarios()) {
				if (u.validarUsuario(nombreUsuario, contrasenaUsuario)) {
					setSistemaAdministradorFlag(false);
					setSistemaUsuarioLogeado(u);
					sistemaCancelarEntradasInvalidas();
					sistemaHabilitarButacasTeatro();
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Metodo encargado de cerrar la sesion del usuario loggeado establecenciendo al
	 * usuario logeado a null
	 * 
	 * @return Si no habia usuario logeado devuelve false o true en cualquier otra
	 *         situacion
	 */
	public boolean sistemaCerrarSesion() {

		sistemaEscribirDatos();
		setSistemaUsuarioLogeado(null);

		return true;
	}

	/**
	 * Metodo encargado de cambiar la sesion del usuario loggeado por uno nuevo
	 * seteando el loggeado a null
	 * 
	 * @return Si no habia usuario logeado devuelve false o true en cualquier otra
	 *         situacion
	 */
	public boolean sistemaCambiarSesion() {

		setSistemaUsuarioLogeado(null);

		return true;
	}

	/**
	 * Metodo para registrar a un usuario en el sistema comprobando que la cuenta no
	 * estuviera creada con anterioridad
	 * 
	 * @param nombreUsuario     nombre del usuario a crear
	 * @param contrasenaUsuario contrasena del usuario a crear
	 * @return false si no se ha podido crear o true si se ha creado
	 */
	public boolean registrarSistemaUsuario(String nombreUsuario, String contrasenaUsuario) {

		/*
		 * Comprobamos si al registrarse, si los datos como conjunto ya existen o si el
		 * nombre de usuario ya existe
		 */
		for (int i = 0; i < this.usuarios.size(); i++) {
			if (this.usuarios.get(i).validarUsuario(nombreUsuario, contrasenaUsuario)
					|| this.usuarios.get(i).getUsuarioNombre().equals(nombreUsuario)) {
				return false;
			}
		}
		this.usuarios.add(new Usuario(nombreUsuario, contrasenaUsuario));
		setSistemaUsuarioLogeado(this.usuarios.get(this.usuarios.size() - 1));
		return true;
	}

	/**
	 * Metodo encargado de crear un evento y que sea anadido al sistema
	 * 
	 * @param evento evento a crear
	 * @return true si se ha creado correctamente el evento o false en caso
	 *         contrario
	 */
	public boolean sistemaCrearEvento(Evento evento) {

		if (!getSistemaAdministradorFlag()) {
			return false;
		}

		for (Evento e : this.getSistemaEventos()) {
			if (e.getEventoTitulo().equals(evento.getEventoTitulo())) {
				return false;
			}
		}

		this.eventos.add(evento);
		evento.setEventoZonas(
				getSistemaZonas()); /* Aï¿½adimos las zonas registradas en el sistema a dicho evento creado */

		return true;
	}

	/**
	 * Metodo encargado de comprobar si una representacion se solaparia con otras ya
	 * existentes
	 * 
	 * @param repre representacion a comprobar
	 * @return true si coincide en fecha y hora con otras o false en caso contrario
	 */
	public boolean sistemaSolapeEntreRepresentaciones(Representacion repre) {

		if (!getSistemaAdministradorFlag()) {
			return false;
		}

		for (Evento e : getSistemaEventos()) { // Se recorre todas las representaciones de todos los eventos del sistema
			for (Representacion r : e.getEventoRepresentaciones()) {
				if (r.getRepresentacionFecha().equals(repre.getRepresentacionFecha())) { // Si coincide en fecha y hora
																							// con cualquier otra
																							// representacion del
																							// sistema no se puede
																							// anadir
					return true;
				}
				// Si coincide en el periodo de duracion del alguna representacion por delante,
				// tampoco se puede
				// Si coincide con alguna representacion que coincide despues de este evento
				// tampoco se puede añadir
				else if (r.getRepresentacionFecha().plusMinutes((long) e.getEventoDuracion())
						.isAfter(repre.getRepresentacionFecha())
						&& (repre.getRepresentacionFecha().plusMinutes((long) e.getEventoDuracion())
								.isAfter(r.getRepresentacionFecha()))) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Metodo encargado de crear una zona y que sea aï¿½adida al sistema
	 * 
	 * @param zona zona a aï¿½adir al sistema
	 * @return true si se ha creado correctamente la zona o false en caso contrario
	 */
	public boolean sistemaCrearZonaTeatro(Zona zona) {

		if (!getSistemaAdministradorFlag()) {
			return false;
		}

		for (Zona z : this.getSistemaZonas()) {
			if (z.equals(zona) || z.getZonaNombre().equals(zona.getZonaNombre())) {
				return false;
			}
		}
		this.zonas.add(zona);
		return true;
	}

	/**
	 * Metodo encargado de deshabilitar una butaca de una zona del teatro dicha zona
	 * tiene que ser una zonaSentado
	 * 
	 * @param zona   zona de la que se deshabilitarÃ¡ una butaca
	 * @param butaca butaca a deshabilitar
	 * @param inicio fecha de inicio de la deshabilitacion de una butaca
	 * @param fin    fecha de fin de la deshabilitacion de una butaca
	 * @param motivo motivo de la deshabilitacion
	 * 
	 * @return true si se ha deshabilitado correctamente la butaca de la zona o
	 *         false en caso contrario
	 */
	public boolean sistemaDeshabilitarButacaTeatro(Zona zona, Butaca butaca, LocalDateTime inicio, LocalDateTime fin,
			String motivo) {

		if (!getSistemaAdministradorFlag() || inicio.isAfter(LocalDateTime.now()) || inicio.isAfter(fin)
				|| fin.isBefore(LocalDateTime.now())) {
			return false;
		}

		for (Zona z : getSistemaZonas()) {
			if (z.equals(zona) && zona.getClass().equals(ZonaSentado.class)) {
				if (((ZonaSentado) z).getZonaSentadoButacas()
						.contains(butaca)) { /* Si la butaca se encuentra en la zona */
					if (butaca.isButacaHabilitada()) { // Deshabilitando las butacas en el sistema se deshabilitan en
														// todos los eventos
						butaca.setButacaHabilitada(false);
						butaca.setButacaInicioDeshabilitacion(
								inicio); /* Establecemos la fecha de inicio y de fin de la deshabilitacion */
						butaca.setButacaFinDeshabilitacion(fin);
						butaca.setButacaMotivoDeshabilitacion(motivo);
						
						for(Evento e : getSistemaEventos()) {
							for(Representacion r : e.getEventoRepresentaciones()) {
								for(Entrada ent : r.getRepresentacionEntradasOcupadasEnZona(zona)) {
									if(ent.getEntradaButaca().getButacaColumna() == butaca.getButacaColumna() && ent.getEntradaButaca().getButacaFila() == butaca.getButacaFila()) {
										if(ent.getEntradaUsuarioCompra() != null) {
											Notificacion notificacion = new Notificacion(Sistema.getInstance().getContadorNotificaciones(),
													"Butaca deshabilitada",
													"La butaca con fila: " + butaca.getButacaFila() + " ,y columna: " +  butaca.getButacaColumna() + " ha sido deshabilitada",  ent.getEntradaUsuarioCompra());
											notificacion.notificarUsuario();
										} else if(ent.getEntradaUsuarioReserva() != null) {
											Notificacion notificacion = new Notificacion(Sistema.getInstance().getContadorNotificaciones(),
													"Butaca deshabilitada",
													"La butaca con fila: " + butaca.getButacaFila() + " ,y columna: " +  butaca.getButacaColumna() + " ha sido deshabilitada",  ent.getEntradaUsuarioCompra());
											notificacion.notificarUsuario();
										}
									}
								}
							}
						}
						
						Sistema.getInstance().setContadorNotificaciones();
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Metodo encargado de comprobar si hay butacas deshabilitadas, y en caso de que
	 * su plazo de deshabilitacion haya expirado, se vuelve a habilitar la butaca
	 * 
	 * 
	 * @return true si se han habilitado correctamente butacas del teatro o false en
	 *         caso contrario
	 */
	public boolean sistemaHabilitarButacasTeatro() {

		boolean butacasHabilitadas = false;

		if (!getSistemaAdministradorFlag()) {
			return butacasHabilitadas;
		}

		for (Zona z : getSistemaZonas()) {
			if (z.getClass().equals(ZonaSentado.class)) {
				for (Butaca b : ((ZonaSentado) z).getZonaSentadoButacas()) {
					if (!b.isButacaHabilitada()) {
						if (b.getButacaFinDeshabilitacion().isBefore(LocalDateTime.now())) {
							b.setButacaHabilitada(true);
							b.setButacaFinDeshabilitacion(null);
							b.setButacaInicioDeshabilitacion(null);
							b.setButacaMotivoDeshabilitacion(" ");
							butacasHabilitadas = true;
						}
					}
				}
			}
		}

		return butacasHabilitadas;
	}

	/**
	 * Metodo encargado de crear un abono y que sea añadida al sistema
	 * 
	 * @param abono abono a añadir al sistema
	 * @return true si se ha creado correctamente el abono o false en caso contrario
	 */
	public boolean sistemaCrearAbonoTeatro(Abono abono) {

		if (getSistemaAdministradorFlag()) {
			if (abono.getClass().equals(Abono.class)) {
				for (Abono a : getSistemaAbonos()) {
					if (a.equals(abono) || a.getAbonoZona().equals(abono.getAbonoZona())) {
						return false;
					}
				}
			}
			this.abonos.add(abono);
			return true;
		}

		return false;
	}

	/**
	 * Metodo encargado de crear un ciclo y que sea añadida al sistema
	 * 
	 * @param ciclo ciclo a añadir al sistema
	 * @return true si se ha creado correctamente el ciclo o false en caso contrario
	 */
	public boolean sistemaCrearCicloTeatro(Ciclo ciclo) {

		if (getSistemaAdministradorFlag()) {
			for (Ciclo c : getSistemaCiclos()) {
				if (c.equals(ciclo)) {
					return false;
				}

			}

			this.ciclos.add(ciclo);
			return true;
		}

		return false;
	}

	/**
	 * Metodo encargado de crear un abono y que sea a�adida al sistema
	 * 
	 * @param abono       abono anual
	 * @param nuevoPrecio double con el nuevo precio que tendr�a el abono anual
	 * @return true si se ha modificado correctamente el abono o false en caso
	 *         contrario
	 */
	public boolean sistemaCambiarPrecioAbonoAnual(Abono abono, double nuevoPrecio) {

		if (!getSistemaAdministradorFlag()) {
			return false;
		}

		abono.setAbonoPrecio(nuevoPrecio);
		return true;

	}

	/**
	 * Metodo encargado de cancelar las entradas reservadas del sistema cuya fecha
	 * de validez haya expirado
	 * 
	 * @return true si se ha cancelado alguna entrada o false en caso contrario
	 */
	public boolean sistemaCancelarEntradasInvalidas() {
		int i, j, k;
		Boolean flag_entradas_canceladas = false;

		for (i = 0; i < getSistemaEventos().size(); i++) {
			for (j = 0; j < getSistemaEventos().get(i).getEventoRepresentaciones().size(); j++) {
				if (LocalDateTime.now().isAfter(this.eventos.get(i).getEventoRepresentaciones().get(j)
						.getRepresentacionFecha().minusHours(1)) && LocalDateTime.now().isBefore(this.eventos.get(i).getEventoRepresentaciones().get(j)
                                .getRepresentacionFecha())) {
					for (k = 0; k < getSistemaEventos().get(i).getEventoRepresentaciones().get(j)
							.getRepresentacionEntradas().size(); k++) {

						/*
						 * Creamos la notificacion corespondiente a una entrada invalidada si la entrada
						 * estaba reservada por un usuario
						 */
						if (this.eventos.get(i).getEventoRepresentaciones().get(j).getRepresentacionEntradas().get(k)
								.getEntradaUsuarioReserva() != null) {
							this.eventos.get(i).getEventoRepresentaciones().get(j).getRepresentacionEntradas().get(k)
									.cancelarEntradaReservada();
							Notificacion notificacion = new Notificacion(getContadorNotificaciones(), 
									"Entrada para el evento " + this.eventos.get(i).getEventoTitulo()
											+ " ha sido cancelada",
									"Tu entrada ha saido cancelada por los siguientes motivos:\n -Tu entrada queda invalidada por no confirmar la compra en el plazo correspondiente", this.eventos.get(i).getEventoRepresentaciones().get(j).getRepresentacionEntradas().get(k).getEntradaUsuarioReserva());
							notificacion.notificarUsuario();
							setContadorNotificaciones();
						}
						flag_entradas_canceladas = true;
					}
				}
			}
		}
		return flag_entradas_canceladas;
	}

	/**
	 * Metodo encargado de buscar todos los eventos que contengan el nombre pasado
	 * por parametros
	 * 
	 * @param cadenaIntroducida es la cadena a buscar coincidencias
	 * @return ArrayList de eventos que coincidan
	 */
	public ArrayList<Evento> sistemaBuscarEventosCoincidentes(String cadenaIntroducida) {
		ArrayList<Evento> eventosCoincidencia = new ArrayList<>();

		for (Evento e : Sistema.getInstance().getSistemaEventos()) {
			if (e.getEventoTitulo().contains(cadenaIntroducida)) {
				eventosCoincidencia.add(e);
			}
		}
		return eventosCoincidencia;
	}

	/**
	 * Metodo encargado de buscar todos los eventos que no se encuentran en el array
	 * pasado por parametros
	 * 
	 * @param eve 	ArrayList de eventos
	 * @return ArrayList de eventos que coincidan
	 */
	public ArrayList<Evento> sistemaEventosSinEncontrar(ArrayList<Evento> eve) {
		ArrayList<Evento> eventosSinEncontrar = new ArrayList<>();

		for (Evento e : Sistema.getInstance().getSistemaEventos()) {
			if (!eve.contains(e)) {
				eventosSinEncontrar.add(e);
			}
		}
		return eventosSinEncontrar;
	}

	/**
	 * Metodo encargado de buscar todos los ciclos que no se encuentran en el array
	 * pasado por parametros
	 * 
	 * @param ciclo		ArrayList de ciclos
	 * @return ArrayList de eventos que coincidan
	 */
	public ArrayList<Ciclo> sistemaCiclosSinEncontrar(ArrayList<Ciclo> ciclo) {
		ArrayList<Ciclo> ciclosSinEncontrar = new ArrayList<>();

		for (Ciclo c : Sistema.getInstance().getSistemaCiclos()) {
			if (!ciclo.contains(c)) {
				ciclosSinEncontrar.add(c);
			}
		}
		return ciclosSinEncontrar;
	}

	/**
	 * Metodo encargado de buscar todos los abonos que contengan el nombre pasado
	 * por parametros
	 * 
	 * @param cadenaIntroducida es la cadena a buscar coincidencias
	 * @return ArrayList de abonos que coincidan
	 */
	public ArrayList<Ciclo> sistemaBuscarCiclos(String cadenaIntroducida) {
		ArrayList<Ciclo> ciclosCoincidencia = new ArrayList<>();

		for (Ciclo c : getSistemaCiclos()) {
			if (c.getCicloNombre().contains(cadenaIntroducida)) {
				ciclosCoincidencia.add(c);
			}
		}

		return ciclosCoincidencia;
	}

	/**
	 * Metodo encargado de buscar si ya existe algun abono asociado a una zona
	 *
	 * @param cadenaIntroducida es la cadena a buscar coincidencias
	 * @return ArrayList de abonos que coincidan
	 */
	public boolean sistemaBuscarAbonosCoincidentes(String cadenaIntroducida) {

		for (Abono a : getSistemaAbonos()) {
			if (a.getAbonoZona().getZonaNombre().contains(cadenaIntroducida)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Metodo encargado de buscar todas las representaciones que contengan el nombre
	 * pasado por parametros
	 * 
	 * @param tipoEvento	cadenaIntroducida es la cadena a buscar coincidencias
	 * @return ArrayList de eventos que coincidan
	 */
	public ArrayList<Representacion> sistemaBuscarRepresentacionesPorTipo(String tipoEvento) {
		ArrayList<Representacion> representaciones = new ArrayList<>();

		for (Evento e : Sistema.getInstance().getSistemaEventos()) {
			if (e.getEventoTipoString().equals(tipoEvento)) {
				for (Representacion r : e.getEventoRepresentaciones()) {
					if (!r.getRepresentacionFecha().isAfter(LocalDateTime.now())) {
						representaciones.add(r);
					}
				}
			}
		}
		return representaciones;
	}

	/**
	 * Metodo encargado de buscar una zona a partir del nombre
	 * 
	 * @param nombreZona es el nombre de la zona
	 * @return Zona coincidente con dicho nombre
	 */
	public Zona sistemaBuscarZona(String nombreZona) {

		if (nombreZona.equals("")) {
			return null;
		}
		for (Zona z : getSistemaZonas()) {
			if (z.getZonaNombre().equalsIgnoreCase(nombreZona)) {
				return z;
			}
		}
		return null;
	}

	/**
	 * Metodo encargado de buscar si la zona esta contenido en el sistema
	 * 
	 * @param cadenaIntroducida es la cadena a buscar coincidencias
	 * @return La zona que ha encontrado o null
	 */
	public Zona sistemaContieneZona(String cadenaIntroducida) {

		for (Zona z : Sistema.getInstance().getSistemaZonas()) {
			if (z.getZonaNombre().equalsIgnoreCase(cadenaIntroducida))
				return z;
		}
		return null;
	}

	/**
	 * Metodo encargado de buscar si el evento esta contenido en el sistema
	 * 
	 * @param cadenaIntroducida es la cadena a buscar coincidencias
	 * @return Evento que ha encontrado
	 */
	public Evento sistemaContieneEvento(String cadenaIntroducida) {

		for (Evento e : Sistema.getInstance().getSistemaEventos()) {
			if (e.getEventoTitulo().equalsIgnoreCase(cadenaIntroducida))
				return e;
		}
		return null;
	}

	/**
	 * Metodo encargado de buscar la representacion con dicha fecha
	 * 
	 * @param evento	evento del que se quiere obtener una representacion
	 * @param fecha		fecha de la representacion a buscar
	 * @return ArrayList de eventos que coincidan
	 */
	public Representacion sistemaObtenerRepresentacion(Evento evento, LocalDateTime fecha) {
		for (Representacion repre : evento.getEventoRepresentaciones()) {
			if (repre.getRepresentacionFecha().equals(fecha)) {
				return repre;
			}
		}
		return null;
	}

	/**
	 * Metodo encargado de formatear una fecha a partir de dos cadenas indicando la
	 * fecha y la hora de una representacion
	 * 
	 * @param fechaDia  es el dian, mes y a�o de LocalDateTime
	 * @param fechaHora es la hora y los minutos de un LocalDateTime
	 * @return LocalDateTime resultante
	 */
	public LocalDateTime sistemaFormatearFechaRepresentacion(String fechaDia, String fechaHora) {
		String strFecha = fechaDia + " " + fechaHora;
		DateTimeFormatter fechaFormateada = DateTimeFormatter.ofPattern("yyyy-M-d H:m");
		LocalDateTime fecha = LocalDateTime.parse(strFecha, fechaFormateada);

		return fecha;
	}

	/** GETTERS **/

	/**
	 * Patron singleton de la clase sistema para tener una instancia de dicha clase
	 * unica en todo el proyecto
	 *
	 * @return instancia de la clase sistema
	 */
	public static Sistema getInstance() {

		if (instancia == null) {
			instancia = new Sistema();
		}
		return instancia;
	}

	/**
	 * Metodo para obtener el estado del adminstrador
	 * 
	 * @return el estado del adminstradror (true o false)
	 */
	public boolean getSistemaAdministradorFlag() {
		return this.administrador;
	}

	/**
	 * Metodo getter del usuario logeado en la sesion como usuario activo
	 * 
	 * @return el usuario en sesion activa
	 */
	public Usuario getSistemaUsuarioLogeado() {
		return this.usuarioLogeado;
	}

	/**
	 * Metodo para obtener los eventos del Sistema
	 * 
	 * @return los eventos del sistema
	 */
	public ArrayList<Evento> getSistemaEventos() {
		return this.eventos;
	}

	/**
	 * Metodo getter de los usuarios del Sistema
	 * 
	 * @return los usuarios del sistema
	 */
	public ArrayList<Usuario> getSistemaUsuarios() {
		return this.usuarios;
	}

	/**
	 * Metodo geter de las zonas del Sistema
	 * 
	 * @return las zonas del sistema
	 */
	public ArrayList<Zona> getSistemaZonas() {
		return this.zonas;
	}

	/**
	 * Metodo geter de las zonas del Sistema que no estan contenidas en alguna
	 * zonaCompuesta
	 * 
	 * @return las zonas del sistema no contenidas
	 */
	public ArrayList<Zona> getSistemaZonasNoContenidas() {

		ArrayList<Zona> zonasNoContenidas = new ArrayList<>();
		ArrayList<Zona> zonasContenidas = new ArrayList<>();

		for (Zona z1 : getSistemaZonas()) {
			if (z1.getClass().equals(ZonaCompuesta.class)) {
				zonasContenidas.addAll(((ZonaCompuesta) z1).getZonaCompuestaZonas());
			}
		}

		for (Zona z2 : getSistemaZonas()) {
			if (!zonasContenidas.contains(z2)) {
				zonasNoContenidas.add(z2);
			}
		}

		return zonasNoContenidas;
	}

	/**
	 * Metodo geter de las zonas del Sistema que son compuestas
	 * 
	 * @return las zonas del sistema que son compuestas
	 */
	public ArrayList<Zona> getSistemaZonasCompuestas() {

		ArrayList<Zona> zonasCompuestas = new ArrayList<>();

		for (Zona z : getSistemaZonas()) {
			if (z.getClass().equals(ZonaCompuesta.class)) {
				zonasCompuestas.add(z);
			}
		}

		return zonasCompuestas;
	}

	/**
	 * Metodo geter de las zonas del Sistema que son sentado
	 * 
	 * @return las zonas del sistema que son sentadas
	 */
	public ArrayList<Zona> getSistemaZonasSentadas() {

		ArrayList<Zona> zonasSentado = new ArrayList<>();

		for (Zona z : getSistemaZonas()) {
			if (z.getClass().equals(ZonaSentado.class)) {
				zonasSentado.add(z);
			}
		}

		return zonasSentado;
	}

	/**
	 * Metodo para obtener la butaca a partir de una fila, clomna y zona
	 * 
	 * 
	 * @param zona	zona donde se encuentra la butaca
	 * @param fila	fila de la butaca
	 * @param columna	columna de la butaca
	 * @return la butaca del teatro
	 */
	public Butaca getSistemaButacaEnZona(Zona zona, Integer fila, Integer columna) {

		if (zona.getClass().equals(ZonaSentado.class)) {
			for (Butaca b : ((ZonaSentado) zona).getZonaSentadoButacas()) {
				if (b.getButacaFila() == fila && b.getButacaColumna() == columna) {
					return b;
				}
			}
		}
		return null;
	}

	/**
	 * Metodo para obtener las zonas restantes a partir de unas entradas ademas de
	 * obtener las zonas que no son contenidas
	 * 
	 * @param zona 	arraylist de zonas de las que se buscaran aquellas que no esten contenidas
	 * @return las zonas del sistema restantes
	 */
	public ArrayList<Zona> getSistemaZonasNoContenidasAPartirDeUnaLista(ArrayList<Zona> zona) {

		if (zona.isEmpty()) {
			return getSistemaZonasNoContenidas();
		}
		ArrayList<Zona> zonasSentado = getSistemaZonasNoContenidas();
		ArrayList<Zona> zonasRes = new ArrayList<Zona>();
		for (Zona z : zonasSentado) {
			if (!zona.contains(z)) {
				zonasRes.add(z);
			}
		}

		return zonasRes;
	}

	/**
	 * Metodo getter de los abonos del Sistema
	 * 
	 * @return los abonos del sistema
	 */
	public ArrayList<Abono> getSistemaAbonos() {
		return this.abonos;
	}

	/**
	 * Metodo getter de los ciclos del Sistema
	 * 
	 * @return los ciclos del sistema
	 */
	public ArrayList<Ciclo> getSistemaCiclos() {
		return this.ciclos;
	}

	/**
	 * Metodo getter del nombre del fichero de almacenado de datos del teatro
	 *
	 * @return cadena con el nombre del fichero
	 */
	public String getSistemaFicheroString() {
		return fichero;
	}

	/**
	 * Metodo getter del numero de horas para confirmar una reserva
	 * 
	 * @return numero maximo de horas para confirmar la reserva de entradas
	 */
	public int getHorasConfirmacionReserva() {
		return horasConfirmacionReserva;
	}

	/**
	 * Metodo getter del numero maximo de entradas que un usuario puede comprar o
	 * reservas seguidamente
	 * 
	 * @return numero maximo de entradas que se pueden comprar o reservar
	 */
	public int getNumMaxEntradas() {
		return numMaxEntradas;
	}

	/**
	 * Metodo getter del identificador de las entradas del teatro
	 * 
	 * @return ultimo identificador no usado
	 */
	public int getSistemaContadorEntradas() {
		return contadorEntradas;
	}

	/**
	 * Metodo getter del hashmap con clave nombre del evento y valor la ocupacion
	 * por zona Dicho hashmap se devuelve ordenado de mayor a menor, siendo la
	 * primera clave-valor el evento que haya tenido mayor ocupacion en la zona
	 * especificada
	 * 
	 * @param zona zona de la que se consultaran las estadisticas
	 * @return HashMap 
	 */
	public HashMap<String, Integer> getSistemaEstadisticasOcupacionEventosPorZona(Zona zona) {
		int ocupacion = 0;
		HashMap<String, Integer> eventosOcupacion = new HashMap<>();

		for (Evento e : getSistemaEventos()) {

			if (e.getEventoEstadisticaOcupacion() != null) {
				ocupacion = e.getEventoEstadisticaOcupacion().get(zona.getZonaNombre());
				eventosOcupacion.put(e.getEventoTitulo(), ocupacion);
				ocupacion = 0;
			}

		}

		return eventosOcupacion.entrySet().stream().sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	/**
	 * Metodo getter del hashmap con clave nombre del evento y valor la recaudacion
	 * por zona Dicho hashmap se devuelve ordenado de mayor a menor, siendo la
	 * primera clave-valor el evento que haya tenido mayor recaudacion en la zona
	 * especificada
	 * 
	 * @param zona zona de la que se consultaran las estadisticas
	 * @return HashMap 
	 */
	public HashMap<String, Double> getSistemaEstadisticasRecaudacionEventosPorZona(Zona zona) {
		double recaudacion = 0.0;
		HashMap<String, Double> eventosOcupacion = new HashMap<>();

		for (Evento e : getSistemaEventos()) {

			if (e.getEventoEstadisticaOcupacion() != null) {
				recaudacion = e.getEventoEstadisticaRecaudacion().get(zona.getZonaNombre());
				eventosOcupacion.put(e.getEventoTitulo(), recaudacion);
				recaudacion = 0.0;
			}

		}

		return eventosOcupacion.entrySet().stream().sorted((Map.Entry.<String, Double>comparingByValue().reversed()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	/**
	 * Metodo getter del hashmap con clave nombre del evento y valor la ocupacion
	 * total Dicho hashmap se devuelve ordenado de mayor a menor, siendo la primera
	 * clave-valor el evento que haya tenido mayor ocupacion en la zona especificada
	 * 
	 * @return HashMap
	 */
	public HashMap<String, Integer> getSistemaEstadisticasOcupacionTotalEventos() {
		int ocupacion = 0;
		HashMap<String, Integer> eventosOcupacion = new HashMap<>();

		for (Evento e : getSistemaEventos()) {

			ocupacion = e.getEventoEstadisticaOcupacionTotal();
			eventosOcupacion.put(e.getEventoTitulo(), ocupacion);
			ocupacion = 0;

		}

		return eventosOcupacion.entrySet().stream().sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	/**
	 * Metodo getter del hashmap con clave nombre del evento y valor la ocupacion
	 * total Dicho hashmap se devuelve ordenado de mayor a menor, siendo la primera
	 * clave-valor el evento que haya tenido mayor ocupacion en la zona especificada
	 * 
	 * @return HashMap
	 */
	public HashMap<String, Double> getSistemaEstadisticasRecaudacionTotalEventos() {
		double recaudacion = 0;
		HashMap<String, Double> eventosOcupacion = new HashMap<>();

		for (Evento e : getSistemaEventos()) {

			recaudacion = e.getEventoEstadisticaRecaudacionTotal();
			eventosOcupacion.put(e.getEventoTitulo(), recaudacion);
			recaudacion = 0;

		}

		return eventosOcupacion.entrySet().stream().sorted((Map.Entry.<String, Double>comparingByValue().reversed()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	/** SETTERS **/

	/**
	 * Se establece el nombre de usuario y la constraseña que distinguen al
	 * administrado de la aplicacion
	 *
	 * @param nombreAdministrador     nombre de usuario del administrador
	 * @param contrasenaAdministrador contraseña del adminsitrador
	 */
	public void setSistemaAdministrador(String nombreAdministrador, String contrasenaAdministrador) {
		if (this.getSistemaAdministradorFlag()) {
			this.nombreAdministrador = nombreAdministrador;
			this.contrasenaAdministrador = contrasenaAdministrador;
		}

	}

	/**
	 * Metodo setter de una bandera para saber si estamos en la sesion del
	 * administrador
	 * 
	 * @param administradorFlag bandera del estado del administrador
	 */
	public void setSistemaAdministradorFlag(boolean administradorFlag) {
		this.administrador = administradorFlag;
	}

	/**
	 * Metodo setter del usuario logeado en la sesion como usuario activo
	 * 
	 * @param usuarioLogeado usuario que ha iniciado sesion
	 */
	public void setSistemaUsuarioLogeado(Usuario usuarioLogeado) {
		this.usuarioLogeado = usuarioLogeado;
	}

	/**
	 * Metodo setter del nombre del fichero de almacenado de datos del teatro
	 * 
	 * @param fichero nombre del fichero
	 */
	public void setSistemaFicheroString(String fichero) {
		this.fichero = fichero;
	}

	/**
	 * Metodo setter del numero de horas para confirmar una reserva
	 * 
	 * @param horasConfirmacionReserva numero maximo de horas para confirmar la
	 *                                 reserva de entradas
	 */
	public void setHorasConfirmacionReserva(int horasConfirmacionReserva) {
		this.horasConfirmacionReserva = horasConfirmacionReserva;
	}

	/**
	 * Metodo setter del numero maximo de entradas que un usuario puede comprar o
	 * reservas seguidamente
	 * 
	 * @param numMaxEntradas numero maximo de entradas que se pueden comprar o
	 *                       reservar
	 */
	public void setNumMaxEntradas(int numMaxEntradas) {

		if (getSistemaAdministradorFlag()) {
			this.numMaxEntradas = numMaxEntradas;
		}
	}

	/**
	 * Metodo que actualiza el contador del identificador de entradas
	 * 
	 */
	public void updateSistemaContadorEntradas() {
		this.contadorEntradas++;
	}

	/**
	 * Metodo getter del identificador unico de las zonas
	 * 
	 * @return contadorZonas el identificador de la zona
	 */
	public int getContadorZonas() {
		return contadorZonas;
	}

	/**
	 * Metodo setter del identificador unico de las zonas
	 * 
	 * @param contadorZonas con el identificador de la zona
	 */
	public void setContadorZonas(int contadorZonas) {
		this.contadorZonas++;
	}
	
	/**
	 * Metodo getter del identificador unico de las zonas
	 * 
	 * @return contadorZonas el identificador de la zona
	 */
	public int getContadorNotificaciones() {
		return contadorNotificaciones;
	}

	/**
	 * Metodo setter del identificador unico de las zonas

	 */
	public void setContadorNotificaciones() {
		this.contadorNotificaciones++;
	}

}