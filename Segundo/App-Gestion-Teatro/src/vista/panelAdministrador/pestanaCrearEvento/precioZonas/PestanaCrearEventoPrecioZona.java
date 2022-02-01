package vista.panelAdministrador.pestanaCrearEvento.precioZonas;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import modelo.sistema.Sistema;
import modelo.zona.Zona;
import modelo.zona.ZonaCompuesta;

/**
 * Clase PestanaCrearEventoPrecioZona para los precios de las zonas al crear un
 * evento
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCrearEventoPrecioZona extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6097747514056693303L;
	private int numZonas = Sistema.getInstance().getSistemaZonas().size();
	private JButton botonCrear = new JButton("Crear");
	private JButton botonCancelar = new JButton("Cancelar");
	private List<JTextField> preciosZonas = new ArrayList<>();
	private GridLayout layoutPrecioZonas = new GridLayout(numZonas + 1, 2, 10, 5);

	/**
	 * Constructor de PestanaCrearEventoPrecioZona
	 */
	public PestanaCrearEventoPrecioZona() {

		TitledBorder centro = new TitledBorder("A�adir precios para cada Zona");
		setBorder(centro);

		setLayout(layoutPrecioZonas);

		for (int i = 0; i < numZonas; i++)
			if (!Sistema.getInstance().getSistemaZonas().get(i).getClass().equals(ZonaCompuesta.class)) {
				anadirComponente(Sistema.getInstance().getSistemaZonas().get(i));
			}

		add(botonCancelar);
		add(botonCrear);
	}

	/**
	 * Metodo para anadir el precio a la zona del evento
	 * 
	 * @param z Zona a anadir
	 */
	public void anadirComponente(Zona z) {
		add(new JLabel("Precio para " + z.getZonaNombre() + ": "));
		JTextField txtPrecio = new JTextField(10);
		preciosZonas.add(txtPrecio);
		add(txtPrecio);
	}

	/**
	 * Metodo para obtener el precio introducido de la zona
	 * 
	 * @param pos Integer con la posicion de la zona del evento
	 * 
	 * @return double con el precio de la zona
	 */
	public double getNombreTxtPrecioZona(Integer pos) {

		try {
			if (Sistema.getInstance().getSistemaZonas().get(pos).getClass().equals(ZonaCompuesta.class)) {
				return 0.1;
			} else {
				JTextField txt = preciosZonas.get(pos);
				return Double.parseDouble(txt.getText());
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "EL valor introducido debe ser un numero", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return 0.0;
	}

	/**
	 * Metodo para limpiar el JTextField de las zonas
	 */
	public void limpiarTxtPrecioZona() {
		for (JTextField txt : preciosZonas) {
			txt.setText("");
		}
	}

	/**
	 * Metodo para obtener el numero de zonas del teatro
	 * 
	 * @return Integer con el numero de zonas
	 */
	public Integer getNumZonasTeatro() {
		return this.numZonas;
	}

	/**
	 * Metodo para establecer el controlador del boton de crear
	 * 
	 * @param e ActionListener con controlador
	 */
	public void setControladorCrear(ActionListener e) {
		botonCrear.addActionListener(e);
	}

	/**
	 * Metodo para establecer el controlador el boton de cancelar
	 * 
	 * @param e ActionListener con controlador
	 */
	public void setControladorCancelar(ActionListener e) {
		botonCancelar.addActionListener(e);
	}
}
