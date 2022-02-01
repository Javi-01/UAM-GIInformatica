package vista.panelUsuarioLogueado.pestanaCompraAbono;

import java.awt.GridLayout;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.abono.Abono;
import modelo.abono.Ciclo;
import modelo.abono.CicloCompuesto;
import modelo.abono.CicloEvento;
import modelo.evento.Evento;
import modelo.sistema.Sistema;

/**
 * Clase PestanaCompraAbono para la pestana compra de abono
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCompraAbono extends JPanel {

	private static final long serialVersionUID = 739315612857496210L;

	private static final String[] nombresTablaAbonos = { "Nombre", "Precio", "Zona", "Eventos del Abono" };
	private String[] registrosTablaAbono = new String[4];

	private DefaultTableModel modeloTablaAbonos;
	private JTable tablaAbonos;

	/**
	 * Constructor de PestanaCompraAbono
	 */
	public PestanaCompraAbono() {

		TitledBorder centro = new TitledBorder("Vista de Abonos");
		setBorder(centro);
		setLayout(new GridLayout(1, 1));

		modeloTablaAbonos = new DefaultTableModel(null, nombresTablaAbonos) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaAbonos = new JTable(modeloTablaAbonos);
		add(new JScrollPane(tablaAbonos));
		actualizarTablaAbonos();
	}

	/**
	 * Metodo para actualizar la tabla de ciclos
	 */
	public void actualizarTablaCiclos() {
		String elementos = "";

		for (Ciclo c : Sistema.getInstance().getSistemaCiclos()) {
			registrosTablaAbono[0] = c.getCicloNombre();
			registrosTablaAbono[1] = String.valueOf(c.getPrecio()) + "$";
			registrosTablaAbono[2] = String.valueOf(c.getCicloZona().getZonaNombre());

			if (c.getClass().equals(CicloCompuesto.class)) {
				for (Ciclo cc : (((CicloCompuesto) c).getCicloCompuestoCiclos())) {
					elementos = elementos + ", " + cc.getCicloNombre();
				}
				for (Evento e : (((CicloCompuesto) c).getCicloCompuestoEventos())) {
					elementos = elementos + ", " + e.getEventoTitulo();
				}
			} else if (c.getClass().equals(CicloEvento.class)) {
				for (Evento e : (((CicloEvento) c).getCicloEventoEventos())) {
					elementos = elementos + ", " + e.getEventoTitulo();
				}
			}
			registrosTablaAbono[3] = elementos;
			modeloTablaAbonos.addRow(registrosTablaAbono);
		}
	}

	/**
	 * Metodo para establecer el controlador de la tabla
	 * 
	 * @param m MouseListener con el controlador
	 */
	public void setControlador(MouseListener m) {
		tablaAbonos.addMouseListener(m);
	}

	/**
	 * Metodo para obtener el modelo de la tabla
	 * 
	 * @return DefaultTableModel con el modelo de la tabla
	 */
	public DefaultTableModel getTablaModeloAbonos() {
		return modeloTablaAbonos;
	}

	/**
	 * Metodo para obtener la tabla
	 * 
	 * @return JTable con la tabla
	 */
	public JTable getTablaAbonos() {
		return tablaAbonos;
	}

	/**
	 * Metodo para actualizar la tabla de abonos
	 */
	public void actualizarTablaAbonos() {
		for (Abono a : Sistema.getInstance().getSistemaAbonos()) {
			registrosTablaAbono[0] = "Abono Anual";
			registrosTablaAbono[1] = String.valueOf(a.getAbonoPrecio()) + "$";
			registrosTablaAbono[2] = String.valueOf(a.getAbonoZona().getZonaNombre());
			registrosTablaAbono[3] = "Todos";

			modeloTablaAbonos.addRow(registrosTablaAbono);
		}
	}

	/**
	 * Metodo para destruir la tabla de abonos
	 */
	public void destruirTablaAbonos() {

		int contRow = modeloTablaAbonos.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaAbonos.removeRow(0);
		}
	}
}
