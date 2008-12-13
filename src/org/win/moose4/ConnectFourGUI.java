package org.win.moose4;

import javax.swing.*;
import java.awt.*;

import java.io.IOException;

public class ConnectFourGUI
extends JFrame
{
	private JLabel _theCells[][], _arrows[]; 
	private int _topOfCol[];
	private byte _colour;

	public ConnectFourGUI(byte colour, String ip, int port)
	throws IOException
	{
		this(7, 7, colour, ip, port);
	}

	public ConnectFourGUI(int r, int c, byte colour, String ip, int port)
	throws IOException
	{
		super("Moose 4");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel centerPanel = new JPanel(new GridLayout(r+1, c));
		initializeCells(r, c, centerPanel);

		getContentPane().add(centerPanel);

		setSize(410, 480);
		setVisible(true);

		addArrowListeners(ip, port);

		_colour = colour;
	}

	private void initializeCells(int r, int c, JPanel centerPanel)
	{
		_arrows = new JLabel[r];
		_theCells = new JLabel[r][c];
		_topOfCol = new int[r];

		for( int i=0; i<_arrows.length; ++i ) {
			_arrows[i] = new JLabel(
				new ImageIcon(Constants.BLANKARROW)
			);
			_topOfCol[i] = _topOfCol.length-1;
			centerPanel.add(_arrows[i]);
		}

		for( int i=0; i<r;++i ) {
			for( int i2=0; i2<c; ++i2 ) {
				_theCells[i][i2] = new JLabel(
					new ImageIcon(Constants.OPENPIC)
				);
				centerPanel.add(_theCells[i][i2]);
			}
		}
	}

	public void setArrow(int col, boolean turnOn)
	{
		if( turnOn ) {
			_arrows[col].setIcon(new ImageIcon(Constants.THEARROW));
		} else {
			_arrows[col].setIcon(
				new ImageIcon(Constants.BLANKARROW)
			);
		}
	}

	private void addArrowListeners(String ip, int port)
	throws IOException
	{ 
		ConnectFourHumanClient client = new ConnectFourHumanClient(
			ip, port
		);
		for( int i=0; i<_arrows.length; ++i ) {
			_arrows[i].addMouseListener(
				new ConnectFourGUIAdapter(
					i, this, client
				)
			);
		}
	}

	public void makeOppMove(int col)
	{
		setSlot(col, (_colour == Constants.WHITE) ?
			Constants.BLACK :
			Constants.WHITE
		);
	}

	public Message makeMyMove(int col)
	{
		Message rtn = null;

		if( setSlot(col, _colour) ) {
			rtn = new Message(_topOfCol[col]+1, col, 0);
		}

		return rtn;
	}

	public boolean setSlot(int col, byte colour)
	{
		if( _topOfCol[col] >= 0 ) {
			_theCells[_topOfCol[col]--][col].setIcon(
				new ImageIcon( (colour == Constants.WHITE) ?
					Constants.WHITEPIC : 
					Constants.BLACKPIC
				)
			);
			return true;
		} else {
			return false;
		}
	}

	public static void main(String argv[])
	throws IOException
	{
		new ConnectFourGUI(Constants.WHITE, "localhost", 44521);
	}
}
