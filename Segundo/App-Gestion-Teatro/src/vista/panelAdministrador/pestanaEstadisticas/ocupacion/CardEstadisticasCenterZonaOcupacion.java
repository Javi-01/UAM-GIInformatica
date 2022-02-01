package vista.panelAdministrador.pestanaEstadisticas.ocupacion;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.sistema.Sistema;
import modelo.zona.Zona;

/**
 * Clase CardEstadisticasCenterZonaOcupacion para el card center de las
 * estadisticas por zonas por ocupacion
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardEstadisticasCenterZonaOcupacion extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9208908221973483791L;

	private static final String[] nombresTablaEstadisticasOcupacion = { "Evento", "Ocupacion/Zona" };

	private String[] registrosTablaEstadisticas = new String[2];

	private DefaultTableModel modeloTablaEventoOcupacion;
	private JTable tablaEstadisticas;

	/**
	 * Constructor de CardEstadisticasCenterZonaOcupacion
	 */
	public CardEstadisticasCenterZonaOcupacion() {

		TitledBorder centro = new TitledBorder("Estadisticas por Ocupacion para una Zona");
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
	}

	/**
	 * Metodo para actualizar la tabla de estadisticas por zonas por ocupacion
	 * 
	 * @param z Zona con que actualizar
	 */
	public void actualizarTablaEstadisticasZonaOcupacion(Zona z) {

		String[] clavesEstadisticas = Sistema.getInstance().getSistemaEstadisticasOcupacionEventosPorZona(z).keySet()
				.toArray(new String[0]);

		for (String clave : clavesEstadisticas) {

			registrosTablaEstadisticas[0] = clave;
			registrosTablaEstadisticas[1] = String
					.valueOf(Sistema.getInstance().getSistemaEstadisticasOcupacionEventosPorZona(z).get(clave));

			modeloTablaEventoOcupacion.addRow(registrosTablaEstadisticas);
		}
	}

	/**
	 * Metodo para destruir las tablas de stadisticas por zonas por ocupacion
	 */
	public void destruirTablaEstadisticasOcupacion() {

		int contRow = modeloTablaEventoOcupacion.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaEventoOcupacion.removeRow(0);
		}
	}
}
