package vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito.vistaComprarSentado;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * Clase CardBotonesElegirSentado card para los botones para poder elegir en sentado
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardBotonesElegirSentado extends JPanel{

	private static final long serialVersionUID = -7287663609533396755L;

	private JButton btAceptarSugerencia = new JButton("Aceptar sugerencia");
	private JButton btElegirEntradas = new JButton("Elegir entradas");
	
	private GridLayout layoutEleccionEntradas = new GridLayout(2, 2, 10, 10);
	/**
	 * Constructor de CardBotonesElegirSentado
	 */
	public CardBotonesElegirSentado() {
		setLayout(layoutEleccionEntradas);
		setPreferredSize(new Dimension(400, 300));
		add(new JLabel(""));
		add(new JLabel(""));
		add(btAceptarSugerencia);
		add(btElegirEntradas);
	}
	
	public void setControladorSugerencia (ActionListener e) {
		btAceptarSugerencia.addActionListener(e);
	}
	
	public void setControladorElegir (ActionListener e) {
		btElegirEntradas.addActionListener(e);
	}
}
