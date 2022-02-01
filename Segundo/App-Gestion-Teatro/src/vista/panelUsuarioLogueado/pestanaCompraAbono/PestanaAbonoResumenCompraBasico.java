package vista.panelUsuarioLogueado.pestanaCompraAbono;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase PestanaAbonoResumenCompraBasico para la pestana de resumen de compra
 * basico
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaAbonoResumenCompraBasico extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4739912655519115000L;

	private JLabel lblNombre = new JLabel("Nombre Elemento: ");
	private JTextField txtNombre = new JTextField(10);

	private JLabel lblZona = new JLabel("Zona Asociada: ");
	private JTextField txtZona = new JTextField(10);

	private JLabel lblPrecio = new JLabel("Precio: ");
	private JTextField txtPrecio = new JTextField(10);

	private JLabel lblElementos = new JLabel("Permite acceso a:");

	private GridLayout layoutCompra = new GridLayout(4, 2, 25, 25);

	/**
	 * Constructor de PestanaAbonoResumenCompraBasico
	 */
	public PestanaAbonoResumenCompraBasico() {
		setLayout(layoutCompra);

		txtNombre.setEditable(false);
		txtZona.setEditable(false);
		txtPrecio.setEditable(false);

		anadirComponentes();
	}

	/**
	 * Metodo para anadir los componentes
	 */
	private void anadirComponentes() {

		add(lblNombre);
		add(txtNombre);
		add(lblZona);
		add(txtZona);
		add(lblPrecio);
		add(txtPrecio);
		add(lblElementos);
		add(new JLabel(""));
	}

	/**
	 * Metodo para establecer el nombre del abono en el text field
	 * 
	 * @param elemns String con el nomrbe
	 */
	public void setTxtNombreAbono(String elemns) {
		txtNombre.setText(elemns);
	}

	/**
	 * Metodo para establecer la zona del abono en el text field
	 * 
	 * @param elemns String con la zona
	 */
	public void setTxtZonaAbono(String elemns) {
		txtZona.setText(elemns);
	}

	/**
	 * Metodo para establecer el precio del abono en el text field
	 * 
	 * @param elemens String el precio
	 */
	public void setTxtPrecioAbono(String elemens) {
		txtPrecio.setText(elemens);
	}

	/**
	 * Metodo para limpiar los text field
	 */
	public void limpiarElementosAbono() {
		txtNombre.setText("");
		txtZona.setText("");
		txtPrecio.setText("");
	}
}
