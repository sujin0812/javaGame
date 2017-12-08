package FINAL;
import javax.swing.*;

// ������ ���࿡ �ʿ��� LevelPanel �� PlayPanel �� ��� �ִ� Ŭ����
public class PanelReversi extends JPanel
{	
	private JPanel FirstScreen;	// �ڷΰ���� ���� ���� ȭ�� ǥ�ø� ���� ���� (����)
	private LevelReversi LevelPanel;	// ���� ���� �г�
	private PlayReversi PlayPanel;		// ������ �÷��� �г�
	
	public PanelReversi(JPanel primary, JPanel THEFIRSTPANEL)
	{
		FirstScreen = THEFIRSTPANEL;
		LevelPanel = new LevelReversi(this);
		PlayPanel = new PlayReversi(this);
		
		PlayPanel.setVisible(false);	// �ʱ⿡ ���� ���� ȭ�鸸 ���̰� �ϱ� ����
		LevelPanel.setBounds(0,0,1280,720);
		PlayPanel.setBounds(0,0,1280,720);
		
		setBounds(0, 0, 1280, 720);
		primary.add(this);
		
		add(LevelPanel);
		add(PlayPanel);
	} // PanelReversi()

	// ���� ���� ȭ�� ǥ��
	public void selectLevel()
	{
		LevelPanel.setVisible(true);
		PlayPanel.setVisible(false);
	} // selectLevel()
	
	// LevelPanel ���� ������ ���õǸ�, ��ư �����ʿ��� ȣ���ϴ� �Լ��̴�
	public void runReversi()
	{
		LevelPanel.setVisible(false);
		PlayPanel.init(LevelPanel.getLevel()); // ������ ������ �ʱ�ȭ
		PlayPanel.setVisible(true);	
	} // runReversi()
	
	// ���� ����
	public void exit()
	{
		LevelPanel.setVisible(false);
		PlayPanel.setVisible(false);
		setVisible(false);
		FirstScreen.setVisible(true);
	} // exit()
	
	// ������ ��ĥ�� ���Ǹ� ���� �������̵�
	@Override
	public void setVisible(boolean aFlag)
	{
		super.setVisible(aFlag);
	
		// aFlag �� true �� ���ڷ� ������ ���� ���� ȭ���� �����ش�
		if (aFlag)
			selectLevel();
	}
}