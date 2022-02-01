package vista.panelPrincipal;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import controlador.eventos.ControladorTxtBuscarEvento;
import controlador.representaciones.ControladorFiltrarRepresentaciones;
import vista.panelPrincipal.pestanaBuscarEvento.*;
import vista.panelPrincipal.pestanaIniciarSesion.PestanaIniciarSesion;
import vista.panelPrincipal.pestanaRepresentaciones.*;

/**
 * Clase PanelPrincipal el panel principal
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PanelPrincipal extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5647412311738224848L;

	private JTabbedPane paneVentanaPrincipal = new JTabbedPane();
	private JPanel pestanaInicioSesion = new JPanel(new GridLayout(1, 2, 15, 80));
	private JPanel pestanaBuscarEvento = new JPanel(new BorderLayout(2, 2));
	private JPanel pestanaBuscarRepresentacion = new JPanel(new BorderLayout(2, 2));
	private static JFrame ventanaRegistroUsuario;

	JLabel imagenTeatro = new JLabel("");

	/**
	 * Constructor de PanelPrincipal
	 */
	public PanelPrincipal() {

		TitledBorder titleVetanaPrincipal = new TitledBorder("Sesion de Invitado");
		setBorder(titleVetanaPrincipal);

		add(paneVentanaPrincipal);
		paneVentanaPrincipal.addTab("Inicio Sesion", new ImageIcon("./sources//user.png"), pestanaInicioSesion);
		pestanaInicioSesion.setPreferredSize(new Dimension(850, 480));
		paneVentanaPrincipal.addTab("Buscar Evento", new ImageIcon("./sources//theatre.png"), pestanaBuscarEvento);
		pestanaBuscarEvento.setPreferredSize(new Dimension(850, 480));
		paneVentanaPrincipal.addTab("Representaciones", new ImageIcon("./sources//curtain.png"),
				pestanaBuscarRepresentacion);
		pestanaBuscarRepresentacion.setPreferredSize(new Dimension(850, 480));

		ventanaRegistroUsuario = new VentanaRegistrarUsuario();

		pestanaInicioSesion();
		pestanaBuscarEvento();
		pestanaBuscarRepresentacion();
	}
	/**
	 * Metodo para establecer la pestana de inicio de sesion
	 */
	public void pestanaInicioSesion() {

		pestanaInicioSesion.add(new PestanaIniciarSesion());
		pestanaInicioSesion.add(imagenTeatro);
		imagenTeatro.setIcon(new ImageIcon("./sources//ImagenEntradaTeatro.jpg"));

	}
	/**
	 * Metodo para establecer la pestana de buscar evento
	 */
	public void pestanaBuscarEvento() {

		PestanaBuscarEventoWest vistaTxtBuscarEventos = new PestanaBuscarEventoWest();
		PestanaBuscarEventoCenter vistaTablaBuscarEventos = new PestanaBuscarEventoCenter();
		pestanaBuscarEvento.add(vistaTxtBuscarEventos, BorderLayout.WEST);
		pestanaBuscarEvento.add(vistaTablaBuscarEventos, BorderLayout.CENTER);

		ControladorTxtBuscarEvento cBuscarEventos = new ControladorTxtBuscarEvento(vistaTxtBuscarEventos,
				vistaTablaBuscarEventos);
		vistaTxtBuscarEventos.setControlador(cBuscarEventos);
	}
	/**
	 * Metodo para establecer la pestana de buscar representacion
	 */
	private void pestanaBuscarRepresentacion() {

		PestanaFiltrarRepresentacionesWest vistaBoxFiltrarRepresentaciones = new PestanaFiltrarRepresentacionesWest();
		PestanaFiltrarRepresentacionesCenter vistaTablaFiltrarRepresentaciones = new PestanaFiltrarRepresentacionesCenter();
		pestanaBuscarRepresentacion.add(vistaBoxFiltrarRepresentaciones, BorderLayout.WEST);
		pestanaBuscarRepresentacion.add(vistaTablaFiltrarRepresentaciones, BorderLayout.CENTER);

		ControladorFiltrarRepresentaciones cFiltrarRepresentaciones = new ControladorFiltrarRepresentaciones(
				vistaBoxFiltrarRepresentaciones, vistaTablaFiltrarRepresentaciones);
		vistaBoxFiltrarRepresentaciones.setControlador(cFiltrarRepresentaciones);
	}
	/**
	 * Metodo para obtener la ventana de registro de usuario
	 * 
	 * @return JFrame objeto con la ventana
	 */
	public static JFrame getVentanaRegistrarUsuario() {
		return ventanaRegistroUsuario;
	}

}
