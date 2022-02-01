package vista.panelAdministrador.pestanaCrearAbonos.ciclo.funciones;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.abono.Ciclo;
import modelo.evento.Evento;

/**
 * Clase TablaElementosEnCiclo para mostrar los elementos de un ciclo
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class TablaElementosEnCiclo extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4175387293724770757L;

	private static final String[] nombresTablaCiclo = { "Elementos del ciclo" };
	private String[] registrosTablaCiclo = new String[1];

	private DefaultTableModel modeloTablaCrearCiclo;
	private JTable tablaEventos;

	/**
	 * Constructor de TablaElementosEnCiclo
	 */
	public TablaElementosEnCiclo() {

		setLayout(new GridLayout(1, 1));

		modeloTablaCrearCiclo = new DefaultTableModel(null, nombresTablaCiclo) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaEventos = new JTable(modeloTablaCrearCiclo);
		add(new JScrollPane(tablaEventos));
	}

	/**
	 * Metodo para actualizar la tabla de los componentes de un ciclo
	 * 
	 * @param ciclo ArrayList con los ciclos actualizados
	 * @param eve   ArrayList con los eventos actualizados
	 */
	public void actualizarTablaCrearCiclo(ArrayList<Ciclo> ciclo, ArrayList<Evento> eve) {

		for (Evento e : eve) {
			registrosTablaCiclo[0] = e.getEventoTitulo();
			modeloTablaCrearCiclo.addRow(registrosTablaCiclo);
		}
		for (Ciclo c : ciclo) {
			registrosTablaCiclo[0] = c.getCicloNombre();
			modeloTablaCrearCiclo.addRow(registrosTablaCiclo);
		}
	}

	/**
	 * Metodo para destruir la tabla de los componentes de un ciclo
	 */
	public void destruirTablaCompraCiclo() {

		int contRow = modeloTablaCrearCiclo.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaCrearCiclo.removeRow(0);
		}
	}

	/**
	 * Metodo para establecer el controlador de la tabla de eventos
	 * 
	 * @param m MouseListener con el controlador a establecer
	 */
	public void setControlador(MouseListener m) {
		tablaEventos.addMouseListener(m);
	}
}
