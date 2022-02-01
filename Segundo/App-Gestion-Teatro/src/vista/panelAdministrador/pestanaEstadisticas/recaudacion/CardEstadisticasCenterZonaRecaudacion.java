package vista.panelAdministrador.pestanaEstadisticas.recaudacion;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.sistema.Sistema;
import modelo.zona.Zona;

/**
 * Clase CardEstadisticasCenterZonaRecaudacion para el card center de las
 * estadisticas por zonas por recaudacion
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardEstadisticasCenterZonaRecaudacion extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2920939826244592567L;

	private static final String[] nombresTablaEstadisticasRecaudacion = { "Evento", "Recaudacion/Zona" };

	private String[] registrosTablaEstadisticas = new String[2];

	private DefaultTableModel modeloTablaEventoRecaudacion;
	private JTable tablaEstadisticas;

	/**
	 * Constructor de CardEstadisticasCenterZonaOcupacion
	 */
	public CardEstadisticasCenterZonaRecaudacion() {

		TitledBorder centro = new TitledBorder("Estadisticas por Recaudacion para una Zona");
		setBorder(centro);
		setLayout(new GridLayout(1, 1));

		modeloTablaEventoRecaudacion = new DefaultTableModel(null, nombresTablaEstadisticasRecaudacion) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaEstadisticas = new JTable(modeloTablaEventoRecaudacion);
		add(new JScrollPane(tablaEstadisticas));
	}

	/**
	 * Metodo para actualizar la tabla de estadisticas por zonas por recaudacion
	 * 
	 * @param z Zona con que actualizar
	 */
	public void actualizarTablaEstadisticasZonaRecaudacion(Zona z) {

		String[] clavesEstadisticas = Sistema.getInstance().getSistemaEstadisticasRecaudacionEventosPorZona(z).keySet()
				.toArray(new String[0]);

		for (String clave : clavesEstadisticas) {

			registrosTablaEstadisticas[0] = clave;
			registrosTablaEstadisticas[1] = String
					.valueOf(Sistema.getInstance().getSistemaEstadisticasRecaudacionEventosPorZona(z).get(clave));

			modeloTablaEventoRecaudacion.addRow(registrosTablaEstadisticas);
		}
	}

	/**
	 * Metodo para destruir las tablas de stadisticas por zonas por recaudacion
	 */
	public void destruirTablaEstadisticasRecaudacion() {

		int contRow = modeloTablaEventoRecaudacion.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaEventoRecaudacion.removeRow(0);
		}
	}
}
