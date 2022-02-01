package vista.panelAdministrador.pestanaGestionRepresentaciones.tabla;

import java.awt.GridLayout;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.evento.Evento;
import modelo.evento.Representacion;

/**
 * Clase PestanaTablaRepresGestionRepresentaciones para la pestana de la tabla
 * de representaciones en gestion representaciones
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaTablaRepresGestionRepresentaciones extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 361088551080335817L;

	private static final String[] nombresTablaRepresentaciones = { "Titulo Evento", "Fecha de Realizacion", "Hora",
			"Entradas Disponibles" };
	private String[] registrosTablaRepresentaciones = new String[4];

	private DefaultTableModel modeloTablaRepresentacion;
	private JTable tablaRepresentacion;

	/**
	 * Constructor de PestanaTablaRepresGestionRepresentaciones
	 */
	public PestanaTablaRepresGestionRepresentaciones() {

		TitledBorder centro = new TitledBorder("Seleccion de Representaciones");
		setBorder(centro);
		setLayout(new GridLayout(1, 1));
		modeloTablaRepresentacion = new DefaultTableModel(null, nombresTablaRepresentaciones) {
			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tablaRepresentacion = new JTable(modeloTablaRepresentacion);
		add(new JScrollPane(tablaRepresentacion));
	}

	/**
	 * Metodo para obtener la tabla de representaciones
	 * 
	 * @return JTable con la tabla de representaciones
	 */
	public JTable getTablaRepresentacion() {
		return tablaRepresentacion;
	}

	/**
	 * Metodo para obtener el modelo de la tabla de representaciones
	 * 
	 * @return DefaultTableModel con el modelo de la tabla de representaciones
	 */
	public DefaultTableModel getTablaModelo() {
		return modeloTablaRepresentacion;
	}

	/**
	 * Metodo para establecer el controlador de la tabla de representaciones
	 * 
	 * @param m MouseListener con el controlador
	 */
	public void setControlador(MouseListener m) {
		tablaRepresentacion.addMouseListener(m);
	}

	/**
	 * Metodo para actualizar la tabla de representaciones
	 * 
	 * @param eve Evento con el evento a actualizar
	 */
	public void actualizarTablaRepresentaciones(Evento eve) {

		for (Representacion r : eve.getEventoRepresentaciones()) {
			registrosTablaRepresentaciones[0] = eve.getEventoTitulo();
			registrosTablaRepresentaciones[1] = String.valueOf(String.valueOf(r.getRepresentacionFecha().getYear()))
					+ "-" + String.valueOf(r.getRepresentacionFecha().getMonthValue()) + "-"
					+ String.valueOf(r.getRepresentacionFecha().getDayOfMonth());
			registrosTablaRepresentaciones[2] = String.valueOf(r.getRepresentacionFecha().getHour()) + ":"
					+ String.valueOf(r.getRepresentacionFecha().getMinute());
			registrosTablaRepresentaciones[3] = String.valueOf(r.getRepresentacionAforoDisponible()) + " / "
					+ String.valueOf(r.getRepresentacionAforoTotal());
			modeloTablaRepresentacion.addRow(registrosTablaRepresentaciones);
		}
	}

	/**
	 * Metodo para destruir la tabla de representaciones
	 */
	public void destruirTablaRepresentaciones() {

		int contRow = modeloTablaRepresentacion.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaRepresentacion.removeRow(0);
		}
	}
}
