package FINAL;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
	�ʱ� ���´� �����
	(��)(��)
	(��)(��)
	�� ���¿��� �����ϰ� �ȴ�.
	
	����ڴ� ������, ��ǻ�ʹ� �򵹷� �÷����ϰ� �Ǹ�
	�������� ����ڰ� ���� ���� �ΰ� �ȴ�.
*/

// ������ �÷��� ȭ��
public class PlayReversi extends JPanel
{	
	private Reversi reversi;						// ���������� �����θ� �����ϴ� �ھ� (�Լ����� ��ȯ���� �̿��Ͽ� ȭ�鿡 ǥ��)
	
	private JButton btnBack;						// �ڷΰ��� ��ư
	private JLabel lblLevel;						// ���� ǥ�ø� ���� ��
	private JLabel lblWhiteCount, lblBlackCount;	// ��, �������� ���� ���� ǥ�ø� ���� ��
	
	private PanelReversi panelRev;					// PanelReversi �� �Լ����� ȣ���ϱ� ���� ���� (����)
	
	public PlayReversi(PanelReversi panel)
	{
		setPreferredSize(new Dimension(1280,720));
		setBackground(new Color(57,131,66));
		setLayout(null);

		reversi = null;	// ������ ���õǸ� init(Level) �Լ����� �ʱ�ȭ�ϰ� �ȴ�
		panelRev = panel;
		
		// ����, ������ ����, �� ���� ǥ�ø� ���� �󺧵� �ʱ�ȭ
		lblLevel = new JLabel();
		lblWhiteCount = new JLabel();
		lblBlackCount = new JLabel();
		
		lblLevel.setForeground(new Color(221,134,117));
		lblWhiteCount.setForeground(Color.white);
		lblBlackCount.setForeground(Color.black);
		
		lblLevel.setFont(new Font("Consolas",Font.BOLD,80));
		lblWhiteCount.setFont(new Font("Consolas",Font.BOLD,120));
		lblBlackCount.setFont(new Font("Consolas",Font.BOLD,120));
		
		lblLevel.setBounds(800,20,360,160);
		lblWhiteCount.setBounds(735,160,240,180);
		lblBlackCount.setBounds(985,160,240,180);
		
		lblLevel.setHorizontalAlignment(SwingConstants.CENTER);
		lblWhiteCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlackCount.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblLevel.setVerticalAlignment(SwingConstants.CENTER);
		lblWhiteCount.setVerticalAlignment(SwingConstants.CENTER);
		lblBlackCount.setVerticalAlignment(SwingConstants.CENTER);
		
		//�ڷΰ��� ��ư �̹��� ����� //
		// �̹��� ������ ��ȯ
		ImageIcon image = new ImageIcon(Main.class.getResource("../images/back.png"));
		Image pre = image.getImage();
		Image scale = pre.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
		
		btnBack = new JButton(new ImageIcon(scale));
		// ��� ����
		btnBack.setBorderPainted(false);
		// ���� ����
		btnBack.setContentAreaFilled(false);
		// ��Ŀ���� ��� ����
		btnBack.setFocusPainted(false);
		
		btnBack.addActionListener(new BackListener());	// �ڷΰ��� ��ư ������
		btnBack.setBounds(1050,630,200,60);
		add(btnBack);
		add(lblLevel);
		add(lblWhiteCount);
		add(lblBlackCount);
		addMouseListener(new BoardListener());	// �ٵ��ǿ��� Ŭ���� ��ġ�� �Ǻ��ϰ� ó���� �����ϱ� ���� ������
	} // PlayReversi Constructor
	
	public void paintComponent(Graphics page)
	{
		// �ٵ���, �ٵϾ� �׸���
		Graphics2D g2d = (Graphics2D) page; // ���� �β��� �����ϱ� ����
		super.paintComponent(page);
		
		page.setColor(new Color(128,64,64));
		g2d.setStroke(new BasicStroke(6f));	// �� �β� ����

		// ��Ƽ �ٸ���� ����
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		
		// 8*8 �ٵ��� �׸���
		for (int i=1; i<=9; i++)
		{
			page.drawLine(70,70*i,630,70*i);
			page.drawLine(70*i,70,70*i,630);
		}
		
		// ���� ���¸� �������� �ٵϾ� �׸���
		g2d.setStroke(new BasicStroke(4f));	// �ٵϾ� �׵θ��� �β�
		
		for (int row=1; row<=Reversi.BOARD_SIZE; row++)
			for (int col=1; col<=Reversi.BOARD_SIZE; col++)
				drawDisc(page, row, col, reversi.statusAt(row, col));
		
	} // paintComponent()
	
