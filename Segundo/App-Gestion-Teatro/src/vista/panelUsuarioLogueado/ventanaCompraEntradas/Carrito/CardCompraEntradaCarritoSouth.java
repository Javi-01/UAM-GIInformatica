package vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
/**
 * Clase CardCompraEntradaCarritoSouth card south para el carrito para la compra de
 * entradas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCompraEntradaCarritoSouth extends JPanel{

	private static final long serialVersionUID = 7285331636669534535L;
	
	private GridLayout layoutBotones = new GridLayout(1, 2, 10, 10);
	private JButton btCompra = new JButton("Confirmar");
	private JButton btCancelar = new JButton("Reservar");
	/**
	 * Constructor de CardCompraEntradaCarritoSouth
	 */
	public CardCompraEntradaCarritoSouth () {
		setLayout(layoutBotones);
		
		add(btCancelar);
		add(btCompra);
	}
	
	public void setControladorComprarEntrada(ActionListener e) {
		btCompra.addActionListener(e);
	}
	
	public void setControladorReservarEntrada(ActionListener e) {
		btCancelar.addActionListener(e);
	}
}
