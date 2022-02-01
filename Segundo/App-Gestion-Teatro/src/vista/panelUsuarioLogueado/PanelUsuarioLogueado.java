package vista.panelUsuarioLogueado;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import controlador.abonos.ControladorAbonoIntroduceTarjeta;
import controlador.abonos.ControladorBuscarCiclo;
import controlador.abonos.ControladorComprarAbonoTarjeta;
import controlador.abonos.ControladorFiltroBuscarAbonos;
import controlador.abonos.ControladorSeleccionarAbonoComprar;
import controlador.entrada.ControladorBotonElegirNumEntradasPie;
import controlador.entrada.ControladorBotonElegirNumEntradasSentado;
import controlador.entrada.ControladorReservarCompra;
import controlador.entrada.ControladorComprarEntrada;
import controlador.entrada.ControladorComprarEntradasSugeridas;
import controlador.entrada.ControladorSeleccionarZona;
import controlador.entrada.ControladorTxtElegirEntradasSentado;
import controlador.entrada.ControladorEntradaIntroduceTarjeta;
import controlador.entrada.ControladorVolverCompraEntrada;
import controlador.eventos.ControladorTxtBuscarEvento;
import controlador.misEntradas.ControladorBotonComprarReserva;
import controlador.misEntradas.ControladorBotonCancelarReserva;
import controlador.misEntradas.ControladorCompraEntradaReservada;
import controlador.misEntradas.ControladorFiltrarMisEntradas;
import controlador.misEntradas.ControladorSeleccionarMiEntradaReservada;
import controlador.notificaciones.ControladorSeleccionarEntradaLeida;
import modelo.sistema.Sistema;
import vista.panelPrincipal.pestanaBuscarEvento.*;
import vista.panelUsuarioLogueado.misEntradas.PestanaMisEntradas;
import vista.panelUsuarioLogueado.notificaciones.NotificacionesUsuarioCenter;
import vista.panelUsuarioLogueado.pestanaBuscarCiclo.PestanaBuscarCicloCenter;
import vista.panelUsuarioLogueado.pestanaBuscarCiclo.PestanaBuscarCicloWest;
import vista.panelUsuarioLogueado.pestanaCompraAbono.*;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.PestanaComprarEntrada;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.PestanaComprarEntradaVolver;

