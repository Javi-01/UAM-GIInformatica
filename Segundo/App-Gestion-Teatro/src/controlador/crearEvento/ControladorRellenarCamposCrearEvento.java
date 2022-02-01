package controlador.crearEvento;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;


import modelo.evento.*;
import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaCrearEvento.PestanaCrearEvento;
import vista.panelAdministrador.pestanaCrearEvento.rellenarCampos.PestanaCrearEventoWest;

/**
 * Clase para controlar las casillas que se deben de rellenar al crear eventos
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorRellenarCamposCrearEvento implements ActionListener{

	private PestanaCrearEventoWest vistaBoxCrearEventoWest;
	private PestanaCrearEvento vistaCrearEventoCenter;
	
	public ControladorRellenarCamposCrearEvento(PestanaCrearEvento vistaCrearEventoCenter, PestanaCrearEventoWest vistaBoxCrearEventoWest) {
		
		this.vistaCrearEventoCenter = vistaCrearEventoCenter;
		this.vistaBoxCrearEventoWest = vistaBoxCrearEventoWest;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (Sistema.getInstance().getSistemaZonas().isEmpty()) {
			JOptionPane.showMessageDialog(vistaCrearEventoCenter, "No puedes crear un evento hasta que no haya zonas en el teatro", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (vistaCrearEventoCenter.getPestanaCrearEventoPrecioZona().getNumZonasTeatro() <= 0) {
			JOptionPane.showMessageDialog(vistaCrearEventoCenter, "Debes reiniciar la aplicacion para que se creen las zonas correctamente", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
						
		if (vistaBoxCrearEventoWest.getCombobox().getSelectedItem().toString().equals(vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanzaTitulo())) {

			if (vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getDuracionDanza() <= 5.0 || vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getTituloEventoDanza().equals("") ||
					vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getDescripcionDanza().equals("") || vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getAutorDanza().equals("") ||
					vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getDirectorDanza().equals("") || vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getDirectorOrquesta().equals("") || 
					vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getBailarinesDanza().equals("") || vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getDanzaOrquesta().equals("")) {

						JOptionPane.showMessageDialog(vistaCrearEventoCenter, "Debes rellenar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}else {
						vistaCrearEventoCenter.setEventoPestanaCrear(new EventoDanza(vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getDuracionDanza(), 
								vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getTituloEventoDanza(), vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getDescripcionDanza(), 
								vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getAutorDanza(),  vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getDirectorDanza(), 
								vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getDanzaOrquesta(), vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getDirectorOrquesta(), 
								vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoDanza().getBailarinesDanza()));	
						
						vistaCrearEventoCenter.showPestanaPrecioZonas();
					}
			}else if (vistaBoxCrearEventoWest.getCombobox().getSelectedItem().toString().equals(vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConciertoTitulo())) {
				
				if (vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getDuracionConcierto() <= 5.0 || vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getTituloEventoConcierto().equals("") ||
						vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getDescripcionConcierto().equals("") || vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getAutorConcierto().equals("") ||
						vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getDirectorConcierto().equals("") || vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getOrquestaConcierto().equals("") || 
						vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getSolistaOrquesta().equals("") || vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getPiezaConcierto().equals("")) {
					
					JOptionPane.showMessageDialog(vistaCrearEventoCenter, "Debes rellenar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}else {
					vistaCrearEventoCenter.setEventoPestanaCrear(new EventoConcierto(vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getDuracionConcierto(), 
							vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getTituloEventoConcierto(), vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getDescripcionConcierto(), 
							vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getAutorConcierto(), vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getDirectorConcierto(), 
							vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getOrquestaConcierto(), vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getSolistaOrquesta(),
							vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoConcierto().getPiezaConcierto()));	
					
					vistaCrearEventoCenter.showPestanaPrecioZonas();
				}
			}else if (vistaBoxCrearEventoWest.getCombobox().getSelectedItem().toString().equals(vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoTeatroTitulo())) {
				
				if (vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoTeatro().getDuracionTeatro() <= 5.0 || vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoTeatro().getTituloEventoTeatro().equals("") ||
						vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoTeatro().getDescripcionTeatro().equals("") || vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoTeatro().getAutorTeatro().equals("") ||
						vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoTeatro().getDirectorTeatro().equals("")) {
					
					JOptionPane.showMessageDialog(vistaCrearEventoCenter, "Debes rellenar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}else {
					vistaCrearEventoCenter.setEventoPestanaCrear(new EventoTeatro(vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoTeatro().getDuracionTeatro(), 
							vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoTeatro().getTituloEventoTeatro(), vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoTeatro().getDescripcionTeatro(), 
							vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoTeatro().getAutorTeatro(), vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoTeatro().getDirectorTeatro(), 
							vistaCrearEventoCenter.getPestanaCrearEventoBorder().getPestanaCrearEventoCenter().getCardEventoTeatro().getActoresTeatro()));	
					
					vistaCrearEventoCenter.showPestanaPrecioZonas();
				}
			}
	}
}
