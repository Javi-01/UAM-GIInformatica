package vista.panelUsuarioLogueado.pestanaCompraAbono;

import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * Clase PestanaResumenCompraAbono para la pestana de resumen compra de abono
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaResumenCompraAbono extends JPanel {

	private static final long serialVersionUID = -1027987849186207565L;

	private PestanaAbonoResumenCompraBasico arribaCompraAbono;
	private PestanaAbonoResumenCompraMasInfo abajoCompraAbono;

	private GridLayout layoutCompra = new GridLayout(2, 1);

	/**
	 * Constructor de PestanaCompraAbonoWest
	 */
	public PestanaResumenCompraAbono() {

		setLayout(layoutCompra);
		arribaCompraAbono = new PestanaAbonoResumenCompraBasico();
		abajoCompraAbono = new PestanaAbonoResumenCompraMasInfo();

		add(arribaCompraAbono);
		add(abajoCompraAbono);
	}

	/**
	 * Metodo para obtener la pestana de resumen de compra de abono basico
	 * 
	 * @return PestanaAbonoResumenCompraBasico con la pesatana
	 */
	public PestanaAbonoResumenCompraBasico getArribaCompraAbono() {
		return arribaCompraAbono;
	}

	/**
	 * Metodo para obtener la pestana de resumen de compra de abono con mas info
	 * 
	 * @return PestanaAbonoResumenCompraMasInfo con la pesatana
	 */
	public PestanaAbonoResumenCompraMasInfo getAbajoCompraAbono() {
		return abajoCompraAbono;
	}

}