package vista.panelUsuarioLogueado.ventanaCompraEntradas.seleccionInfo;

import java.awt.BorderLayout;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.evento.Representacion;
import modelo.sistema.Sistema;
import modelo.zona.Zona;

/**
 * Clase PestanaComprarEntrada para seleccinoar la zona simple
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardSeleccionarZonaSimpleEntrada extends JPanel {

	private static final long serialVersionUID = 730120146219878903L;

	private static final String[] nombresTablaZonas = { "Zona", "Tipo de Zona", "Aforo Restante" };
	private String[] registrosTablaZonas = new String[3];

	private DefaultTableModel modeloTablaZona;
	private JTable tablaZonas;

	/**
	 * Constructor de CardSeleccionarZonaSimpleEntrada
	 */
	public CardSeleccionarZonaSimpleEntrada() {

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

	public DefaultTableModel getTablaModelo() {
		return modeloTablaZona;
	}

	public JTable getTablaZonas() {
		return tablaZonas;
	}

	public void setControlador(MouseListener m) {
		tablaZonas.addMouseListener(m);
	}

	public void actualizarTablaZonas(Representacion representacion) {

		for (Zona z : Sistema.getInstance().getSistemaZonasNoContenidas()) {

			registrosTablaZonas[0] = z.getZonaNombre();
			registrosTablaZonas[1] = representacion.getRepresentacionTipoEntrada(z);
			registrosTablaZonas[2] = String.valueOf(representacion.getRepresentacionEntradasDisponiblesEnZona(z).size())
					+ "/" + String.valueOf(representacion.getRepresentacionEntradasEnZona(z).size());

			modeloTablaZona.addRow(registrosTablaZonas);

		}
	}

	public void destruirTablaZonas() {

		int contRow = modeloTablaZona.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaZona.removeRow(0);
		}
	}
}
