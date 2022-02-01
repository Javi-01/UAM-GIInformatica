package controlador.representaciones;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JOptionPane;
import modelo.evento.Representacion;
import vista.panelAdministrador.pestanaGestionRepresentaciones.PestanaGestionRepresentaciones;
import vista.panelAdministrador.pestanaGestionRepresentaciones.PestanaGestionRepresentacionesCenter;
import vista.panelAdministrador.pestanaGestionRepresentaciones.PestanaGestionRepresentacionesWest;

/**
* Clase encargada de accionar el boton de comfirmar la gestion, es un boton compartido entre las tres gestiones
* disponibles, tanto de cancelar, como de posponer, como de añadoir una representacion
*
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorBotonConfirmarGestionRepres implements ActionListener{

	private PestanaGestionRepresentacionesCenter vistaGestionRepre;
	private PestanaGestionRepresentacionesWest vistaCbRepre;
	private PestanaGestionRepresentaciones vistaGeneral;
	private String strFecha;
	DateTimeFormatter formatter;
	LocalDateTime fecha;
	
	/**
	* Contructor de la clase que recibe la pestaña principal de mis entradas, 
	* y la opcion a elegir de mis entradas que se corresponde con la ventana flotantes
	* accionando el boton de comprar
	* 
	* @param vistaGestionRepre es el panel principal de mis entradas
	* @param vistaGeneral es la ventana con los botones de las opciones
	* @param vistaCbRepre	panel combobox de la pestaña representacion
	*
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorBotonConfirmarGestionRepres(PestanaGestionRepresentacionesCenter vistaGestionRepre,
			PestanaGestionRepresentaciones vistaGeneral, PestanaGestionRepresentacionesWest vistaCbRepre) {

		this.vistaGestionRepre = vistaGestionRepre;
		this.vistaCbRepre = vistaCbRepre;
		this.vistaGeneral = vistaGeneral;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (vistaGestionRepre.getEventoGestionRepre() == null) {
			JOptionPane.showMessageDialog(vistaGeneral, "Debes seleccionar el evento", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (vistaCbRepre.getCombobox().getSelectedItem().toString().equals("Añadir")) {
			
			strFecha = vistaGestionRepre.getCardAnadirRepre().getFechaAsociada().getText();
			formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			try {
				fecha = LocalDateTime.parse(strFecha, formatter);	
			}catch (DateTimeParseException e1) {
				JOptionPane.showMessageDialog(vistaGeneral, "No se ha podido establecer dicha fecha", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(fecha.isBefore(LocalDateTime.now())) {
				JOptionPane.showMessageDialog(vistaGeneral, "No se ha podido establecer dicha fecha porque es anterior a la actual", "Error", JOptionPane.ERROR_MESSAGE);
				return;
				}
			
			Representacion repre = new Representacion(vistaGestionRepre.getEventoGestionRepre(), fecha);
			
			if (!vistaGestionRepre.getEventoGestionRepre().addEventoRepresentacion(repre)) {
				JOptionPane.showMessageDialog(vistaGeneral, "No se ha podido añadir la Representacion.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			JOptionPane.showMessageDialog(vistaGeneral, "La representacion se ha añadido correctamente");	
			
		}else if (vistaCbRepre.getCombobox().getSelectedItem().toString().equals("Cancelar")) {
			if (vistaGestionRepre.getRepresentacionGestion() == null) {
				JOptionPane.showMessageDialog(vistaGeneral, "Debes seleccionar la representacion.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}else {
				if (!vistaGestionRepre.getEventoGestionRepre().cancelarEventoRepresentacion(vistaGestionRepre.getRepresentacionGestion())) {
					JOptionPane.showMessageDialog(vistaGeneral, "No se ha podido cancelar la Representacion.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				JOptionPane.showMessageDialog(vistaGeneral, "La representacion se ha cancelado correctamente");	
			}
		}else if (vistaCbRepre.getCombobox().getSelectedItem().toString().equals("Posponer")) {
			
			if (vistaGestionRepre.getRepresentacionGestion() == null) {
				JOptionPane.showMessageDialog(vistaGeneral, "Debes seleccionar la representacion.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}else {
				strFecha = vistaGestionRepre.getCardPosponerRepre().getFechaAsociada().getText();
				formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				try {
					fecha = LocalDateTime.parse(strFecha, formatter);	
				}catch (DateTimeParseException e1) {
					JOptionPane.showMessageDialog(vistaGeneral, "No se ha podido establecer dicha fecha", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!vistaGestionRepre.getEventoGestionRepre().posponerEventoRepresentacion(vistaGestionRepre.getRepresentacionGestion(), fecha)) {
					JOptionPane.showMessageDialog(vistaGeneral, "No se ha podido poponer la Representacion.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			JOptionPane.showMessageDialog(vistaGeneral, "La representacion se ha pospuesto correctamente");	
		}
		vistaGestionRepre.setRepresentacionGestion(null);
		vistaGestionRepre.setEventoGestionRepre(null);
		vistaGeneral.getPestanaTablaEventos().destruirTablaEventos();
		vistaGeneral.getPestanaTablaRepres().destruirTablaRepresentaciones();
		vistaGeneral.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardAnadirRepre().limpiarEventoAsociado();
		vistaGeneral.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardPosponerRepre().limpiarTxtEventoAsocido();
		vistaGeneral.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardPosponerRepre().limpiarTxtRepreAsocido();
		vistaGeneral.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardCancelarRepre().limpiarTxtEventoAsocido();
		vistaGeneral.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardCancelarRepre().limpiarTxtRepreAsocido();
	}

}
