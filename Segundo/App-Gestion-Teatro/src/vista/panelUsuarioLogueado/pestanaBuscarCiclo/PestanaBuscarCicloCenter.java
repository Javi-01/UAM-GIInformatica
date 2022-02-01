package vista.panelUsuarioLogueado.pestanaBuscarCiclo;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.abono.Ciclo;


/**
 * Clase PestanaBuscarCicloCenter para la pestana de buscar ciclo center
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class PestanaBuscarCicloCenter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6781365079816003830L;
	
	private static final String[] nombresTablaCiclo = { "Nombre", "Precio", "Zona" };
	private String [] registrosTablaCiclo = new String[3];

	private DefaultTableModel modeloTablaCiclo;
	private JTable tablaCiclo;
	/**
	 * Constructor de PestanaBuscarCicloCenter
	 */
	public PestanaBuscarCicloCenter() {
		
		TitledBorder centro = new TitledBorder("Vista de eventos");
		setBorder(centro);
		setLayout(new GridLayout(1, 1));
		
		 modeloTablaCiclo = new DefaultTableModel (null, nombresTablaCiclo) {

			private static final long serialVersionUID = 5122012196219622715L;

			@Override
			   public boolean isCellEditable(int row, int column) {
			       return false;
			   }
		};
			
		tablaCiclo= new JTable(modeloTablaCiclo);
		add(new JScrollPane(tablaCiclo));
	}
	/**
	 *  Metodo para actualizar la tabla de ciclos
	 *  
	 *  @param CicloCoincidentes con los ciclos a actualizar
	 */
	public void actualizarTablaCiclos(ArrayList <Ciclo> CicloCoincidentes) {
		for (Ciclo c : CicloCoincidentes) {
			registrosTablaCiclo[0] = c.getCicloNombre();
			registrosTablaCiclo[1] = String.valueOf(c.getPrecio());
			registrosTablaCiclo[2] = c.getCicloZona().getZonaNombre();
			
			modeloTablaCiclo.addRow(registrosTablaCiclo);
		}
	}
	/**
	 *  Metodo para destruir la tabla
	 */
	public void destruirTablaCiclos() {
	
			int contRow =modeloTablaCiclo.getRowCount();
				
			for (int i = 0; contRow>i; i++) {
	               modeloTablaCiclo.removeRow(0);
	        }		
	}		
}
