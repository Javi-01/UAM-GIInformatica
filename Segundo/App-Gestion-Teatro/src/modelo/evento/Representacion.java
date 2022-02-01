package modelo.evento;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import es.uam.eps.padsof.tickets.NonExistentFileException;
import es.uam.eps.padsof.tickets.UnsupportedImageTypeException;
import modelo.entrada.*;
import modelo.notificacion.Notificacion;
import modelo.sistema.Sistema;
import modelo.zona.*;

/**
 * Clase de Representacion de un Evento
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class Representacion implements Serializable {

	private static final long serialVersionUID = 2998496965474318518L;

	private int aforoOcupado;
	private int aforoDisponible;
	private int aforoTotal;

	private ArrayList<Double> dineroRecaudadoZona;
	private ArrayList<ListaEspera> listasEspera;
	private ArrayList<Entrada> entradas;
	private LocalDateTime fecha;
	private Evento evento;

	/**
	 * Constructor de la Representacion
	 *
	 * @param evento evento del cual se realiza la representacion
	 * @param fecha  fecha establecida para dicha representacion
	 */
	public Representacion(Evento evento, LocalDateTime fecha) {
		this.fecha = fecha;
		this.evento = evento;
		this.entradas = new ArrayList<>();
		this.listasEspera = new ArrayList<>();

		int aforoTotal = 0;
		for (Zona z : this.evento.getEventoZonas()) {
			aforoTotal += z.getZonaAforo();
		}

		this.representacionGenerarEntradas(); /* Se generan todas las entradas de la represetnacion */
		this.representacionGenerarListaDeEspera(); /* Se genera la lista de espera por cada zona */
		this.setRepresentacionAforoOcupado(0);
		this.setRepresentacionAforoDisponible(aforoTotal);
		this.setRepresentacionAforoTotal(aforoTotal);
	}

	/**
	 * M�todo para generar las entradas de la representacion en base al evento
	 * y a el PrecioZona del evento
	 * 
	 */
	public void representacionGenerarEntradas() {
		for (PrecioZona z : this.getRepresentacionEvento().getEventoPrecioZonas()) {
			int butaca = 0;
			for (int i = 0; i < z.getPrecioZonaZona().getZonaAforo(); i++) {

				if (z.getPrecioZonaZona().getClass().equals(ZonaPie.class)) {
					Entrada e = new EntradaNoNumerada(Sistema.getInstance().getSistemaContadorEntradas(), this,
							z.getPrecioZonaZona(), z.getPrecioZonaPrecio(), this.getRepresentacionFecha());
					this.entradas.add(e);

				} else if (z.getPrecioZonaZona().getClass().equals(ZonaSentado.class)) {
					Entrada e = new EntradaNumerada(Sistema.getInstance().getSistemaContadorEntradas(), this,
							z.getPrecioZonaZona(), z.getPrecioZonaPrecio(), this.getRepresentacionFecha());
					this.entradas.add(e);
					ZonaSentado zs = (ZonaSentado) e.getEntradaZona();
					e.setEntradaButaca(zs.getZonaSentadoButacas().get(butaca));
					butaca++;
				}
				Sistema.getInstance().updateSistemaContadorEntradas(); /*
																		 * Actualizamos el contador que lleva el
																		 * identificador de las entradas
																		 */
			}
		}
	}

	/**
	 * M�todo para generar la lista de espera de una representacion
	 * 
	 */
	public void representacionGenerarListaDeEspera() {
		for (Zona z : this.getRepresentacionEvento().getEventoZonas()) {
			if (z.getClass().equals(ZonaPie.class)) {
				this.listasEspera.add(new ListaEspera(this, z));

			} else if (z.getClass().equals(ZonaSentado.class)) {
				this.listasEspera.add(new ListaEspera(this, z));
			}
		}
	}

	/**
	 * M�todo para eliminar la entrada de una representacion
	 * 
	 * @param entrada entrada a eliminar de la representacion
	 * 
	 */
	public void eliminarRepresentacionEntrada(Entrada entrada) {
		this.entradas.remove(entrada);
	}

	/**
	 * M�todo para comprar unas entradas de una zona que fueron elegidas
	 * anteriormente se comprueba si al menos una se puede comprar con alguno de los
	 * abonos que pueda poseer el usuairo
	 * 
	 * @param entradas   conjunto de entradas a comprar
	 * @param numTarjeta numero de tarjeta para comprar dicha entrada
	 * 
	 * @throws NonExistentFileException      excepcion lanzada cuando no se
	 *                                       encuentra la raiz donde guardar el pdf
	 *                                       de las entradas generadas
	 * @throws UnsupportedImageTypeException excepcion lanzada cuando no se
	 *                                       encuentra la imagen de la entrada
	 * 
	 * @return true si se ha podido comprar y false si no se ha podido comprar o ha
	 *         entrado a la lista de espera
	 */
	public Boolean comprarRepresentacionEntradaTarjeta(ArrayList<Entrada> entradas, String numTarjeta)
			throws NonExistentFileException, UnsupportedImageTypeException {
		
		int cont = 0;
		
		for (Entrada e : entradas) {
			if (e.comprarEntradaTarjeta(numTarjeta)){
				cont++;
			}
		}
		if (cont == entradas.size()) {
			return true;
		}
		return false;
	}

	/**
	 * Metodo para comprar unas entradas de una zona que fueron elegidas
	 * anteriormente que pueda comprar con abono
	 * 
	 * @param entradas conjunto de entradas a comprar
	 * 
	 * @throws NonExistentFileException      excepcion lanzada cuando no se
	 *                                       encuentra la raiz donde guardar el pdf
	 *                                       de las entradas generadas
	 * @throws UnsupportedImageTypeException excepcion lanzada cuando no se
	 *                                       encuentra la imagen de la entrada
	 * 
	 * @return true si se ha podido comprar y false si no se ha podido comprar o ha
	 *         entrado a la lista de espera
	 */
	public  Boolean comprarRepresentacionEntradaAbono(ArrayList<Entrada> entradas)
			throws NonExistentFileException, UnsupportedImageTypeException {
		
		for (Entrada e : entradas) {

			if (Sistema.getInstance().getSistemaUsuarioLogeado()
					.compraUsuarioAbonoCiclo(e)) { /* Comprueba si la puede pagar con un abono */
				e.comprarEntradaAbono();
			} 
		}
		return true;
	}

	/**
	 * Metodo para comprar unas entradas de una zona que fueron elegidas
	 * anteriormente que se puedan comprar con abono
	 * 
	 * @param entradas conjunto de entradas a comprar
	 * @return ArrayList de las entradas que puede comprar
	 */
	public ArrayList<Entrada> representacionEntradaConAbono (ArrayList<Entrada> entradas){
		
		ArrayList<Entrada> entradasAbono = new ArrayList<>();

		if (entradas.size() > getRepresentacionEntradasDisponiblesEnZona(entradas.get(0).getEntradaZona()).size()) {
			return null;
		}
		for (Entrada e : entradas) {

			if (Sistema.getInstance().getSistemaUsuarioLogeado()
					.compraUsuarioAbonoCiclo(e)) { /* Comprueba si la puede pagar con un abono */
				entradasAbono.add(e);
			} 
		}
		return entradasAbono;
	}
	
	/**
	 * M�todo para reservar unas entradas de una zona que fueron elegidas
	 * anteriormente
	 * 
	 * @param entradas conjunto de entradas a comprar
	 * @return true si se ha podido comprar y false si no se ha podido reservar o ha
	 *         entrado a la lista de espera
	 */
	public Boolean reservarRepresentacionEntrada(ArrayList<Entrada> entradas) {

		Boolean entradasReservada = false;

		for (Entrada e : entradas) {

			e.reservarEntrada();
			entradasReservada = true;

		}

		return entradasReservada;
	}

	/**
	 * Metodo encargado de comprar un numero de entradas especificas que ya tiene
	 * reservadas el usuario
	 * 
	 * @param numTarjeta  numero de tarjeta con la que se pagara
	 * @param e 	entrada reservada a comprar 
	 *
	 * @throws NonExistentFileException      excepcion lanzada cuando no se
	 *                                       encuentra la raiz donde guardar el pdf
	 *                                       de las entradas generadas
	 * @throws UnsupportedImageTypeException excepcion lanzada cuando no se
	 *                                       encuentra la imagen de la entrada
	 * 
	 * @return true si se ha podido comprar y false si no se ha podido reservar o ha
	 *         entrado a la lista de espera
	 */
	public Boolean comprarRepresentacionEntradaReservada(String numTarjeta, Entrada e)
			throws NonExistentFileException, UnsupportedImageTypeException {

			Boolean entradasCompradas = false;
			if (Sistema.getInstance().getSistemaUsuarioLogeado()
					.compraUsuarioAbonoCiclo(e)) { /* Comprueba si la puede pagar con un abono */
				e.comprarEntradaAbono();
				entradasCompradas = true;
			} else {
				e.comprarEntradaTarjeta(numTarjeta);
				entradasCompradas = true;
			}

			return entradasCompradas;
			}
	
	/**
	 * Metodo encargado de sugerir una posible eleccion de entradas a
	 * comprar/reservar dependiendo de la zona y del numero de entradas. Estas
	 * entradas se encuentran alejadas
	 * 
	 * @param zona        zona donde se sugeriran las entradas (solo si es una
	 *                    ZonaSentado )
	 * @param numEntradas numero de entradas a elegir
	 * @return un array de entradas sugeridas o null en caso de que no haya entradas
	 *         suficientes a sugerir o no sea una zonaPie
	 */
	
	public ArrayList<Entrada> sugerirRepresentacionEntradasAlejadas(Zona zona, int numEntradas){
		ArrayList<Entrada> entradasSugeridas = new ArrayList<>();
		int posXEntrada = 0;
		int posYEntrada = 0;
		int posXAux = 0;
		int posYAux = 0;
		double Distancia = 0;
		double DistanciaAux = 10000;
		double aux1 = 0;
		Entrada seleccionada = null;
		
		if (numEntradas != 1) {
			return null;
		}
		
		if(zona.getClass().equals(ZonaPie.class)) { 
			return null; 
		}
		
		if(getRepresentacionEntradasDisponiblesEnZona(zona).size() < 0) {
			return null;
		}
		
		for (Entrada e: getRepresentacionEntradasDisponiblesEnZona(zona)) {
			DistanciaAux = 10000;
			aux1 = 0;
			posXEntrada = e.getEntradaButaca().getButacaFila();
			posYEntrada = e.getEntradaButaca().getButacaColumna(); 		
			for (Entrada w: getRepresentacionEntradasOcupadasEnZona(zona)) {
				if (!e.equals(w) && w.getEntradaOcupada()) {
					posXAux = w.getEntradaButaca().getButacaFila();
					posYAux = w.getEntradaButaca().getButacaColumna(); 	
					aux1 = Math.sqrt((posXEntrada - posXAux)*(posXEntrada - posXAux) + (posYEntrada - posYAux)*(posYEntrada - posYAux));
					if (DistanciaAux > aux1) {
						DistanciaAux = aux1;
					}
				}
			

		}	
		if (Distancia < DistanciaAux) {
			Distancia = DistanciaAux;
			seleccionada = e;
		}

		
		}
		entradasSugeridas.add(seleccionada);
		return entradasSugeridas;
	}

	/**
	 * Metodo encargado de sugerir una posible eleccion de entradas a
	 * comprar/reservar dependiendo de la zona y del numero de entradas. Estas
	 * entradas se encuentran juntas en como m�nimos un cuadrado de entradas 2x2,
	 * pero hay mas posibilidades, como por ejemplo, todas en un misma fila
	 * 
	 * @param zona        zona donde se sugeriran las entradas (solo si es una
	 *                    ZonaSentado )
	 * @param numEntradas numero de entradas a elegir
	 * @return un array de entradas sugeridas o null en caso de que no haya entradas
	 *         suficientes a sugerir o no sea una zonaPie
	 */
	public ArrayList<Entrada> sugerirRepresentacionEntradasJuntas(Zona zona, int numEntradas) {

		int numEntradasDisponibles = 0, contadorEntradas = 0;
		ArrayList<Entrada> entradasSugeridas = new ArrayList<>();

		for (int i = 0; i < ((ZonaSentado) zona).getFilasMax(); i++) {

			/*
			 * Si ya se han podido elegir todas las entradas que el usuario queria comprar,
			 * se finalica la b�squeda de entradas a sugerir
			 */
			if (contadorEntradas == numEntradas) {
				break;
			}

			/*
			 * En cada ronda inicialziamos las varaibles de gesti�n de las entradas a
			 * sugerir
			 */
			if (numEntradasDisponibles < numEntradas) {
				contadorEntradas = 0;
				entradasSugeridas.removeAll(entradasSugeridas);
			}

			/* Comprobamos cuantas entradas disponibles hay en la fila correspondiente */
			numEntradasDisponibles = getRepresentacionEntradasDisponiblesFila(zona, i).size();

			if (numEntradasDisponibles > 0) {
				for (Entrada e1 : getRepresentacionEntradasDisponiblesFila(zona, i)) {

					/* A�adimos la primera entrada que servir� de base para elgir al resto */
					if (entradasSugeridas.size() == 0 && contadorEntradas < numEntradas) {
						entradasSugeridas.add(e1);
						contadorEntradas++;
					}

					/*
					 * Si ya se han podido elegir todas las entradas que el usuario queria comprar,
					 * se finalica la b�squeda de entradas a sugerir
					 */
					if (contadorEntradas == numEntradas) {
						break;
					} else {
						/*
						 * Partiendo de la entrada base, se buscar�n huecos, en la misma fila, en la de
						 * detr�s y en la de delante
						 */
						if (getRepresentacionEntradasDisponiblesFilaAPartirDe(zona, i,
								entradasSugeridas.get(0).getEntradaButaca().getButacaColumna())
										.size() > 0) { /* Fila de delante a las primeras elecciones */
							for (Entrada e2 : getRepresentacionEntradasDisponiblesFilaAPartirDe(zona, i,
									entradasSugeridas.get(0).getEntradaButaca().getButacaColumna())) {
								if (contadorEntradas < numEntradas && !entradasSugeridas.contains(e2)) {
									entradasSugeridas.add(e2);
									contadorEntradas++;
								}
							}
						}

						if (getRepresentacionEntradasDisponiblesFilaAPartirDe(zona, i - 1,
								entradasSugeridas.get(0).getEntradaButaca().getButacaColumna())
										.size() > 0) { /* Fila de delante a las primeras elecciones */
							for (Entrada e3 : getRepresentacionEntradasDisponiblesFilaAPartirDe(zona, i - 1,
									entradasSugeridas.get(0).getEntradaButaca().getButacaColumna())) {
								if (contadorEntradas < numEntradas & !entradasSugeridas.contains(e3)) {
									entradasSugeridas.add(e3);
									contadorEntradas++;
								}
							}
						}

						if (getRepresentacionEntradasDisponiblesFilaAPartirDe(zona, i + 1,
								entradasSugeridas.get(0).getEntradaButaca().getButacaColumna())
										.size() > 0) { /* Fila de detas a las primeras elecciones */
							for (Entrada e4 : getRepresentacionEntradasDisponiblesFilaAPartirDe(zona, i + 1,
									entradasSugeridas.get(0).getEntradaButaca().getButacaColumna())) {
								if (contadorEntradas < numEntradas && !entradasSugeridas.contains(e4)) {
									entradasSugeridas.add(e4);
									contadorEntradas++;
								}
							}
						}
					}
				}
			}
		}

		/*
		 * Si se han podido sugerir el mismo n�mero de entradas que el usuario quer�a
		 * comprar, entonces se devuelve la sugerenica de entradas
		 */
		if (numEntradas == contadorEntradas) {
			return entradasSugeridas;
		}

		return null; /*
						 * Si no ha podido sugerir para todas las entradas que se quer�an, entonces no
						 * se efectua la sugerencia
						 */
	}
	
	/**
	 * Metodo encargado de sugerir una posible eleccion de entradas a
	 * comprar/reservar dependiendo de la zona y del numero de entradas. Se pueden
	 * elegir sin gente alrededor, entradas juntas, en las filas de delante o de
	 * detras.
	 * 
	 * @param zona        zona donde se sugeriran las entradas (solo si es una
	 *                    ZonaSentado )
	 * @param numEntradas numero de entradas a elegir
	 * @param preferencia	preferencia que quiere que el sistema sugiera entradas a partir de ella
	 * @return un array de entradas sugeridas o null en caso de que no haya entradas
	 *         suficientes a sugerir o no sea una zonaPie
	 */
	public ArrayList<Entrada> sugerirRepresentacionEntradas(String preferencia, Zona zona, int numEntradas) {
		int numFilasEnZona = ((ZonaSentado) zona).getFilasMax();
		int numFilasSubZona = 0;
		int i, j;

		/* Si la zona no es una zona sentado no se puede sugerir */
		if (!zona.getClass().equals(ZonaSentado.class)) {
			return null;
		}

		/* Si no hay entradas suficientes no se puede sugerir */
		if (getRepresentacionEntradasDisponiblesEnZona(zona).size() < numEntradas) {
			return null;
		}

		/* Comprobamos la preferencia del usuario */
		if (preferencia.equals("Juntas")) {
			return sugerirRepresentacionEntradasJuntas(zona, numEntradas); 
		} else if (preferencia.equals("Alejadas")) {
			return sugerirRepresentacionEntradasAlejadas(zona, numEntradas);
		} else {
			/* En caso de escoger la preferenica de primera o ultima fila */
			/*
			 * Establecemos que seran para nosotros el numero de filas que estan dentra de
			 * la subzona delante/detras
			 */
			if (numFilasEnZona >= 3 && numFilasEnZona < 6) {
				numFilasSubZona = 1;
			} else if (numFilasEnZona >= 6) {
				numFilasSubZona = 2;
			}
			
			ArrayList <Entrada> entradasSugeridas = new ArrayList<>();
			/*
			 * La idea de la sugerencia delante o detras es la misma, por lo que
			 * explicaremos solo una
			 */
			if (preferencia.equals("Al Frente")) {
				for (i = 0; i < numFilasSubZona; i++) { /*
														 * Recorremos el numeros de filas que se admiten en la subzona
														 * delante
														 */
					if (getRepresentacionEntradasDisponiblesFila(zona, i)
							.size() < numEntradas && i == (numFilasSubZona - 1)) { /* Si no hay entradas suficiente no se podran sugerir entradas */
						return null;
					} else {
						/*
						 * Para la fila X buscamos a partir de cada columna, cuantas entradas libreas
						 * hay seguidas, de modo que se suigeran un numero de entradas igual o superir
						 * al que quiera comprar el usuario
						 */
						for (j = 0; j < ((ZonaSentado) zona).getColumnasMax(); j++) {
							if (getRepresentacionEntradasDisponiblesFilaAPartirDe(zona, i, j).size() >= numEntradas) {
								for(int k = 0; k < numEntradas; k++) {
									entradasSugeridas.add(getRepresentacionEntradasDisponiblesFilaAPartirDe(zona, numFilasEnZona - (i + 1),
									j).get(k));
									}
									return entradasSugeridas;
							}
						}
					}
				}
			} else if (preferencia.equals("Detras")) {
				for (i = 0; i < numFilasSubZona; i++) {
					if (getRepresentacionEntradasDisponiblesFila(zona, numFilasEnZona - (i + 1)).size() < numEntradas
							&& i == (numFilasSubZona - 1)) {
						return null;
					} else {
						for (j = 0; j < ((ZonaSentado) zona).getColumnasMax(); j++) {
							if (getRepresentacionEntradasDisponiblesFilaAPartirDe(zona, numFilasEnZona - (i + 1), j)
									.size() >= numEntradas) {
								for(int k = 0; k < numEntradas; k++) {
									entradasSugeridas.add(getRepresentacionEntradasDisponiblesFilaAPartirDe(zona, numFilasEnZona - (i + 1),
									j).get(k));
									}
									return entradasSugeridas;
							}
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * M�todo para cancelar una entrada de una representacion
	 * 
	 * @param entrada entrada a cancelar
	 * @return true si se ha podido cancelar correctamente o false en caso contrario
	 */
	public Boolean cancelarRepresentacionEntrada(Entrada entrada) {
		for (Entrada e : this.getRepresentacionEntradasReservadasEnZona(entrada.getEntradaZona())) {
			if (e.equals(entrada)) {
				entrada.cancelarEntradaReservada();
			}
			return true;
		}
		return false;
	}

	/**
	 * M�todo para avisar a los usuarios que han reservado o comprado entradas
	 * de que la representacion ha sido cancelada
	 * 
	 */
	public void represetacionNotificarCancelacion() {

		/* Notifiacamos a los usaurios que compraron o reservaron alguna entrada */
		for (Entrada e : getRepresentacionEntradas()) {
			if (e.getEntradaUsuarioCompra() != null) {
				/* Creamos la entrada correspondiente a la cancelacion de la representacion */
				Notificacion notificacion = new Notificacion(Sistema.getInstance().getContadorNotificaciones(), 
						"Representacion del evento " + e.getEventName() + " cancelada",
						"Estimado " + e.getEntradaUsuarioCompra().getUsuarioNombre() + "la representacion del evento "
								+ this.getRepresentacionEvento().getEventoTitulo()
								+ "ha sido cancelada, sentimos las molestias", e.getEntradaUsuarioCompra());
				notificacion.notificarUsuario();
				Sistema.getInstance().setContadorNotificaciones();
			}

			if (e.getEntradaUsuarioReserva() != null) {
				/* Creamos la entrada correspondiente a la cancelacion de la representacion */
				Notificacion notificacion = new Notificacion(Sistema.getInstance().getContadorNotificaciones(),
						"Representacion del evento " + e.getEventName() + " cancelada",
						"Estimado " + e.getEntradaUsuarioCompra().getUsuarioNombre() + "la representacion del evento "
								+ this.getRepresentacionEvento().getEventoTitulo()
								+ "ha sido cancelada, sentimos las molestias", e.getEntradaUsuarioReserva());
				notificacion.notificarUsuario();
				Sistema.getInstance().setContadorNotificaciones();
			}
		}

	}

	/**
	 * M�todo para avisar a los usuarios que han reservado o comprado entradas
	 * de que la representacion ha sido pospuesta
	 * 
	 */
	public void represetacionNotificarPospuesta() {

		/* Notifiacamos a los usaurios que compraron o reservaron alguna entrada */
		for (Entrada e : getRepresentacionEntradas()) {
			if (e.getEntradaUsuarioCompra() != null) {
				/* Creamos la entrada correspondiente a la cancelacion de la representacion */
				Notificacion notificacion = new Notificacion(Sistema.getInstance().getContadorNotificaciones(),
						"Representacion del evento " + e.getEventName() + " ha sido pospuesta",
						"Estimado " + e.getEntradaUsuarioCompra().getUsuarioNombre() + "la representacion del evento "
								+ this.getRepresentacionEvento().getEventoTitulo() + "ha sido pospuesta para el "
								+ this.getRepresentacionFecha() + ", sentimos las molestias", e.getEntradaUsuarioCompra());
				notificacion.notificarUsuario();
				Sistema.getInstance().setContadorNotificaciones();
			}

			if (e.getEntradaUsuarioReserva() != null) {
				/* Creamos la entrada correspondiente a la cancelacion de la representacion */
				Notificacion notificacion = new Notificacion(Sistema.getInstance().getContadorNotificaciones(),
						"Representacion del evento " + e.getEventName() + " cancelada",
						"Estimado " + e.getEntradaUsuarioCompra().getUsuarioNombre() + "la representacion del evento "
								+ this.getRepresentacionEvento().getEventoTitulo() + "ha sido pospuesta para el "
								+ this.getRepresentacionFecha() + ", sentimos las molestias", e.getEntradaUsuarioReserva());
				notificacion.notificarUsuario();
				Sistema.getInstance().setContadorNotificaciones();
			}
		}

	}

	/*** GETTERS ***/

	/**
	 * Metodo para obtener la fecha de la representacion
	 * 
	 * @return fecha de la representacion
	 */
	public LocalDateTime getRepresentacionFecha() {
		return fecha;
	}

	/**
	 * Metodo para obtener el evento al que pertenece dicha representacion
	 * 
	 * @return Evento al que pertenece
	 */
	public Evento getRepresentacionEvento() {
		return evento;
	}

	/**
	 * Metodo para obtener el aforo ocupado en dicha representacion
	 * 
	 * @return aforo ocupado en dicha representacion
	 */
	public int getRepresentacionAforoOcupado() {
		return aforoOcupado;
	}

	/**
	 * Metodo para obtener el aforo disponible en dicha representacion
	 * 
	 * @return aforo disponible en dicha representacion
	 */
	public int getRepresentacionAforoDisponible() {
		return aforoDisponible;
	}

	/**
	 * Metodo para obtener el aforo total en dicha representacion
	 * 
	 * @return aforo total en dicha representacion
	 */
	public int getRepresentacionAforoTotal() {
		return aforoTotal;
	}

	/**
	 * Metodo para obtener el dinero recaudado en dicha representacion
	 * 
	 * @return dinero recaudado para dicha representacion
	 */
	public ArrayList<Double> getRepresentacionDineroRecaudado() {
		return dineroRecaudadoZona;
	}

	/**
	 * Metodo para obtener la lista de espera de dicha representacion
	 * 
	 * @return ArrayList de lista de espera
	 */
	public ArrayList<ListaEspera> getRepresentacionListaEspera() {
		return listasEspera;
	}

	/**
	 * Metodo para obtener las entradas de dicha representacion
	 * 
	 * @return ArrayList de entradas
	 */
	public ArrayList<Entrada> getRepresentacionEntradas() {
		return this.entradas;
	}

	/**
	 * Metodo para obtener las entradas de una zona en dicha representacion
	 * 
	 * @param zona zona de la que se quiere obtener las entradas
	 * @return ArrayList de entradas de una zona
	 */
	public ArrayList<Entrada> getRepresentacionEntradasEnZona(Zona zona) {
		ArrayList<Entrada> entradasZona = new ArrayList<>();

		if (this.getRepresentacionEvento().getEventoZonas().contains(zona)) {
			for (Entrada e : this.getRepresentacionEntradas()) {
				if (e.getEntradaZona().equals(zona)) {
					entradasZona.add(e);
				}
			}
		}
		return entradasZona;
	}

	/**
	 * Metodo para obtener las entradas ocupadas en una zona en dicha
	 * representacion, siendpo estas o vendidas o reservadas
	 * 
	 * @param zona zona de la que se quiere obtener las entradas ocupadas
	 * @return ArrayList de entradas ocupadas
	 */
	public ArrayList<Entrada> getRepresentacionEntradasOcupadasEnZona(Zona zona) {
		ArrayList<Entrada> entradasOcupadas = new ArrayList<>();

		if (this.getRepresentacionEvento().getEventoZonas().contains(zona)) {
			for (Entrada e : this.getRepresentacionEntradas()) {
				if (e.getEntradaOcupada() && e.getEntradaZona().equals(zona)) {
					entradasOcupadas.add(e);
				}
			}
		}
		return entradasOcupadas;
	}

	/**
	 * Metodo para obtener las entradas vendidas en una zona en dicha representacion
	 * 
	 * @param zona zona de la que se quiere obtener las entradas vendidas
	 * @return ArrayList de entradas vendidas
	 */
	public ArrayList<Entrada> getRepresentacionEntradasVendidasEnZona(Zona zona) {
		ArrayList<Entrada> entradasVendidasZona = new ArrayList<>();

		if (this.getRepresentacionEvento().getEventoZonas().contains(zona)) {
			for (Entrada e : this.getRepresentacionEntradas()) {
				if (e.getEntradaZona().equals(zona) && e.getEntradaOcupada() && e.getValidezEntrada()
						&& e.getEntradaHabilitada()) {
					entradasVendidasZona.add(e);
				}
			}
		}
		return entradasVendidasZona;
	}
	
	/**
	 * Metodo para obtener una entrada a partir de una zona y un identificador
	 * 
	 * @param zona zona de la que se quiere obtener las entradas vendidas
	 * @param identificador	 int con el identificador
	 * @return Entrada encontrada o null
	 */
	public Entrada getRepresentacionEntradaAPartirDeUnaZonaYUnIdentificador(Zona zona, int identificador) {
		ArrayList <Entrada> entradasZona = getRepresentacionEntradasEnZona(zona);
		
		for (Entrada e : entradasZona) {
			if (e.getCodigoValidacion() == identificador) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Metodo para obtener las entradas reservadas en una zona en dicha
	 * representacion
	 * 
	 * @param zona zona de la que se quiere obtener las entradas reservadas
	 * @return ArrayList de entradas reservadas
	 */
	public ArrayList<Entrada> getRepresentacionEntradasReservadasEnZona(Zona zona) {
		ArrayList<Entrada> entradasReservadasZona = new ArrayList<>();

		if (this.getRepresentacionEvento().getEventoZonas().contains(zona)) {
			for (Entrada e : this.getRepresentacionEntradas()) {
				if (e.getEntradaZona().equals(zona) && e.getEntradaOcupada() && !e.getValidezEntrada()
						&& e.getEntradaHabilitada()) {
					entradasReservadasZona.add(e);
				}
			}
		}
		return entradasReservadasZona;
	}

	/**
	 * Metodo para obtener las entradas disponibles en una zona en dicha
	 * representacion
	 * 
	 * @param zona zona de la que se quiere obtener las entradas disponibles
	 * @return ArrayList de entradas disponibles
	 */
	public ArrayList<Entrada> getRepresentacionEntradasDisponiblesEnZona(Zona zona) {
		ArrayList<Entrada> entradasDisponiblesZona = new ArrayList<>();

		if (this.getRepresentacionEvento().getEventoZonas().contains(zona)) {
			for (Entrada e : this.getRepresentacionEntradas()) {
				if (e.getEntradaZona().equals(zona) && !e.getEntradaOcupada() && e.getEntradaHabilitada()) {
					if (zona.getClass().equals(ZonaSentado.class)) {
						if (e.getEntradaButaca().isButacaHabilitada()) {
							entradasDisponiblesZona.add(e);
						}
					} else {
						entradasDisponiblesZona.add(e);
					}
				}
			}
		}
		return entradasDisponiblesZona;
	}

	/**
	 * Metodo para obtener las entradas disponibles para comprar en una zona a
	 * partir del numero de entradas pasadas por parametros
	 * 
	 * @param zona zona de la que se quiere obtener las entradas disponibles
	 * @param num  numero de entradas a comprar
	 * @return ArrayList de entradas a comprar
	 */
	public ArrayList<Entrada> getRepresentacionEntradasSobreUnNumero(Zona zona, Integer num) {
		ArrayList<Entrada> entradasDisponiblesZona = getRepresentacionEntradasDisponiblesEnZona(zona);
		ArrayList<Entrada> entradasAComprar = new ArrayList<Entrada>();

		if (entradasDisponiblesZona.size() < num) {
			return null;
		}
		for (int i = 0; i < num; i++) {
			entradasAComprar.add(entradasDisponiblesZona.get(i));
		}
		return entradasAComprar;
	}

	/**
	 * Metodo para obtener las entradas deshabilitadas de un teatro(Se usa para
	 * cuando se deshabilitan butacas)
	 * 
	 * @param zona zona de la que se quiere obtener las entradas deshabilitadas
	 * @return ArrayList de entradas deshabilitadas
	 */
	public ArrayList<Entrada> getRepresentacionEntradasDeshabilitadasEnZona(Zona zona) {
		ArrayList<Entrada> entradasDeshabilitadasZona = new ArrayList<>();

		if (this.getRepresentacionEvento().getEventoZonas().contains(zona)
				&& zona.getClass().equals(ZonaSentado.class)) {
			for (Entrada e : this.getRepresentacionEntradas()) {
				if (e.getEntradaZona().equals(zona) && !e.getEntradaHabilitada()) {
					entradasDeshabilitadasZona.add(e);
				}
			}
		}
		return entradasDeshabilitadasZona;
	}

	/**
	 * Metodo para obtener las entradas compradas en una fila para una zona sentado
	 * 
	 * @param zona zona de la que se quiere obtener las entradas disponibles
	 * @param fila de la cual se quieren obtener las entradas
	 * @return ArrayList de entradas vendidas en dicha fila
	 */
	public ArrayList<Entrada> getRepresentacionEntradasVendidasFila(Zona zona, int fila) {
		ArrayList<Entrada> entradasVendidas = new ArrayList<>();

		if (this.getRepresentacionEvento().getEventoZonas().contains(zona)
				&& zona.getClass().equals(ZonaSentado.class)) {
			for (Entrada e : this.getRepresentacionEntradasVendidasEnZona(zona)) {
				if (e.getEntradaButaca().getButacaFila() == fila) {
					entradasVendidas.add(e);
				}
			}
		}
		return entradasVendidas;
	}

	/**
	 * Metodo para obtener las entradas disponibles en una fila para una zona
	 * sentado
	 * 
	 * @param zona zona de la que se quiere obtener las entradas disponibles
	 * @param fila fila de la cual se quieren obtener las entradas
	 * @return ArrayList de entradas disponibles en dicha fila
	 */
	public ArrayList<Entrada> getRepresentacionEntradasDisponiblesFila(Zona zona, int fila) {
		ArrayList<Entrada> entradasDisponibles = new ArrayList<>();

		if (this.getRepresentacionEvento().getEventoZonas().contains(zona)
				&& zona.getClass().equals(ZonaSentado.class)) {
			for (Entrada e : this.getRepresentacionEntradasDisponiblesEnZona(zona)) {
				if (e.getEntradaButaca().getButacaFila() == fila && e.getEntradaButaca().isButacaHabilitada()) {
					entradasDisponibles.add(e);
				}
			}
		}
		return entradasDisponibles;
	}

	/**
	 * Metodo para obtener las entradas disponibles en una fila y a partir de una
	 * columna cierta columna para una zona sentado
	 * 
	 * @param zona    zona de la que se quiere obtener las entradas disponibles
	 * @param fila    fila de la cual se quieren obtener las entradas
	 * @param columna columna de la cual se quieren obtener las entradas
	 * @return ArrayList de entradas disponibles en dicha fila
	 */
	public ArrayList<Entrada> getRepresentacionEntradasDisponiblesFilaAPartirDe(Zona zona, int fila, int columna) {
		ArrayList<Entrada> entradasDisponibles = new ArrayList<>();
		int i = columna;

		if (this.getRepresentacionEvento().getEventoZonas().contains(zona)
				&& zona.getClass().equals(ZonaSentado.class)) {
			for (Entrada e : this.getRepresentacionEntradasDisponiblesEnZona(zona)) {
				if (e.getEntradaButaca().getButacaFila() == fila && e.getEntradaButaca().getButacaColumna() >= columna
						&& ((i + 1) == e.getEntradaButaca().getButacaColumna()
								|| i == e.getEntradaButaca().getButacaColumna())) {
					entradasDisponibles.add(e);
					i = e.getEntradaButaca().getButacaColumna();
				}
			}
		}
		return entradasDisponibles;
	}

	/**
	 * Metodo para obtener las entradas reservadas en una fila para una zona sentado
	 * 
	 * @param zona zona de la que se quiere obtener las entradas disponibles
	 * @param fila fila de la cual se quieren obtener las entradas
	 * @return ArrayList de entradas reservadas en dicha fila
	 */
	public ArrayList<Entrada> getRepresentacionEntradasReservadasFila(Zona zona, int fila) {
		ArrayList<Entrada> entradasReservadas = new ArrayList<>();

		if (this.getRepresentacionEvento().getEventoZonas().contains(zona)
				&& zona.getClass().equals(ZonaSentado.class)) {
			for (Entrada e : this.getRepresentacionEntradasReservadasEnZona(zona)) {
				if (e.getEntradaButaca().getButacaFila() == fila) {
					entradasReservadas.add(e);
				}
			}
		}
		return entradasReservadas;
	}

	/**
	 * Metodo para obtener la lista de espera de una zona
	 * 
	 * @param zona zona de la que se quiere obtener la lista de espera
	 * @return Lista de Espera de dicha zona
	 */
	public ListaEspera getRepresentacionListaDeEsperaEnZona(Zona zona) {

		if (this.getRepresentacionEvento().getEventoZonas().contains(zona)) {
			for (ListaEspera l : this.getRepresentacionListaEspera()) {
				if (l.getListaEsperaZona().equals(zona)) {
					return l;
				}
			}
		}
		return null;
	}

	/*** SETTERS ***/

	/**
	 * Metodo para establecer la fecha de una representacion
	 * 
	 * @param fecha de la representacion
	 */
	public void setRepresentacionFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	/**
	 * Metodo para establecer el aforo ocupado de una representacion
	 * 
	 * @param aforoOcupado ocupado de una representacion
	 */
	public void setRepresentacionAforoOcupado(int aforoOcupado) {
		this.aforoOcupado = aforoOcupado;
	}

	/**
	 * Metodo para establecer el aforo disponible de una representacion
	 * 
	 * @param aforoDisponible disponible de una representacion
	 */
	public void setRepresentacionAforoDisponible(int aforoDisponible) {
		this.aforoDisponible = aforoDisponible;
	}

	/**
	 * Metodo para establecer el aforo total de una representacion
	 * 
	 * @param aforoTotal total de una representacion
	 */
	public void setRepresentacionAforoTotal(int aforoTotal) {
		this.aforoTotal = aforoTotal;
	}

	public String getRepresentacionTipoEntrada(Zona z) {
		if (z.getClass().equals(ZonaPie.class)) {
			return "Zona de pie";
		} else if (z.getClass().equals(ZonaSentado.class)) {
			return "Zona sentado";
		}
		return "Zona compuesta";
	}
}
