package net.objectof.actof.connectorui.beans;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Connection")
public interface Connection extends Resource<Composite>
{
  @net.objectof.Selector("parameters")
  public net.objectof.aggr.Mapping<String,String> getParameters();

  @net.objectof.Selector("parameters:")
  public void setParameters(net.objectof.aggr.Mapping<String,String> a);
  @net.objectof.Selector("name")
  public String getName();

  @net.objectof.Selector("name:")
  public void setName(String a);
  @net.objectof.Selector("type")
  public String getType();

  @net.objectof.Selector("type:")
  public void setType(String a);

}
