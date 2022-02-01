package controlador.representaciones;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;

import modelo.evento.Representacion;
import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaGestionRepresentaciones.PestanaGestionRepresentaciones;
import vista.panelAdministrador.pestanaGestionRepresentaciones.PestanaGestionRepresentacionesWest;
import vista.panelAdministrador.pestanaGestionRepresentaciones.tabla.PestanaTablaRepresGestionRepresentaciones;

/**
* Clase que se mantiene a la escucha de una tabla de registros, para ello mira el evento
* que ha sido seleccionado y actua sobre ello
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorSeleccionarRepre implements MouseListener{

	private PestanaGestionRepresentaciones vistaGestionRepres;
	private PestanaGestionRepresentacionesWest vistaCbGestion;
	private PestanaTablaRepresGestionRepresentaciones vistaTablaGestion;
	
	/**
	* Contructor que selecciona un evento de la gestion del la repsentacion
	* 
	* @param vistaGestionRepres pestaña con la vista principal del panel 
	* @param vistaTablaGestion es la tabla con la informacion a realizar
	*  @param vistaCbGestion es la vista con el combobox de la gestion a realizar
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorSeleccionarRepre(PestanaGestionRepresentaciones vistaGestionRepres,
			PestanaGestionRepresentacionesWest vistaCbGestion,
			PestanaTablaRepresGestionRepresentaciones vistaTablaGestion) {

		this.vistaGestionRepres = vistaGestionRepres;
		this.vistaCbGestion = vistaCbGestion;
		this.vistaTablaGestion = vistaTablaGestion;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	
		int representacionElegida = vistaTablaGestion.getTablaRepresentacion().rowAtPoint(e.getPoint());
		Representacion repre;
		
		if (representacionElegida != -1) {
			String horaRepreString = (String) vistaTablaGestion.getTablaModelo().getValueAt(representacionElegida, 2);
			String fechaRepreString = (String) vistaTablaGestion.getTablaModelo().getValueAt(representacionElegida, 1);
			String  eventoRepreTitulo = (String) vistaTablaGestion.getTablaModelo().getValueAt(representacionElegida, 0);
			
			LocalDateTime fechaRepre = Sistema.getInstance().sistemaFormatearFechaRepresentacion(fechaRepreString, horaRepreString);
			repre = Sistema.getInstance().sistemaObtenerRepresentacion(
					Sistema.getInstance().sistemaBuscarEventosCoincidentes(eventoRepreTitulo).get(0), fechaRepre);
			vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().setRepresentacionGestion(repre);
			
			if (vistaCbGestion.getCombobox().getSelectedItem().toString().equals("Posponer")) {
				vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardPosponerRepre().setRepreAsociado(fechaRepreString + " " +horaRepreString);
				
			}else if (vistaCbGestion.getCombobox().getSelectedItem().toString().equals("Cancelar")) {
				vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardCancelarRepre().setRepreAsociado(fechaRepreString + " " +horaRepreString);
			}
			vistaGestionRepres.showPestanaBorderGestionRepres();
			
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
