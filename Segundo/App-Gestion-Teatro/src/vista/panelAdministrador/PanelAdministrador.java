package vista.panelAdministrador;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import controlador.abonos.ControladorAnadirCicloACiclo;
import controlador.abonos.ControladorAnadirEventoACiclo;
import controlador.abonos.ControladorBotonCiclosCiclo;
import controlador.abonos.ControladorBotonCrearCiclo;
import controlador.abonos.ControladorBotonEventosCiclo;
import controlador.abonos.ControladorVolverCrearCiclo;
import controlador.crearEvento.ControladorBotonCrearEvento;
import controlador.crearEvento.ControladorCancelarCrearEvento;
import controlador.crearEvento.ControladorRellenarCamposCrearEvento;
import controlador.estadisticas.ControladorFiltrarEstadisticas;
import controlador.estadisticas.ControladorFiltrarEstadisticasAvanzado;
import controlador.representaciones.ControladorBotonBuscarEventoRepres;
import controlador.representaciones.ControladorBotonBuscarRepresentacion;
import controlador.representaciones.ControladorBotonConfirmarGestionRepres;
import controlador.representaciones.ControladorSeleccionarEventoRepre;
import controlador.representaciones.ControladorSeleccionarRepre;
import controlador.zonasAforo.ControladorBotonDeshabilitarButaca;
import controlador.zonasAforo.ControladorBuscarEventoZonaAforo;
import controlador.zonasAforo.ControladorBuscarZonaAforo;
import controlador.zonasAforo.ControladorFiltroZonasYAforo;
import controlador.zonasAforo.ControladorMaxEntradasCompra;
import controlador.zonasAforo.ControladorReducirAforoEvento;
import controlador.zonasAforo.ControladorSeleccionarButacaAforo;
import controlador.zonasAforo.ControladorSeleccionarEventoZonaAforo;
import controlador.zonasAforo.ControladorSeleccionarZonaAforo;
import vista.panelAdministrador.pestanaCrearAbonos.PestanaCrearAbono;
import vista.panelAdministrador.pestanaCrearEvento.PestanaCrearEvento;
import vista.panelAdministrador.pestanaEstadisticas.PestanaEstadisticasCenter;
import vista.panelAdministrador.pestanaEstadisticas.PestanaEstadisticasWest;
import vista.panelAdministrador.pestanaGestionRepresentaciones.PestanaGestionRepresentaciones;
import vista.panelAdministrador.pestanaZonasYAforo.PestanaZonasAforo;

