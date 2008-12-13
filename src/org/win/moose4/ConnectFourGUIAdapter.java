package org.win.moose4;

import javax.swing.JOptionPane;
import java.awt.event.*;
import java.io.IOException;

public class ConnectFourGUIAdapter
extends MouseAdapter
{
	private int _col;
	private ConnectFourGUI _gui;
	private boolean _myTurn;
	private ConnectFourHumanClient _client;

	public ConnectFourGUIAdapter(int col, ConnectFourGUI gui,
		ConnectFourHumanClient client)
	throws IOException
	{
		_col = col;
		_gui = gui;
		_myTurn = true;
		_client = client; 
	}

	public void mouseEntered(MouseEvent me)
	{
		_gui.setArrow(_col, true);
	}

	public void mouseExited(MouseEvent me)
	{
		_gui.setArrow(_col, false);
	}

	public void mouseClicked(MouseEvent me)
	{
		Message oppMove;

		if( _myTurn ) {
			if( _client.myMove(_gui.makeMyMove(_col)) ) {
				_myTurn = false;
				oppMove = _client.oppMove();

				if( oppMove.mType == 1 ) {
					JOptionPane.showMessageDialog(null,
						"Opponent claims you made an "+
						"illegal move"
					);
					System.exit(1);
				} else {
					//TODO: Validate Move
					_gui.makeOppMove(oppMove.col);
					if( oppMove.mType == 2 ) {
						JOptionPane.showMessageDialog(
							null,
							"Opponent claims " +
							"victory with BLACK @ "+
							oppMove.row + "-" +
							oppMove.col
						);
					} else {
						_myTurn = true;
					}
				}
			}
		}
	}
}
