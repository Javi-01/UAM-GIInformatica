package vista.panelUsuarioLogueado.ventanaCompraEntradas;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PestanaComprarEntradaVolver extends JPanel{

	private static final long serialVersionUID = -1952112421335671467L;

	private GridLayout CompraSurLayout = new GridLayout(1, 1, 10, 10);
	
	private JButton btVolver = new JButton("Volver");
	
	public PestanaComprarEntradaVolver () {
		setLayout(CompraSurLayout);
		
		anadirComponentes();
	}

	public void anadirComponentes() {
		add(btVolver);
	}
	
	public void setControlador (ActionListener e) {
		btVolver.addActionListener(e);
	}
}
