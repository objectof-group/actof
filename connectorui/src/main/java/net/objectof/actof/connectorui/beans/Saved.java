package net.objectof.actof.connectorui.beans;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Saved")
public interface Saved extends Resource<Composite>
{
  @net.objectof.Selector("connections")
  public net.objectof.aggr.Listing<Connection> getConnections();

  @net.objectof.Selector("connections:")
  public void setConnections(net.objectof.aggr.Listing<Connection> a);

}
