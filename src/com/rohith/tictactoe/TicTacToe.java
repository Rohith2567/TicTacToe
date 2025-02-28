package com.rohith.tictactoe;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;

import javax.swing.*;

public class TicTacToe {
	int boardWidth = 600;
	int boardHeight = 650; // 650

	Image icon = new ImageIcon(getClass().getResource("tictactoe-logo.png")).getImage();
	JFrame frame = new JFrame("Tic-Tac-Toe");
	JLabel textLabel = new JLabel();
	JPanel textPanel = new JPanel();
	JPanel boardPanel = new JPanel();
	JPanel resetPanel = new JPanel();
	JPanel gnewPanel = new JPanel();
	JPanel topPanel = new JPanel(new GridBagLayout());
	JPanel bottomPanel = new JPanel();
	GridBagConstraints gbc = new GridBagConstraints();
	
	JPanel scorePanel = new JPanel(); // new FlowLayout(FlowLayout.CENTER, 50, 0)
	JLabel xscoreLabel = new JLabel();
	JLabel oscoreLabel = new JLabel();
	
	JButton[][] board = new JButton[3][3]; 
	String playerX = "X";
	String playerO = "O";
	String currentPlayer = playerX;
	int turns = 0;
	
	int xScore = 0;
	int oScore = 0;
	
	boolean gameOver = false;
	
