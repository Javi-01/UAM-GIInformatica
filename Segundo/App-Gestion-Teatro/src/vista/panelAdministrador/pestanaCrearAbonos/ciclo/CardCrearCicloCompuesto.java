package vista.panelAdministrador.pestanaCrearAbonos.ciclo;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.abono.Ciclo;

/**
 * Clase CardCrearCicloCompuesto para el card para crear un ciclo compuesto
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCrearCicloCompuesto extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 859293247525619917L;

	private static final String[] nombresTablaCiclo = { "Nombre", "Zona", "Precio" };
	private String[] registrosTablaCiclo = new String[3];

	private DefaultTableModel modeloTablaCiclos;
	private JTable tablaCiclos;
	private PestanaCrearCicloVolver btVolverCiclo;

	/**
	 * Constructor de CardCrearCicloCompuesto
	 */
	public CardCrearCicloCompuesto() {

		setLayout(new GridLayout(2, 1));

		modeloTablaCiclos = new DefaultTableModel(null, nombresTablaCiclo) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaCiclos = new JTable(modeloTablaCiclos);
		add(new JScrollPane(tablaCiclos));
		btVolverCiclo = new PestanaCrearCicloVolver();
		add(btVolverCiclo);
	}

	/**
	 * Metodo para actualizar las tablas que muestran los ciclos
	 * 
	 * @param ciclosCoincidentes		array de ciclos coincidentes
	 */
	public void actualizarTablaCiclos(ArrayList<Ciclo> ciclosCoincidentes) {
		for (Ciclo c : ciclosCoincidentes) {
			registrosTablaCiclo[0] = c.getCicloNombre();
			registrosTablaCiclo[1] = c.getCicloZona().getZonaNombre();
			registrosTablaCiclo[2] = String.valueOf(c.getPrecio());

			modeloTablaCiclos.addRow(registrosTablaCiclo);
		}
	}

	/**
	 * Metodo para establecer el controlador de la tabla de ciclos
	 * 
	 * @param m MouseListener con el controlador a anadir
	 */
	public void setControlador(MouseListener m) {
		tablaCiclos.addMouseListener(m);
	}

	/**
	 * Metodo para obtener el modelo de tabla de ciclos
	 * 
	 * @return DefaultTableModel con el modelo de la tabla de ciclos
	 */
	public DefaultTableModel getTablaModeloCiclo() {
		return modeloTablaCiclos;
	}

	/**
	 * Metodo para obtener la tabla de ciclos
	 * 
	 * @return JTable con la tabla de ciclos
	 */
	public JTable getTablaCiclo() {
		return tablaCiclos;
	}

	/**
	 * Metodo para destruir la tabla de ciclos
	 */
	public void destruirTablaCiclos() {

		int contRow = modeloTablaCiclos.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaCiclos.removeRow(0);
		}
	}

	/**
	 * Metodo para obtener el boton de volver
	 * 
	 * @return PestanaCrearCicloVolver con la pestana para el boton de volver
	 */
	public PestanaCrearCicloVolver getBtVolverAnadeCiclo() {
		return btVolverCiclo;
	}
}
