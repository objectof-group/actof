package net.objectof.actof.connectorui.beans.composite;
import net.objectof.actof.connectorui.beans.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IConnection")
public class IConnectionBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Connection
{
  public IConnectionBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public net.objectof.aggr.Mapping<String,String> getParameters()
  {
    return (net.objectof.aggr.Mapping<String,String>) _("parameters");
  }
  public void setParameters(net.objectof.aggr.Mapping<String,String> a)
  {
    _("parameters", a);
  }
  public String getName()
  {
    return (String) _("name");
  }
  public void setName(String a)
  {
    _("name", a);
  }
  public String getType()
  {
    return (String) _("type");
  }
  public void setType(String a)
  {
    _("type", a);
  }

}