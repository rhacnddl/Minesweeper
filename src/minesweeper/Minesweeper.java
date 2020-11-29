package minesweeper;

import minesweeper.Buttons;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Minesweeper extends JFrame{
	
	//�� ���� ���� ��ư���� setBounds�� �� �ʺ�, ����
	final static int HUNDRED = 100; //easy, expert
	final static int FIFITY  =  50; //master, custom
	
	
	//�� ���鿡 ���� ��� ����
	final static int EASY_MODE = 1;
	final static int EXPERT_MODE = 2;
	final static int MASTER_MODE = 3;
	final static int CUSTOMIZE_MODE = 4;
	//��忡 ���� flag ����, �ʱⰪ�� easy���
	
	static int mode = 1;
	static int state = 0;
	
	private Random ran = new Random();
	
	private JLabel remain = new JLabel("�� ���� ��: ");
	private JLabel mine = new JLabel(); //�� ���� �� ǥ��
	
	private JButton check_bt = new JButton(); //���ڿ� ����� �Ȱ� ������ üũ���ִ� ��ư
	
	//�޴�
	private JMenuBar menubar;
	private JMenu menu;
	private JMenuItem[] menuItem = new JMenuItem[4];
	private String[] itemTitle= {"Easy", "Expert", "Master", "Customize"};
	
	//���ں��带 ��� JPanel
	private JPanel panel;
	
	//Image Files
	private ImageIcon basic    = new ImageIcon("pic//Button.png");
	private ImageIcon flagIcon = new ImageIcon("pic//Flag.png");
	private ImageIcon question = new ImageIcon("pic//Question.png");
	private ImageIcon mineIcon = new ImageIcon("pic//Mine.png");
	
	private ImageIcon basic_50    = new ImageIcon("pic//Button_50.png");
	private ImageIcon flagIcon_50 = new ImageIcon("pic//Flag_50.png");
	private ImageIcon question_50 = new ImageIcon("pic//Question_50.png");
	private ImageIcon mineIcon_50 = new ImageIcon("pic//Mine_50.png");
	
	private ImageIcon check = new ImageIcon("pic//Check.png");
	
	private ImageIcon[] num = new ImageIcon[9];
	private String[] num_char = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
	
	//GUI ����
	public Minesweeper() {
		
		//�ʱ� ����
		super("Minesweeper");
		Container ct = getContentPane();
		ct.setLayout(null);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		//�ʱ� ����
		
		//�޴� ����
		menubar = new JMenuBar();
		menu = new JMenu("Mode");
		for(int i=0; i<4; i++) {
			menuItem[i] = new JMenuItem(itemTitle[i]);
			menuItem[i].addActionListener(new OnActionListener());
			menu.add(menuItem[i]);
		}
		menuItem[0].setAccelerator(KeyStroke.getKeyStroke('E', InputEvent.CTRL_MASK)); //�޴������ۿ� ����Ű ����
		menuItem[1].setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
		menuItem[2].setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_MASK));
		menubar.add(menu);
		setJMenuBar(menubar);
		//�޴� ����
		
		//JPanel ����
		panel = new JPanel();
		
		int[][] arr; //createMine ���ϰ� ���� �迭
		int cnt;     //main ���� ����
		
		switch(mode) {
		
		/*
		 * Easy Mode 6x6
		 */
		case EASY_MODE:
			setBounds(0, 0, 600, 720);
			panel.setBounds(0, 50, HUNDRED * 6, HUNDRED * 6);
			panel.setLayout(null);
			Buttons[][] easy_bt = new Buttons[6][6]; //����ã�� ���� ����
			
			//�̹������� ��üȭ
			for(int i=0; i<9; i++)
				num[i] = new ImageIcon("pic//" + num_char[i] + ".png");
			
			arr = createMine(6, 6); //���ڰ��� ����
			cnt = howManyMine(arr, 6, 6); //�� ���� ����� cnt�� return
			mine.setText(Integer.toString(cnt) + " ��");
			
			for(int i=0, y=0; i<6; i++, y+=100) {
				for(int j=0, x=0; j<6; j++, x+=100) {
					
					//���ڹ�ư, '*'�� ASCII CODE�� 42�� ǥ���ȴ�
					if( arr[i][j] == 42 ) easy_bt[i][j] = new Buttons("*");
					
						
					//���ڹ�ư, ��ư�� �ֺ��� ���ڰ� � �ִ��� ǥ��
					else easy_bt[i][j] = new Buttons(Integer.toString(arr[i][j]));


					easy_bt[i][j].setBackground(new Color(88, 88, 88));
					easy_bt[i][j].setBounds(x, y, HUNDRED, HUNDRED);
					easy_bt[i][j].setIcon(basic); //��ư �̹����� �⺻�̹���
					easy_bt[i][j].addMouseListener(new MouseAdapter() {//easy_bt�� �����ϱ� ���� �͸�Ŭ������ ���콺����� �ۼ� ==> '0'�� ������ �ֺ��� ��������
						
						@Override
						public void mousePressed(MouseEvent e) {
							
							Buttons button = (Buttons)e.getSource();
							
							//���콺 ��Ŭ�� ��
							if(e.getButton() == 1) {
								button.setFlag(1); // ��ư�� flag= 1�� ���� ==> ��Ŭ�� �Ұ�
								//System.out.println(button.getX()/100+ ", " + button.getY()/100);
								//������ ���
								if( button.getText().equals("*") ) {
									button.setIcon(mineIcon);
									//���ڹ�ư ������ ������ �ɼ����� ����
									JOptionPane.showMessageDialog(Minesweeper.this, "Mine is activated!", "Game Over", JOptionPane.WARNING_MESSAGE);
									System.exit(0);
								}
								
								//�ֺ� ���ڰ� 0���� ���
								else if( button.getText().equals("0") ) {
									button.setIcon( num[ Integer.parseInt(button.getText()) ] );
									
									//button.getX()/100�� Ŭ���� ��ư�� x��ǥ, button.getY()/100�� Ŭ���� ��ư�� y��ǥ�̴�
									zeroClicked(easy_bt, button.getY()/100, button.getX()/100);
								}
								
								//�ֺ� ���ڰ� n���� ���
								else button.setIcon( num[ Integer.parseInt(button.getText()) ] );
							}
							
							//���콺 ��Ŭ�� ��
							if( e.getButton() == 3 && button.getFlag() == 0) {
								
								if(button.getChange() == 0) {
									button.setIcon(flagIcon);
									button.setChange(1);
								}
								else if(button.getChange() == 1) {
									button.setIcon(question);
									button.setChange(2);
								}
								else {
									button.setIcon(basic);
									button.setChange(0);
								}
								
							}
						}
					});//���콺 ������ ��� �κ�
										
					panel.add(easy_bt[i][j]);
				}
			}
			check_bt.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					
					int mine_flag_cnt = 0;
					
					for(int yy=0; yy<6; yy++) 
						for(int xx=0; xx<6; xx++) //��ư�� �����̰� ����� ������(change = 1) ī��Ʈ ����
							if(easy_bt[yy][xx].getText().equals("*") && easy_bt[yy][xx].getChange() == 1)
								mine_flag_cnt++;
						
					if(mine_flag_cnt == cnt) 
						JOptionPane.showMessageDialog(panel, "You remove all Mines!", "Complete", JOptionPane.INFORMATION_MESSAGE);
						
					
					else
						JOptionPane.showMessageDialog(panel, "Check again.", "Again", JOptionPane.WARNING_MESSAGE);
				
				}
			});
			
			break;
			
		/*
		 * Expert Mode 9x9
		 */
		case EXPERT_MODE:
			setBounds(0, 0, 900, 1020);
			panel.setBounds(0, 50, HUNDRED * 9, HUNDRED * 9);
			panel.setLayout(null);
			Buttons[][] expert_bt = new Buttons[9][9]; //����ã�� ���� ����
			
			//�̹������� ��üȭ
			for(int i=0; i<9; i++)
				num[i] = new ImageIcon("pic//" + num_char[i] + ".png");
		
			arr = createMine(9, 9);
			cnt = howManyMine(arr, 9, 9);
			mine.setText(Integer.toString(cnt) + " ��");
			
			for(int i=0, y=0; i<9; i++, y+=100) {
				for(int j=0, x=0; j<9; j++, x+=100) {
					
					//���ڹ�ư, '*'�� ASCII CODE�� 42�� ǥ���ȴ�
					if( arr[i][j] == 42 ) expert_bt[i][j] = new Buttons("*");

						
					//���ڹ�ư, ��ư�� �ֺ��� ���ڰ� � �ִ��� ǥ��
					else 
						expert_bt[i][j] = new Buttons(Integer.toString(arr[i][j]));

					expert_bt[i][j].setBackground(new Color(88, 88, 88));
					expert_bt[i][j].setBounds(x, y, HUNDRED, HUNDRED);
					expert_bt[i][j].setIcon(basic); //��ư �̹����� �⺻�̹���
					expert_bt[i][j].addMouseListener(new MouseAdapter() {//expert_bt�� �����ϱ� ���� �͸�Ŭ������ ���콺����� �ۼ� ==> '0'�� ������ �ֺ��� ��������
						
						@Override
						public void mousePressed(MouseEvent e) {
							
							Buttons button = (Buttons)e.getSource();
							
							//���콺 ��Ŭ�� ��
							if(e.getButton() == 1) {
								button.setFlag(1); // ��ư�� flag= 1�� ���� ==> ��Ŭ�� �Ұ�
								//System.out.println(button.getX()/100+ ", " + button.getY()/100);
								//������ ���
								if( button.getText().equals("*") ) {
									button.setIcon(mineIcon);
									//���ڹ�ư ������ ������ �ɼ����� ����
									JOptionPane.showMessageDialog(Minesweeper.this, "Mine is activated!", "Game Over", JOptionPane.WARNING_MESSAGE);
									System.exit(0);
								}
								
								//�ֺ� ���ڰ� 0���� ���
								else if( button.getText().equals("0") ) {
									button.setIcon( num[ Integer.parseInt(button.getText()) ] );
									
									//button.getX()/100�� Ŭ���� ��ư�� x��ǥ, button.getY()/100�� Ŭ���� ��ư�� y��ǥ�̴�
									zeroClicked(expert_bt, button.getY()/100, button.getX()/100);
								}
								
								//�ֺ� ���ڰ� n���� ���
								else button.setIcon( num[ Integer.parseInt(button.getText()) ] );
							}
							
							//���콺 ��Ŭ�� ��
							if( e.getButton() == 3 && button.getFlag() == 0) {
								
								if(button.getChange() == 0) {
									button.setIcon(flagIcon);
									button.setChange(1);
								}
								else if(button.getChange() == 1) {
									button.setIcon(question);
									button.setChange(2);
								}
								else {
									button.setIcon(basic);
									button.setChange(0);
								}
								
							}
						}
					}); //��Ŭ�� ������ ����
					
					panel.add(expert_bt[i][j]);
					
				}
			}
			check_bt.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					
					int mine_flag_cnt = 0;
					
					for(int yy=0; yy<9; yy++) 
						for(int xx=0; xx<9; xx++) //��ư�� �����̰� ����� ������(change = 1) ī��Ʈ ����
							if(expert_bt[yy][xx].getText().equals("*") && expert_bt[yy][xx].getChange() == 1)
								mine_flag_cnt++;
						
					if(mine_flag_cnt == cnt) 
						JOptionPane.showMessageDialog(panel, "You remove all Mines!", "Complete", JOptionPane.INFORMATION_MESSAGE);
						
					
					else
						JOptionPane.showMessageDialog(panel, "Check again.", "Again", JOptionPane.WARNING_MESSAGE);
				
				}
			});
			
			break;
			
		/*
		 * Master Mode 16x16
		 */
		case MASTER_MODE:
			setBounds(10, 10, 800+10, 920+10);
			panel.setBounds(0, 50, FIFITY * 16, FIFITY * 16);
			panel.setLayout(null);
			Buttons[][] master_bt = new Buttons[16][16]; //����ã�� ���� ����
			
			//�̹������� ��üȭ
			for(int i=0; i<9; i++)
				num[i] = new ImageIcon("pic//" + num_char[i] + "_50.png");
			
			arr = createMine(16, 16);
			cnt = howManyMine(arr, 16, 16);
			mine.setText(Integer.toString(cnt) + " ��");
			
			for(int i=0, y=0; i<16; i++, y+=50) {
				for(int j=0, x=0; j<16; j++, x+=50) {
					
					//���ڹ�ư, '*'�� ASCII CODE�� 42�� ǥ���ȴ�
					if( arr[i][j] == 42 ) master_bt[i][j] = new Buttons("*");

						
					//���ڹ�ư, ��ư�� �ֺ��� ���ڰ� � �ִ��� ǥ��
					else 
						master_bt[i][j] = new Buttons(Integer.toString(arr[i][j]));

					master_bt[i][j].setBackground(new Color(88, 88, 88));
					master_bt[i][j].setBounds(x, y, FIFITY, FIFITY);
					master_bt[i][j].setIcon(basic_50); //��ư �̹����� �⺻�̹���
					master_bt[i][j].addMouseListener(new MouseAdapter() {//master_bt�� �����ϱ� ���� �͸�Ŭ������ ���콺����� �ۼ� ==> '0'�� ������ �ֺ��� ��������
						
						@Override
						public void mousePressed(MouseEvent e) {
							
							Buttons button = (Buttons)e.getSource();
							
							//���콺 ��Ŭ�� ��
							if(e.getButton() == 1) {
								button.setFlag(1); // ��ư�� flag= 1�� ���� ==> ��Ŭ�� �Ұ�
								//System.out.println(button.getX()/50+ ", " + button.getY()/50);
								//������ ���
								if( button.getText().equals("*") ) {
									button.setIcon(mineIcon_50);
									//���ڹ�ư ������ ������ �ɼ����� ����
									JOptionPane.showMessageDialog(Minesweeper.this, "Mine is activated!", "Game Over", JOptionPane.WARNING_MESSAGE);
									System.exit(0);
								}
								
								//�ֺ� ���ڰ� 0���� ���
								else if( button.getText().equals("0") ) {
									button.setIcon( num[ Integer.parseInt(button.getText()) ] );
									
									//button.getX()/50�� Ŭ���� ��ư�� x��ǥ, button.getY()/50�� Ŭ���� ��ư�� y��ǥ�̴�
									zeroClicked(master_bt, button.getY()/50, button.getX()/50);
								}
								
								//�ֺ� ���ڰ� n���� ���
								else button.setIcon( num[ Integer.parseInt(button.getText()) ] );
							}
							
							//���콺 ��Ŭ�� ��
							if( e.getButton() == 3 && button.getFlag() == 0) {
								
								if(button.getChange() == 0) {
									button.setIcon(flagIcon_50);
									button.setChange(1);
								}
								else if(button.getChange() == 1) {
									button.setIcon(question_50);
									button.setChange(2);
								}
								else {
									button.setIcon(basic_50);
									button.setChange(0);
								}
								
							}
						}
					}); //��Ŭ�� ������ ����
					
					panel.add(master_bt[i][j]);
				}
			}
			check_bt.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mousePressed(MouseEvent e) {
					
					int mine_flag_cnt = 0;
					
					for(int yy=0; yy<16; yy++) 
						for(int xx=0; xx<16; xx++) //��ư�� �����̰� ����� ������(change = 1) ī��Ʈ ����
							if(master_bt[yy][xx].getText().equals("*") && master_bt[yy][xx].getChange() == 1)
								mine_flag_cnt++;
						
					if(mine_flag_cnt == cnt) 
						JOptionPane.showMessageDialog(panel, "You remove all Mines!", "Complete", JOptionPane.INFORMATION_MESSAGE);
						
					
					else
						JOptionPane.showMessageDialog(panel, "Check again.", "Again", JOptionPane.WARNING_MESSAGE);
				
				}
			});
			
			break;
			
		/*
		 * Customize Mode NxM
		 */
		case CUSTOMIZE_MODE:
			//����ũ��� ����ũ�⸦ �˾� ���̾�α׷� �Է¹���
			int XX = 0;
			int YY = 0;
			
			try {
			
				XX = Integer.parseInt(JOptionPane.showInputDialog(Minesweeper.this, "����ũ�⸦ �Է��ϼ���."));
				YY = Integer.parseInt(JOptionPane.showInputDialog(Minesweeper.this, "����ũ�⸦ �Է��ϼ���."));
			
			} catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(Minesweeper.this, "���ڸ� �Է����ּ���");
				System.exit(0);
				
			} catch(IllegalArgumentException e) {
				JOptionPane.showMessageDialog(Minesweeper.this, "���ڸ� �Է����ּ���");
				System.exit(0);
			}
			
			int XXX = XX; //�ؿ� check_bt ������ for���� XX�� YY�� �ν� ���ؼ� �� ������ �Ҵ�
			int YYY = YY;
			
			
			setBounds(10, 10, FIFITY * XX + 10, FIFITY * YY + 10 + 120);
			panel.setBounds(0, 50, FIFITY * XX, FIFITY * YY);
			panel.setLayout(null);
			Buttons[][] custom_bt = new Buttons[YY][XX]; //����ã�� ���� ����
			
			//�̹������� ��üȭ
			for(int i=0; i<9; i++)
				num[i] = new ImageIcon("pic//" + num_char[i] + "_50.png");

			arr = createMine(YY, XX);
			cnt = howManyMine(arr, YY, XX);
			mine.setText(Integer.toString(cnt) + " ��");
			
			for(int i=0, y=0; i<YY; i++, y+=50) {
				for(int j=0, x=0; j<XX; j++, x+=50) {
					
					//���ڹ�ư, '*'�� ASCII CODE�� 42�� ǥ���ȴ�
					if( arr[i][j] == 42 ) custom_bt[i][j] = new Buttons("*");
						
					//���ڹ�ư, ��ư�� �ֺ��� ���ڰ� � �ִ��� ǥ��
					else 
						custom_bt[i][j] = new Buttons(Integer.toString(arr[i][j]));

					custom_bt[i][j].setBackground(new Color(88, 88, 88));
					custom_bt[i][j].setBounds(x, y, FIFITY, FIFITY);
					custom_bt[i][j].setIcon(basic_50); //��ư �̹����� �⺻�̹���
					custom_bt[i][j].addMouseListener(new MouseAdapter() {//custom_bt�� �����ϱ� ���� �͸�Ŭ������ ���콺����� �ۼ� ==> '0'�� ������ �ֺ��� ��������
						
						@Override
						public void mousePressed(MouseEvent e) {
							
							Buttons button = (Buttons)e.getSource();
							
							//���콺 ��Ŭ�� ��
							if(e.getButton() == 1) {
								button.setFlag(1); // ��ư�� flag= 1�� ���� ==> ��Ŭ�� �Ұ�
								//System.out.println(button.getX() / 50+ ", " + button.getY() / 50);
								//������ ���
								if( button.getText().equals("*") ) {
									button.setIcon(mineIcon_50);
									//���ڹ�ư ������ ������ �ɼ����� ����
									JOptionPane.showMessageDialog(Minesweeper.this, "Mine is activated!", "Game Over", JOptionPane.WARNING_MESSAGE);
									System.exit(0);
								}
								
								//�ֺ� ���ڰ� 0���� ���
								else if( button.getText().equals("0") ) {
									button.setIcon( num[ Integer.parseInt(button.getText()) ] );
									
									//button.getX()/50�� Ŭ���� ��ư�� x��ǥ, button.getY()/50�� Ŭ���� ��ư�� y��ǥ�̴�
									zeroClicked(custom_bt, button.getY()/50, button.getX()/50);
								}
								
								//�ֺ� ���ڰ� n���� ���
								else button.setIcon( num[ Integer.parseInt(button.getText()) ] );
							}
							
							//���콺 ��Ŭ�� ��
							if( e.getButton() == 3 && button.getFlag() == 0) {
								
								if(button.getChange() == 0) {
									button.setIcon(flagIcon_50);
									button.setChange(1);
								}
								else if(button.getChange() == 1) {
									button.setIcon(question_50);
									button.setChange(2);
								}
								else {
									button.setIcon(basic_50);
									button.setChange(0);
								}
								
							}
						}
					}); //��Ŭ�� ������ ����
					
					panel.add(custom_bt[i][j]);
				}
			}
			check_bt.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					
					int mine_flag_cnt = 0;
					
					for(int yy=0; yy<YYY; yy++) 
						for(int xx=0; xx<XXX; xx++) //��ư�� �����̰� ����� ������(change = 1) ī��Ʈ ����
							if(custom_bt[yy][xx].getText().equals("*") && custom_bt[yy][xx].getChange() == 1)
								mine_flag_cnt++;
						
					if(mine_flag_cnt == cnt) 
						JOptionPane.showMessageDialog(panel, "You remove all Mines!", "Complete", JOptionPane.INFORMATION_MESSAGE);
						
					
					else
						JOptionPane.showMessageDialog(panel, "Check again.", "Again", JOptionPane.WARNING_MESSAGE);
				
				}
			});
			
			
		}//switch�� ��
		
		//������ ����
		check_bt.setIcon(check);

		remain.setBounds(210, 10, 100, 25); //�������� ��:
		mine.setBounds(300, 10, 30, 25); // n��
		check_bt.setBounds(380, 0, 50, 50);
		ct.add(remain);
		ct.add(mine);
		ct.add(check_bt);
		ct.add(panel);
		
		setVisible(true);
		//������ ����
	}
	
	//n, m�� �޾� ���ڰ� �ִ� ����(�迭) ����
	public int[][] createMine(int m, int n) {
		
		int[][] mat = new int[m][n]; //��� �迭��� �ʱⰪ = 0
		int count = 0; //�迭��� �ֺ��� �ִ� ���ڰ��� Counting ����
		
		int rand;
		//���� ������ (m+n)�� ���� �̻����� ���� = for�� Ƚ���� (m+n)/2 �̻� ����
		do {
			rand = ran.nextInt(m + n);
		}while(rand < (m + n) / 2);
		
		//m+n ������ ������ �߻����� �׸�ŭ  loop�� ������ �������� ����'*'�� ����
		for(int i=0; i<rand; i++) {
			
			int m1 = ran.nextInt(m);
			int n1 = ran.nextInt(n);
			
			mat[m1][n1] = '*';
		}
		
		//�� �迭��� �ֺ� ���� ���� Counting
		for(int i=0; i<m; i++) {
			for(int j=0; j<n; j++) {
				count = 0;
				if( mat[i][j] == '*') continue;
				
				else if(i == 0 && j == 0 && mat[0][0] != '*') { //0,0 �� ��
					for(int q=0; q<2; q++)
						for(int w=0; w<2; w++)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				else if(i == 0 && j == n-1 && mat[0][n-1] != '*') { //0,n-1 �� ��
					for(int q=0; q<2; q++)
						for(int w=n-1; w>=n-2; w--)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				else if(i == m-1 && j == 0 && mat[m-1][0] != '*') { //m-1, 0 �� ��
					for(int q=i; q>=i-1; q--)
						for(int w=0; w<2; w++)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				else if(i == m-1 && j == n-1 && mat[m-1][n-1] != '*') { //m-1, n-1 �� ��
					for(int q=i; q>=i-1; q--)
						for(int w=j; w>=j-1; w--)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				//////////////////////////////////////////////////////////////////////
				else if(i == 0 && mat[0][j] != '*') {// y=0�� ��
					for(int q=0; q<2; q++)
						for(int w=j-1; w<=j+1; w++)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				else if(i == m-1 && mat[m-1][j] != '*') {// y=m-1�� ��
					for(int q=m-2; q<=m-1; q++)
						for(int w=j-1; w<=j+1; w++)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				else if(j == 0 && mat[i][0] != '*') { // x=0�� ��
					for(int q=i-1; q<=i+1; q++)
						for(int w=0; w<2; w++)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				else if(j == n-1 && mat[i][n-1] != '*') { // x=n-1�� ��
					for(int q=i-1; q<=i+1; q++)
						for(int w=n-2; w<=n-1; w++)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				
				////////////////////////////////////////////////////////////
				else {
					for(int q=i-1; q<i+2; q++) 
						for(int w=j-1; w<j+2; w++) 
							if(mat[q][w] == '*') 
								count++;
					mat[i][j] = count;
				}
			}
		}//mat �迭 ����loop ��
		return mat;
	}
	
	//�� ���ڰ��� �����ִ� �޼ҵ�
	public int howManyMine(int[][] arr, int m, int n) {
		
		int cnt = 0;
		
		for(int i=0; i<m; i++)
			for(int j=0; j<n; j++)
				if(arr[i][j] == '*')
					cnt++;
		
		return cnt;
	}
	
	//�޴��� �߰� �� �׼Ǹ�����
	class OnActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			RestartThread th = new RestartThread(); //��尡 ����Ǹ� �� thread�� ���� �� ���� ����
			
			String name = e.getActionCommand();
			switch(name) {
			case "Easy":
				mode = EASY_MODE;
				break;
			case "Expert":
				mode = EXPERT_MODE;
				break;
			case "Master":
				mode = MASTER_MODE;
				break;
			case "Customize":
				mode = CUSTOMIZE_MODE;
				break;
			}
			th.start();
		}
	}	
	
	public void zeroClicked(Buttons[][] bt, int i, int j) {
		/* i = y��ǥ, j = x��ǥ
		 * ��ǥ�� �𼭸��� �׵θ��� �κк��� ��������.
		 * �𼭸� (y, x)
		 * (0, 0), (n-1, m-1), (0, m-1), (n-1, 0)
		 * �׵θ� (y, x)
		 * (0, *), (n-1, *), (*, 0), (*, m-1) easy��忡�� n=6 m=6
		 */
		int arr_Y, arr_X;
		
		if(mode == 4) {
			arr_Y = bt.length - 1; arr_X = bt[0].length - 1;
		}
		else
			arr_X = arr_Y = bt.length - 1;
		
		//System.out.println(arr_X + ", " + arr_Y); //������
		
		if(i == 0 && j == 0) {//(y, x) = (0, 0)�� ��
			for(int y=i; y<i+2; y++) {
				for(int x=j; x<j+2; x++) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
						
				}
			}
		}//(0, 0)
		
		else if(i == arr_Y && j == 0) {//(y, x) = (5, 0)�� ��
			for(int y=i; y>i-2; y--) {
				for(int x=j; x<j+2; x++) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
					
				}
			}
		}//(5, 0)
		
		else if(i == 0 && j == arr_X) {//(y, x) = (0, 5)�� ��
			for(int y=i; y<i+2; y++) {
				for(int x=j; x>j-2; x--) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
					
				}
			}
		}//(0, 5)
		
		else if(i == arr_Y && j == arr_X) {//(y, x) = (5, 5)�� ��
			for(int y=i; y>i-2; y--) {
				for(int x=j; x>j-2; x--) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
					
				}
			}
		}//(5, 5)
		//<<<<<<<<<<<<<<<<<<<<<<<<<<<���� ���� �𼭸�, �Ʒ����� �׵θ�>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		
		else if(i == 0) {//(y, x) = (0, *)�� ��
			for(int y=i; y<i+2; y++) {
				for(int x=j-1; x<j+2; x++) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
					
				}
			}
		}//(0, *)
		
		else if(i == arr_Y) {//(y, x) = (5, *)�� ��
			for(int y=i; y>i-2; y--) {
				for(int x=j-1; x<j+2; x++) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );	
					
				}
			}
		}//(5, *)
		
		else if(j == 0) {//(y, x) = (*, 0)�� ��
			for(int y=i-1; y<i+2; y++) {
				for(int x=j; x<j+2; x++) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
					
				}
			}
		}//(*, 0)
		
		else if(j == arr_X) {//(y, x) = (*, 5)�� ��
			for(int y=i-1; y<i+2; y++) {
				for(int x=j; x>j-2; x--) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
					
				}
			}
		}//(*, 5)
		
		else {
			for(int y=i-1; y<i+2; y++) {
				for(int x=j-1; x<j+2; x++) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
					
					
				}
			}
		}
	}
	
	//Thread ���� 12�� ����
	class RestartThread extends Thread{
		@Override
		public void run() {
			if(mode != state) {
				state = mode;
				new Minesweeper();
				return;
			}	
		}
	}
	
	public static void main(String[] args) {
		
			new Minesweeper();
	}
}
