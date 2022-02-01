package vista.panelUsuarioLogueado.ventanaCompraEntradas;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

import javax.swing.*;

import modelo.entrada.Entrada;
import modelo.evento.Representacion;
import modelo.zona.*;
import vista.panelUsuarioLogueado.PanelUsuarioLogueado;

/**
 * Clase VentanaCompraEntradasSugeridas para la ventana de compra de entradas
 * sugeridas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class VentanaCompraEntradasSugeridas extends JFrame {

	private static final long serialVersionUID = -4344394960107830450L;
	private Zona zona;
	private Representacion repre;

	private ArrayList<Entrada> entradasSugeridas = new ArrayList<Entrada>();
	private GridLayout layoutEntradas;

	/**
	 * Constructor de VentanaCompraEntradasSugeridas
	 * 
	 * @param z        Zona sugerida
	 * @param r        Representacion sugerida
	 * @param entradas ArrayList de entradas sugeridas
	 */
	public VentanaCompraEntradasSugeridas(Zona z, Representacion r, ArrayList<Entrada> entradas) {

		this.repre = r;
		this.zona = z;

		if (entradas != null) {
			this.entradasSugeridas.addAll(entradas);
		}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Mapa de zona sentado " + z.getZonaNombre());
		setBounds(0, 0, 500 + ((ZonaSentado) zona).getFilasMax(), 500 + ((ZonaSentado) zona).getColumnasMax());
		setLocationRelativeTo(null);

		layoutEntradas = new GridLayout(((ZonaSentado) zona).getFilasMax(), ((ZonaSentado) zona).getFilasMax(), 5, 5);
		setLayout(layoutEntradas);

		anadirComponentes();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				PanelUsuarioLogueado.getVentanaElegirCompraSentado().setVisible(false);
			}
		});
	}

	public Integer getNumBotonesCrear(Zona z) {
		return ((ZonaSentado) z).getZonaAforo();
	}

	public ArrayList<Entrada> getEntradasSugeridas() {
		return entradasSugeridas;
	}

	public void anadirComponentes() {
		JButton boton = null;
		Entrada entrada;

		int contTotal = getNumBotonesCrear(zona), sugeridas;

		if (entradasSugeridas.size() > 0) {
			sugeridas = entradasSugeridas.size();
		} else {
			sugeridas = 0;
		}

		for (int i = 0; contTotal > i; i++) {

			entrada = repre.getRepresentacionEntradasEnZona(zona).get(i);
			if (sugeridas > 0) {
				if (entradasSugeridas.contains(entrada)) {
					boton = new JButton("Butaca Sugerida :" + entrada.getCodigoValidacion());
					boton.setBackground(Color.GREEN);
					sugeridas--;
				} else {
					if (!entrada.getEntradaButaca().isButacaHabilitada()) {
						boton = new JButton("Butaca deshabilitada");
						boton.setBackground(Color.DARK_GRAY);

					} else if (entrada.getEntradaOcupada()) {
						boton = new JButton("Butaca Ocupada");
						boton.setBackground(Color.GRAY);

					} else {
						boton = new JButton("Butaca libre: " + entrada.getCodigoValidacion());
					}
				}
			} else {
				if (!entrada.getEntradaButaca().isButacaHabilitada()) {
					boton = new JButton("Butaca deshabilitada");
					boton.setBackground(Color.DARK_GRAY);

				} else if (entrada.getEntradaOcupada()) {
					boton = new JButton("Butaca Ocupada");
					boton.setBackground(Color.GRAY);

				} else {
					boton = new JButton("Butaca libre: " + entrada.getCodigoValidacion());
				}
			}
			add(boton);
		}
	}
}
