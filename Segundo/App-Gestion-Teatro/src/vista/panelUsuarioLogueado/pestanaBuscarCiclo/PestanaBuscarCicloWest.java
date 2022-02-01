package vista.panelUsuarioLogueado.pestanaBuscarCiclo;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Clase PestanaBuscarCicloCenter para la pestana de buscar ciclo west
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaBuscarCicloWest extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 728679869346462349L;

	private JTextField txtBuscarCiclo = new JTextField(12);
	private JButton btBuscarCiclo = new JButton("Buscar");

	/**
	 * Constructor de PestanaBuscarCicloWest
	 */
	public PestanaBuscarCicloWest() {

		TitledBorder oeste = new TitledBorder("Introduce el nombre");
		setBorder(oeste);
		anadirComponentes();
	}

	/**
	 * Metodo para anadir los componentes
	 */
	public void anadirComponentes() {
		add(txtBuscarCiclo);
		add(btBuscarCiclo);
	}

	/**
	 * Metodo para obtener la dimension establecida
	 * 
	 * @return Dimension con la dimension
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(160, 60);
	}

	/**
	 * Metodo para establecer el controlador de boton de buscar ciclo
	 * 
	 * @param c ActionListener con el controlador
	 */
	public void setControlador(ActionListener c) {
		btBuscarCiclo.addActionListener(c);
	}

	/**
	 * Metodo para obtener el cilo a buscar del JtextField
	 * 
	 * @return String el cilo a buscar
	 */
	public String getPestanaNombreBuscarCiclo() {
		return txtBuscarCiclo.getText();
	}

	/**
	 * Metodo para limpiar el JtextField
	 */
	public void limpiarPestanaBuscarCiclo() {
		txtBuscarCiclo.setText("");
	}
}
