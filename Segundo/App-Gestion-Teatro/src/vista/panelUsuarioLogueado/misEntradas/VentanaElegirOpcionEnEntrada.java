package vista.panelUsuarioLogueado.misEntradas;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Clase VentanaElegirOpcionEnEntrada para la ventana de elegir la opcion de
 * entrada
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class VentanaElegirOpcionEnEntrada extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3372911458543697794L;

	private JLabel lblSeleccion = new JLabel("Introduce una eleccion: ");

	private JButton btCancelar = new JButton("Cancelar");
	private JButton btComprar = new JButton("Comprar");

	private GridLayout registroLayout = new GridLayout(3, 2, 10, 15);

	/**
	 * Constructor de VentanaElegirOpcionEnEntrada
	 */
	public VentanaElegirOpcionEnEntrada() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Compra");
		setBounds(0, 0, 400, 150);
		setLocationRelativeTo(null);
		setLayout(registroLayout);

		anadirComponentes();

	}

	/**
	 * Metodo para anadir los componentes
	 */
	public void anadirComponentes() {

		add(lblSeleccion);
		add(new JLabel(""));
		add(btComprar);
		add(btCancelar);
	}

	/**
	 * Metodo para establecer el controlador del boton de comprar
	 * 
	 * @param c ActionListener con el controlador
	 */
	public void setControladorComprar(ActionListener c) {
		btComprar.addActionListener(c);
	}

	/**
	 * Metodo para establecer el controlador del boton de cancelar
	 * 
	 * @param c ActionListener con el controlador
	 */
	public void setControladorCancelar(ActionListener c) {
		btCancelar.addActionListener(c);
	}
}
