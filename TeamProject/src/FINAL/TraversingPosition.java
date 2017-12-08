package FINAL;

// ���� �ΰ��� �������� ������ ���� �������� 8�������� �̵����� �������� Ŭ����
public class TraversingPosition extends Position
{
	public TraversingPosition()
	{
		super();
	}
	public TraversingPosition(int row, int col)
	{
		super(row,col);
	}
	
	// ���� �̵����� ���� �޼ҵ��
	public void moveUp()	{ row--; }	// ��
	public void moveDown()	{ row++; }	// ��
	public void moveLeft()	{ col--; }	// ��
	public void moveRight()	{ col++; }	// ��
	public void moveRightUp()	{ moveUp(); moveRight(); }	// ��/��
	public void moveRightDown()	{ moveDown(); moveRight(); }	// ��/��
	public void moveLeftUp()	{ moveUp(); moveLeft(); }	// ��/��
	public void moveLeftDown()	{ moveDown(); moveLeft(); }		// ��/��
} // TraversingPosition Class