/**
 * Clase PanelUsuarioLogueado para el panel del usuario logeado representaciones
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PanelUsuarioLogueado extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5647412311738224848L;

	private JTabbedPane paneVentanaUsuarioLogueado = new JTabbedPane();
	private JPanel pestanaComprarAbono = new JPanel(new BorderLayout(2, 2));
	private JPanel pestanaBuscarEvento = new JPanel(new BorderLayout(2, 2));
	private JPanel pestanaBuscarCiclo = new JPanel(new BorderLayout(2, 2));
	private JPanel pestanaComprarEntradas = new JPanel(new BorderLayout(2, 2));
	private JPanel pestanaNotificacionesUsuario = new JPanel(new GridLayout(1, 1));
	private JPanel pestanaMisEntradas = new JPanel(new GridLayout(1, 1));

	private static VentanaNumeroDeEntradasSentado vistaNumEntradasSentado;
	private static VentanaNumeroDeEntradasPie vistaNumEntradasPie;
	private static VentanaElegirCompraSentado vistaEntradasSentado;
	private static NotificacionesUsuarioCenter vistaNotificaciones = new NotificacionesUsuarioCenter();

	/**
	 * Constructor de PanelUsuarioLogueado
	 */
	public PanelUsuarioLogueado() {

		TitledBorder titleVetanaRegistrada = new TitledBorder("Sesion de Usuario Registrado");
		setBorder(titleVetanaRegistrada);

		add(paneVentanaUsuarioLogueado);
		paneVentanaUsuarioLogueado.addTab("Buscar Evento", new ImageIcon("./sources//theatre.png"),
				pestanaBuscarEvento);
		pestanaBuscarEvento.setPreferredSize(new Dimension(850, 480));
		paneVentanaUsuarioLogueado.addTab("Buscar Ciclos", new ImageIcon("./sources//curtain.png"), pestanaBuscarCiclo);
		pestanaBuscarCiclo.setPreferredSize(new Dimension(850, 480));
		paneVentanaUsuarioLogueado.addTab("Comprar Entradas", new ImageIcon("./sources//credit-cards.png"),
				pestanaComprarEntradas);
		pestanaComprarEntradas.setPreferredSize(new Dimension(850, 480));
		paneVentanaUsuarioLogueado.addTab("Comprar Abono", new ImageIcon("./sources//tickets.png"),
				pestanaComprarAbono);
		pestanaComprarAbono.setPreferredSize(new Dimension(850, 480));
		paneVentanaUsuarioLogueado.addTab("Notificaciones", new ImageIcon("./sources//into-inbox-symbol.png"),
				pestanaNotificacionesUsuario);
		pestanaNotificacionesUsuario.setPreferredSize(new Dimension(850, 480));
		paneVentanaUsuarioLogueado.addTab("Mis Compras", new ImageIcon("./sources//user.png"), pestanaMisEntradas);
		pestanaMisEntradas.setPreferredSize(new Dimension(850, 480));

		vistaNumEntradasSentado = new VentanaNumeroDeEntradasSentado();
		vistaNumEntradasPie = new VentanaNumeroDeEntradasPie();
		vistaEntradasSentado = new VentanaElegirCompraSentado();

		pestanaBuscarEvento();
		pestanaBuscarCiclos();
		pestanaComprarEntradas();
		pestanaComprarAbono();
		pestanaNotificaciones();
		pestanaMisEntradas();
	}

	/**
	 * Metodo para mostrar la pestana de buscar evento
	 */
	private void pestanaBuscarEvento() {

		PestanaBuscarEventoWest vistaTxtBuscarEventos = new PestanaBuscarEventoWest();
		PestanaBuscarEventoCenter vistaTablaBuscarEventos = new PestanaBuscarEventoCenter();
		pestanaBuscarEvento.add(vistaTxtBuscarEventos, BorderLayout.WEST);
		pestanaBuscarEvento.add(vistaTablaBuscarEventos, BorderLayout.CENTER);

		ControladorTxtBuscarEvento cBuscarEventos = new ControladorTxtBuscarEvento(vistaTxtBuscarEventos,
				vistaTablaBuscarEventos);
		vistaTxtBuscarEventos.setControlador(cBuscarEventos);
	}

	/**
	 * Metodo para mostrar la pestana de buscar ciclos
	 */
	private void pestanaBuscarCiclos() {

		PestanaBuscarCicloCenter vistaTableCiclos = new PestanaBuscarCicloCenter();
		PestanaBuscarCicloWest vistaTxtBuscarCiclo = new PestanaBuscarCicloWest();
		pestanaBuscarCiclo.add(vistaTableCiclos, BorderLayout.CENTER);
		pestanaBuscarCiclo.add(vistaTxtBuscarCiclo, BorderLayout.WEST);

		ControladorBuscarCiclo cBuscarCiclo = new ControladorBuscarCiclo(vistaTxtBuscarCiclo, vistaTableCiclos);
		vistaTxtBuscarCiclo.setControlador(cBuscarCiclo);
	}

	private PestanaComprarEntrada vistaCompraEntrada;

	/**
	 * Metodo para mostrar la pestana de comprar entradas
	 */
	private void pestanaComprarEntradas() {

		vistaCompraEntrada = new PestanaComprarEntrada();
		PestanaComprarEntradaVolver vistaCompraVolver = new PestanaComprarEntradaVolver();
		pestanaComprarEntradas.add(vistaCompraEntrada, BorderLayout.CENTER);
		pestanaComprarEntradas.add(vistaCompraVolver, BorderLayout.SOUTH);

		ControladorVolverCompraEntrada cVolverCompraEntrada = new ControladorVolverCompraEntrada(vistaCompraEntrada,
				getVentanaNumEntradasPie(), getVentanaNumEntradasSentado(), getVentanaElegirCompraSentado());

		vistaCompraVolver.setControlador(cVolverCompraEntrada);

		ControladorBotonElegirNumEntradasPie cNumEntradasPie = new ControladorBotonElegirNumEntradasPie(
				vistaCompraEntrada, getVentanaNumEntradasPie());

		getVentanaNumEntradasPie().setControladorBoton(cNumEntradasPie);
		getVentanaNumEntradasPie().setControladorSlider(cNumEntradasPie);

		ControladorBotonElegirNumEntradasSentado cNumEntradasSentado = new ControladorBotonElegirNumEntradasSentado(
				vistaCompraEntrada, getVentanaNumEntradasSentado(), getVentanaElegirCompraSentado());

		getVentanaNumEntradasSentado().setControladorCb(cNumEntradasSentado);
		getVentanaNumEntradasSentado().setControladorSlider(cNumEntradasSentado);
		getVentanaNumEntradasSentado().setControladorBoton(cNumEntradasSentado);

		ControladorSeleccionarZona cSeleccionZona = new ControladorSeleccionarZona(vistaCompraEntrada,
				vistaCompraEntrada.getCardZonaEntrada(), vistaCompraEntrada.getCardZonaCompuestaEntrada());
		vistaCompraEntrada.getCardZonaEntrada().setControlador(cSeleccionZona);
		vistaCompraEntrada.getCardZonaCompuestaEntrada().setControlador(cSeleccionZona);

		ControladorComprarEntradasSugeridas cComprarSugeridas = new ControladorComprarEntradasSugeridas(
				getVentanaElegirCompraSentado(), vistaCompraEntrada);

		getVentanaElegirCompraSentado().getCardMenuSugerencias().setControladorSugerencia(cComprarSugeridas);

		ControladorEntradaIntroduceTarjeta cTarjeta = new ControladorEntradaIntroduceTarjeta(vistaCompraEntrada);
		vistaCompraEntrada.getCardCarritoCompra().getBotonesCarritoCompra().setControladorComprarEntrada(cTarjeta);

		ControladorComprarEntrada cCompraEntrada = new ControladorComprarEntrada(
				vistaCompraEntrada.getVentanaVistaTarjeta(), vistaCompraEntrada,
				vistaCompraEntrada.getCardCarritoCompra());
		vistaCompraEntrada.getVentanaVistaTarjeta().setControladorCompra(cCompraEntrada);

		ControladorReservarCompra cReservarCompra = new ControladorReservarCompra(vistaCompraEntrada,
				vistaCompraEntrada.getCardCarritoCompra());
		vistaCompraEntrada.getCardCarritoCompra().getBotonesCarritoCompra()
				.setControladorReservarEntrada(cReservarCompra);

		ControladorTxtElegirEntradasSentado cElegirEntradasSentado = new ControladorTxtElegirEntradasSentado(
				vistaCompraEntrada, getVentanaElegirCompraSentado(), getVentanaNumEntradasSentado());

		getVentanaElegirCompraSentado().getCardElegirEntradas().setControladorAceptar(cElegirEntradasSentado);

	}

	/**
	 * Metodo para mostrar la pestana de comprar abono
	 */
	private void pestanaComprarAbono() {

		PestanaCompraAbonoWest vistaBoxFiltrarAbonos = new PestanaCompraAbonoWest();
		PestanaCompraAbonoCenter vistaTableAbonos = new PestanaCompraAbonoCenter();
		pestanaComprarAbono.add(vistaBoxFiltrarAbonos, BorderLayout.WEST);
		pestanaComprarAbono.add(vistaTableAbonos, BorderLayout.CENTER);

		ControladorFiltroBuscarAbonos cFiltrarAbonos = new ControladorFiltroBuscarAbonos(vistaBoxFiltrarAbonos,
				vistaTableAbonos);
		vistaBoxFiltrarAbonos.setControlador(cFiltrarAbonos);

		ControladorSeleccionarAbonoComprar cSeleccionarAbono = new ControladorSeleccionarAbonoComprar(vistaTableAbonos,
				vistaBoxFiltrarAbonos);

		ControladorAbonoIntroduceTarjeta cTarjeta = new ControladorAbonoIntroduceTarjeta(vistaTableAbonos);

		vistaTableAbonos.getCardResumenCompra().getAbajoCompraAbono().setControlador(cTarjeta);
		vistaTableAbonos.getCardTablaAbonos().setControlador(cSeleccionarAbono);

		ControladorComprarAbonoTarjeta cComprarTarjeta = new ControladorComprarAbonoTarjeta(
				vistaTableAbonos.getVentanaVistaTarjeta(), vistaTableAbonos, vistaBoxFiltrarAbonos);

		vistaTableAbonos.getVentanaVistaTarjeta().setControladorCompra(cComprarTarjeta);
	}

	/**
	 * Metodo para mostrar la pestana de mis entradas
	 */
	private void pestanaMisEntradas() {

		PestanaMisEntradas vistaEntradas = new PestanaMisEntradas();
		pestanaMisEntradas.add(vistaEntradas);

		ControladorFiltrarMisEntradas cFiltrarEntradas = new ControladorFiltrarMisEntradas(vistaEntradas);
		vistaEntradas.getPestanaMisEntradasWest().setControlador(cFiltrarEntradas);

		ControladorSeleccionarMiEntradaReservada cSeleccionarEntrada = new ControladorSeleccionarMiEntradaReservada(
				vistaEntradas.getPestanaMisEntradasCentre(),
				vistaEntradas.getPestanaMisEntradasCentre().getVentanaOpcionReserva());

		vistaEntradas.getPestanaMisEntradasCentre().getCardEntradasReservadas().setControlador(cSeleccionarEntrada);

		ControladorBotonComprarReserva cTarjetaSeleccionada = new ControladorBotonComprarReserva(
				vistaEntradas.getPestanaMisEntradasCentre(),
				vistaEntradas.getPestanaMisEntradasCentre().getVentanaVistaTarjeta());

		vistaEntradas.getPestanaMisEntradasCentre().getVentanaOpcionReserva()
				.setControladorComprar(cTarjetaSeleccionada);

		ControladorCompraEntradaReservada cComprarSeleccionada = new ControladorCompraEntradaReservada(
				vistaEntradas.getPestanaMisEntradasCentre(),
				vistaEntradas.getPestanaMisEntradasCentre().getVentanaVistaTarjeta(),
				vistaEntradas.getPestanaMisEntradasCentre().getVentanaOpcionReserva());

		vistaEntradas.getPestanaMisEntradasCentre().getVentanaVistaTarjeta().setControladorCompra(cComprarSeleccionada);

		ControladorBotonCancelarReserva cCancelarSeleccionada = new ControladorBotonCancelarReserva(
				vistaEntradas.getPestanaMisEntradasCentre(),
				vistaEntradas.getPestanaMisEntradasCentre().getVentanaOpcionReserva());

		vistaEntradas.getPestanaMisEntradasCentre().getVentanaOpcionReserva()
				.setControladorCancelar(cCancelarSeleccionada);

	}

	/**
	 * Metodo para mostrar la pestana de notificaciones
	 */
	private void pestanaNotificaciones() {
		pestanaNotificacionesUsuario.add(vistaNotificaciones);

		ControladorSeleccionarEntradaLeida cLeerEntrada = new ControladorSeleccionarEntradaLeida(vistaNotificaciones);
		vistaNotificaciones.setControlador(cLeerEntrada);

	}

	public static void updatePestanaNotificaciones() {
		vistaNotificaciones.actualizarTablaNotificaciones(
				Sistema.getInstance().getSistemaUsuarioLogeado().usuarioConsultarNotificacionesSinLeer());
	}

	public static void cleanPestanaNotificaciones() {
		vistaNotificaciones.destruirTablaNotificaciones();
	}

	/**
	 * Metodo para obtener la ventanda de entradas numeradas sentado
	 * 
	 * @return VentanaNumeroDeEntradasSentado con el objeto
	 */
	public static VentanaNumeroDeEntradasSentado getVentanaNumEntradasSentado() {
		return vistaNumEntradasSentado;
	}

	/**
	 * Metodo para obtener la ventanda de entradas numeradas de pie
	 * 
	 * @return VentanaNumeroDeEntradasPie con el objeto
	 */
	public static VentanaNumeroDeEntradasPie getVentanaNumEntradasPie() {
		return vistaNumEntradasPie;
	}

	/**
	 * Metodo para obtener la ventanda de elegir compra sentado
	 * 
	 * @return VentanaElegirCompraSentado con el objeto
	 */
	public static VentanaElegirCompraSentado getVentanaElegirCompraSentado() {
		return vistaEntradasSentado;
	}

}