	// �ٵ����� �ش� (��, ��) �� BLACK/WHITE �ٵϾ��� �׸��� �Լ� 
	public void drawDisc(Graphics page, int row, int col, Status status)
	{
		// Status �� EMPTY � �����ϰ� �ֱ� ������ WHITE/BLACK �϶��� �׸�
		if (status==Status.WHITE || status==status.BLACK)
		{
			// �ٵϾ� �׸���
			page.setColor((status==Status.WHITE ? Color.white : Color.black));
			page.fillOval(70*row+6, 70*col+6, 58, 58);
			// �ٵϾ� �׵θ� �׸���
			page.setColor(Color.black);
			page.drawOval(70*row+6, 70*col+6, 58, 58);
		}
	} // drawDisc()
	
	// ���� ������ ���� ǥ�� �󺧿� ������ �����ϴ� �Լ�
	public void highlightLabel()
	{
		if (reversi.getTurn() == Status.WHITE)
		{
			lblWhiteCount.setBorder(BorderFactory.createLineBorder(new Color(234, 204, 26), 10, true));
			lblBlackCount.setBorder(null);
		}
		else
		{
			lblWhiteCount.setBorder(null);
			lblBlackCount.setBorder(BorderFactory.createLineBorder(new Color(234, 204, 26), 10, true));
		}
	} // highlightLabel()
	
	// �÷��� �� �ʱ�ȭ�ϴ� �Լ�
	// LevelPanel ���� ������ ���õǰ� �� PanelReversi ���� ȣ���ϴ� �Լ�
	public void init(Level level)
	{
		// �˸��� ������ �ھ� �ʱ�ȭ
		reversi = new Reversi(level);
		
		lblLevel.setText(reversi.getLevel().toString());
		lblWhiteCount.setText("2");
		lblBlackCount.setText("2");
		
		highlightLabel();
	} // init()
	
	// ���� ����� ���� ������ �����ϱ� ���� �Լ�
	public void handleEnd()
	{
		// ������ ���и� ǥ���ϰ�, ��õ��� ������ �����ϴ� �޽��� �ڽ��� ���
		int select = JOptionPane.showConfirmDialog(this,
								(reversi.getBlackCount() > reversi.getWhiteCount() ? "Player" : "Computer") + " win !!"
								+ System.lineSeparator() + "Retry?",
									"Game finished", JOptionPane.YES_NO_OPTION);
	
		if (select == JOptionPane.YES_OPTION)
		{
			// �ʱ�ȭ �� ȭ�� �ٽ� �׸���
			init(reversi.getLevel());
			repaint();
		}
		// �ƴϿ��� �����ų� �޽��� �ڽ��� �׳� ������ �ƹ� �۾��� ���� ����
		
	} // handlEnd()
	
