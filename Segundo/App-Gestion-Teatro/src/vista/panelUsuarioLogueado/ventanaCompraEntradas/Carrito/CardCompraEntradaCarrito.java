package vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito;

import java.awt.BorderLayout;
import java.util.*;

import javax.swing.JPanel;

import modelo.entrada.Entrada;

/**
 * Clase CardCompraEntradaCarrito card para el carrito para la compra de
 * entradas
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class CardCompraEntradaCarrito extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8492735525602493273L;

	private ArrayList<Entrada> entradasCompra = new ArrayList<>();
	private CardCompraEntradaCarritoSouth botonesCarritoCompra;
	private CardCompraEntradaCarritoCenter tablaCarritoCompra;

	private BorderLayout layoutCarrito = new BorderLayout(2, 2);

	/**
	 * Constructor de CardCompraEntradaCarrito
	 */
	public CardCompraEntradaCarrito() {
		setLayout(layoutCarrito);

		botonesCarritoCompra = new CardCompraEntradaCarritoSouth();
		tablaCarritoCompra = new CardCompraEntradaCarritoCenter();

		add(tablaCarritoCompra, BorderLayout.CENTER);
		add(botonesCarritoCompra, BorderLayout.SOUTH);
	}

	/**
	 * Metodo para obtener el card de entrada carrito south
	 * 
	 * @return CardCompraEntradaCarritoSouth con el objeto
	 */
	public CardCompraEntradaCarritoSouth getBotonesCarritoCompra() {
		return botonesCarritoCompra;
	}

	/**
	 * Metodo para obtener el card de entrada carrito center
	 * 
	 * @return CardCompraEntradaCarritoCenter con el objeto
	 */
	public CardCompraEntradaCarritoCenter getTablaCarritoCompra() {
		return tablaCarritoCompra;
	}

	/**
	 * Metodo para establecer las entradas a comprar
	 * 
	 * @param entradas a comprar
	 */
	public void setEntradasCompra(ArrayList<Entrada> entradas) {
		entradasCompra.addAll(entradas);
	}

	/**
	 * Metodo para borrar todas las entradas a comprar
	 */
	public void delEntradasCompra() {
		entradasCompra.removeAll(entradasCompra);
	}

	/**
	 * Metodo para recoger todas las entradas a comprar
	 * 
	 * @return ArrayList de Entrada
	 */
	public ArrayList<Entrada> getEntradasCompra() {
		return entradasCompra;
	}
}
