package controlador.representaciones;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import modelo.evento.Evento;
import modelo.sistema.Sistema;
import vista.panelAdministrador.pestanaGestionRepresentaciones.PestanaGestionRepresentaciones;
import vista.panelAdministrador.pestanaGestionRepresentaciones.PestanaGestionRepresentacionesWest;
import vista.panelAdministrador.pestanaGestionRepresentaciones.tabla.PestanaTablaEventosGestionRepresentaciones;

/**
* Clase para seleccionar la accion a realizar de una gestion sobre representaciones,
* cada pestaña hara los cambios necesarios
* 
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorSeleccionarEventoRepre implements MouseListener {

	private PestanaGestionRepresentaciones vistaGestionRepres;
	private PestanaGestionRepresentacionesWest vistaCbGestion;
	private PestanaTablaEventosGestionRepresentaciones vistaTablaGestion;

	/**
	* Contructor que selecciona un evento de la gestion del la repsentacion
	* 
	* @param vistaGestionRepres pestaña con la vista principal del panel 
	* @param vistaTableGestion es la tabla con la informacion a realizar
	*  @param vistaCbGestion es la vista con el combobox de la gestion a realizar
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorSeleccionarEventoRepre(PestanaGestionRepresentaciones vistaGestionRepres,
			PestanaTablaEventosGestionRepresentaciones vistaTableGestion, PestanaGestionRepresentacionesWest vistaCbGestion) {

		this.vistaGestionRepres = vistaGestionRepres;
		this.vistaTablaGestion = vistaTableGestion;
		this.vistaCbGestion = vistaCbGestion;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		int eventoElegido = vistaTablaGestion.getTablaRepres().rowAtPoint(e.getPoint());
		vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardPosponerRepre().limpiarTxtRepreAsocido();
		
		if (eventoElegido != -1) {
			String nombreEvento = (String)vistaTablaGestion.getTablaModeloRepres().getValueAt(eventoElegido, 0);
			Evento eve = Sistema.getInstance().sistemaBuscarEventosCoincidentes(nombreEvento).get(0);
			vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().setEventoGestionRepre(eve);
			
			if (vistaCbGestion.getCombobox().getSelectedItem().toString().equals("Añadir")) {
				
				vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardAnadirRepre().setEventoAsociado(nombreEvento);
				vistaGestionRepres.showPestanaBorderGestionRepres();
				
			}else if (vistaCbGestion.getCombobox().getSelectedItem().toString().equals("Cancelar")) {
		
				vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardCancelarRepre().setEventoAsociado(nombreEvento);
				vistaGestionRepres.showPestanaBorderGestionRepres();
				
			}else if (vistaCbGestion.getCombobox().getSelectedItem().toString().equals("Posponer")) {

				vistaGestionRepres.getPrestanaGestionRepres().getPestanaGestionRepresCenter().getCardPosponerRepre().setEventoAsociado(nombreEvento);
				vistaGestionRepres.showPestanaBorderGestionRepres();
			}
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
