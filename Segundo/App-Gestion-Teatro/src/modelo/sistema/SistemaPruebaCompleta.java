package modelo.sistema;

import java.time.LocalDateTime;
import java.util.ArrayList;

import modelo.abono.*;
import modelo.entrada.Entrada;
import modelo.evento.*;
import modelo.zona.*;
import es.uam.eps.padsof.tickets.NonExistentFileException;
import es.uam.eps.padsof.tickets.UnsupportedImageTypeException;

/**
 * Main de prueba de funcionalidad completa de la aplicacion
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class SistemaPruebaCompleta {

	private static boolean flagPrueba;

	/**
	 * Metodo para obtener el estado del falg prueba que se utiliza en el main
	 *
	 * @return flagPrueba
	 */
	public static boolean isFlagPrueba() {
		return flagPrueba;
	}

	public static void main(String[] args) throws NonExistentFileException, UnsupportedImageTypeException {

		/****************************************************************************************************************************************************************************/
		/**
		 * ENTRA EL ADMINISTRADOR CON LA CLAVE Y LA CONTRASE�A DE DESARROLLADOR PARA
		 * A�ADIR LO QUE DE MOMENTO PERMITIRA EL SISTEMA
		 **/
		Sistema.getInstance().sistemaIniciarSesion("Administrador", "teatro");

		/** genera las zonas del teatro **/
		Sistema.getInstance().sistemaCrearZonaTeatro(new ZonaSentado(1, 2, 10, "Zona A"));
		Sistema.getInstance().sistemaCrearZonaTeatro(new ZonaPie(2, 20, "Zona B"));
		Sistema.getInstance().sistemaCrearZonaTeatro(new ZonaSentado(3, 10, 2, "Zona C"));
		Sistema.getInstance().sistemaCrearZonaTeatro(new ZonaPie(4, 12, "Zona D"));
		Sistema.getInstance().sistemaCrearZonaTeatro(new ZonaSentado(5, 2, 3, "Zona E"));
		Sistema.getInstance().sistemaCrearZonaTeatro(new ZonaPie(6, 10, "Zona F"));
		Sistema.getInstance().sistemaCrearZonaTeatro(new ZonaSentado(7, 12, 2, "Zona G"));
		Sistema.getInstance().sistemaCrearZonaTeatro(new ZonaPie(8, 30, "Zona H"));

		Sistema.getInstance().sistemaCerrarSesion();

		/** crea los eventos del teatro **/
		Sistema.getInstance().sistemaCrearEvento(new EventoDanza(25, "El baile de los cisnes", "Hola mundo",  "Carlos guemes", "nombres", "Pedro Fernandez", "Pepe", "Carlos guemes"));
		Sistema.getInstance().sistemaCrearEvento(new EventoDanza(40, "The Last Dance", "Nada","Julio Iglesias", "nombres", "Carlos Perez", "Pedro", "Julio Iglesias"));
		Sistema.getInstance().sistemaCrearEvento(new EventoConcierto(20, "La novena sinfonia", "Hola mundo", "Carlos wuemes", "nombres", "pieza", "La novena", "Carlos guemes"));
		Sistema.getInstance().sistemaCrearEvento(new EventoConcierto(15, "Concierto Real", "Nada", "Julio Aglesias", "nombres", "pieza", "La novena", "Julio Iglesias"));
		Sistema.getInstance().sistemaCrearEvento(new EventoTeatro(17, "EL Lazarillo", "Hola mundo", "Carlos guemes", "Carlos guemes", "nombres"));
		Sistema.getInstance().sistemaCrearEvento(new EventoTeatro(25, "El Duque", "Nada", "Julio Iglesias", "Julio Iglesias", "nombres"));
		Sistema.getInstance().sistemaCrearEvento(new EventoTeatro(17, "Holmes", "", "Carlos guemes", "Carlos guemes", "nombres"));
		
		/** se establece el precio que tendra cada zona de cada evento del teatro **/
		Sistema.getInstance().getSistemaEventos().get(0)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(0),
						Sistema.getInstance().getSistemaEventos().get(0).getEventoZonas().get(0), 15));
		Sistema.getInstance().getSistemaEventos().get(0)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(0),
						Sistema.getInstance().getSistemaEventos().get(0).getEventoZonas().get(1), 30));
		Sistema.getInstance().getSistemaEventos().get(0)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(0),
						Sistema.getInstance().getSistemaEventos().get(0).getEventoZonas().get(2), 25));
		Sistema.getInstance().getSistemaEventos().get(0)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(0),
						Sistema.getInstance().getSistemaEventos().get(0).getEventoZonas().get(3), 20));
		Sistema.getInstance().getSistemaEventos().get(0)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(0),
						Sistema.getInstance().getSistemaEventos().get(0).getEventoZonas().get(4), 15));
		Sistema.getInstance().getSistemaEventos().get(0)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(0),
						Sistema.getInstance().getSistemaEventos().get(0).getEventoZonas().get(5), 30));

		Sistema.getInstance().getSistemaEventos().get(1)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(1),
						Sistema.getInstance().getSistemaEventos().get(1).getEventoZonas().get(0), 15));
		Sistema.getInstance().getSistemaEventos().get(1)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(1),
						Sistema.getInstance().getSistemaEventos().get(1).getEventoZonas().get(1), 30));
		Sistema.getInstance().getSistemaEventos().get(1)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(1),
						Sistema.getInstance().getSistemaEventos().get(1).getEventoZonas().get(2), 25));
		Sistema.getInstance().getSistemaEventos().get(1)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(1),
						Sistema.getInstance().getSistemaEventos().get(1).getEventoZonas().get(3), 20));
		Sistema.getInstance().getSistemaEventos().get(1)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(1),
						Sistema.getInstance().getSistemaEventos().get(1).getEventoZonas().get(4), 15));
		Sistema.getInstance().getSistemaEventos().get(1)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(1),
						Sistema.getInstance().getSistemaEventos().get(1).getEventoZonas().get(5), 30));


		Sistema.getInstance().getSistemaEventos().get(2)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(2),
						Sistema.getInstance().getSistemaEventos().get(2).getEventoZonas().get(0), 15));
		Sistema.getInstance().getSistemaEventos().get(2)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(2),
						Sistema.getInstance().getSistemaEventos().get(2).getEventoZonas().get(1), 30));
		Sistema.getInstance().getSistemaEventos().get(2)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(2),
						Sistema.getInstance().getSistemaEventos().get(2).getEventoZonas().get(2), 25));
		Sistema.getInstance().getSistemaEventos().get(2)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(2),
						Sistema.getInstance().getSistemaEventos().get(2).getEventoZonas().get(3), 20));
		Sistema.getInstance().getSistemaEventos().get(2)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(2),
						Sistema.getInstance().getSistemaEventos().get(2).getEventoZonas().get(4), 15));
		Sistema.getInstance().getSistemaEventos().get(2)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(2),
						Sistema.getInstance().getSistemaEventos().get(2).getEventoZonas().get(5), 30));

		Sistema.getInstance().getSistemaEventos().get(3)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(3),
						Sistema.getInstance().getSistemaEventos().get(3).getEventoZonas().get(0), 15));
		Sistema.getInstance().getSistemaEventos().get(3)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(3),
						Sistema.getInstance().getSistemaEventos().get(3).getEventoZonas().get(1), 30));
		Sistema.getInstance().getSistemaEventos().get(3)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(3),
						Sistema.getInstance().getSistemaEventos().get(3).getEventoZonas().get(2), 25));
		Sistema.getInstance().getSistemaEventos().get(3)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(3),
						Sistema.getInstance().getSistemaEventos().get(3).getEventoZonas().get(3), 20));
		Sistema.getInstance().getSistemaEventos().get(3)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(3),
						Sistema.getInstance().getSistemaEventos().get(3).getEventoZonas().get(4), 15));
		Sistema.getInstance().getSistemaEventos().get(3)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(3),
						Sistema.getInstance().getSistemaEventos().get(3).getEventoZonas().get(5), 30));

		Sistema.getInstance().getSistemaEventos().get(4)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(4),
						Sistema.getInstance().getSistemaEventos().get(4).getEventoZonas().get(0), 15));
		Sistema.getInstance().getSistemaEventos().get(4)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(4),
						Sistema.getInstance().getSistemaEventos().get(4).getEventoZonas().get(1), 30));
		Sistema.getInstance().getSistemaEventos().get(4)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(4),
						Sistema.getInstance().getSistemaEventos().get(4).getEventoZonas().get(2), 25));
		Sistema.getInstance().getSistemaEventos().get(4)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(4),
						Sistema.getInstance().getSistemaEventos().get(4).getEventoZonas().get(3), 20));
		Sistema.getInstance().getSistemaEventos().get(4)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(4),
						Sistema.getInstance().getSistemaEventos().get(4).getEventoZonas().get(4), 15));
		Sistema.getInstance().getSistemaEventos().get(4)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(4),
						Sistema.getInstance().getSistemaEventos().get(4).getEventoZonas().get(5), 30));

		Sistema.getInstance().getSistemaEventos().get(5)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(5),
						Sistema.getInstance().getSistemaEventos().get(5).getEventoZonas().get(0), 15));
		Sistema.getInstance().getSistemaEventos().get(5)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(5),
						Sistema.getInstance().getSistemaEventos().get(5).getEventoZonas().get(1), 30));
		Sistema.getInstance().getSistemaEventos().get(5)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(5),
						Sistema.getInstance().getSistemaEventos().get(5).getEventoZonas().get(2), 25));
		Sistema.getInstance().getSistemaEventos().get(5)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(5),
						Sistema.getInstance().getSistemaEventos().get(5).getEventoZonas().get(3), 20));
		Sistema.getInstance().getSistemaEventos().get(5)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(5),
						Sistema.getInstance().getSistemaEventos().get(5).getEventoZonas().get(4), 15));
		Sistema.getInstance().getSistemaEventos().get(5)
				.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(5),
						Sistema.getInstance().getSistemaEventos().get(5).getEventoZonas().get(5), 30));

		Sistema.getInstance().getSistemaEventos().get(6)
		.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(6),
				Sistema.getInstance().getSistemaEventos().get(6).getEventoZonas().get(0), 15));
