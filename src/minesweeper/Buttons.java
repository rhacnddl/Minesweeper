package minesweeper;

import javax.swing.JButton;

//flag 역할 하는 state, change 변수를 설정하기 위해 Buttons Class 생성
public class Buttons extends JButton{
	
	//마우스 왼클릭 된 버튼은 flag=1로 되어 깃발아이콘 설정 불가
	private int flag = 0;
	//마우스 왼클릭 할 때 깃발 -> 물음표 -> basic으로 순환
	private int change = 0;
	
	public Buttons(String str) {
		super(str);
	}
	public Buttons() {
		super();
	}
	
	public void setFlag(int state) {
		this.flag = state;
	}
	public int getFlag() {
		return flag;
	}
	public void setChange(int change) {
		this.change = change;
	}
	public int getChange() {
		return change;
	}
	
}
