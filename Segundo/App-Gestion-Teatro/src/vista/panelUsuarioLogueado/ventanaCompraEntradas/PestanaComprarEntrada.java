package vista.panelUsuarioLogueado.ventanaCompraEntradas;

import java.awt.CardLayout;

import javax.swing.JPanel;

import controlador.entrada.ControladorSeleccionarRepresentacion;
import modelo.evento.Representacion;
import modelo.zona.Zona;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito.CardCompraEntradaCarrito;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito.VentanaIntroducirTarjetaCredito;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.seleccionInfo.CardSeleccionarRepresentacion;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.seleccionInfo.CardSeleccionarZonaCompuestaEntrada;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.seleccionInfo.CardSeleccionarZonaSimpleEntrada;

/**
 * Clase PestanaComprarEntrada para la pestana de comprar entrada
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaComprarEntrada extends JPanel {

	private static final long serialVersionUID = -658508034779646933L;
	private Representacion representacion;
	private Zona zona;

	private final static String tituloSeleccionRepre = "Representacion Entrada";
	private final static String tituloSeleccionZona = "Zona Simple";
	private final static String tituloSeleccionCompuesta = "Zona Compuesta";
	private final static String tituloCarritoCompra = "CarritoCompra";

	private CardSeleccionarRepresentacion representacionEntrada;
	private CardSeleccionarZonaSimpleEntrada zonaSimple;
	private CardCompraEntradaCarrito carritoCompra;
	private CardSeleccionarZonaCompuestaEntrada zonaCompuesta;

	private static CardLayout compraLayout = new CardLayout();

	private VentanaIntroducirTarjetaCredito vistaTarjeta;

	/**
	 * Constructor de PestanaComprarEntrada
	 */
	public PestanaComprarEntrada() {

		setLayout(compraLayout);

		representacionEntrada = new CardSeleccionarRepresentacion();
		zonaSimple = new CardSeleccionarZonaSimpleEntrada();
		carritoCompra = new CardCompraEntradaCarrito();
		zonaCompuesta = new CardSeleccionarZonaCompuestaEntrada();
		vistaTarjeta = new VentanaIntroducirTarjetaCredito();

		add(representacionEntrada, tituloSeleccionRepre);
		add(zonaSimple, tituloSeleccionZona);
		add(zonaCompuesta, tituloSeleccionCompuesta);
		add(carritoCompra, tituloCarritoCompra);

		establecerControladores();
	}

	/**
	 * Metodo para establecer los controladores
	 */
	public void establecerControladores() {

		ControladorSeleccionarRepresentacion cSeleccionarRepre = new ControladorSeleccionarRepresentacion(this,
				representacionEntrada.getTablaRepreEntrada());
		representacionEntrada.getTablaRepreEntrada().setControlador(cSeleccionarRepre);

	}

	/**
	 * Metodo para mostrar el LayOut para elegir la representacion
	 */
	public void showElegirRepreEntrada() {
		compraLayout.show(this, tituloSeleccionRepre);
	}

	/**
	 * Metodo para mostrar el LayOut para elegir las zonas simples
	 */
	public void showElegirZonaSimple() {
		compraLayout.show(this, tituloSeleccionZona);
	}

	/**
	 * Metodo para mostrar el LayOut para elegir las zonas compuestas
	 */
	public void showElegirZonaCompuesta() {
		compraLayout.show(this, tituloSeleccionCompuesta);
	}

	/**
	 * Metodo para mostrar el LayOut del carrito de la compra
	 */
	public void showCarritoCompra() {
		compraLayout.show(this, tituloCarritoCompra);
	}

	/**
	 * Metodo para obtener el card de seleccionar representacion
	 * 
	 * @return CardSeleccionarRepresentacion con el card
	 */
	public CardSeleccionarRepresentacion getCardRepreEntrada() {
		return representacionEntrada;
	}

	/**
	 * Metodo para obtener el card de seleccionar zona simple entrada
	 * 
	 * @return CardSeleccionarZonaSimpleEntrada con el card
	 */
	public CardSeleccionarZonaSimpleEntrada getCardZonaEntrada() {
		return zonaSimple;
	}

	/**
	 * Metodo para obtener el card de seleccionar zona compuesta entrada
	 * 
	 * @return CardSeleccionarZonaCompuestaEntrada con el card
	 */
	public CardSeleccionarZonaCompuestaEntrada getCardZonaCompuestaEntrada() {
		return zonaCompuesta;
	}

	/**
	 * Metodo para obtener el card de comprar entrada en el carrito
	 * 
	 * @return CardCompraEntradaCarrito con el card
	 */
	public CardCompraEntradaCarrito getCardCarritoCompra() {
		return carritoCompra;
	}

	/**
	 * Metodo para obtener la representacion elegida
	 * 
	 * @return Representacion elegida
	 */
	public Representacion getRepresentacionVistaEntrada() {
		return representacion;
	}

	/**
	 * Metodo para establecer la representacion
	 * 
	 * @param repre Representacion
	 */
	public void setRepresentacionVistaEntrada(Representacion repre) {
		representacion = repre;
	}

	/**
	 * Metodo para obtener la zona elegida
	 * 
	 * @return Zona elegida
	 */
	public Zona getZonaVistaEntrada() {
		return zona;
	}

	/**
	 * Metodo para establecer la zona
	 * 
	 * @param zon Zona
	 */
	public void setZonaVistaEntrada(Zona zon) {
		zona = zon;
	}

	/**
	 * Metodo para obtener la ventana para introducir la tarjeta
	 * 
	 * @return VentanaIntroducirTarjetaCredito con la ventana
	 */
	public VentanaIntroducirTarjetaCredito getVentanaVistaTarjeta() {
		return vistaTarjeta;
	}
}