	// �ڷΰ��� ��ư�� ���� ������
	public class BackListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent evt)
		{
			if (evt.getSource() == btnBack)
			{
				
				// ������ ���� ���¿��� �ڷΰ��� ��ư�� ���� ��� (handleEnd �Լ����� �ƴϿ��� ��ȭ���ڸ� ���� ���)
				if (reversi.isEnd())
					panelRev.selectLevel(); // �ٷ� ���� ���� ȭ���� ���
				// ���� ���߿� �ڷΰ��� ��ư�� Ŭ���ϸ�
				else
				{	// ���� �ڷΰ��� �Ұ����� �ѹ� ���
					int select = JOptionPane.showConfirmDialog(PlayReversi.this,
															"Are you sure?",
															"Back to level selection", JOptionPane.YES_NO_OPTION);
					
					if (select == JOptionPane.YES_OPTION)
						panelRev.selectLevel();
				}
			}
		} // actionPerformed()
	} // BackListener Inner Class
	
	// �ٵ��ǿ��� �̺�Ʈ�� �߻��ϸ� ��ǥ�� �̿��� (��, ��) �� �Ǻ��ϰ� ó���� �����ϱ� ���� �̳� Ŭ����
	public class BoardListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent evt) {}
		@Override
		public void mousePressed(MouseEvent evt) {}
		@Override
		public void mouseEntered(MouseEvent evt) {}
		@Override
		public void mouseExited(MouseEvent evt) {}
		
		// �÷����� �ֵ��ڰ� �����̱� ������(������ ���� ���ƾ� ��ǻ�Ͱ� ���� ���� ������)
		// ��ǻ�Ͱ� ���� ���� ���� ó���ϴ� �κб��� mouseReleased(MouseEvent) �Լ��� ���Խ��Ѿ� ��.
		@Override
		public void mouseReleased(MouseEvent evt)
		{
			Point pt = evt.getPoint();
			// �̺�Ʈ�� �߻��� ���� ��ǥ�� ���� (��, ��) ������ ����
			int row = pt.x / 70;
			int col = pt.y / 70;
			
			// ��ǥ�� �ٵ����� ����ų� ������ ���� ���¶�� �ƹ��͵� ����
			if ( (row<1 || 8<row) || (col<1 || 8<col) || reversi.isEnd() )
				return;
			
			// ���� ���� �� �ִ� ��� ���������� ���� �� ������Ʈ�� �� true
			// ���� �� ���� ��� false
			if (reversi.handleInput(row, col))
			{
				repaint(); // �ٵ��� �ٽ� �׸�
				
				// �󺧿� ǥ�õǴ� ���� ���� ����
				lblWhiteCount.setText(Integer.toString(reversi.getWhiteCount()));
				lblBlackCount.setText(Integer.toString(reversi.getBlackCount()));
				
				// ������ ����Ǿ��� ��� true (�� �÷��̾� ��� ���� �� ���� ���� ���)
				if (reversi.isEnd())
				{
					handleEnd();
					return;
				}
				
				// ���� ������ �󺧿� ���� ����
				highlightLabel();
				
				// ���� ������ �÷��̾ ���� ���� �� �ִ� ���� ���� ��� true (���� Computer �� ����)
				if (reversi.isPass())
				{
					JOptionPane.showMessageDialog(PlayReversi.this, "Computer can't put disc anywhere. Pass.");
					reversi.toggleTurn();	// ���ʸ� ���� (�� -> �浹) (��ǻ�� -> ����)
					highlightLabel();	// ���� ������ ���� �� ���� ����
				}
				else // ��ǻ�Ͱ� �� ���� ���� ���
				{
					while (true) // ��ǻ�Ͱ� ���� ���� ������Ʈ ������, ������ ���� �� �ִ� ���� ���� ��찡 ���������� �߻��� �� �ֱ� ���� 
					{
						// Reversi�� ��ü ������ ���ڷ� �Ѱ��� level �� �̿��Ͽ� ������ �˸��� ��ġ�� �����Ͽ� ��ȯ 
						Position computerPosition = reversi.positionGenerator();
						
						// 3~6 �ʰ� �������� ��ǻ���� �� �������ϴ� �κ� //
						
						/*
							Thread.sleep() ���� �Լ��� ����� ��� ���� repaint() �� ������� �ʴ� ����
							������ �־�, �޽��� �ڽ��� ���� ���� repaint() �Լ��� ����ȴٴ� ���� �̿�.
							�޽��� �ڽ��� ����, Timer ���� ������ �ð��� ������ dispose ��Ű�� ������� �۵�.
							�޽��� �ڽ��� ��ǥ�� (-1000, -1000) �̹Ƿ� ȭ�鿡 �������� ����.
						*/
						
						JDialog jd = (new JOptionPane("",JOptionPane.PLAIN_MESSAGE)).createDialog("");
						jd.setLocation(-1000, -1000);
						
						Timer timer = new Timer(1, new ActionListener() {
					        @Override
					        public void actionPerformed(ActionEvent e) { jd.dispose(); }
					    });
						
						timer.setInitialDelay(((int)(Math.random()*4) + 3) * 1000); // ��ǻ�͸� ��ٸ��� �ð��� �����Ϸ��� ms ������ �Է��ϼ���
						//timer.setInitialDelay(ms);
						timer.setRepeats(false);
						timer.start();
						jd.setVisible(true);
						
						// 3~6 �ʰ� �������� ��ǻ���� �� �������ϴ� �κ� //

						// ��ǻ�Ͱ� ������ ��ġ�� �̿��Ͽ� ���������� ó��
						reversi.handleInput(computerPosition.getRow(), computerPosition.getCol());
						
						repaint();
						lblWhiteCount.setText(Integer.toString(reversi.getWhiteCount()));
						lblBlackCount.setText(Integer.toString(reversi.getBlackCount()));
						
						// ������ ����Ǿ��� ��� true (�� �÷��̾� ��� ���� �� ���� ���� ���)
						if (reversi.isEnd())
						{
							handleEnd();
							break;
						}
						
						// ���� ������ ���� �� ���� ����
						highlightLabel();
						
						// ���� ������ �÷��̾ ���� ���� �� �ִ� ���� ���� ��� true (���� ������ ����)
						if (reversi.isPass())
						{
							JOptionPane.showMessageDialog(PlayReversi.this, "Player can't put disc anywhere. Pass.");
							reversi.toggleTurn();	// ���ʸ� ���� (�浹 -> �鵹) (���� -> ��ǻ��)
							highlightLabel(); // ���� ������ ���� �� ���� ����
						}
						else // ������ ���� ���� �� �ִ� ��ġ�� ���� ���
						{
							break; // while ������ �����ϰ� mouseReleased() �� �����
						}
					} // while
				}
			}
			else // ������ ��ġ�� ���� ���� �� ���� ��ġ�� ���
			{
				JOptionPane.showMessageDialog(PlayReversi.this, "Cant put disc to there.");
			}
		} // mouseReleased()
	} // BoardListener Inner Class
} // PlayReversi Class