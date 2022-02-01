package vista.panelUsuarioLogueado.misEntradas;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.entrada.Entrada;

/**
 * Clase CardMisEntradasCompradas para el card del panel de mis entradas compradas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardMisEntradasCompradas extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4623366990371141767L;

	private static final String[] nombresTablaMisEntradas = { "Identificador", "Representacion", "Hora", "Zona" };
	private String[] registrosTablaMisEntradas = new String[4];

	private DefaultTableModel modeloTablaMisEntradas;
	private JTable tablaMisEntradas;

	/**
	 * Constructor de VentanaNumeroDeEntradasSentado
	 */
	public CardMisEntradasCompradas() {

		TitledBorder centro = new TitledBorder("Vista de entradas compradas");
		setBorder(centro);

		setLayout(new GridLayout(1, 1));

		modeloTablaMisEntradas = new DefaultTableModel(null, nombresTablaMisEntradas) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaMisEntradas = new JTable(modeloTablaMisEntradas);
		add(new JScrollPane(tablaMisEntradas));
	}

	/**
	 * Metodo para actualizar la tabla de las entradas compradas
	 * 
	 * @param entradas ArrayList con las entradas compradas
	 */
	public void actualizarTablaMisEntradasCompradas(ArrayList<Entrada> entradas) {
		for (Entrada ent : entradas) {

			registrosTablaMisEntradas[0] = String.valueOf(ent.getCodigoValidacion());
			registrosTablaMisEntradas[1] = String
					.valueOf(String.valueOf(ent.getEntradaRepresentacion().getRepresentacionFecha().getYear())) + "-"
					+ String.valueOf(ent.getEntradaRepresentacion().getRepresentacionFecha().getMonthValue()) + "-"
					+ String.valueOf(ent.getEntradaRepresentacion().getRepresentacionFecha().getDayOfMonth());

			registrosTablaMisEntradas[2] = String
					.valueOf(ent.getEntradaRepresentacion().getRepresentacionFecha().getHour()) + ":"
					+ String.valueOf(ent.getEntradaRepresentacion().getRepresentacionFecha().getMinute());

			registrosTablaMisEntradas[3] = ent.getEntradaZona().getZonaNombre();
			modeloTablaMisEntradas.addRow(registrosTablaMisEntradas);
		}
	}

	/**
	 * Metodo para limpiar la tabla
	 */
	public void destruirTablaMisEntradasCompradas() {

		int contRow = modeloTablaMisEntradas.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaMisEntradas.removeRow(0);
		}
	}
}
