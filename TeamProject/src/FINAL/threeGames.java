package FINAL;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class threeGames extends JFrame {

	//�⺻������ threeGamesŬ������ JFrame�� ��ӹ޾� Frame���μ� ������ �Ѵ�.
	private JFrame t;//�ڽ��� ���� ����
	private JPanel primary = new JPanel();// ��� �������� ���� �г�(���� ������ ���� ���� �����߱⿡ ��ġ�� ���� ����� ���� ��ġ�̴�)
	private JPanel ScreenOne = new JPanel();// game image�� ���� �г�, �� �ؿ� 2������ ���� �г�
	private JPanel game1 = new JPanel();// ������ ���ð� ������ �г�
	private JPanel game2 = new JPanel();// ��Ʈ���� ���ð� ������ �г�

	private Mou ML;// game1,game2�г� ������ �ϴ� �ൿ�鿡 �����Ҽ��ֵ��� ���ִ� ���콺 ������

	// game menu mouseExited image
	private ImageIcon preImage1, preImage2;// ���콺 �ø����� �̹����� ��Ÿ����
	// game menu mouseEntered image
	private ImageIcon postImage1, postImage2;//���콺�� �ø��� �̹����� ��Ÿ����

	// Image Icon�̹��� ������ ��ȯ�ϱ� ���� ����
	private Image img, scale;
	// game name label
	private JLabel img1, img2;

	// menu-bar
	private JLabel menuBar = new JLabel(new ImageIcon(Main.class.getResource("../images/menubar.jpg")));

	// program exit button mouseEntered image
	private ImageIcon exitBEImage = new ImageIcon(Main.class.getResource("../images/exitbutton.png"));

	// program exit button mouseExited image
	private ImageIcon exitBDImage = new ImageIcon(Main.class.getResource("../images/exitbutton_disable.png"));
	private JButton exitButton = new JButton(exitBDImage);

	// get X, Y from mouse Input
	private int mouseX, mouseY;


	public threeGames() {
		t = this;
		initialize();
		// â�� ���� initialize

		// game menu mouseExited image

		preImage1 = new ImageIcon(Main.class.getResource("../images/logo1.png"));
		img = preImage1.getImage();
		scale = img.getScaledInstance(635, 690, java.awt.Image.SCALE_SMOOTH);
		preImage1 = new ImageIcon(scale);

		preImage2 = new ImageIcon(Main.class.getResource("../images/logo2.png"));
		img = preImage2.getImage();
		scale = img.getScaledInstance(635, 690, java.awt.Image.SCALE_SMOOTH);
		preImage2 = new ImageIcon(scale);

		// game menu mouseEntered image
		postImage1 = new ImageIcon(Main.class.getResource("../images/othello.gif"));
		postImage2 = new ImageIcon(Main.class.getResource("../images/rhythm.gif"));

		// ������ �ٷ����� ���� primary�� ���� �ʱ�ȭ
		primary.setPreferredSize(new Dimension(Main.screenWidth, Main.screenHeight));
		primary.setLayout(null);
		primary.setBounds(0, 30, 1280, 720);// JFrame�� ��ġ 0,30 ���κ��� ���� 1280 ���� 720
		primary.setBackground(Color.red);

		// game ����Panel 
		ScreenOne.setPreferredSize(new Dimension(1280, 720));
		ScreenOne.setLayout(null);
		ScreenOne.setBounds(0, -30, 1280, 720);// primary�������� (0,-30)�� ���� 1280 ���� 720�� ũ�⸸ŭ�� �ش�
		ScreenOne.setBackground(Color.black);// ���� �������� ����
		// primary�� 30��ŭ ���������� ����ġ ��Ű����
		ML = new Mou();

		// �޴��ٿ� �����ư�� ���� �����ӿ� ������ �ִ´�.
		ExampleImagesEdit();

		// display image
		ScreenOne.add(game1);
		ScreenOne.add(game2);
		primary.add(ScreenOne);

		// Ÿ��Ʋ��
		exitButtonEdit();
		add(exitButton);
		menuBarEdit();
		add(menuBar);

		// Ÿ��Ʋ��
		this.getContentPane().add(primary);
		setVisible(true);

	}// twoGames() ������

	private void initialize() {
		// ������ ����

		// delete frame title bar
		setUndecorated(true);
		// set Size
		setSize(new Dimension(Main.screenWidth, Main.screenHeight));
		// setBackground(new Color(0, 0, 0, 0));

		// can not change frame size
		setResizable(true);

		// display frame on the center of screen
		setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
	}// initialize()

	// exit Button setting
	private void exitButtonEdit() {
		exitButton.setBounds(1250, 0, 30, 30);// �� ������ ���� ����

		// remove border
		exitButton.setBorderPainted(false);
		// JButton�� Focus���� �� ������ ������ �ʰ� ��
		exitButton.setFocusPainted(false);
		// JButton ���뿵�� ��ä��
		exitButton.setContentAreaFilled(false);
		// ��ư ����� �Ӽ� ���ش�
		exitButton.addMouseListener(ML);
	}// exitButtonEdit

	// setting menu-bar
	private void menuBarEdit() {
		menuBar.setBounds(0, 0, 1280, 30);
		menuBar.addMouseListener(ML);
		menuBar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			// menu bar�� �����̸� �װͿ� �°� ȭ��â�� �����̰� �ϴ� event
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - mouseX, y - mouseY);
			}
		});
	}// menuBarEdit

	//game�� �̹��� �� ���콺 �����ʸ�  �޼ҵ�
	private void ExampleImagesEdit() {
		img1 = new JLabel(preImage1);
		img2 = new JLabel(preImage2);

		// game1�� ���� �г� ������ ���콺 ������ ����
		game1.setPreferredSize(new Dimension(635, 690));
		game1.setBounds(0, 30, 635, 690);
		game1.addMouseListener(ML);
		game1.setLayout(null);
		img1.setBounds(0, 0, 635, 690);
		game1.add(img1);

		// game2�� ���� �г� ������ ���콺 ������ ����
		game2.setPreferredSize(new Dimension(635, 690));
		game2.setBounds(645, 30, 635, 690);
		game2.addMouseListener(ML);
		game2.setLayout(null);
		img2.setBounds(0, 0, 635, 690);
		game2.add(img2);

	}// Example Image Edit

	// ���콺 ������ ����
	private class Mou implements MouseListener {
		public void mousePressed(MouseEvent e) {
			Object obj = e.getSource();

			if (obj == exitButton) {// click exit button
				System.exit(0);
			} else if (obj == menuBar) { // click menu bar
				mouseX = e.getX();
				mouseY = e.getY();
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			Object obj = e.getSource();
			if (obj == game1) { // othello

				System.out.println("START Othello");
				new PanelReversi(primary, ScreenOne);
				ScreenOne.setVisible(false);

			} else if (obj == game2) { // rhythm game

				System.out.println("START Rhythm Game");
				//new RhythmGame(primary, ScreenOne, t);
				ScreenOne.setVisible(false);
				// isRhythmGameMainScreen = true;
				// isMainScreen = false;
			}
			

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			Object obj = e.getSource();
			if (obj == game1) { // othdello
				System.out.println("Play preview GAME1");
				img1.setIcon(postImage1);
				img1.setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else if (obj == game2) { // Rhythm game
				System.out.println("Play preview GAME2");
				img2.setIcon(postImage2);
				img2.setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else if (obj == exitButton) { // exit button
				exitButton.setIcon(exitBEImage);
				// change cursor
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// stop Image
			Object obj = e.getSource();
			if (obj == game1) {// Othello
				System.out.println("Stop preview GAME1");
				img1.setIcon(preImage1);
				img1.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			} else if (obj == game2) {// Rhythm game
				System.out.println("Stop preview GAME2");
				img2.setIcon(preImage2);
				img2.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			} else if (obj == exitButton) {
				exitButton.setIcon(exitBDImage);
				// change cursor
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			//
			Object obj = e.getSource();
			if (obj == game1) {
				System.out.println("Stop preview GAME1");
			} else if (obj == game2) {
				System.out.println("Stop preview GAME2");

			}
		}
	}// MouseListener

}