Sistema.getInstance().getSistemaEventos().get(6)
		.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(6),
				Sistema.getInstance().getSistemaEventos().get(6).getEventoZonas().get(1), 30));
Sistema.getInstance().getSistemaEventos().get(6)
		.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(6),
				Sistema.getInstance().getSistemaEventos().get(6).getEventoZonas().get(2), 25));
Sistema.getInstance().getSistemaEventos().get(6)
		.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(6),
				Sistema.getInstance().getSistemaEventos().get(6).getEventoZonas().get(3), 20));
Sistema.getInstance().getSistemaEventos().get(6)
		.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(6),
				Sistema.getInstance().getSistemaEventos().get(6).getEventoZonas().get(4), 15));
Sistema.getInstance().getSistemaEventos().get(6)
		.addEventoPrecioZona(new PrecioZona(Sistema.getInstance().getSistemaEventos().get(6),
				Sistema.getInstance().getSistemaEventos().get(6).getEventoZonas().get(6), 30));


		/** se crean distintas representaciones para cada evento del teatro **/
		Sistema.getInstance().getSistemaEventos().get(0).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(0), LocalDateTime.of(2021, 4, 29, 10, 20)));
		Sistema.getInstance().getSistemaEventos().get(0).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(0), LocalDateTime.of(2021, 5, 5, 11, 10)));
		Sistema.getInstance().getSistemaEventos().get(0).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(0), LocalDateTime.of(2021, 6, 12, 11, 00)));
		Sistema.getInstance().getSistemaEventos().get(0).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(0), LocalDateTime.of(2021, 6, 13, 10, 10)));
		Sistema.getInstance().getSistemaEventos().get(0).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(0), LocalDateTime.of(2021, 6, 15, 11, 10)));

		Sistema.getInstance().getSistemaEventos().get(1).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(1), LocalDateTime.of(2021, 5, 1, 10, 30)));
		Sistema.getInstance().getSistemaEventos().get(1).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(1), LocalDateTime.of(2021, 5, 4, 11, 30)));
		Sistema.getInstance().getSistemaEventos().get(1).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(1), LocalDateTime.of(2021, 5, 6, 10, 00)));
		Sistema.getInstance().getSistemaEventos().get(1).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(1), LocalDateTime.of(2021, 6, 11, 11, 00)));
		Sistema.getInstance().getSistemaEventos().get(1).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(1), LocalDateTime.of(2021, 6, 15, 12, 00)));

		Sistema.getInstance().getSistemaEventos().get(2).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(2), LocalDateTime.of(2021, 5, 12, 9, 00)));
		Sistema.getInstance().getSistemaEventos().get(2).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(2), LocalDateTime.of(2021, 6, 10, 9, 30)));
		Sistema.getInstance().getSistemaEventos().get(2).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(2), LocalDateTime.of(2021, 7, 12, 10, 00)));
		Sistema.getInstance().getSistemaEventos().get(2).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(2), LocalDateTime.of(2021, 7, 17, 9, 15)));
		Sistema.getInstance().getSistemaEventos().get(2).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(2), LocalDateTime.of(2021, 8, 1, 10, 00)));

		Sistema.getInstance().getSistemaEventos().get(3).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(3), LocalDateTime.of(2021, 8, 15, 13, 00)));
		Sistema.getInstance().getSistemaEventos().get(3).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(3), LocalDateTime.of(2021, 8, 19, 12, 10)));
		Sistema.getInstance().getSistemaEventos().get(3).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(3), LocalDateTime.of(2021, 9, 1, 13, 30)));
		Sistema.getInstance().getSistemaEventos().get(3).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(3), LocalDateTime.of(2021, 9, 8, 12, 20)));
		Sistema.getInstance().getSistemaEventos().get(3).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(3), LocalDateTime.of(2021, 9, 12, 13, 10)));

		Sistema.getInstance().getSistemaEventos().get(4).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(4), LocalDateTime.of(2021, 9, 10, 9, 10)));
		Sistema.getInstance().getSistemaEventos().get(4).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(4), LocalDateTime.of(2021, 10, 2, 13, 00)));
		Sistema.getInstance().getSistemaEventos().get(4).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(4), LocalDateTime.of(2021, 10, 15, 9, 20)));
		Sistema.getInstance().getSistemaEventos().get(4).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(4), LocalDateTime.of(2021, 11, 12, 9, 00)));
		Sistema.getInstance().getSistemaEventos().get(4).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(4), LocalDateTime.of(2021, 11, 21, 11, 00)));

		Sistema.getInstance().getSistemaEventos().get(5).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(5), LocalDateTime.of(2021, 11, 11, 11, 40)));
		Sistema.getInstance().getSistemaEventos().get(5).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(5), LocalDateTime.of(2021, 11, 15, 11, 30)));
		Sistema.getInstance().getSistemaEventos().get(5).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(5), LocalDateTime.of(2021, 12, 19, 13, 00)));
		Sistema.getInstance().getSistemaEventos().get(5).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(5), LocalDateTime.of(2021, 12, 23, 11, 00)));
		Sistema.getInstance().getSistemaEventos().get(5).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(5), LocalDateTime.of(2021, 12, 25, 11, 15)));

		Sistema.getInstance().getSistemaEventos().get(6).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(6), LocalDateTime.of(2022, 11, 11, 11, 40)));
		Sistema.getInstance().getSistemaEventos().get(6).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(6), LocalDateTime.of(2022, 11, 15, 11, 30)));
		Sistema.getInstance().getSistemaEventos().get(6).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(6), LocalDateTime.of(2022, 12, 19, 13, 00)));
		Sistema.getInstance().getSistemaEventos().get(6).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(6), LocalDateTime.of(2022, 12, 23, 11, 00)));
		Sistema.getInstance().getSistemaEventos().get(6).addEventoRepresentacion(new Representacion(
				Sistema.getInstance().getSistemaEventos().get(6), LocalDateTime.of(2022, 12, 25, 11, 15)));
		/** adicionalmente el administrador decide crear abonos **/

		ArrayList<Entrada> entradase1r1 = new ArrayList<>();
		ArrayList<Entrada> entradase2r1 = new ArrayList<>();
		ArrayList<Entrada> entradase3r1 = new ArrayList<>();
		ArrayList<Entrada> entradase4r1 = new ArrayList<>();
		ArrayList<Entrada> entradase5r1 = new ArrayList<>();
		ArrayList<Entrada> entradase6r1 = new ArrayList<>();
		ArrayList<Entrada> entradase1r2 = new ArrayList<>();
		ArrayList<Entrada> entradase2r2 = new ArrayList<>();
		ArrayList<Entrada> entradase3r2 = new ArrayList<>();
		ArrayList<Entrada> entradase4r2 = new ArrayList<>();
		ArrayList<Entrada> entradase5r2 = new ArrayList<>();
		ArrayList<Entrada> entradase6r2 = new ArrayList<>();
		ArrayList<Entrada> entradase1r3 = new ArrayList<>();
		ArrayList<Entrada> entradase2r3 = new ArrayList<>();

		for (int i = 0; i < 25; i++) {
			entradase1r1.add(Sistema.getInstance().getSistemaEventos().get(0).getEventoRepresentaciones().get(0)
					.getRepresentacionEntradas().get(i));
			entradase2r1.add(Sistema.getInstance().getSistemaEventos().get(1).getEventoRepresentaciones().get(0)
					.getRepresentacionEntradas().get(i));
		}
		for (int i = 0; i < 12; i++) {
			entradase3r1.add(Sistema.getInstance().getSistemaEventos().get(2).getEventoRepresentaciones().get(0)
					.getRepresentacionEntradas().get(i));
			entradase4r1.add(Sistema.getInstance().getSistemaEventos().get(3).getEventoRepresentaciones().get(0)
					.getRepresentacionEntradas().get(i));
		}
		for (int i = 0; i < 5; i++) {
			entradase5r1.add(Sistema.getInstance().getSistemaEventos().get(4).getEventoRepresentaciones().get(0)
					.getRepresentacionEntradas().get(i));
			entradase6r1.add(Sistema.getInstance().getSistemaEventos().get(5).getEventoRepresentaciones().get(0)
					.getRepresentacionEntradas().get(i));
		}
		for (int i = 0; i < 30; i++) {
			entradase1r2.add(Sistema.getInstance().getSistemaEventos().get(0).getEventoRepresentaciones().get(1)
					.getRepresentacionEntradas().get(i));
			entradase2r2.add(Sistema.getInstance().getSistemaEventos().get(1).getEventoRepresentaciones().get(1)
					.getRepresentacionEntradas().get(i));
		}
		for (int i = 0; i < 14; i++) {
			entradase3r2.add(Sistema.getInstance().getSistemaEventos().get(2).getEventoRepresentaciones().get(1)
					.getRepresentacionEntradas().get(i));
			entradase4r2.add(Sistema.getInstance().getSistemaEventos().get(3).getEventoRepresentaciones().get(1)
					.getRepresentacionEntradas().get(i));
		}
		for (int i = 0; i < 5; i++) {
			entradase5r2.add(Sistema.getInstance().getSistemaEventos().get(4).getEventoRepresentaciones().get(1)
					.getRepresentacionEntradas().get(i));
			entradase6r2.add(Sistema.getInstance().getSistemaEventos().get(5).getEventoRepresentaciones().get(1)
					.getRepresentacionEntradas().get(i));
		}
		for (int i = 0; i < 10; i++) {
			entradase1r3.add(Sistema.getInstance().getSistemaEventos().get(0).getEventoRepresentaciones().get(2)
					.getRepresentacionEntradas().get(i));
			entradase2r3.add(Sistema.getInstance().getSistemaEventos().get(1).getEventoRepresentaciones().get(2)
					.getRepresentacionEntradas().get(i));
		}

		Sistema.getInstance().sistemaCrearAbonoTeatro(new Abono(500, Sistema.getInstance().getSistemaZonas().get(0)));
		Sistema.getInstance().sistemaCrearAbonoTeatro(new Abono(200, Sistema.getInstance().getSistemaZonas().get(1)));
		Sistema.getInstance().sistemaCrearAbonoTeatro(new Abono(300, Sistema.getInstance().getSistemaZonas().get(2)));

		Sistema.getInstance().sistemaCerrarSesion();

		/*****************************************************************************************************************************************************************************/
		/** ENTRA EL PRIMER USUARIO QUE QUIERE REALIZAR UNA COMPRA **/
		/** Como no tiene cuenta, se tiene que registrar **/
		Sistema.getInstance().registrarSistemaUsuario("PedroPerez@gmail.com", "1231235125ewq");
		Sistema.getInstance().sistemaIniciarSesion("PedroPerez@gmail.com", "1231235125ewq");

		Sistema.getInstance().getSistemaEventos().get(0).getEventoRepresentaciones().get(0)
				.comprarRepresentacionEntradaTarjeta(entradase1r1, "1234567890123456");
		Sistema.getInstance().getSistemaEventos().get(1).getEventoRepresentaciones().get(0)
				.comprarRepresentacionEntradaTarjeta(entradase2r1, "1234567890123456");
		Sistema.getInstance().getSistemaEventos().get(2).getEventoRepresentaciones().get(0)
				.comprarRepresentacionEntradaTarjeta(entradase3r1, "1234567890123456");

		Sistema.getInstance().sistemaCerrarSesion();

		/*****************************************************************************************************************************************************************************/
		/** ENTRA EL SEGUNDO USUARIO QUE QUIERE REALIZAR UNA COMPRA **/
		/** Como no tiene cuenta, se tiene que registrar **/
		Sistema.getInstance().registrarSistemaUsuario("Carolina@gmail.com", "45egavwgt43");
		Sistema.getInstance().sistemaIniciarSesion("Carolina@gmail.com", "45egavwgt43");

		Sistema.getInstance().getSistemaEventos().get(3).getEventoRepresentaciones().get(0)
				.comprarRepresentacionEntradaTarjeta(entradase4r1, "1234567890123456");
		Sistema.getInstance().getSistemaEventos().get(4).getEventoRepresentaciones().get(0)
				.comprarRepresentacionEntradaTarjeta(entradase5r1, "1234567890123456");
		Sistema.getInstance().getSistemaEventos().get(5).getEventoRepresentaciones().get(0)
				.comprarRepresentacionEntradaTarjeta(entradase6r1, "1234567890123456");

		Sistema.getInstance().sistemaCerrarSesion();

		/*****************************************************************************************************************************************************************************/
		/** ENTRA EL TERCER USUARIO QUE QUIERE REALIZAR UNA COMPRA **/
		/** Como no tiene cuenta, se tiene que registrar **/
		Sistema.getInstance().registrarSistemaUsuario("Javier@gmail.com", "574hfgybe");
		Sistema.getInstance().sistemaIniciarSesion("Javier@gmail.com", "574hfgybe");
		Sistema.getInstance().getSistemaAbonos().get(0).CompraAbonoUsuario("1234567890123456");

		Sistema.getInstance().getSistemaEventos().get(0).getEventoRepresentaciones().get(1)
				.comprarRepresentacionEntradaTarjeta(entradase1r2,
						"1234567890123456");
		Sistema.getInstance().getSistemaEventos().get(1).getEventoRepresentaciones().get(1)
				.comprarRepresentacionEntradaTarjeta(entradase2r2,
						"1234567890123456");
		Sistema.getInstance().getSistemaEventos().get(2).getEventoRepresentaciones().get(1)
				.comprarRepresentacionEntradaTarjeta(entradase3r2,
						"1234567890123456");

		Sistema.getInstance().sistemaCerrarSesion();

		/*****************************************************************************************************************************************************************************/
		/** ENTRA EL CUARTO USUARIO QUE QUIERE REALIZAR UNA COMPRA **/
		/** Como no tiene cuenta, se tiene que registrar **/

		Sistema.getInstance().registrarSistemaUsuario("PacoHernandez@gmail.com", "12145gqdasv35");
		Sistema.getInstance().sistemaIniciarSesion("PacoHernandez@gmail.com", "12145gqdasv35");

			Sistema.getInstance().getSistemaEventos().get(3).getEventoRepresentaciones().get(1)
					.comprarRepresentacionEntradaTarjeta(entradase4r2,
							"1234567890123456");
			Sistema.getInstance().getSistemaEventos().get(4).getEventoRepresentaciones().get(1)
					.comprarRepresentacionEntradaTarjeta(entradase5r2,
							"1234567890123456");
			Sistema.getInstance().getSistemaEventos().get(5).getEventoRepresentaciones().get(1)
					.comprarRepresentacionEntradaTarjeta(entradase6r2,
							"1234567890123456");

		Sistema.getInstance().sistemaCerrarSesion();

		/*****************************************************************************************************************************************************************************/
		/** ENTRA EL SEXTO USUARIO QUE QUIERE REALIZAR UNA COMPRA **/
		/** Como no tiene cuenta, se tiene que registrar **/

		Sistema.getInstance().registrarSistemaUsuario("LuisaDuran@gmail.com", "45e32gt43");
		Sistema.getInstance().sistemaIniciarSesion("LuisaDuran@gmail.com", "45e32gt43");

			Sistema.getInstance().getSistemaEventos().get(0).getEventoRepresentaciones().get(2)
					.comprarRepresentacionEntradaTarjeta(entradase1r3,
							"1234567890123456");
			Sistema.getInstance().getSistemaEventos().get(1).getEventoRepresentaciones().get(2)
					.comprarRepresentacionEntradaTarjeta(entradase2r3,
							"1234567890123456");

		Sistema.getInstance().sistemaCerrarSesion();

		/****************************************************************************************************************************************************************************/
		/**
		 * ENTRA EL ADMINISTRADOR DE NUEVO PORQUE SE DA CUENTA DE QUE ALGUNAS
		 * REPRESENTACIONES TENDRAN QUE SER MOVIDAS DE FECHA POR UN PROBLEMA CON LOS
		 * ACTORES
		 **/
		Sistema.getInstance().sistemaIniciarSesion("Administrador", "teatro");

		Sistema.getInstance().getSistemaEventos().get(3).posponerEventoRepresentacion(
				Sistema.getInstance().getSistemaEventos().get(3).getEventoRepresentaciones().get(0),
				LocalDateTime.of(2020, 11, 11, 11, 40));
		Sistema.getInstance().getSistemaEventos().get(5).posponerEventoRepresentacion(
				Sistema.getInstance().getSistemaEventos().get(5).getEventoRepresentaciones().get(3),
				LocalDateTime.of(2022, 1, 12, 19, 40));

		/* REVISA LAS ESTADISTICAS PARA VER COMO VAN LOS EVENTOS Y SI REDUCE AFORO */
		for (Evento e : Sistema.getInstance().getSistemaEventos()) {
			e.getEventoEstadisticaOcupacion();
			e.getEventoEstadisticaRecaudacion();
		}
		/* VA A REDUCIR EL AFORO DE OTROS EVENTOS QUE NO HAN SIDO MENOS APOYADOS */
		Sistema.getInstance().getSistemaEventos().get(4).reducirEventoAforo(25);
		Sistema.getInstance().getSistemaEventos().get(5).reducirEventoAforo(50);

		/* FINALMENTE VA A A�ADIR AL SISTEMA UNOS ABONOS DE CICLO */

		ArrayList<Evento> eventos1 = new ArrayList<>();
		eventos1.add(Sistema.getInstance().getSistemaEventos().get(0));
		eventos1.add(Sistema.getInstance().getSistemaEventos().get(1));
		eventos1.add(Sistema.getInstance().getSistemaEventos().get(3));

		ArrayList<Evento> eventos2 = new ArrayList<>();
		eventos2.add(Sistema.getInstance().getSistemaEventos().get(2));

		ArrayList<Evento> eventos3 = new ArrayList<>();
		eventos3.add(Sistema.getInstance().getSistemaEventos().get(4));
		eventos3.add(Sistema.getInstance().getSistemaEventos().get(5));

		CicloEvento cicloCompleto1 = new CicloEvento(45, "Ciclo1", eventos1,
				Sistema.getInstance().getSistemaZonas().get(1));
		CicloEvento cicloCompleto2 = new CicloEvento(40, "Ciclo2", eventos3,
				Sistema.getInstance().getSistemaZonas().get(2));
		Sistema.getInstance().sistemaCrearCicloTeatro(cicloCompleto2);
		
		ArrayList<Ciclo> ciclos = new ArrayList<Ciclo>();
		ciclos.add(cicloCompleto2);
		ciclos.add(cicloCompleto1);

		CicloCompuesto cicloCompleto = new CicloCompuesto(100, "EL ciclo de la jungla", eventos2,
				Sistema.getInstance().getSistemaZonas().get(1), ciclos);
		Sistema.getInstance().sistemaCrearCicloTeatro(cicloCompleto);

		/* Se cierra sesion */
		Sistema.getInstance().sistemaCerrarSesion();

	
		/****************************************************************************************************************************************************************************/
		/** ENTRA EL ADMINISTRADOR FINALMENTE PARA REALIZAR UNOS ULTIMOS CAMBIOS **/

		Sistema.getInstance().sistemaIniciarSesion("Administrador", "teatro");

		/* Revision de estadisticas final */
		for (Evento e : Sistema.getInstance().getSistemaEventos()) {
			e.getEventoEstadisticaOcupacion();
			e.getEventoEstadisticaRecaudacion();
		}
		Sistema.getInstance().sistemaCerrarSesion();
	}

}
