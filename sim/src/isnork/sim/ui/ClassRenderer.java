package isnork.sim.ui;

import isnork.sim.Player;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;


public class ClassRenderer extends DefaultListCellRenderer
{
  private static final long serialVersionUID = 25123L;

  @SuppressWarnings("unchecked")
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		try
		{
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value instanceof Class)
			{
				Class<Player> p = (Class<Player>) value;
				Player pl = p.newInstance();
				String strPlayer = pl.getName();
				if (strPlayer != null)
				{
					setText(strPlayer);
				}
			}
		} catch (Exception e)
		{

		}
		return this;
	}

}