/**
 * Clase PanelAdministrador para el panel del administrador
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PanelAdministrador extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5647412311738224848L;

	private JTabbedPane paneVentanaAdministrador = new JTabbedPane();

	private JPanel pestanaCrearEvento = new JPanel(new GridLayout(1, 1));
	private JPanel pestanaGestionarRepresentaciones = new JPanel(new GridLayout(1, 1));
	private JPanel pestanaCrearAbonos = new JPanel(new GridLayout(1, 1));
	private JPanel pestanaZonasYAforo = new JPanel(new GridLayout(1, 1));
	private JPanel pestanaRevisarEstadisticas = new JPanel(new BorderLayout(2, 2));

	/**
	 * Constructor de PanelAdministrador
	 */
	public PanelAdministrador() {

		TitledBorder titleVetanaAdmoinistrador = new TitledBorder("Sesion de Adminstrador");
		setBorder(titleVetanaAdmoinistrador);

		add(paneVentanaAdministrador);
		paneVentanaAdministrador.addTab("Crear Evento", new ImageIcon("./sources//theatre.png"), pestanaCrearEvento);
		pestanaCrearEvento.setPreferredSize(new Dimension(850, 480));
		paneVentanaAdministrador.addTab("Crear Abonos", new ImageIcon("./sources//tickets.png"), pestanaCrearAbonos);
		pestanaCrearAbonos.setPreferredSize(new Dimension(850, 480));
		paneVentanaAdministrador.addTab("Gestion Representaciones", new ImageIcon("./sources//curtain.png"),
				pestanaGestionarRepresentaciones);
		pestanaGestionarRepresentaciones.setPreferredSize(new Dimension(850, 480));
		paneVentanaAdministrador.addTab("Zonas y Aforo", new ImageIcon("./sources//clapperboard.png"),
				pestanaZonasYAforo);
		pestanaZonasYAforo.setPreferredSize(new Dimension(850, 480));
		paneVentanaAdministrador.addTab("Estadisticas", new ImageIcon("./sources//volume.png"),
				pestanaRevisarEstadisticas);
		pestanaRevisarEstadisticas.setPreferredSize(new Dimension(850, 480));

		pestanaCrearEvento();
		pestanaCrearAbono();
		pestanaGestionRepresentaciones();
		pestanaGestionZonasyAforo();
		pestanaRevisarEstadisticas();
	}

	/**
	 * Metodo para crear la pesatana necesaria para la creacion de eventos
	 */
	public void pestanaCrearEvento() {

		PestanaCrearEvento vistaCrearEvento = new PestanaCrearEvento();
		pestanaCrearEvento.add(vistaCrearEvento);

		ControladorRellenarCamposCrearEvento cBotonRellenarEvento = new ControladorRellenarCamposCrearEvento(
				vistaCrearEvento, vistaCrearEvento.getPestanaCrearEventoBorder().getPestanaCrearEventoWest());
		ControladorBotonCrearEvento cBotonCrearEvento = new ControladorBotonCrearEvento(vistaCrearEvento,
				vistaCrearEvento.getPestanaCrearEventoPrecioZona());
		ControladorCancelarCrearEvento cBotonCancelarCreacion = new ControladorCancelarCrearEvento(vistaCrearEvento,
				vistaCrearEvento.getPestanaCrearEventoPrecioZona());

		vistaCrearEvento.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza()
				.setControlador(cBotonRellenarEvento);
		vistaCrearEvento.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto()
				.setControlador(cBotonRellenarEvento);
		vistaCrearEvento.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoTeatro()
				.setControlador(cBotonRellenarEvento);

		vistaCrearEvento.getPestanaCrearEventoPrecioZona().setControladorCrear(cBotonCrearEvento);
		vistaCrearEvento.getPestanaCrearEventoPrecioZona().setControladorCancelar(cBotonCancelarCreacion);

	}

	/**
	 * Metodo para crear la pesatana necesaria para la creacion de abonos
	 */
	public void pestanaCrearAbono() {

		PestanaCrearAbono vistaCrearAbono = new PestanaCrearAbono();
		pestanaCrearAbonos.add(vistaCrearAbono);

		ControladorBotonEventosCiclo btAnadirEventos = new ControladorBotonEventosCiclo(vistaCrearAbono,
				vistaCrearAbono.getPestanaCrearAbonoBorder().getPestanaCrearAbonoCenter().getPestanaCrearCiclo());

		ControladorBotonCiclosCiclo btAnadirCiclo = new ControladorBotonCiclosCiclo(vistaCrearAbono,
				vistaCrearAbono.getPestanaCrearAbonoBorder().getPestanaCrearAbonoCenter().getPestanaCrearCiclo());

		ControladorVolverCrearCiclo btCancelarAnadirCiclo = new ControladorVolverCrearCiclo(vistaCrearAbono);

		ControladorAnadirEventoACiclo anadirEventoACiclo = new ControladorAnadirEventoACiclo(
				vistaCrearAbono.getCardAnadirCicloEvento(), vistaCrearAbono);
		ControladorAnadirCicloACiclo anadirCicloACiclo = new ControladorAnadirCicloACiclo(
				vistaCrearAbono.getCardAnadirCompuesto(), vistaCrearAbono);

		ControladorBotonCrearCiclo btCrearCiclo = new ControladorBotonCrearCiclo(
				vistaCrearAbono.getPestanaCrearAbonoBorder().getPestanaCrearAbonoCenter().getPestanaCrearCiclo());

		vistaCrearAbono.getCardAnadirCicloEvento().getBtVolverAnadeCiclo().setControlador(btCancelarAnadirCiclo);
		vistaCrearAbono.getCardAnadirCompuesto().getBtVolverAnadeCiclo().setControlador(btCancelarAnadirCiclo);

		vistaCrearAbono.getPestanaCrearAbonoBorder().getPestanaCrearAbonoCenter().getPestanaCrearCiclo()
				.getCamposCiclo().setControladorAnadeCiclo(btAnadirCiclo);

		vistaCrearAbono.getPestanaCrearAbonoBorder().getPestanaCrearAbonoCenter().getPestanaCrearCiclo()
				.getCamposCiclo().setControladorAnadeEvento(btAnadirEventos);

		vistaCrearAbono.getCardAnadirCicloEvento().setControlador(anadirEventoACiclo);
		vistaCrearAbono.getCardAnadirCompuesto().setControlador(anadirCicloACiclo);

		vistaCrearAbono.getPestanaCrearAbonoBorder().getPestanaCrearAbonoCenter().getPestanaCrearCiclo()
				.getBotonCrearCiclo().setControladorCrear(btCrearCiclo);
	}

	/**
	 * Metodo para crear la pesatana necesaria para la gestion de representaciones
	 */
	private void pestanaGestionRepresentaciones() {

		PestanaGestionRepresentaciones vistaGestionRepres = new PestanaGestionRepresentaciones();
		pestanaGestionarRepresentaciones.add(vistaGestionRepres);

		ControladorBotonBuscarEventoRepres cBuscarEventoRepre = new ControladorBotonBuscarEventoRepres(
				vistaGestionRepres);

		vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardAnadirRepre()
				.setControladorEvento(cBuscarEventoRepre);
		vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardPosponerRepre()
				.setControladorEvento(cBuscarEventoRepre);
		vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardCancelarRepre()
				.setControladorEvento(cBuscarEventoRepre);

		ControladorBotonBuscarRepresentacion cBuscarRepre = new ControladorBotonBuscarRepresentacion(
				vistaGestionRepres);

		vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardPosponerRepre()
				.setControladorRepre(cBuscarRepre);
		vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardCancelarRepre()
				.setControladorRepre(cBuscarRepre);

		ControladorSeleccionarEventoRepre cSeleccionarEvento = new ControladorSeleccionarEventoRepre(vistaGestionRepres,
				vistaGestionRepres.getPestanaTablaEventos(),
				vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresWest());

		vistaGestionRepres.getPestanaTablaEventos().setControlador(cSeleccionarEvento);

		ControladorSeleccionarRepre cSeleccionRepre = new ControladorSeleccionarRepre(vistaGestionRepres,
				vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresWest(),
				vistaGestionRepres.getPestanaTablaRepres());

		vistaGestionRepres.getPestanaTablaRepres().setControlador(cSeleccionRepre);

		ControladorBotonConfirmarGestionRepres cConfirmarGestion = new ControladorBotonConfirmarGestionRepres(
				vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter(), vistaGestionRepres,
				vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresWest());

		vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardAnadirRepre()
				.setControladorCrear(cConfirmarGestion);
		vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardPosponerRepre()
				.setControladorPosponer(cConfirmarGestion);
		vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardCancelarRepre()
				.setControladorCrancelar(cConfirmarGestion);
	}

	/**
	 * Metodo para crear la pesatana necesaria para la gestion de zonas y aforos
	 */
	private void pestanaGestionZonasyAforo() {

		PestanaZonasAforo vistaZonasYAforo = new PestanaZonasAforo();
		pestanaZonasYAforo.add(vistaZonasYAforo);

		ControladorFiltroZonasYAforo cFiltroZonasYAforo = new ControladorFiltroZonasYAforo(
				vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoCenter(),
				vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoWest());

		vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoWest().setControlador(cFiltroZonasYAforo);

		ControladorMaxEntradasCompra cMaxEntradas = new ControladorMaxEntradasCompra(
				vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoCenter());

		vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoCenter().getCardCambiarMaxEntradas()
				.setControladorAceptar(cMaxEntradas);
		vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoCenter().getCardCambiarMaxEntradas()
				.setControladorSlider(cMaxEntradas);

		ControladorBuscarEventoZonaAforo cBuscarEvento = new ControladorBuscarEventoZonaAforo(vistaZonasYAforo);
		vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoCenter().getCardReducirAforo()
				.setControladorEvento(cBuscarEvento);

		ControladorSeleccionarEventoZonaAforo cSeleccionarEvento = new ControladorSeleccionarEventoZonaAforo(
				vistaZonasYAforo, vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoCenter());

		vistaZonasYAforo.getPestanaTablaEventos().setControlador(cSeleccionarEvento);

		ControladorReducirAforoEvento cReducirAforo = new ControladorReducirAforoEvento(
				vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoCenter());

		vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoCenter().getCardReducirAforo()
				.setControladorAceptar(cReducirAforo);

		ControladorBuscarZonaAforo cBuscarZonaAforo = new ControladorBuscarZonaAforo(vistaZonasYAforo,
				vistaZonasYAforo.getPestanaTablaZonas());

		vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoCenter().getCardDeshabilitarButacas()
				.setControladorZona(cBuscarZonaAforo);

		ControladorSeleccionarZonaAforo cSeleccionarZona = new ControladorSeleccionarZonaAforo(vistaZonasYAforo,
				vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoCenter());

		vistaZonasYAforo.getPestanaTablaZonas().setControlador(cSeleccionarZona);

		ControladorSeleccionarButacaAforo cSeleccionarButaca = new ControladorSeleccionarButacaAforo(vistaZonasYAforo,
				vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoCenter());

		vistaZonasYAforo.getPestanaTablaButacas().setControlador(cSeleccionarButaca);

		ControladorBotonDeshabilitarButaca cDeshabilitarButaca = new ControladorBotonDeshabilitarButaca(
				vistaZonasYAforo, vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoCenter());

		vistaZonasYAforo.getPestanaZonasYAforoBorder().getVistaZonasAforoCenter().getCardDeshabilitarButacas()
				.setControladorConfirmar(cDeshabilitarButaca);
	}

	/**
	 * Metodo para crear la pesatana necesaria para la revision de estadisticas
	 */
	public void pestanaRevisarEstadisticas() {

		PestanaEstadisticasCenter vistaEstadisticasCenter = new PestanaEstadisticasCenter();
		PestanaEstadisticasWest vistaEstadisticasWest = new PestanaEstadisticasWest();

		ControladorFiltrarEstadisticas cFiltroEstadisticas = new ControladorFiltrarEstadisticas(
				vistaEstadisticasWest.getEstadisticasFiltro(), vistaEstadisticasCenter);
		ControladorFiltrarEstadisticasAvanzado cFiltroEstadisticasAvanzado = new ControladorFiltrarEstadisticasAvanzado(
				vistaEstadisticasWest.getEstadisticasFiltro(), vistaEstadisticasWest.getEstadisticasTipoBusqueda(),
				vistaEstadisticasCenter);

		vistaEstadisticasWest.getEstadisticasFiltro().setControlador(cFiltroEstadisticas);
		vistaEstadisticasWest.getEstadisticasTipoBusqueda().setControlador(cFiltroEstadisticasAvanzado);

		pestanaRevisarEstadisticas.add(vistaEstadisticasCenter, BorderLayout.CENTER);
		pestanaRevisarEstadisticas.add(vistaEstadisticasWest, BorderLayout.WEST);
	}
}
