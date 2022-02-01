package vista;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import modelo.sistema.Sistema;
import vista.panelAdministrador.*;
import vista.panelPrincipal.*;
import vista.panelUsuarioLogueado.*;

/**
 * Clase para la ventana principal de la interfaz del teatro
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class VentanaFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6459874555729176912L;

	private static CardLayout layoutPaneles;

	final static String tituloVentanaPrincipal = "ventana principal";
	final static String tituloVentanaAdminstrador = "ventana administrador";
	final static String tituloVentanaUsuarioLogeado = "ventana usuario logueado";

	private static JPanel paneles;
	private static JPanel panelPrincipal;
	private static JPanel panelAdministrador;
	private static JPanel panelUsuarioLogeado;

	/**
	 * Constructor de VentanaFrame
	 */
	public VentanaFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 900, 600);
		setResizable(false);
		setTitle("App Teatro");
		setLocationRelativeTo(null);

		layoutPaneles = new CardLayout();
		paneles = new JPanel(layoutPaneles);

		panelPrincipal = new PanelPrincipal();
		panelAdministrador = new PanelAdministrador();
		panelUsuarioLogeado = new PanelUsuarioLogueado();

		paneles.add(panelPrincipal, tituloVentanaPrincipal);
		paneles.add(panelAdministrador, tituloVentanaAdminstrador);
		paneles.add(panelUsuarioLogeado, tituloVentanaUsuarioLogeado);

		add(paneles);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				Sistema.getInstance().sistemaCerrarSesion();
			}
		});
	}

	/**
	 * Metodo para mostrar el panel pincipal
	 */
	public static void showPanelPrincipal() {
		layoutPaneles.show(paneles, tituloVentanaPrincipal);
	}

	/**
	 * Metodo para mostrar el panel de administrador
	 */
	public static void showPanelAdministrador() {
		layoutPaneles.show(paneles, tituloVentanaAdminstrador);
	}

	/**
	 * Metodo para mostrar el panel de usuario logeado
	 */
	public static void showUsuarioLogeado() {
		layoutPaneles.show(paneles, tituloVentanaUsuarioLogeado);
	}

	/**
	 * Metodo static main para ejecutar la aplicacion
	 * 
	 * @param args argumentos de entrada
	 */
	public static void main(String... args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaFrame frame = new VentanaFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
