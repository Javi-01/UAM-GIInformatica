package vista.panelAdministrador.pestanaEstadisticas.ocupacion;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.evento.Evento;

/**
 * Clase CardEstadisticasCenterEventoOcupacion para el card center de las
 * estadisticas por evento por ocupacion
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardEstadisticasCenterEventoOcupacion extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7526136508820406139L;

	private static final String[] nombresTablaEstadisticasOcupacion = { "Zona", "Ocupacion/Evento" };

	private String[] registrosTablaEstadisticas = new String[2];

	private DefaultTableModel modeloTablaEventoOcupacion;
	private JTable tablaEstadisticas;

	/**
	 * Constructor de CardEstadisticasCenterEventoOcupacion
	 */
	public CardEstadisticasCenterEventoOcupacion() {

		TitledBorder centro = new TitledBorder("Estadisticas por Ocupacion para el Evento");
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
	 * Metodo para actualizar la tabla de estadisticas por evento por ocupacion
	 * 
	 * @param e Evento que actualizar
	 */
	public void actualizarTablaEstadisticasEventoOcupacion(Evento e) {

		String[] clavesEstadisticas = e.getEventoEstadisticaOcupacion().keySet().toArray(new String[0]);

		for (String clave : clavesEstadisticas) {

			registrosTablaEstadisticas[0] = clave;
			registrosTablaEstadisticas[1] = String.valueOf(e.getEventoEstadisticaOcupacion().get(clave));

			modeloTablaEventoOcupacion.addRow(registrosTablaEstadisticas);
		}
	}

	/**
	 * Metodo para destruir las tablas de estadisticas por evento por ocupacion
	 */
	public void destruirTablaEstadisticasOcupacion() {

		int contRow = modeloTablaEventoOcupacion.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaEventoOcupacion.removeRow(0);
		}
	}
}