	public TicTacToe() {
		frame.setVisible(true);
		frame.setSize(boardWidth, boardHeight);
		frame.setIconImage(icon);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		textLabel.setBackground(Color.darkGray);
		textLabel.setForeground(Color.white);
		textLabel.setFont(new Font("Arial", Font.BOLD, 50));
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		textLabel.setText("Tic-Tac-Toe");
		textLabel.setOpaque(true);
		
		xscoreLabel.setBackground(Color.DARK_GRAY);
		xscoreLabel.setForeground(Color.white);
		xscoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
		xscoreLabel.setText("Player X: " + xScore);
		xscoreLabel.setOpaque(true);
		
		oscoreLabel.setBackground(Color.DARK_GRAY);
		oscoreLabel.setForeground(Color.white);
		oscoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
		oscoreLabel.setText("Player O: " + oScore);
		oscoreLabel.setOpaque(true);
		
		// top text 
		textPanel.setLayout(new BorderLayout());
		textPanel.add(textLabel);
//		frame.add(textPanel, BorderLayout.NORTH);
		
		// score
//		scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		scorePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 20)); // Top Left Bottom Right padding
		scorePanel.setLayout(new BorderLayout());
		scorePanel.setBackground(Color.DARK_GRAY);
		scorePanel.add(xscoreLabel, BorderLayout.WEST);
		scorePanel.add(oscoreLabel, BorderLayout.EAST);
		
//		topPanel.setLayout(new GridLayout(2, 1));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1; // Full width
		gbc.weighty = 0.8; // Text panel gets 80% of topPanel height
		gbc.fill = GridBagConstraints.BOTH; // Expand to fill
		topPanel.add(textPanel, gbc);
//		topPanel.add(textPanel);
//		topPanel.add(scorePanel);
		gbc.gridy = 1;
		gbc.weighty = 0.2; // Score panel gets 20% of topPanel height
		topPanel.add(scorePanel, gbc);
		frame.add(topPanel, BorderLayout.NORTH);
		
		// board
		boardPanel.setLayout(new GridLayout(3, 3));
		boardPanel.setBackground(Color.DARK_GRAY);
		frame.add(boardPanel);
		
		// reset
		resetPanel.setBackground(Color.DARK_GRAY);
		resetPanel.setLayout(new BorderLayout());
		
		// new game
		gnewPanel.setBackground(Color.DARK_GRAY);
		gnewPanel.setLayout(new BorderLayout());
		
		bottomPanel.setLayout(new GridLayout(1, 2));
		bottomPanel.add(resetPanel);
		bottomPanel.add(gnewPanel);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		
		JButton resetButton = new JButton();
		resetPanel.add(resetButton);
		
		JButton gnewButton = new JButton();
		gnewPanel.add(gnewButton);
		
		resetButton.setBackground(Color.DARK_GRAY);
		resetButton.setForeground(new Color(0, 255, 200));
		resetButton.setFont(new Font("Arial", Font.BOLD, 30));
		resetButton.setFocusable(false);
		resetButton.setText("reset");
		resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		
		gnewButton.setBackground(Color.DARK_GRAY);        // 255, 140, 0
		gnewButton.setForeground(new Color(255, 102, 102)); // set color in rgb 
		// gnewPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 140, 0), 3)); // set border color
		gnewButton.setFont(new Font("Arial", Font.BOLD, 30));
		gnewButton.setFocusable(false);
		gnewButton.setText("new game");
		gnewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
				xScore = 0;
				oScore = 0;
				xscoreLabel.setText("Player X: " + xScore);
				oscoreLabel.setText("Player O: " + oScore);
			}
		});
		
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				JButton tile = new JButton();
				board[r][c] = tile;
				boardPanel.add(tile);
				
				tile.setBackground(Color.DARK_GRAY);
				tile.setForeground(Color.white);
				tile.setFont(new Font("Arial", Font.BOLD, 120));
				tile.setFocusable(false);
				
				tile.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (gameOver) return;
						JButton tile = (JButton) e.getSource();
						if (tile.getText() == "") {
							tile.setText(currentPlayer);
							turns++;
							checkWinner();
							if (!gameOver) {
								currentPlayer = currentPlayer == playerX ? playerO : playerX;
								textLabel.setText(currentPlayer + "'s turn.");
							}
						}
						
					}
				});
			}
		}
	}
	
	void checkWinner() {
		// horizontal
		for (int r = 0; r < 3; r++) {
			if (board[r][0].getText() == "") continue;
			
			if (board[r][0].getText() == board[r][1].getText() && 
				board[r][1].getText() == board[r][2].getText()) {
				for (int i = 0; i < 3; i++) {
					setWinner(board[r][i]);
				}
				gameOver = true;
				setScore();
				return;
			}
		}
		
		// vertical
		for (int c = 0; c < 3; c++) {
			if (board[0][c].getText() == "") continue;
			
			if (board[0][c].getText() == board[1][c].getText() &&
				board[1][c].getText() == board[2][c].getText()	) {
				for (int i = 0; i < 3; i++) {
					setWinner(board[i][c]);
				}
				gameOver = true;
				setScore();
				return;
			}
		}
		
		// diagonal
		if (board[0][0].getText() == board[1][1].getText() &&
			board[1][1].getText() == board[2][2].getText() && 
			board[0][0].getText() != "") {
			for (int i = 0; i < 3; i++) {
				setWinner(board[i][i]);
			}
			gameOver = true;
			setScore();
			return;
		}
		
		// anti-diagonal
		if (board[0][2].getText() == board[1][1].getText() &&
			board[1][1].getText() == board[2][0].getText() && 
			board[0][2].getText() != "") {
			setWinner(board[0][2]);
			setWinner(board[1][1]);
			setWinner(board[2][0]);
			gameOver = true;
			setScore();
			return;
		}
		
		if (turns == 9) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					setTie(board[i][j]);
				}
			}
			gameOver = true;
		}
	}
	
	void setWinner(JButton tile) {
		tile.setForeground(Color.green);
		tile.setBackground(Color.gray);
		textLabel.setText(currentPlayer + " is the winner!");
	}
	
	void setScore() {
		if (currentPlayer.equals(playerX)) {
			xScore +=1;
			xscoreLabel.setText("Player X: " + xScore);
		} else {
			oScore +=1;
			oscoreLabel.setText("Player O: " + oScore);
		}
	}
	
	void setTie(JButton tile) {
		tile.setForeground(Color.orange);
		tile.setBackground(Color.gray);
		textLabel.setText("Tie!");
	}
	
	void reset() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j].setText("");
				board[i][j].setBackground(Color.DARK_GRAY);
				board[i][j].setForeground(Color.WHITE);
			}
		}
		currentPlayer = playerX;
		textLabel.setText("Tic-Tac-Toe");
		turns = 0;
		gameOver = false;
	}
}
