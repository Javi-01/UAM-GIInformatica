package vista.panelPrincipal.pestanaRepresentaciones;

import java.awt.GridLayout;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.evento.Evento;
import modelo.evento.Representacion;
import modelo.sistema.Sistema;

/**
 * Clase PestanaFiltrarRepresentacionesCenter para la pesatna de filtro de
 * representaciones
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaFiltrarRepresentacionesCenter extends JPanel {

	private static final long serialVersionUID = -6781365079816003830L;

	private static final String[] nombresTablaRepresentaciones = { "Titulo Evento", "Fecha de Realizacion", "Hora",
			"Entradas Disponibles" };
	private String[] registrosTablaRepresentaciones = new String[4];

	private DefaultTableModel modeloTablaRepresentacion;
	private JTable tablaRepresentacion;

	/**
	 * Constructor de PestanaFiltrarRepresentacionesCenter
	 */
	public PestanaFiltrarRepresentacionesCenter() {

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
		actualizarTablaRepresentaciones("Danza");
		tablaRepresentacion = new JTable(modeloTablaRepresentacion);
		add(new JScrollPane(tablaRepresentacion));
	}

	/**
	 * Metodo para obtener la tabla
	 * 
	 * @return JTable con la tabla
	 */
	public JTable getTablaRepresentacion() {
		return tablaRepresentacion;
	}

	/**
	 * Metodo para obtener el modelo de la tabla
	 * 
	 * @return DefaultTableModel con el modelo de la tabla
	 */
	public DefaultTableModel getTablaModelo() {
		return modeloTablaRepresentacion;
	}

	/**
	 * Metodo para establecer el controlador de la tabla
	 * 
	 * @param m MouseListener con el controlador
	 */
	public void setControlador(MouseListener m) {
		tablaRepresentacion.addMouseListener(m);
	}

	/**
	 * Metodo para actualizar la tabla de las representaciones
	 * 
	 * @param tipoEvento String con el tipo de evento a actualizar
	 */
	public void actualizarTablaRepresentaciones(String tipoEvento) {
		for (Evento e : Sistema.getInstance().getSistemaEventos()) {
			if (tipoEvento.equals(e.getEventoTipoString())) {
				for (Representacion r : e.getEventoRepresentaciones()) {
					registrosTablaRepresentaciones[0] = e.getEventoTitulo();
					registrosTablaRepresentaciones[1] = String
							.valueOf(String.valueOf(r.getRepresentacionFecha().getYear())) + "-"
							+ String.valueOf(r.getRepresentacionFecha().getMonthValue()) + "-"
							+ String.valueOf(r.getRepresentacionFecha().getDayOfMonth());
					registrosTablaRepresentaciones[2] = String.valueOf(r.getRepresentacionFecha().getHour()) + ":"
							+ String.valueOf(r.getRepresentacionFecha().getMinute());
					registrosTablaRepresentaciones[3] = String.valueOf(r.getRepresentacionAforoDisponible()) + " / "
							+ String.valueOf(r.getRepresentacionAforoTotal());
					modeloTablaRepresentacion.addRow(registrosTablaRepresentaciones);
				}
			}
		}
	}

	/**
	 * Metodo limpiar la tabla
	 */
	public void destruirTablaRepresentaciones() {

		int contRow = modeloTablaRepresentacion.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaRepresentacion.removeRow(0);
		}
	}
}
