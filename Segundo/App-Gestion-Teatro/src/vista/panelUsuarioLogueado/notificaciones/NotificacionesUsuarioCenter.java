package vista.panelUsuarioLogueado.notificaciones;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.notificacion.Notificacion;

/**
 * Clase NotificacionesUsuarioCenter para el panel de notificaciones de usuario
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class NotificacionesUsuarioCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2154303373465960889L;

	private static final String[] nombresTablaNotificaciones = { "Identificador", "Titulo", "Asunto" };
	private String[] registrosTablaNotificaciones = new String[3];

	private DefaultTableModel modeloTablaNotificaciones;
	private JTable tablaNotificaciones;

	/**
	 * Constructor de NotificacionesUsuarioCenter
	 */
	public NotificacionesUsuarioCenter() {

		TitledBorder centro = new TitledBorder("Vista de notificaciones");
		setBorder(centro);
		setLayout(new GridLayout(1, 1));

		modeloTablaNotificaciones = new DefaultTableModel(null, nombresTablaNotificaciones) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaNotificaciones = new JTable(modeloTablaNotificaciones);
		add(new JScrollPane(tablaNotificaciones));
	}

	/**
	 * Metodo actualizar la tabla de notificaciones
	 * 
	 * @param notificaciones ArrayList con las notificaciones
	 */
	public void actualizarTablaNotificaciones(ArrayList<Notificacion> notificaciones) {
		for (Notificacion n : notificaciones) {
			registrosTablaNotificaciones[0] = String.valueOf(n.getIdentificadorNotificacion());
			registrosTablaNotificaciones[1] = n.getTituloNotificacion();
			registrosTablaNotificaciones[2] = n.getAsuntoNotificacion();

			modeloTablaNotificaciones.addRow(registrosTablaNotificaciones);
		}
	}

	/**
	 * Metodo para obtener la tabla de notificaciones
	 * 
	 * @return JTable con la tabla
	 */
	public JTable getTablaNotificaciones() {
		return tablaNotificaciones;
	}

	/**
	 * Metodo para obtener el modelo de la tabla de notificaciones
	 * 
	 * @return DefaultTableModel con el modelo de la tabla
	 */
	public DefaultTableModel getTablaModeloNotificaciones() {
		return modeloTablaNotificaciones;
	}

	/**
	 * Metodo para establecer el controlador de la tabla
	 * 
	 * @param m MouseListener con el controlador
	 */
	public void setControlador(MouseListener m) {
		tablaNotificaciones.addMouseListener(m);
	}

	/**
	 * Metodo para destruir la tabla
	 */
	public void destruirTablaNotificaciones() {

		int contRow = modeloTablaNotificaciones.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaNotificaciones.removeRow(0);
		}
	}
}
