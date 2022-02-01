package vista.panelUsuarioLogueado.pestanaCompraAbono;

import java.awt.CardLayout;

import javax.swing.JPanel;

import modelo.abono.Abono;
import modelo.abono.Ciclo;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito.VentanaIntroducirTarjetaCredito;

/**
 * Clase PestanaCompraAbonoCenter para la pestana compra de abono center
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaCompraAbonoCenter extends JPanel {

	private static final long serialVersionUID = 390768957364044496L;

	private final static String cardResumen = "resumenCompra";
	private final static String cardTabla = "tablaAbono";

	private PestanaCompraAbono cardTablaAbono;
	private PestanaResumenCompraAbono cardCompraAbono;

	private CardLayout layoutCenter = new CardLayout();

	private Abono abono;
	private Ciclo ciclo;

	private VentanaIntroducirTarjetaCredito vistaTarjeta;

	/**
	 * Constructor de PestanaCompraAbonoCenter
	 */
	public PestanaCompraAbonoCenter() {

		setLayout(layoutCenter);

		cardCompraAbono = new PestanaResumenCompraAbono();
		cardTablaAbono = new PestanaCompraAbono();
		vistaTarjeta = new VentanaIntroducirTarjetaCredito();

		add(cardTablaAbono, cardTabla);
		add(cardCompraAbono, cardResumen);
	}

	/**
	 * Metodo para mostrar el LayOut del resumen de compra
	 */
	public void showCardResumenCompra() {
		layoutCenter.show(this, cardResumen);
	}

	/**
	 * Metodo para mostrar el LayOut de la tabla de abonos
	 */
	public void showCardTablaAbonos() {
		layoutCenter.show(this, cardTabla);
	}

	/**
	 * Metodo para obtener la pestana de compra abono
	 * 
	 * @return PestanaCompraAbono con la pesatana
	 */
	public PestanaCompraAbono getCardTablaAbonos() {
		return cardTablaAbono;
	}

	/**
	 * Metodo para obtener la pestana de resumen de compra de abono
	 * 
	 * @return PestanaCompraAbono con la pesatana
	 */
	public PestanaResumenCompraAbono getCardResumenCompra() {
		return cardCompraAbono;
	}

	/**
	 * Metodo para obtener el abono a comprar
	 * 
	 * @return Abono a comprar
	 */
	public Abono getAbonoAComprar() {
		return this.abono;
	}

	/**
	 * Metodo para obtener el ciclo asociado
	 * 
	 * @return Ciclo con el ciclo asociado
	 */
	public Ciclo getCicloAsociado() {
		return this.ciclo;
	}

	/**
	 * Metodo para establecer el abono asociado
	 * 
	 * @param abono Abono con el abono asociado
	 */
	public void setAbonoAsociado(Abono abono) {
		this.abono = abono;
	}

	/**
	 * Metodo para establecer el ciclo asociado
	 * 
	 * @param ciclo Ciclo con el ciclo asociado
	 */
	public void setCicloAsociado(Ciclo ciclo) {
		this.ciclo = ciclo;
	}

	/**
	 * Metodo para obtener la ventana para introducir la targeta de credito
	 * 
	 * @return VentanaIntroducirTarjetaCredito con la ventana
	 */
	public VentanaIntroducirTarjetaCredito getVentanaVistaTarjeta() {
		return vistaTarjeta;
	}
}
