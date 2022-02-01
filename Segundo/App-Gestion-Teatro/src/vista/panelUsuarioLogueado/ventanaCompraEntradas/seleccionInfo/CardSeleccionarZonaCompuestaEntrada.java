package vista.panelUsuarioLogueado.ventanaCompraEntradas.seleccionInfo;

import java.awt.BorderLayout;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.evento.Representacion;
import modelo.zona.*;

/**
 * Clase PestanaComprarEntrada para seleccinoar la zona compuesta
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardSeleccionarZonaCompuestaEntrada extends JPanel {

	private static final long serialVersionUID = -5893219252503670887L;

	private static final String[] nombresTablaZonas = { "Zona", "Tipo de Zona", "Aforo Restante" };
	private String[] registrosTablaZonas = new String[3];

	private DefaultTableModel modeloTablaZona;
	private JTable tablaZonas;

	/**
	 * Constructor de CardSeleccionarZonaCompuestaEntrada
	 */
	public CardSeleccionarZonaCompuestaEntrada() {

		TitledBorder centro = new TitledBorder("Selecciona la zona del Teatro para la representacion ");
		setBorder(centro);
		setLayout(new BorderLayout(1, 1));

		modeloTablaZona = new DefaultTableModel(null, nombresTablaZonas) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaZonas = new JTable(modeloTablaZona);
		add(new JScrollPane(tablaZonas));

	}

	/**
	 * Metodo para obtener el modelo de la tabla
	 * 
	 * @return DefaultTableModel con el modelo de la tabla
	 */
	public DefaultTableModel getTablaModelo() {
		return modeloTablaZona;
	}

	/**
	 * Metodo para obtener la tabla
	 * 
	 * @return JTable con la tabla
	 */
	public JTable getTablaZonas() {
		return tablaZonas;
	}

	/**
	 * Metodo para establecer el controlador de las tablas
	 * 
	 * @param m MouseListener con el controlador
	 */
	public void setControlador(MouseListener m) {
		tablaZonas.addMouseListener(m);
	}

	/**
	 * Metodo para actualizar la tabla de zonas compuestas
	 * 
	 * @param representacion Representacion a actualizar
	 * @param zona           Zona a actualizar
	 */
	public void actualizarTablaZonasCompuestas(Representacion representacion, Zona zona) {

		for (Zona z : ((ZonaCompuesta) zona).getZonaCompuestaZonas()) {

			registrosTablaZonas[0] = z.getZonaNombre();
			registrosTablaZonas[1] = representacion.getRepresentacionTipoEntrada(z);
			registrosTablaZonas[2] = String.valueOf(representacion.getRepresentacionEntradasDisponiblesEnZona(z).size())
					+ "/" + String.valueOf(representacion.getRepresentacionEntradasEnZona(z).size());

			modeloTablaZona.addRow(registrosTablaZonas);
		}
	}

	/**
	 * Metodo para destruir tablas
	 */
	public void destruirTablaZonasCompuestas() {

		int contRow = modeloTablaZona.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaZona.removeRow(0);
		}
	}
}
