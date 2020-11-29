package minesweeper;

import minesweeper.Buttons;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Minesweeper extends JFrame{
	
	//각 모드애 따른 버튼들의 setBounds에 들어갈 너비, 높이
	final static int HUNDRED = 100; //easy, expert
	final static int FIFITY  =  50; //master, custom
	
	
	//각 모드들에 대한 상수 정의
	final static int EASY_MODE = 1;
	final static int EXPERT_MODE = 2;
	final static int MASTER_MODE = 3;
	final static int CUSTOMIZE_MODE = 4;
	//모드에 따른 flag 선언, 초기값은 easy모드
	
	static int mode = 1;
	static int state = 0;
	
	private Random ran = new Random();
	
	private JLabel remain = new JLabel("총 지뢰 수: ");
	private JLabel mine = new JLabel(); //총 지뢰 수 표시
	
	private JButton check_bt = new JButton(); //지뢰에 깃발을 꽂고 누르면 체크해주는 버튼
	
	//메뉴
	private JMenuBar menubar;
	private JMenu menu;
	private JMenuItem[] menuItem = new JMenuItem[4];
	private String[] itemTitle= {"Easy", "Expert", "Master", "Customize"};
	
	//지뢰보드를 담는 JPanel
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
	
	//GUI 구현
	public Minesweeper() {
		
		//초기 설정
		super("Minesweeper");
		Container ct = getContentPane();
		ct.setLayout(null);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		//초기 설정
		
		//메뉴 설정
		menubar = new JMenuBar();
		menu = new JMenu("Mode");
		for(int i=0; i<4; i++) {
			menuItem[i] = new JMenuItem(itemTitle[i]);
			menuItem[i].addActionListener(new OnActionListener());
			menu.add(menuItem[i]);
		}
		menuItem[0].setAccelerator(KeyStroke.getKeyStroke('E', InputEvent.CTRL_MASK)); //메뉴아이템에 단축키 설정
		menuItem[1].setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
		menuItem[2].setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_MASK));
		menubar.add(menu);
		setJMenuBar(menubar);
		//메뉴 설정
		
		//JPanel 생성
		panel = new JPanel();
		
		int[][] arr; //createMine 리턴값 받을 배열
		int cnt;     //main 지뢰 갯수
		
		switch(mode) {
		
		/*
		 * Easy Mode 6x6
		 */
		case EASY_MODE:
			setBounds(0, 0, 600, 720);
			panel.setBounds(0, 50, HUNDRED * 6, HUNDRED * 6);
			panel.setLayout(null);
			Buttons[][] easy_bt = new Buttons[6][6]; //지뢰찾기 보드 생성
			
			//이미지파일 객체화
			for(int i=0; i<9; i++)
				num[i] = new ImageIcon("pic//" + num_char[i] + ".png");
			
			arr = createMine(6, 6); //지뢰게임 생성
			cnt = howManyMine(arr, 6, 6); //총 지뢰 몇개인지 cnt에 return
			mine.setText(Integer.toString(cnt) + " 개");
			
			for(int i=0, y=0; i<6; i++, y+=100) {
				for(int j=0, x=0; j<6; j++, x+=100) {
					
					//지뢰버튼, '*'는 ASCII CODE로 42로 표현된다
					if( arr[i][j] == 42 ) easy_bt[i][j] = new Buttons("*");
					
						
					//숫자버튼, 버튼에 주변에 지뢰가 몇개 있는지 표시
					else easy_bt[i][j] = new Buttons(Integer.toString(arr[i][j]));


					easy_bt[i][j].setBackground(new Color(88, 88, 88));
					easy_bt[i][j].setBounds(x, y, HUNDRED, HUNDRED);
					easy_bt[i][j].setIcon(basic); //버튼 이미지는 기본이미지
					easy_bt[i][j].addMouseListener(new MouseAdapter() {//easy_bt에 접근하기 위해 익명클래스로 마우스어댑터 작성 ==> '0'을 누르면 주변도 눌려야함
						
						@Override
						public void mousePressed(MouseEvent e) {
							
							Buttons button = (Buttons)e.getSource();
							
							//마우스 왼클릭 시
							if(e.getButton() == 1) {
								button.setFlag(1); // 버튼의 flag= 1로 설정 ==> 우클릭 불가
								//System.out.println(button.getX()/100+ ", " + button.getY()/100);
								//지뢰일 경우
								if( button.getText().equals("*") ) {
									button.setIcon(mineIcon);
									//지뢰버튼 누르면 끝나는 옵션페인 설정
									JOptionPane.showMessageDialog(Minesweeper.this, "Mine is activated!", "Game Over", JOptionPane.WARNING_MESSAGE);
									System.exit(0);
								}
								
								//주변 지뢰가 0개일 경우
								else if( button.getText().equals("0") ) {
									button.setIcon( num[ Integer.parseInt(button.getText()) ] );
									
									//button.getX()/100은 클릭한 버튼의 x좌표, button.getY()/100은 클릭한 버튼의 y좌표이다
									zeroClicked(easy_bt, button.getY()/100, button.getX()/100);
								}
								
								//주변 지뢰가 n개일 경우
								else button.setIcon( num[ Integer.parseInt(button.getText()) ] );
							}
							
							//마우스 우클릭 시
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
					});//마우스 리스너 등록 부분
										
					panel.add(easy_bt[i][j]);
				}
			}
			check_bt.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					
					int mine_flag_cnt = 0;
					
					for(int yy=0; yy<6; yy++) 
						for(int xx=0; xx<6; xx++) //버튼이 지뢰이고 깃발을 꽂으면(change = 1) 카운트 증가
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
			Buttons[][] expert_bt = new Buttons[9][9]; //지뢰찾기 보드 생성
			
			//이미지파일 객체화
			for(int i=0; i<9; i++)
				num[i] = new ImageIcon("pic//" + num_char[i] + ".png");
		
			arr = createMine(9, 9);
			cnt = howManyMine(arr, 9, 9);
			mine.setText(Integer.toString(cnt) + " 개");
			
			for(int i=0, y=0; i<9; i++, y+=100) {
				for(int j=0, x=0; j<9; j++, x+=100) {
					
					//지뢰버튼, '*'는 ASCII CODE로 42로 표현된다
					if( arr[i][j] == 42 ) expert_bt[i][j] = new Buttons("*");

						
					//숫자버튼, 버튼에 주변에 지뢰가 몇개 있는지 표시
					else 
						expert_bt[i][j] = new Buttons(Integer.toString(arr[i][j]));

					expert_bt[i][j].setBackground(new Color(88, 88, 88));
					expert_bt[i][j].setBounds(x, y, HUNDRED, HUNDRED);
					expert_bt[i][j].setIcon(basic); //버튼 이미지는 기본이미지
					expert_bt[i][j].addMouseListener(new MouseAdapter() {//expert_bt에 접근하기 위해 익명클래스로 마우스어댑터 작성 ==> '0'을 누르면 주변도 눌려야함
						
						@Override
						public void mousePressed(MouseEvent e) {
							
							Buttons button = (Buttons)e.getSource();
							
							//마우스 왼클릭 시
							if(e.getButton() == 1) {
								button.setFlag(1); // 버튼의 flag= 1로 설정 ==> 우클릭 불가
								//System.out.println(button.getX()/100+ ", " + button.getY()/100);
								//지뢰일 경우
								if( button.getText().equals("*") ) {
									button.setIcon(mineIcon);
									//지뢰버튼 누르면 끝나는 옵션페인 설정
									JOptionPane.showMessageDialog(Minesweeper.this, "Mine is activated!", "Game Over", JOptionPane.WARNING_MESSAGE);
									System.exit(0);
								}
								
								//주변 지뢰가 0개일 경우
								else if( button.getText().equals("0") ) {
									button.setIcon( num[ Integer.parseInt(button.getText()) ] );
									
									//button.getX()/100은 클릭한 버튼의 x좌표, button.getY()/100은 클릭한 버튼의 y좌표이다
									zeroClicked(expert_bt, button.getY()/100, button.getX()/100);
								}
								
								//주변 지뢰가 n개일 경우
								else button.setIcon( num[ Integer.parseInt(button.getText()) ] );
							}
							
							//마우스 우클릭 시
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
					}); //우클릭 리스너 설정
					
					panel.add(expert_bt[i][j]);
					
				}
			}
			check_bt.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					
					int mine_flag_cnt = 0;
					
					for(int yy=0; yy<9; yy++) 
						for(int xx=0; xx<9; xx++) //버튼이 지뢰이고 깃발을 꽂으면(change = 1) 카운트 증가
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
			Buttons[][] master_bt = new Buttons[16][16]; //지뢰찾기 보드 생성
			
			//이미지파일 객체화
			for(int i=0; i<9; i++)
				num[i] = new ImageIcon("pic//" + num_char[i] + "_50.png");
			
			arr = createMine(16, 16);
			cnt = howManyMine(arr, 16, 16);
			mine.setText(Integer.toString(cnt) + " 개");
			
			for(int i=0, y=0; i<16; i++, y+=50) {
				for(int j=0, x=0; j<16; j++, x+=50) {
					
					//지뢰버튼, '*'는 ASCII CODE로 42로 표현된다
					if( arr[i][j] == 42 ) master_bt[i][j] = new Buttons("*");

						
					//숫자버튼, 버튼에 주변에 지뢰가 몇개 있는지 표시
					else 
						master_bt[i][j] = new Buttons(Integer.toString(arr[i][j]));

					master_bt[i][j].setBackground(new Color(88, 88, 88));
					master_bt[i][j].setBounds(x, y, FIFITY, FIFITY);
					master_bt[i][j].setIcon(basic_50); //버튼 이미지는 기본이미지
					master_bt[i][j].addMouseListener(new MouseAdapter() {//master_bt에 접근하기 위해 익명클래스로 마우스어댑터 작성 ==> '0'을 누르면 주변도 눌려야함
						
						@Override
						public void mousePressed(MouseEvent e) {
							
							Buttons button = (Buttons)e.getSource();
							
							//마우스 왼클릭 시
							if(e.getButton() == 1) {
								button.setFlag(1); // 버튼의 flag= 1로 설정 ==> 우클릭 불가
								//System.out.println(button.getX()/50+ ", " + button.getY()/50);
								//지뢰일 경우
								if( button.getText().equals("*") ) {
									button.setIcon(mineIcon_50);
									//지뢰버튼 누르면 끝나는 옵션페인 설정
									JOptionPane.showMessageDialog(Minesweeper.this, "Mine is activated!", "Game Over", JOptionPane.WARNING_MESSAGE);
									System.exit(0);
								}
								
								//주변 지뢰가 0개일 경우
								else if( button.getText().equals("0") ) {
									button.setIcon( num[ Integer.parseInt(button.getText()) ] );
									
									//button.getX()/50은 클릭한 버튼의 x좌표, button.getY()/50은 클릭한 버튼의 y좌표이다
									zeroClicked(master_bt, button.getY()/50, button.getX()/50);
								}
								
								//주변 지뢰가 n개일 경우
								else button.setIcon( num[ Integer.parseInt(button.getText()) ] );
							}
							
							//마우스 우클릭 시
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
					}); //우클릭 리스너 설정
					
					panel.add(master_bt[i][j]);
				}
			}
			check_bt.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mousePressed(MouseEvent e) {
					
					int mine_flag_cnt = 0;
					
					for(int yy=0; yy<16; yy++) 
						for(int xx=0; xx<16; xx++) //버튼이 지뢰이고 깃발을 꽂으면(change = 1) 카운트 증가
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
			//가로크기와 세로크기를 팝업 다이어로그로 입력받음
			int XX = 0;
			int YY = 0;
			
			try {
			
				XX = Integer.parseInt(JOptionPane.showInputDialog(Minesweeper.this, "가로크기를 입력하세요."));
				YY = Integer.parseInt(JOptionPane.showInputDialog(Minesweeper.this, "세로크기를 입력하세요."));
			
			} catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(Minesweeper.this, "숫자를 입력해주세요");
				System.exit(0);
				
			} catch(IllegalArgumentException e) {
				JOptionPane.showMessageDialog(Minesweeper.this, "숫자를 입력해주세요");
				System.exit(0);
			}
			
			int XXX = XX; //밑에 check_bt 리스너 for문에 XX와 YY를 인식 못해서 새 변수에 할당
			int YYY = YY;
			
			
			setBounds(10, 10, FIFITY * XX + 10, FIFITY * YY + 10 + 120);
			panel.setBounds(0, 50, FIFITY * XX, FIFITY * YY);
			panel.setLayout(null);
			Buttons[][] custom_bt = new Buttons[YY][XX]; //지뢰찾기 보드 생성
			
			//이미지파일 객체화
			for(int i=0; i<9; i++)
				num[i] = new ImageIcon("pic//" + num_char[i] + "_50.png");

			arr = createMine(YY, XX);
			cnt = howManyMine(arr, YY, XX);
			mine.setText(Integer.toString(cnt) + " 개");
			
			for(int i=0, y=0; i<YY; i++, y+=50) {
				for(int j=0, x=0; j<XX; j++, x+=50) {
					
					//지뢰버튼, '*'는 ASCII CODE로 42로 표현된다
					if( arr[i][j] == 42 ) custom_bt[i][j] = new Buttons("*");
						
					//숫자버튼, 버튼에 주변에 지뢰가 몇개 있는지 표시
					else 
						custom_bt[i][j] = new Buttons(Integer.toString(arr[i][j]));

					custom_bt[i][j].setBackground(new Color(88, 88, 88));
					custom_bt[i][j].setBounds(x, y, FIFITY, FIFITY);
					custom_bt[i][j].setIcon(basic_50); //버튼 이미지는 기본이미지
					custom_bt[i][j].addMouseListener(new MouseAdapter() {//custom_bt에 접근하기 위해 익명클래스로 마우스어댑터 작성 ==> '0'을 누르면 주변도 눌려야함
						
						@Override
						public void mousePressed(MouseEvent e) {
							
							Buttons button = (Buttons)e.getSource();
							
							//마우스 왼클릭 시
							if(e.getButton() == 1) {
								button.setFlag(1); // 버튼의 flag= 1로 설정 ==> 우클릭 불가
								//System.out.println(button.getX() / 50+ ", " + button.getY() / 50);
								//지뢰일 경우
								if( button.getText().equals("*") ) {
									button.setIcon(mineIcon_50);
									//지뢰버튼 누르면 끝나는 옵션페인 설정
									JOptionPane.showMessageDialog(Minesweeper.this, "Mine is activated!", "Game Over", JOptionPane.WARNING_MESSAGE);
									System.exit(0);
								}
								
								//주변 지뢰가 0개일 경우
								else if( button.getText().equals("0") ) {
									button.setIcon( num[ Integer.parseInt(button.getText()) ] );
									
									//button.getX()/50은 클릭한 버튼의 x좌표, button.getY()/50은 클릭한 버튼의 y좌표이다
									zeroClicked(custom_bt, button.getY()/50, button.getX()/50);
								}
								
								//주변 지뢰가 n개일 경우
								else button.setIcon( num[ Integer.parseInt(button.getText()) ] );
							}
							
							//마우스 우클릭 시
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
					}); //우클릭 리스너 설정
					
					panel.add(custom_bt[i][j]);
				}
			}
			check_bt.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					
					int mine_flag_cnt = 0;
					
					for(int yy=0; yy<YYY; yy++) 
						for(int xx=0; xx<XXX; xx++) //버튼이 지뢰이고 깃발을 꽂으면(change = 1) 카운트 증가
							if(custom_bt[yy][xx].getText().equals("*") && custom_bt[yy][xx].getChange() == 1)
								mine_flag_cnt++;
						
					if(mine_flag_cnt == cnt) 
						JOptionPane.showMessageDialog(panel, "You remove all Mines!", "Complete", JOptionPane.INFORMATION_MESSAGE);
						
					
					else
						JOptionPane.showMessageDialog(panel, "Check again.", "Again", JOptionPane.WARNING_MESSAGE);
				
				}
			});
			
			
		}//switch문 끝
		
		//마무리 세팅
		check_bt.setIcon(check);

		remain.setBounds(210, 10, 100, 25); //남은지뢰 수:
		mine.setBounds(300, 10, 30, 25); // n개
		check_bt.setBounds(380, 0, 50, 50);
		ct.add(remain);
		ct.add(mine);
		ct.add(check_bt);
		ct.add(panel);
		
		setVisible(true);
		//마무리 세팅
	}
	
	//n, m를 받아 지뢰가 있는 보드(배열) 생성
	public int[][] createMine(int m, int n) {
		
		int[][] mat = new int[m][n]; //모든 배열요소 초기값 = 0
		int count = 0; //배열요소 주변에 있는 지뢰갯수 Counting 변수
		
		int rand;
		//지뢰 개수를 (m+n)의 절반 이상으로 설정 = for문 횟수를 (m+n)/2 이상 설정
		do {
			rand = ran.nextInt(m + n);
		}while(rand < (m + n) / 2);
		
		//m+n 이하의 난수를 발생시켜 그만큼  loop를 돌리고 랜덤으로 지뢰'*'를 생성
		for(int i=0; i<rand; i++) {
			
			int m1 = ran.nextInt(m);
			int n1 = ran.nextInt(n);
			
			mat[m1][n1] = '*';
		}
		
		//각 배열요소 주변 지뢰 개수 Counting
		for(int i=0; i<m; i++) {
			for(int j=0; j<n; j++) {
				count = 0;
				if( mat[i][j] == '*') continue;
				
				else if(i == 0 && j == 0 && mat[0][0] != '*') { //0,0 일 때
					for(int q=0; q<2; q++)
						for(int w=0; w<2; w++)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				else if(i == 0 && j == n-1 && mat[0][n-1] != '*') { //0,n-1 일 때
					for(int q=0; q<2; q++)
						for(int w=n-1; w>=n-2; w--)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				else if(i == m-1 && j == 0 && mat[m-1][0] != '*') { //m-1, 0 일 때
					for(int q=i; q>=i-1; q--)
						for(int w=0; w<2; w++)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				else if(i == m-1 && j == n-1 && mat[m-1][n-1] != '*') { //m-1, n-1 일 때
					for(int q=i; q>=i-1; q--)
						for(int w=j; w>=j-1; w--)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				//////////////////////////////////////////////////////////////////////
				else if(i == 0 && mat[0][j] != '*') {// y=0일 때
					for(int q=0; q<2; q++)
						for(int w=j-1; w<=j+1; w++)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				else if(i == m-1 && mat[m-1][j] != '*') {// y=m-1일 때
					for(int q=m-2; q<=m-1; q++)
						for(int w=j-1; w<=j+1; w++)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				else if(j == 0 && mat[i][0] != '*') { // x=0일 때
					for(int q=i-1; q<=i+1; q++)
						for(int w=0; w<2; w++)
							if( mat[q][w] == '*') count++;
					mat[i][j] = count;
				}
				else if(j == n-1 && mat[i][n-1] != '*') { // x=n-1일 때
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
		}//mat 배열 생성loop 끝
		return mat;
	}
	
	//총 지뢰갯수 세어주는 메소드
	public int howManyMine(int[][] arr, int m, int n) {
		
		int cnt = 0;
		
		for(int i=0; i<m; i++)
			for(int j=0; j<n; j++)
				if(arr[i][j] == '*')
					cnt++;
		
		return cnt;
	}
	
	//메뉴에 추가 할 액션리스너
	class OnActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			RestartThread th = new RestartThread(); //모드가 변경되면 새 thread를 열어 새 게임 시작
			
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
		/* i = y좌표, j = x좌표
		 * 좌표가 모서리와 테두리인 부분부터 구현하자.
		 * 모서리 (y, x)
		 * (0, 0), (n-1, m-1), (0, m-1), (n-1, 0)
		 * 테두리 (y, x)
		 * (0, *), (n-1, *), (*, 0), (*, m-1) easy모드에서 n=6 m=6
		 */
		int arr_Y, arr_X;
		
		if(mode == 4) {
			arr_Y = bt.length - 1; arr_X = bt[0].length - 1;
		}
		else
			arr_X = arr_Y = bt.length - 1;
		
		//System.out.println(arr_X + ", " + arr_Y); //디버깅용
		
		if(i == 0 && j == 0) {//(y, x) = (0, 0)일 때
			for(int y=i; y<i+2; y++) {
				for(int x=j; x<j+2; x++) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
						
				}
			}
		}//(0, 0)
		
		else if(i == arr_Y && j == 0) {//(y, x) = (5, 0)일 때
			for(int y=i; y>i-2; y--) {
				for(int x=j; x<j+2; x++) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
					
				}
			}
		}//(5, 0)
		
		else if(i == 0 && j == arr_X) {//(y, x) = (0, 5)일 때
			for(int y=i; y<i+2; y++) {
				for(int x=j; x>j-2; x--) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
					
				}
			}
		}//(0, 5)
		
		else if(i == arr_Y && j == arr_X) {//(y, x) = (5, 5)일 때
			for(int y=i; y>i-2; y--) {
				for(int x=j; x>j-2; x--) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
					
				}
			}
		}//(5, 5)
		//<<<<<<<<<<<<<<<<<<<<<<<<<<<여기 까지 모서리, 아래부터 테두리>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		
		else if(i == 0) {//(y, x) = (0, *)일 때
			for(int y=i; y<i+2; y++) {
				for(int x=j-1; x<j+2; x++) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
					
				}
			}
		}//(0, *)
		
		else if(i == arr_Y) {//(y, x) = (5, *)일 때
			for(int y=i; y>i-2; y--) {
				for(int x=j-1; x<j+2; x++) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );	
					
				}
			}
		}//(5, *)
		
		else if(j == 0) {//(y, x) = (*, 0)일 때
			for(int y=i-1; y<i+2; y++) {
				for(int x=j; x<j+2; x++) {
					
					if(y == i && x == j) continue;
					
					bt[y][x].setFlag(1);
					bt[y][x].setIcon( num[ Integer.parseInt(bt[y][x].getText()) ] );
					
				}
			}
		}//(*, 0)
		
		else if(j == arr_X) {//(y, x) = (*, 5)일 때
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
	
	//Thread 교재 12장 참조
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
