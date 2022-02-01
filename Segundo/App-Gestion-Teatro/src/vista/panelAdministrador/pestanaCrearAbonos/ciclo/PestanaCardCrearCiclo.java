package vista.panelAdministrador.pestanaCrearAbonos.ciclo;

import java.awt.GridLayout;

import javax.swing.JPanel;

import vista.panelAdministrador.pestanaCrearAbonos.ciclo.funciones.BotonCrearCiclo;
import vista.panelAdministrador.pestanaCrearAbonos.ciclo.funciones.RellenarCamposCrearCiclo;
import vista.panelAdministrador.pestanaCrearAbonos.ciclo.funciones.TablaElementosEnCiclo;

/**
 * Clase PestanaCardCrearCiclo para el card para crear un ciclo
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCardCrearCiclo extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3844117879823240464L;

	private GridLayout layoutCrearCiclo = new GridLayout(3, 1, 5, 5);

	private RellenarCamposCrearCiclo camposCiclo;
	private TablaElementosEnCiclo tablaElementos;
	private BotonCrearCiclo botonCrearCiclo;

	/**
	 * Constructor de PestanaCardCrearCiclo
	 */
	public PestanaCardCrearCiclo() {
		setLayout(layoutCrearCiclo);

		camposCiclo = new RellenarCamposCrearCiclo();
		tablaElementos = new TablaElementosEnCiclo();
		botonCrearCiclo = new BotonCrearCiclo();

		add(camposCiclo);
		add(tablaElementos);
		add(botonCrearCiclo);
	}

	/**
	 * Metodo obtener los campos de ciclo
	 * 
	 * @return RellenarCamposCrearCiclo objeto con los campos de ciclo
	 */
	public RellenarCamposCrearCiclo getCamposCiclo() {
		return camposCiclo;
	}

	/**
	 * Metodo obtener la tabla con los elementos del ciclo
	 * 
	 * @return TablaElementosEnCiclo objeto con los elementos del ciclo
	 */
	public TablaElementosEnCiclo getTablaElementos() {
		return tablaElementos;
	}

	/**
	 * Metodo obtener el boton para crear el ciclo
	 * 
	 * @return BotonCrearCiclo objeto el boton para crear el ciclo
	 */
	public BotonCrearCiclo getBotonCrearCiclo() {
		return botonCrearCiclo;
	}

}
