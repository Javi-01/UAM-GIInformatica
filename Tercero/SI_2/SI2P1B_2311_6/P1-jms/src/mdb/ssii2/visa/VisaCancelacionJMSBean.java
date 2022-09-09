/**
 * Pr&aacute;ctricas de Sistemas Inform&aacute;ticos II
 * VisaCancelacionJMSBean.java
 */

package ssii2.visa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.ActivationConfigProperty;
import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * @author jaime
 */
@MessageDriven(mappedName = "jms/VisaPagosQueue")
public class VisaCancelacionJMSBean extends DBTester implements MessageListener {
  static final Logger logger = Logger.getLogger("VisaCancelacionJMSBean");
  @Resource
  private MessageDrivenContext mdc;

  private static final String GET_CODRESPUESTA_QRY = 
                       "select codrespuesta from pago where " +
                       " idautorizacion=?";
  private static final String UPDATE_CANCELA_QRY = 
                       "update pago set codrespuesta=999 where " +
                       " idautorizacion=?";
  private static final String UPDATE_SALDO_QRY = 
                       "update tarjeta set saldo=saldo+" +
                       " (select importe from pago where idautorizacion=?)" +
                       " where numerotarjeta =" +
                       " (select numerotarjeta from pago where idautorizacion=?)";


  public VisaCancelacionJMSBean() {
  }

  // TODO : Método onMessage de ejemplo
  // Modificarlo para ejecutar el UPDATE definido más arriba,
  // asignando el idAutorizacion a lo recibido por el mensaje
  // Para ello conecte a la BD, prepareStatement() y ejecute correctamente
  // la actualización
  public void onMessage(Message inMessage) {
      TextMessage msg = null;
      Connection con = null;
      PreparedStatement pstmt = null;
      boolean ret = false;
      ResultSet rs = null;

      try {
        if (inMessage instanceof TextMessage) {
            msg = (TextMessage) inMessage;
            logger.info("MESSAGE BEAN: Message received: " + msg.getText());
        } else {
            logger.warning(
                    "Message of wrong type: "
                    + inMessage.getClass().getName());
        }

        //Se obtiene el id de autorizacion del mensaje recibido por la cola
        int idAutorizacion = Integer.parseInt(msg.getText());

        //nos conectamos a la base de datos y hacemos el procedimiento que nos
        //el codigo de respuesta para comprobar que este sea 000 (transaccion
        //que se realizo exitosamente)

        con = getConnection();
        //Se prepara la query para obtener el codigo de respuesta
        String codResQuery = GET_CODRESPUESTA_QRY;
        logger.info(codResQuery);

        pstmt = con.prepareStatement(codResQuery);
        pstmt.setInt(1, idAutorizacion);

        rs = pstmt.executeQuery();
        //Se mueve el cursor de la consulta a la primera fila (primer registro
        //obtenido desde la base de datos)
        if (rs.next()){
            //Se coge el codigo de respuesta
            int codigoRes = rs.getInt("codrespuesta");
            //Si ese codigo de respuesta es 000 continuamos con el proceso
            if (codigoRes == 000){

                String updCodResQuery = UPDATE_CANCELA_QRY;
                logger.info(updCodResQuery);
                //El nuevo codigo es 999
                pstmt = con.prepareStatement(updCodResQuery);
                pstmt.setInt(1, idAutorizacion);
                //Ejecutamos la consulta y si se ha actualzado solo un
                //registro, el retorno sera valido, en caso contrario, el retorno
                //permanece a false para saber que ha fallado la transaccioni
                ret = false;
                if (!pstmt.execute()
                    && pstmt.getUpdateCount() == 1) {
                    ret = true;
                }
                //Si se ejecuta bien la actualizacion seguimos para actualizar el
                //saldo
                if (ret == true) {
                    String updSaldoQuery = UPDATE_SALDO_QRY;
                    logger.info(updSaldoQuery);
                    //El nuevo saldo es el actual del cliente mas el de la tarjeta
                    //asociada al pago con ese id autorizacion
                    pstmt = con.prepareStatement(updSaldoQuery);
                    pstmt.setInt(1, idAutorizacion);
                    pstmt.setInt(2, idAutorizacion);
                    //Ejecutamos la consulta y si se ha actualzado solo un
                    //registro, el retorno sera valido, en caso contrario, el retorno
                    //permanece a false para saber que ha fallado la transaccioni
                    ret = false;
                    if (!pstmt.execute()
                        && pstmt.getUpdateCount() == 1) {
                        ret = true;
                    }
                }
                //EN caso contrario mandamos un mensaje de que no se actualizo
                //correctamente
                else{
                    logger.warning(
                        "No se actualizo el registro correctamente: "
                        + inMessage.getClass().getName());
                    return;
                }
            //En caso de que el codigo de respuesta este mal, mandamos error
            }else{
                logger.warning(
                    "Codigo Respues != 000: "
                    + inMessage.getClass().getName());
                return;
            }
        //En el caso de que la consulta no de filas (no se haya encontrado)
        //no haya un pago registrado con ese id de autorizacion,
        //devolvemos error
        }else{
            logger.warning(
                "IdAutorizacion no encontrado: "
                + inMessage.getClass().getName());
            return;
        }

        //Si no se ha actualizado el saldo, enviamos mensaje de error
        if (ret == false){
            logger.warning(
                "Saldo no ha sido actualizado: "
                + inMessage.getClass().getName());
            return;
        }

      } catch (JMSException e) {
          e.printStackTrace();
          mdc.setRollbackOnly();
      } catch (Throwable te) {
          te.printStackTrace();
      }
  }
}
