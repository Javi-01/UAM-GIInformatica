package vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.abono.Abono;
import modelo.abono.Ciclo;
import modelo.entrada.Entrada;
import modelo.sistema.Sistema;

/**
 * Clase CardCompraEntradaCarritoCenter card center para el carrito para la compra de
 * entradas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCompraEntradaCarritoCenter extends JPanel {

	private static final long serialVersionUID = 7285331636669534535L;

	BorderLayout layoutCarritoCompra = new BorderLayout(2, 2);
	private ArrayList<Entrada> entradasCompraTarjeta = new ArrayList<>();
	private ArrayList<Entrada> entradasCompraAbono = new ArrayList<>();

	private static final String[] nombresTablaCiclo = { "Entrada", "Zona", "Representacion", "Fecha",
			"Metodo de pago" };
	private String[] registrosTablaCompra = new String[5];
	private DefaultTableModel modeloTablaCompra;

	private JTable tablaCompra;
	/**
	 * Constructor de CardCompraEntradaCarritoCenter
	 */
	public CardCompraEntradaCarritoCenter() {

		TitledBorder centro = new TitledBorder("Vista de entradas");
		setBorder(centro);
		setLayout(layoutCarritoCompra);

		modeloTablaCompra = new DefaultTableModel(null, nombresTablaCiclo) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaCompra = new JTable(modeloTablaCompra);
		add(new JScrollPane(tablaCompra));
	}

	public void actualizarTablaCompra(ArrayList<Entrada> entradas) {
		ArrayList<Abono> abonosCompra = new ArrayList<>();
		ArrayList<Ciclo> ciclosCompra = new ArrayList<>();

		for (Entrada e : entradas) {
			registrosTablaCompra[0] = String.valueOf(e.getCodigoValidacion());
			registrosTablaCompra[1] = e.getEntradaZona().getZonaNombre();
			registrosTablaCompra[2] = e.getEntradaRepresentacion().getRepresentacionEvento().getEventoTitulo();
			registrosTablaCompra[3] = String
					.valueOf(String.valueOf(e.getEntradaRepresentacion().getRepresentacionFecha().getYear())) + "-"
					+ String.valueOf(e.getEntradaRepresentacion().getRepresentacionFecha().getMonthValue()) + "-"
					+ String.valueOf(e.getEntradaRepresentacion().getRepresentacionFecha().getDayOfMonth());

			if (Sistema.getInstance().getSistemaUsuarioLogeado().compraUsuarioAbonoCiclo(e)) {

				if (Sistema.getInstance().getSistemaUsuarioLogeado().compraUsuarioAbono(e) != null) {
					if (!abonosCompra
							.contains(Sistema.getInstance().getSistemaUsuarioLogeado().compraUsuarioAbono(e))) {
						registrosTablaCompra[4] = "Abono Anual";
						abonosCompra.add(Sistema.getInstance().getSistemaUsuarioLogeado().compraUsuarioAbono(e));
					} else {
						registrosTablaCompra[4] = "Tarjeta (" + String.valueOf(e.getPrecioEntrada()) + "$)";
					}
				} else {
					registrosTablaCompra[4] = "Tarjeta (" + String.valueOf(e.getPrecioEntrada()) + "$)";
				}

				if (Sistema.getInstance().getSistemaUsuarioLogeado().compraUsuarioCiclo(e) != null
						&& !registrosTablaCompra[4].equals("Abono Anual")) {
					if (!ciclosCompra
							.contains(Sistema.getInstance().getSistemaUsuarioLogeado().compraUsuarioCiclo(e))) {
						ciclosCompra.add(Sistema.getInstance().getSistemaUsuarioLogeado().compraUsuarioCiclo(e));
						registrosTablaCompra[4] = ciclosCompra.get(ciclosCompra.size() - 1).getCicloNombre();
					} else {
						registrosTablaCompra[4] = "Tarjeta (" + String.valueOf(e.getPrecioEntrada()) + "$)";
					}
				} else if (!registrosTablaCompra[4].equals("Abono Anual")) {
					registrosTablaCompra[4] = "Tarjeta (" + String.valueOf(e.getPrecioEntrada()) + "$)";
				}

			} else {
				registrosTablaCompra[4] = "Tarjeta (" + String.valueOf(e.getPrecioEntrada()) + "$)";
			}

			modeloTablaCompra.addRow(registrosTablaCompra);
		}
	}

	public void destruirTablaCompra() {

		int contRow = modeloTablaCompra.getRowCount();

		for (int i = 0; contRow > i; i++) {
			modeloTablaCompra.removeRow(0);
		}
	}

	public ArrayList<Entrada> getEntradasCompraTarjeta() {
		return entradasCompraTarjeta;
	}

	public ArrayList<Entrada> getEntradasCompraAbono() {
		return entradasCompraAbono;
	}

	public void setEntradasCompraTarjeta(ArrayList<Entrada> entradasCompraTarjeta) {
		this.entradasCompraTarjeta.addAll(entradasCompraTarjeta);
	}

	public void delEntradasCompraTarjeta() {
		this.entradasCompraTarjeta.removeAll(this.entradasCompraTarjeta);
	}

	public void setEntradasCompraAbono(ArrayList<Entrada> entradasAbono) {
		this.entradasCompraAbono.addAll(entradasAbono);
	}

	public void delEntradasCompraAbono() {
		this.entradasCompraAbono.removeAll(this.entradasCompraAbono);
	}
}
