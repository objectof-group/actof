package net.objectof.actof.connectorui.beans;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Last")
public interface Last extends Resource<Composite>
{
  @net.objectof.Selector("connection")
  public Connection getConnection();

  @net.objectof.Selector("connection:")
  public void setConnection(Connection a);

}
