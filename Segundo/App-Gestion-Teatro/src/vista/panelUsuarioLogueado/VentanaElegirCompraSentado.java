package vista.panelUsuarioLogueado;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controlador.entrada.ControladorBotonElegirEntradasSentado;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito.vistaComprarSentado.CardBotonesElegirSentado;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito.vistaComprarSentado.CardElegirEntradasSentado;

/**
 * Clase VentanaElegirCompraSentado para el panel de elegir compra sentado
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class VentanaElegirCompraSentado extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6258026951141517167L;

	private final static String cardMenu = "Menu";
	private final static String cardElegir = "Elegir sentado";

	private CardBotonesElegirSentado menuSugerencias;
	private CardElegirEntradasSentado elegirEntradas;

	private CardLayout layoutSentado = new CardLayout();

	private JPanel paneles;

	/**
	 * Constructor de VentanaElegirCompraSentado
	 */
	public VentanaElegirCompraSentado() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(true);
		setTitle("Elegir como comprar");
		setBounds(0, 0, 500, 150);
		setLocationRelativeTo(null);

		paneles = new JPanel(layoutSentado);
		menuSugerencias = new CardBotonesElegirSentado();
		elegirEntradas = new CardElegirEntradasSentado();

		paneles.add(menuSugerencias, cardMenu);
		paneles.add(elegirEntradas, cardElegir);

		add(paneles);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				PanelUsuarioLogueado.getVentanaElegirCompraSentado().setVisible(true);
			}
		});

		ControladorBotonElegirEntradasSentado cElegir = new ControladorBotonElegirEntradasSentado(this);
		getCardMenuSugerencias().setControladorElegir(cElegir);

	}

	/**
	 * Metodo para mostrar el LayOut del menu de sugerencias
	 */
	public void showCardMenuSugerencias() {
		layoutSentado.show(paneles, cardMenu);
	}

	/**
	 * Metodo para mostrar el LayOut de elegir entradas
	 */
	public void showCardElegirEntradas() {
		layoutSentado.show(paneles, cardElegir);
	}

	/**
	 * Metodo para obtener el Card de los botones para elegir sentado
	 * 
	 * @return CardBotonesElegirSentado con el card
	 */
	public CardBotonesElegirSentado getCardMenuSugerencias() {
		return menuSugerencias;
	}

	/**
	 * Metodo para obtener el Card para elegir entradas sentado
	 * 
	 * @return CardElegirEntradasSentado con el card
	 */
	public CardElegirEntradasSentado getCardElegirEntradas() {
		return elegirEntradas;
	}
}
