package vista.panelAdministrador.pestanaZonasYAforo.tablas;

import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.zona.Butaca;
import modelo.zona.Zona;

/**
 * Clase PestanaZonasAforoTablaButacas para las tablas de butacas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaZonasAforoTablaButacas extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3266709808093848477L;

	private static final String[] nombresTablaButacas = { "Zona", "Fila", "Columna" };
	private String[] registrosTablaButacas = new String[3];

	private DefaultTableModel modeloTablaButacas;
	private JTable tablaButacas;

	/**
	 * Constructor de PestanaZonasAforoTablaButacas
	 */
	public PestanaZonasAforoTablaButacas() {

		TitledBorder centro = new TitledBorder("Selecciona la Butaca a deshabilitar");
		setBorder(centro);
		setLayout(new BorderLayout(1, 1));

		modeloTablaButacas = new DefaultTableModel(null, nombresTablaButacas) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaButacas = new JTable(modeloTablaButacas);
		add(new JScrollPane(tablaButacas));

	}

	/**
	 * Metodo para obtener el modelo de las tablas
	 * 
	 * @return DefaultTableModel con el modelo de las tablas
	 */
	public DefaultTableModel getTablaModelo() {
		return modeloTablaButacas;
	}

	/**
	 * Metodo para obtener las tablas
	 * 
	 * @return JTable con las tablas
	 */
	public JTable getTablaButacas() {
		return tablaButacas;
	}

	/**
	 * Metodo para establecer el controlador la tabla de zonas
	 * 
	 * @param m MouseListener con el controlador
	 */
	public void setControlador(MouseListener m) {
		tablaButacas.addMouseListener(m);
	}

	/**
	 * Metodo para actualizar la tabla de las zonas y butacas
	 * 
	 * @param butacas ArrayList de las butacas a actualizar
	 * @param z       Zona a actualizar
	 */
	public void actualizarTablaButacas(ArrayList<Butaca> butacas, Zona z) {

		for (Butaca b : butacas) {

			registrosTablaButacas[0] = z.getZonaNombre();
			registrosTablaButacas[1] = String.valueOf(b.getButacaFila());
			registrosTablaButacas[2] = String.valueOf(b.getButacaColumna());

			modeloTablaButacas.addRow(registrosTablaButacas);
		}
	}

	/**
	 * Metodo para destruir la tabla
	 */
	public void destruirTablaButacas() {

		int contRow = modeloTablaButacas.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaButacas.removeRow(0);
		}
	}
}
