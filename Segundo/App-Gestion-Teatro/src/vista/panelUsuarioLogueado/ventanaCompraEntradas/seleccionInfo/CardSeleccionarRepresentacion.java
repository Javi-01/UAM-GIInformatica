package vista.panelUsuarioLogueado.ventanaCompraEntradas.seleccionInfo;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import controlador.representaciones.ControladorFiltrarRepresentaciones;
import vista.panelPrincipal.pestanaRepresentaciones.PestanaFiltrarRepresentacionesCenter;
import vista.panelPrincipal.pestanaRepresentaciones.PestanaFiltrarRepresentacionesWest;

/**
 * Clase CardSeleccionarRepresentacion card para sekeccionar la representacion
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardSeleccionarRepresentacion extends JPanel {

	private static final long serialVersionUID = -8777949086756108237L;
	private BorderLayout repreLayout = new BorderLayout(2, 2);

	private PestanaFiltrarRepresentacionesCenter vistaTablaRepre;
	private PestanaFiltrarRepresentacionesWest vistaBoxRepre;

	/**
	 * Constructor de CardSeleccionarRepresentacion
	 */
	public CardSeleccionarRepresentacion() {
		setLayout(repreLayout);

		vistaTablaRepre = new PestanaFiltrarRepresentacionesCenter();
		vistaBoxRepre = new PestanaFiltrarRepresentacionesWest();

		add(vistaTablaRepre, BorderLayout.CENTER);
		add(vistaBoxRepre, BorderLayout.WEST);

		ControladorFiltrarRepresentaciones cFiltraRepre = new ControladorFiltrarRepresentaciones(vistaBoxRepre,
				vistaTablaRepre);
		vistaBoxRepre.setControlador(cFiltraRepre);
	}

	public PestanaFiltrarRepresentacionesCenter getTablaRepreEntrada() {
		return vistaTablaRepre;
	}

	public PestanaFiltrarRepresentacionesWest getBoxRepreEntrada() {
		return vistaBoxRepre;
	}
}
