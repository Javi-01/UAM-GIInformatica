package vista.panelUsuarioLogueado.misEntradas;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.entrada.Entrada;

/**
 * Clase CardMisEntradasReservadas para el card del panel de mis entradas
 * reservadas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardMisEntradasReservadas extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3126108226686168526L;
	private static final String[] nombresTablaMisEntradasReservadas = { "Identificador", "Representacion", "Hora",
			"Zona", "Fecha Validez", "Precio" };

	private String[] registrosTablaMisEntradasCompradas = new String[6];

	private DefaultTableModel modeloTablaMisEntradas;
	private JTable tablaMisEntradas;

	/**
	 * Constructor de CardMisEntradasReservadas
	 */
	public CardMisEntradasReservadas() {

		TitledBorder centro = new TitledBorder("Vista de entradas reservadas (Selecciona para mas opciones)");
		setBorder(centro);
		setLayout(new GridLayout(1, 1));

		modeloTablaMisEntradas = new DefaultTableModel(null, nombresTablaMisEntradasReservadas) {

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
	 * Metodo para obtener la tabla de las entradas reservadas
	 * 
	 * @return JTable con la tabla de entradas
	 */
	public JTable getTablaEntradas() {
		return tablaMisEntradas;
	}

	/**
	 * Metodo para obtener el modelo de la tabla de las entradas reservadas
	 * 
	 * @return DefaultTableModel con el modelo de la tabla
	 */
	public DefaultTableModel getTablaModeloEntradas() {
		return modeloTablaMisEntradas;
	}

	/**
	 * Metodo para establecer el controlador de la tabla
	 * 
	 * @param m MouseListener con el controlador
	 */
	public void setControlador(MouseListener m) {
		tablaMisEntradas.addMouseListener(m);
	}

	/**
	 * Metodo para actualizar la tabla de las entradas reservadas
	 * 
	 * @param entradas ArrayList con las entradas reservadas
	 */
	public void actualizarTablaMisEntradasReservadas(ArrayList<Entrada> entradas) {
		for (Entrada ent : entradas) {

			registrosTablaMisEntradasCompradas[0] = String.valueOf(ent.getCodigoValidacion());
			registrosTablaMisEntradasCompradas[1] = String
					.valueOf(String.valueOf(ent.getEntradaRepresentacion().getRepresentacionFecha().getYear())) + "-"
					+ String.valueOf(ent.getEntradaRepresentacion().getRepresentacionFecha().getMonthValue()) + "-"
					+ String.valueOf(ent.getEntradaRepresentacion().getRepresentacionFecha().getDayOfMonth());

			registrosTablaMisEntradasCompradas[2] = String
					.valueOf(ent.getEntradaRepresentacion().getRepresentacionFecha().getHour()) + ":"
					+ String.valueOf(ent.getEntradaRepresentacion().getRepresentacionFecha().getMinute());

			registrosTablaMisEntradasCompradas[3] = ent.getEntradaZona().getZonaNombre();
			registrosTablaMisEntradasCompradas[4] = String
					.valueOf(String.valueOf(ent.getFechaValidezEntrada().getYear())) + "-"
					+ String.valueOf(ent.getFechaValidezEntrada().getMonthValue()) + "-"
					+ String.valueOf(ent.getFechaValidezEntrada().getDayOfMonth()) + " "
					+ String.valueOf(ent.getFechaValidezEntrada().getHour()) + ":"
					+ String.valueOf(ent.getFechaValidezEntrada().getMinute());

			registrosTablaMisEntradasCompradas[5] = String.valueOf(ent.getPrecioEntrada());

			modeloTablaMisEntradas.addRow(registrosTablaMisEntradasCompradas);
		}
	}

	/**
	 * Metodo para limpiar la tabla
	 */
	public void destruirTablaMisEntradasReservadas() {

		int contRow = modeloTablaMisEntradas.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaMisEntradas.removeRow(0);
		}
	}

}
