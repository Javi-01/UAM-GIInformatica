package vista.panelAdministrador.pestanaGestionRepresentaciones;

import java.awt.CardLayout;

import javax.swing.JPanel;

import vista.panelAdministrador.pestanaGestionRepresentaciones.tabla.PestanaTablaEventosGestionRepresentaciones;
import vista.panelAdministrador.pestanaGestionRepresentaciones.tabla.PestanaTablaRepresGestionRepresentaciones;

/**
 * Clase PestanaGestionRepresentaciones para la pestana de gestion de
 * representaciones
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaGestionRepresentaciones extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9201898312583276403L;

	private final static String cardBorderRepres = "CardRepre";
	private final static String cardTablaEventos = "TablaEventos";
	private final static String cardTablaRepres = "TablaRepre";

	private PestanaGestionRepresentacionesBorder gestionRepres;
	private PestanaTablaEventosGestionRepresentaciones tablaEventos;
	private PestanaTablaRepresGestionRepresentaciones tablaRepres;

	private CardLayout layoutRepres = new CardLayout();

	/**
	 * Constructor de PestanaGestionRepresentaciones
	 */
	public PestanaGestionRepresentaciones() {
		setLayout(layoutRepres);

		gestionRepres = new PestanaGestionRepresentacionesBorder();
		tablaEventos = new PestanaTablaEventosGestionRepresentaciones();
		tablaRepres = new PestanaTablaRepresGestionRepresentaciones();

		add(gestionRepres, cardBorderRepres);
		add(tablaEventos, cardTablaEventos);
		add(tablaRepres, cardTablaRepres);

	}

	/**
	 * Metodo para mostrar el LayOut del border de representaciones
	 */
	public void showPestanaBorderGestionRepres() {
		layoutRepres.show(this, cardBorderRepres);
	}

	/**
	 * Metodo para mostrar el LayOut de la tabla de eventos de representaciones
	 */
	public void showPestanaTablaGestionEventos() {
		layoutRepres.show(this, cardTablaEventos);
	}

	/**
	 * Metodo para mostrar el LayOut de la tabla de representaciones
	 */
	public void showPestanaTablaGestionRepres() {
		layoutRepres.show(this, cardTablaRepres);
	}

	/**
	 * Metodo para obtener la pestana de gestion de representaciones en el border
	 * 
	 * @return PestanaGestionRepresentacionesBorder objeto con la pestana
	 */
	public PestanaGestionRepresentacionesBorder getPrestanaGestionRepres() {
		return gestionRepres;
	}

	/**
	 * Metodo para obtener la pestana de tablas de eventos de gestion de
	 * representaciones
	 * 
	 * @return PestanaTablaEventosGestionRepresentaciones objeto con la pestana
	 */
	public PestanaTablaEventosGestionRepresentaciones getPestanaTablaEventos() {
		return tablaEventos;
	}

	/**
	 * Metodo para obtener la pestana de tablas de representaciones de gestion de
	 * representaciones
	 * 
	 * @return PestanaTablaRepresGestionRepresentaciones objeto con la pestana
	 */
	public PestanaTablaRepresGestionRepresentaciones getPestanaTablaRepres() {
		return tablaRepres;
	}
}
