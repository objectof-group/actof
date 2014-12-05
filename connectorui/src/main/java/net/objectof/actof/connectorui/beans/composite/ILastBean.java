package net.objectof.actof.connectorui.beans.composite;
import net.objectof.actof.connectorui.beans.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("ILast")
public class ILastBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Last
{
  public ILastBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public Connection getConnection()
  {
    return (Connection) _("connection");
  }
  public void setConnection(Connection a)
  {
    _("connection", a);
  }

}