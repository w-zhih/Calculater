package tool;

import java.awt.GridBagConstraints;

public class GridBagConstraint {
	static public void setConstraint(GridBagConstraints con,int gridx,int gridy,int gridwidth,int gridheight) {
		con.gridx=gridx;
		con.gridy=gridy;
		con.gridheight=gridheight;
		con.gridwidth=gridwidth;
		
	}
	
	static public void setConstraint(GridBagConstraints con,int gridx,int gridy) {
		con.gridx=gridx;
		con.gridy=gridy;
		
	}
}
