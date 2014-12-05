package net.objectof.actof.connectorui.beans.composite;
import net.objectof.actof.connectorui.beans.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("ISaved")
public class ISavedBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Saved
{
  public ISavedBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public net.objectof.aggr.Listing<Connection> getConnections()
  {
    return (net.objectof.aggr.Listing<Connection>) _("connections");
  }
  public void setConnections(net.objectof.aggr.Listing<Connection> a)
  {
    _("connections", a);
  }

}