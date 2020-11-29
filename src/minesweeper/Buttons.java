package minesweeper;

import javax.swing.JButton;

//flag ���� �ϴ� state, change ������ �����ϱ� ���� Buttons Class ����
public class Buttons extends JButton{
	
	//���콺 ��Ŭ�� �� ��ư�� flag=1�� �Ǿ� ��߾����� ���� �Ұ�
	private int flag = 0;
	//���콺 ��Ŭ�� �� �� ��� -> ����ǥ -> basic���� ��ȯ
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
