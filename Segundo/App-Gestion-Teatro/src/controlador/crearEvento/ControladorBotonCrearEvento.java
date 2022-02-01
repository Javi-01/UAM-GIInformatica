package controlador.crearEvento;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import vista.panelAdministrador.pestanaCrearEvento.PestanaCrearEvento;
import vista.panelAdministrador.pestanaCrearEvento.precioZonas.PestanaCrearEventoPrecioZona;

import modelo.sistema.*;
import modelo.evento.*;
import modelo.zona.*;

/**
 * Clase para controlar el boton con el que se confirma la 
 * creacion de un evento tras añadir los campos
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorBotonCrearEvento implements ActionListener {

	private PestanaCrearEvento vistaCrearEvento;
	private PestanaCrearEventoPrecioZona vistaBtCrearEvento;

	public ControladorBotonCrearEvento(PestanaCrearEvento vistaCrearEvento,
			PestanaCrearEventoPrecioZona vistaBtCrearEvento) {

		this.vistaCrearEvento = vistaCrearEvento;
		this.vistaBtCrearEvento = vistaBtCrearEvento;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (vistaBtCrearEvento.getNumZonasTeatro() == 0) {
			JOptionPane.showMessageDialog(vistaBtCrearEvento, "Debes actualizar la aplicacion para que se carguen las zonas",
					"Error", JOptionPane.ERROR_MESSAGE);
			vistaCrearEvento.showPestanaRellenarCampos();
			return;
		}
		/* Primera comprobacion de que todos los campos esten llenos */
		for (Integer i = 0; i < vistaBtCrearEvento.getNumZonasTeatro(); i++) {
			if (vistaBtCrearEvento.getNombreTxtPrecioZona(i) == 0.0) {
				JOptionPane.showMessageDialog(vistaBtCrearEvento, "Debes asignar todos los precios a las zonas",
						"Error", JOptionPane.ERROR_MESSAGE);
				vistaBtCrearEvento.limpiarTxtPrecioZona();
				return;
			}
		}
		
		/*Se crea el evento*/
		Sistema.getInstance().sistemaCrearEvento(vistaCrearEvento.getEventoPestanaCrear());
		
		/*Finalmente se recorre el bucle de nuevo para asignar los campos*/
		for (Integer i = 0; i < vistaBtCrearEvento.getNumZonasTeatro(); i++) {

			Zona z = Sistema.getInstance().getSistemaZonas().get(i);
			Evento eve = vistaCrearEvento.getEventoPestanaCrear();

			if (!z.getClass().equals(ZonaCompuesta.class)) {
				Sistema.getInstance().getSistemaEventos().get(Sistema.getInstance().getSistemaEventos().size() - 1)
				.addEventoPrecioZona(new PrecioZona(eve, z, vistaBtCrearEvento.getNombreTxtPrecioZona(i)));
			}
		}
		
		JOptionPane.showMessageDialog(vistaBtCrearEvento, "El evento  " + Sistema.getInstance().getSistemaEventos()
								.get(Sistema.getInstance().getSistemaEventos().size() - 1).getEventoTitulo()
						+ " se ha creado correctamente");
		vistaCrearEvento.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().limpiarCampos();
		vistaCrearEvento.getPestanaCrearEventoPrecioZona().limpiarTxtPrecioZona();
		vistaCrearEvento.showPestanaRellenarCampos();
	}

}
