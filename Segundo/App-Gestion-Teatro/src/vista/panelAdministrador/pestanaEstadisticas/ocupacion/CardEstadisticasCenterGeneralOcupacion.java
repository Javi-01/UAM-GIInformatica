package vista.panelAdministrador.pestanaEstadisticas.ocupacion;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.sistema.Sistema;

/**
 * Clase CardEstadisticasCenterGeneralOcupacion para el card center de las
 * estadisticas generales por ocupacion
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardEstadisticasCenterGeneralOcupacion extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5390148761146419016L;

	private static final String[] nombresTablaEstadisticasOcupacion = { "Eventos", "Ocupacion" };

	private String[] registrosTablaEstadisticas = new String[2];

	private DefaultTableModel modeloTablaEventoOcupacion;
	private JTable tablaEstadisticas;

	/**
	 * Constructor de CardEstadisticasCenterGeneralOcupacion
	 */
	public CardEstadisticasCenterGeneralOcupacion() {

		TitledBorder centro = new TitledBorder("Estadisticas por Ocupacion Total");
		setBorder(centro);
		setLayout(new GridLayout(1, 1));

		modeloTablaEventoOcupacion = new DefaultTableModel(null, nombresTablaEstadisticasOcupacion) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaEstadisticas = new JTable(modeloTablaEventoOcupacion);
		add(new JScrollPane(tablaEstadisticas));
		actualizarTablaEstadisticasGeneralOcupacion();
	}

	/**
	 * Metodo para actualizar la tabla de estadisticas generales por ocupacion
	 */
	public void actualizarTablaEstadisticasGeneralOcupacion() {

		String[] clavesEstadisticas = Sistema.getInstance().getSistemaEstadisticasOcupacionTotalEventos().keySet()
				.toArray(new String[0]);

		for (String clave : clavesEstadisticas) {

			registrosTablaEstadisticas[0] = clave;
			registrosTablaEstadisticas[1] = String
					.valueOf(Sistema.getInstance().getSistemaEstadisticasOcupacionTotalEventos().get(clave));

			modeloTablaEventoOcupacion.addRow(registrosTablaEstadisticas);
		}
	}

	/**
	 * Metodo para destruir las tablas de stadisticas generales por ocupacion
	 */
	public void destruirTablaEstadisticasOcupacion() {

		int contRow = modeloTablaEventoOcupacion.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaEventoOcupacion.removeRow(0);
		}
	}
}
