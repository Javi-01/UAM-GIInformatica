package vista.panelAdministrador.pestanaEstadisticas.recaudacion;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.evento.Evento;

/**
 * Clase CardEstadisticasCenterEventoRecaudacion para el card center de las
 * estadisticas por evento por recaudacion
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardEstadisticasCenterEventoRecaudacion extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5343669923838280745L;

	private static final String[] nombresTablaEstadisticasRecaudacion = { "Zona", "Recaudacion/Evento" };

	private String[] registrosTablaEstadisticas = new String[2];

	private DefaultTableModel modeloTablaEventoRecaudacion;
	private JTable tablaEstadisticas;

	/**
	 * Constructor de CardEstadisticasCenterEventoRecaudacion
	 */
	public CardEstadisticasCenterEventoRecaudacion() {

		TitledBorder centro = new TitledBorder("Estadisticas por Recaudacion para el Evento");
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
	 * Metodo para actualizar la tabla de estadisticas por evento por recaudacion
	 * 
	 * @param e Evento que actualizar
	 */
	public void actualizarTablaEstadisticasEventoRecaudacion(Evento e) {

		String[] clavesEstadisticas = e.getEventoEstadisticaRecaudacion().keySet().toArray(new String[0]);

		for (String clave : clavesEstadisticas) {

			registrosTablaEstadisticas[0] = clave;
			registrosTablaEstadisticas[1] = String.valueOf(e.getEventoEstadisticaRecaudacion().get(clave));

			modeloTablaEventoRecaudacion.addRow(registrosTablaEstadisticas);
		}
	}

	/**
	 * Metodo para destruir las tablas de estadisticas por evento por recaudacion
	 */
	public void destruirTablaEstadisticasRecaudacion() {

		int contRow = modeloTablaEventoRecaudacion.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaEventoRecaudacion.removeRow(0);
		}
	}
}
