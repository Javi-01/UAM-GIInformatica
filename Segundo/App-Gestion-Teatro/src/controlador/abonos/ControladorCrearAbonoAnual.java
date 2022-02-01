package controlador.abonos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import modelo.abono.Abono;
import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaCrearAbonos.anual.PestanaCardCrearAnual;

/**
 * Clase para controlar el boton con el que se crea un abono anual
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorCrearAbonoAnual implements ActionListener {

	private PestanaCardCrearAnual vistaBtCrearAnual;

	/**
	 * constructor del action listener encargado de la vista
	 * central de la creacion de los abonos anuales
	 *
	 * @param vistaBtCrearAnual		panel desde el que se crean los abonos anuales	
	 */
	public ControladorCrearAbonoAnual(PestanaCardCrearAnual vistaBtCrearAnual) {
		this.vistaBtCrearAnual = vistaBtCrearAnual;
	}

	/**
	 * controlador del evento que ocurre al confirmar la creacion del abono anual
	 *
	 * @param e		evento de raton que ocurre tras confirmar la creacion del abono	anual
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (vistaBtCrearAnual.getTextPrecioAbono() <= 0.0 || vistaBtCrearAnual.getTextZonaAbono().equals("")) {
			JOptionPane.showMessageDialog(vistaBtCrearAnual, "Debes rellenar todos los campos", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (Sistema.getInstance().sistemaBuscarAbonosCoincidentes(vistaBtCrearAnual.getTextZonaAbono())) {
			JOptionPane.showMessageDialog(vistaBtCrearAnual, " La zona"+ vistaBtCrearAnual.getTextZonaAbono() +"ya tiene un abono asociado", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (Sistema.getInstance().sistemaBuscarZona(vistaBtCrearAnual.getTextZonaAbono()) == null) {
			JOptionPane.showMessageDialog(vistaBtCrearAnual, " La zona"+ vistaBtCrearAnual.getTextZonaAbono() +"no existe, prueba otra vez", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Sistema.getInstance().sistemaCrearAbonoTeatro(new Abono(vistaBtCrearAnual.getTextPrecioAbono(),
				Sistema.getInstance().sistemaBuscarZona(vistaBtCrearAnual.getTextZonaAbono())));

		JOptionPane.showMessageDialog(vistaBtCrearAnual, "El abono anual se ha creado correctamente");
		vistaBtCrearAnual.limpiarCampos();
	}
}
