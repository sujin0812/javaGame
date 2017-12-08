package FINAL;

import java.util.*;

/*
	Reversi Ŭ������ �������� ������ ���������� ó���ϸ�
	GUI �� �����ϱ� ���� ������ ��� �޼ҵ带 ���� ���޹޴´�.
	(���� ���� �ڵ����� �ٲ�Ƿ� Ư���� ���� �����ϰ�� ��������� �ٲ��� �ʿ䰡 ����)
	(Ư���� ���� ���� �� ���� ���� �н��ϴ� ���)
*/

// ���������� �����θ� �����ϴ� �ھ�
public class Reversi
{
	public static final int BOARD_SIZE = 8; 			// �ٵ����� ũ�� ����
	private Level level;								// ����
	private Status[][] status;							// �ٵ����� [n+2][n+2] ũ���� ����(Status)�� ���� (�𼭸��� Status.BOUND)
	private Status currentTurn;							// ���� ����
	private int whiteCount;								// ���� �� ���� ����
	private int blackCount;								// ���� ���� ���� ����
	private ArrayList <Position> whiteList;				// ���� ��� �� ������ ��ġ�� �����ϴ� ����Ʈ	
	private ArrayList <Position> blackList;				// ���� ��� ���� ������ ��ġ�� �����ϴ� ����Ʈ
	private ArrayList <Position> whiteAvailableList;	// ���� �� ���� �� �� �ִ� ��ġ���� �����ϴ� ����Ʈ
	private ArrayList <Position> blackAvailableList;	// ���� ���� ���� �� �� �ִ� ��ġ���� �����ϴ� ����Ʈ

	// ���� �� �� �ִ� ��ġ���� ���������μ�
	// ���� ����� ���� �δ°��� ���� ������ �Ǻ��� ����������, ��ǻ���� ��ġ ���� ����� �����ϴµ� �����ϴ�.
	
	public Reversi(Level lv)
	{
		level = lv;
		
		whiteList = new ArrayList<Position>();		
		blackList = new ArrayList<Position>();
		whiteAvailableList = new ArrayList<Position>();
		blackAvailableList = new ArrayList<Position>();	

		status = new Status[BOARD_SIZE+2][BOARD_SIZE+2];
		
		currentTurn = Status.BLACK; // ���� ��(����) ���� ����
		
		// �ٵ����� �ʱ� ���·� �ʱ�ȭ
		for (int row=0; row<BOARD_SIZE+2; row++)
			for (int col=0; col<BOARD_SIZE+2; col++)
				if (row==0 || col==0 || row==BOARD_SIZE+1 || col==BOARD_SIZE+1)
					status[row][col] = Status.BOUND;
				else if ((row==BOARD_SIZE/2 && col==BOARD_SIZE/2) || (row==BOARD_SIZE/2+1 && col==BOARD_SIZE/2+1))
					status[row][col] = Status.WHITE;
				else if ((row==BOARD_SIZE/2 && col==BOARD_SIZE/2+1) || (row==BOARD_SIZE/2+1 && col==BOARD_SIZE/2))
					status[row][col] = Status.BLACK;
				else
					status[row][col] = Status.EMPTY;

		whiteCount = 2;
		blackCount = 2;

		refreshList();			// ���� ������ ���� ��ġ�� �����ϴ� whiteList, blackList �� ����
		refreshAvailableList();	// ���� ���� ���� �� �ִ� ��ġ�� �����ϴ� whiteAvailableList, blackAvailableList �� ����
	} // Reversi Constructor

	// Getter/Setter
	public Level getLevel()	{ return level; }
	public void setLevel(Level level)	{ this.level = level; }
	public Status getTurn()	{ return currentTurn; }
	public Status statusAt(int row, int col)	{ return status[row][col]; }
	public int getWhiteCount()	{ return whiteCount; }
	public int getBlackCount()	{ return blackCount; }
	public int getAvailableSize(Status status)
	{ return (status==Status.WHITE ? whiteAvailableList.size() : blackAvailableList.size()); }
	
	// ���� ���� �� �ִ� ���, (��, ��) ������ �̿��Ͽ� ���������� ���¿� ������� ����
	public boolean handleInput(int row, int col)
	{
		if (isPutAvailable(row,col))	// �ش� ��ġ�� ���� ���� �� �ִ� ��ġ���� Ȯ�� 
		{
			putDisc(row,col);	// status[row][col] �� ���� ����
			refreshStatus(row,col);	// �ش� (��, ��) ���� 8�������� �̵��ϸ� ����� ���� ������
			refreshList(); // ���� ������ ���� ��ġ�� �����ϴ� whiteList, blackList �� ����
			refreshAvailableList(); // ���� ���� ���� �� �ִ� ��ġ�� �����ϴ� whiteAvailableList, blackAvailableList �� ����
			toggleTurn();	// ���� ���� (currentTurn ���)
			return true;
		}
		else	// �ش� ��ġ�� ���� �� �� ���� ���
		{
			return false;
		}
	} // handleInput()
	
	// ���� ����
	public void toggleTurn()	{ currentTurn = (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); }
	
	// status[row][col] �� ���� ����
	public void putDisc(int row, int col)
	{
		if (currentTurn == Status.WHITE)
		{
			status[row][col] = Status.WHITE;
			whiteCount++;
		}
		else
		{
			status[row][col] = Status.BLACK;
			blackCount++;		
		}
	} // putDisc()
	
	// ���� ������ ������ ���� ���� �� �ִ� ��ġ�� ������ Ȯ��
	public boolean isPass()
	{
		return ( (currentTurn==Status.WHITE ? whiteAvailableList.size() : blackAvailableList.size()) == 0 );
	}
	
	// ������ ����Ǿ����� Ȯ�� (�� �÷��̾� ��� �� �� �ִ� ��ġ�� ���� ���)
	public boolean isEnd()
	{
		return (whiteAvailableList.size()==0 && blackAvailableList.size()==0);
	}
	
	// �ش� (��, ��) �� ���� �� �� �ִ��� Ȯ��
	public boolean isPutAvailable(int row, int col)
	{
		ArrayList <Position> availableList = (currentTurn==Status.WHITE ? whiteAvailableList : blackAvailableList);
		int size = availableList.size();

		// availableList�� ��ġ�ϴ� (��, ��) �� ������ true
		for (int i=0; i<size; i++)
			if (availableList.get(i).getRow()==row && availableList.get(i).getCol()==col)
				return true;

		return false;
	} // isPutAvailable()

	// �ش� (��, ��) ���� 8�������� �̵��غ��� ����� ���� ������ �Լ�
	public void refreshStatus(int row, int col)
	{	
		TraversingPosition traversal = new TraversingPosition();
		TraversingPosition selectedPosition = new TraversingPosition();

		traversal.setPosition(row,col);	// 
		selectedPosition.setPosition(row,col);

		// �ش� (��, ��) ���� ����� ���� ���� ���� �̵��Ѵ�
		for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveUp());
		
		// ���� ���� ���� ������ ���� ���̶��, selectedPosition �� �ش� (��, ��) �κ��� traversal �� �����Դ� ��θ� ���� ����� ���� �ڽ��� ���� �����´�.
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			for (selectedPosition.moveUp(); !selectedPosition.equals(traversal); selectedPosition.moveUp())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				// ���� �������� ������ ����
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}
		// if ���� ������� �ʴ� ���� �̵� �� Status.EMPTY(�� ĭ) �̳� Status.BOUND(��輱) �� ����, �ش� ���⿡���� ���� ������ �� ���� ����̴�.
		
		/*
			�Ʒ� �ڵ���� ���⸸ �ٸ��� �ϴ� �۾��� ��� �����ϴ�.
		*/
		
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);

		for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightUp());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			for (selectedPosition.moveRightUp(); !selectedPosition.equals(traversal); selectedPosition.moveRightUp())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);

		for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRight());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			for (selectedPosition.moveRight(); !selectedPosition.equals(traversal); selectedPosition.moveRight())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);

		for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightDown());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			for (selectedPosition.moveRightDown(); !selectedPosition.equals(traversal); selectedPosition.moveRightDown())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);

		for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveDown());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			for (selectedPosition.moveDown(); !selectedPosition.equals(traversal); selectedPosition.moveDown())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);

		for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftDown());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			for (selectedPosition.moveLeftDown(); !selectedPosition.equals(traversal); selectedPosition.moveLeftDown())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);

		for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeft());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			for (selectedPosition.moveLeft(); !selectedPosition.equals(traversal); selectedPosition.moveLeft())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);

		for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftUp());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			for (selectedPosition.moveLeftUp(); !selectedPosition.equals(traversal); selectedPosition.moveLeftUp())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}
	} // refreshStatus()
	
	// ���� �ٵ���(status[][]) �� �������� whiteList �� blackList �� ���� ������ ��ġ�� ����
	public void refreshList()
	{
		whiteList.clear();
		blackList.clear();

		for (int row=1; row<BOARD_SIZE+1; row++)
			for (int col=1; col<BOARD_SIZE+1; col++)
				 if (status[row][col] == Status.WHITE)
					 whiteList.add(new Position(row,col));
				 else if (status[row][col] == Status.BLACK)
					 blackList.add(new Position(row,col));
	} // refreshList()
	
	// whiteList, blackList ������ ��� ��ġ�鿡�� 8�������� �̵��غ���
	// ���� ���� �� �ִ� ��ġ(whiteAvailableList, blackAvailableList) �� �����ϴ� �Լ�
	public void refreshAvailableList()
	{	
		int size;
		int currentRow;
		int currentCol;

		TraversingPosition traversal = new TraversingPosition();	// ���� �� �� �ִ� ��ġ���� Ȯ���ϱ� ���� 8�������� �̵����� ���� ���� ����
		TraversingPosition oneBlankGoPosition = new TraversingPosition();	// �ش� (��, ��) �� �ٷο� ��ġ���� Ȯ���ϱ� ���� ����

		// whiteAvailableList �� ���� //
		// blackAvailableList �� �����ϴ� ����� �����ϴ�.
		
		size = whiteList.size(); // ���� �ٵϵ��� ������ŭ ����
		whiteAvailableList.clear(); // ���� ������ ���� ����Ʈ �ʱ�ȭ
		for (int i=0; i<size; i++)
		{
			currentRow = whiteList.get(i).getRow();
			currentCol = whiteList.get(i).getCol();

			traversal.setPosition(currentRow,currentCol);
			
			// �Ʒ� if ������ ��ĭ�� ��츸 Ȯ���� ���, �ٷ� ���� ���� whiteAvailableList �� ���Ե� �� �ֱ� ������ Ȯ���ϱ� ����
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveUp();

			// whiteList ���� ������ (��, ��) ���� �浹�� ���� ���� �̵� 
			for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveUp());
			// ��ĭ�̶� ���߰� �װ��� �ش� (��, ��)�� �ٷ� ���� �ƴ� ��� ���� ���� �� �ִ� ��ġ
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);	// ���� ������ �����ϹǷ�, �ߺ��� ���̱� ���� ���� ��ġ�� �ִ��� Ȯ���� �߰�

			/*
				�Ʒ� �ڵ���� ���⸸ �ٸ��� �ϴ� �۾��� ��� �����ϴ�.
			*/
			
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRightUp();

			for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveRightUp());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRight();

			for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveRight());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRightDown();

			for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveRightDown());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveDown();

			for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveDown());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeftDown();

			for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveLeftDown());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeft();

			for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveLeft());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeftUp();

			for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveLeftUp());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);
		} // whiteAvailableList ���� �Ϸ�

		size = blackList.size();
		blackAvailableList.clear();
		for (int i=0; i<size; i++)
		{
			currentRow = blackList.get(i).getRow();
			currentCol = blackList.get(i).getCol();

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveUp();

			for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveUp());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRightUp();

			for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveRightUp());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRight();

			for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveRight());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRightDown();

			for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveRightDown());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveDown();

			for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveDown());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeftDown();

			for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveLeftDown());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeft();

			for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveLeft());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeftUp();

			for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveLeftUp());
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);
		} // blackAvailableList ���� �Ϸ�
	} // refreshStatus()
	
	// ��ǻ���� ������ ���� ������ ��ġ�� ������ ��ȯ�ϴ� �Լ�
	public Position positionGenerator()
	{
		Position computerPosition = null;
		
		switch (level)
		{
		case EASY :		computerPosition = positionGeneratorEasy();		break;
		case NORMAL :	computerPosition = positionGeneratorNormal();	break;
		case HARD :		computerPosition = positionGeneratorHard();		break;
		case EXPERT :	computerPosition = positionGeneratorExpert();	break;
		case MASTER :	computerPosition = positionGeneratorMaster();	break;
		}
		
		return computerPosition;
	} // positionGenerator()
	
	/*
		EASY �������� ��ǻ�Ͱ� ������ ��ġ�� ��ȯ
		������ ���� ���� ���� ���� ��ġ�� ��, �߽ɿ� ���� ����� ��ġ�� �����Ѵ�.
	
		������ ���� ���� ���� ���� ��ġ���� minPositionListGenerator() �� ��ȯ�� ����Ʈ�� ����ְ�,
		�߽ɿ� ���� ����� ��ġ�� �����ϱ� ���� minDistanceList �� minDistanceToBound() �� ����Ѵ�.
		�߽ɿ� ����� ������ �Ǻ��ϱ� ���� ��輱������ �ִܰŸ��� �̿��Ͽ���. (�ִܰŸ��� Ŭ���� �߽ɿ� ������)
	*/
	public Position positionGeneratorEasy()
	{
		int size;

		// ������ ���� ���� ���� ���� ��ġ���� ������ ����Ʈ
		ArrayList <Position> minPositionList = minPositionListGenerator();
		// minPositionList �� ��� ��ġ�鿡���� ��輱������ �ִܰŸ��� �����ϴ� ����Ʈ (�� ����Ʈ�� ũ��� ���� �ȴ�)
		ArrayList <Integer> minDistanceList = new ArrayList<Integer>();

		size = minPositionList.size();
		for (int i=0; i<size; i++)
			// ��輱������ �ִܰŸ��� ����Ͽ� ����Ʈ�� �߰�
			// 8���� ��ο����� �ִܰŸ��� ����ϱ� ������, 1 <= �ִܰŸ� <= 4 �� �ȴ�.
			minDistanceList.add(minDistanceToBound(minPositionList.get(i)));

		// ��輱������ �ִܰŸ� �� �ִ�(�߽ɿ� ���� ����� ���� �Ǻ��ϱ� ����) �� ����
		int maxDistance = Collections.max(minDistanceList);
		
		// ����������, ������ ���� ���� ���� ����, ��輱������ �ִܰŸ��� �ִ�(�߽ɿ� ���� �����)�� ��ġ���� �����ϴ� ����
		// �׷� ��ġ�� �������� �� �ֱ� ������ ����Ʈ�� ����
		ArrayList <Position> maxDistancePositionList = new ArrayList<Position>();

		size = minPositionList.size();	// minDistanceList.size() �� ����ص� �����ϴ�.
		// ��輱������ �ִܰŸ��� �ִ�(�߽ɿ� ���� ������)�̰�
		// ������ ���� ���� ���� ���� ��ġ��(minPositionList) �� maxDistancePositionList �� �߰��Ѵ�.
		for (int i=0; i<size; i++)
			if (maxDistance == minDistanceList.get(i))
				maxDistancePositionList.add(minPositionList.get(i));

		// ������ ���� ���� ���� ����, �߽ɿ� ���� ����� ��ġ ������ �� �ϳ��� �������� ��ȯ
		Position generatedPosition = maxDistancePositionList.get( (int)(Math.random() * maxDistancePositionList.size()) );

		return generatedPosition;
	} // positionGeneratorEasy()
	
	/*
		NORMAL �������� ��ǻ�Ͱ� ������ ��ġ�� ��ȯ
		
		��ǻ�Ͱ� �� �� �ִ� ��ġ�� �� �ϳ��� �������� ��ȯ(HARD)�ϰų�
		������ ���� ���� ���� ���� ��ġ�� �� �ϳ��� �������� ��ȯ (EASY �� �ٸ��� �߽ɿ� ����� ������ ������� �ʴ´�)
	*/	
	public Position positionGeneratorNormal()
	{
		if ((int)(Math.random()*2) == 0)
		{
			return positionGeneratorHard();
		}
		else
		{
			ArrayList <Position> minPositionList = minPositionListGenerator();	// ������ ���� ���� ���� �� �� �ִ� ��ġ���� ����Ʈ
			Position generatedPosition = minPositionList.get( (int)(Math.random() * minPositionList.size()) );	

			return generatedPosition;
		}
	} // positionGeneratorNormal()
	
	/*
		HARD �������� ��ǻ�Ͱ� ������ ��ġ�� ��ȯ
		��ǻ�Ͱ� �� �� �ִ� ��ġ�� �� �ϳ��� �������� ��ȯ
	*/	
	public Position positionGeneratorHard()
	{
		return (currentTurn==Status.WHITE ?
			whiteAvailableList.get( (int)(Math.random()*whiteAvailableList.size()) ) :
			blackAvailableList.get( (int)(Math.random()*blackAvailableList.size()) ));
	} // positionGeneratorHard()
	
	
	/*
		EXPERT �������� ��ǻ�Ͱ� ������ ��ġ�� ��ȯ
		
		��ǻ�Ͱ� �� �� �ִ� ��ġ�� �� �ϳ��� �������� ��ȯ(HARD)�ϰų�
		������ ���� ���� ���� ���� ��ġ�� �� �ϳ��� �������� ��ȯ (MASTER �� �ٸ��� ��輱�� ����� ������ ������� �ʴ´�)
	*/
	public Position positionGeneratorExpert()
	{
		if ((int)(Math.random()*2) == 0)
		{
			return positionGeneratorHard();
		}
		else
		{
			ArrayList <Position> maxPositionList = maxPositionListGenerator();	// ������ ���� ���� ���� �� �� �ִ� ��ġ���� ����Ʈ
			Position generatedPosition = maxPositionList.get( (int)(Math.random() * maxPositionList.size()) );

			return generatedPosition;
		}
	} // positionGeneratorExpert()

	/*
		MASTER �������� ��ǻ�Ͱ� ������ ��ġ�� ��ȯ
		������ ���� ���� ���� ���� ��ġ�� ��, ��輱�� ���� ����� ��ġ�� �����Ѵ�. (EASY �� ���ݴ��� �����ϸ� �ȴ�)
	
		������ ���� ���� ���� ���� ��ġ���� maxPositionListGenerator() �� ��ȯ�� ����Ʈ�� ����ְ�,
		��輱�� ���� ����� ��ġ�� �����ϱ� ���� minDistanceList �� minDistanceToBound() �� ����Ѵ�.
		��輱�� ����� ������ �Ǻ��ϱ� ���� ��輱������ �ִܰŸ��� �̿��Ͽ���. (�ִܰŸ��� �������� ��輱�� ������)
	*/
	public Position positionGeneratorMaster()
	{
		int size;

		// ������ ���� ���� ���� ���� ��ġ���� ������ ����Ʈ
		ArrayList <Position> maxPositionList = maxPositionListGenerator();
		// maxPositionList �� ��� ��ġ�鿡���� ��輱������ �ִܰŸ��� �����ϴ� ����Ʈ (�� ����Ʈ�� ũ��� ���� �ȴ�)
		ArrayList <Integer> minDistanceList = new ArrayList<Integer>();

		size = maxPositionList.size();
		for (int i=0; i<size; i++)
			// ��輱������ �ִܰŸ��� ����Ͽ� ����Ʈ�� �߰�
			// 8���� ��ο����� �ִܰŸ��� ����ϱ� ������, 1 <= �ִܰŸ� <= 4 �� �ȴ�.
			minDistanceList.add(minDistanceToBound(maxPositionList.get(i)));

		// ��輱������ �ִܰŸ� �� �ּڰ�(��輱�� ���� ����� ���� �Ǻ��ϱ� ����) �� ����
		int minDistance = Collections.min(minDistanceList);
		
		// ����������, ������ ���� ���� ���� ����, ��輱������ �ִܰŸ��� �ּ�(��輱�� ���� �����)�� ��ġ���� �����ϴ� ����
		// �׷� ��ġ�� �������� �� �ֱ� ������ ����Ʈ�� ����
		ArrayList <Position> minDistancePositionList = new ArrayList<Position>();

		size = maxPositionList.size();
		// ��輱������ �ִܰŸ��� �ּ�(��輱�� ���� ������)�̰�
		// ������ ���� ���� ���� ���� ��ġ��(maxPositionList) �� minDistancePositionList �� �߰��Ѵ�.
		for (int i=0; i<size; i++)
			if (minDistance == minDistanceList.get(i))
				minDistancePositionList.add(maxPositionList.get(i));

		// ������ ���� ���� ���� ����, ��輱�� ���� ����� ��ġ ������ �� �ϳ��� �������� ��ȯ
		Position generatedPosition = minDistancePositionList.get( (int)(Math.random() * minDistancePositionList.size()) );

		return generatedPosition;
	} // positionGeneratorMaster()
	
	// ������ ���� ���� ���� ���� ��ġ���� ������ ����Ʈ�� �����Ͽ� ��ȯ
	private ArrayList <Position> minPositionListGenerator()
	{
		int size;
		int currentRow;
		int currentCol;

		// ���� �� �� �ִ��� ���θ� �Ǻ��ϱ� ���� ���� ������ ���� ����
		TraversingPosition traversal = new TraversingPosition();
		// traversal �� �̿��� ���� �� �� �ִٰ� �Ǻ��� ���, �̵��ϸ� �� �� �ִ� ���� ������ ���� ���� ����
		TraversingPosition selectedPosition = new TraversingPosition();

		ArrayList <Position> availableList = (currentTurn==Status.WHITE ? whiteAvailableList : blackAvailableList);

		int point;	// �� ��ġ���� �� �� �ִ� ���� ������ �����ϱ� ���� �ӽ� ����
		// �� ��ġ(availableList)������ �� �� �ִ� ���� ������ �����ϴ� ����Ʈ
		// pointList �� availableList �� ũ��� ���� �ȴ�.
		ArrayList <Integer> pointList = new ArrayList<Integer>();
		
		size = availableList.size();
		for (int i=0; i<size; i++)
		{
			point = 0;
			currentRow = availableList.get(i).getRow();
			currentCol = availableList.get(i).getCol();

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			// traversal �� �ش� (��, ��) ���� ����� ���� ���� ���� �̵��Ѵ�. (��ǻ� ��ǻ�Ͱ� �����ϱ� ������ �浹�� ���� �̵�)
			for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveUp());
			
			// ���� ���� ���� ���� ���� ���̶��, for ���� selectedPosition �� �̿��� �ش� (��, ��) �� traversal ���̿� �ִ� ����� ���� ������ ��
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveUp(); !selectedPosition.equals(traversal); selectedPosition.moveUp())
					point++;	// �� �������� �� �� �ִ� ���� ������ ����
			
			/*
				�Ʒ� �ڵ���� ���⸸ �ٸ��� �ϴ� �۾��� �����ϴ�.
			*/

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRightUp(); !selectedPosition.equals(traversal); selectedPosition.moveRightUp())
					point++;

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRight());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRight(); !selectedPosition.equals(traversal); selectedPosition.moveRight())
					point++;
	
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRightDown(); !selectedPosition.equals(traversal); selectedPosition.moveRightDown())
					point++;

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveDown(); !selectedPosition.equals(traversal); selectedPosition.moveDown())
					point++;

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeftDown(); !selectedPosition.equals(traversal); selectedPosition.moveLeftDown())
					point++;

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeft());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeft(); !selectedPosition.equals(traversal); selectedPosition.moveLeft())
					point++;

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeftUp(); !selectedPosition.equals(traversal); selectedPosition.moveLeftUp())
					point++;

			// �ش� (��, ��) ���� ����� ���� �� �� �ִ� ������ ����Ʈ�� �߰�
			pointList.add(point);
		}

		// ����� ���� ���� ���� ���� ������ ����
		int min = Collections.min(pointList);

		// ����� ���� ���� ���� ���� ��ġ���� �����ϱ� ���� ����Ʈ
		ArrayList <Position> minPositionList = new ArrayList<Position>();

		size = availableList.size();
		for (int i=0; i<size; i++)
			if (pointList.get(i) == min)	// �ش� ��ġ���� ����� ���� ���� ���� ���� ���
				minPositionList.add(availableList.get(i));	// minPositionList �� ��ġ �߰�

		return minPositionList;	// ����� ���� ���� ���� ���� ��ġ���� ��ȯ
	} // minPositionListGenerator()

	// ������ ���� ���� ���� ���� ��ġ���� ������ ����Ʈ�� �����Ͽ� ��ȯ
	private ArrayList <Position> maxPositionListGenerator()
	{
		int size;
		int currentRow;
		int currentCol;

		// ���� �� �� �ִ��� ���θ� �Ǻ��ϱ� ���� ���� ������ ���� ����
		TraversingPosition traversal = new TraversingPosition();
		// traversal �� �̿��� ���� �� �� �ִٰ� �Ǻ��� ���, �̵��ϸ� �� �� �ִ� ���� ������ ���� ���� ����
		TraversingPosition selectedPosition = new TraversingPosition();

		ArrayList <Position> availableList = (currentTurn==Status.WHITE ? whiteAvailableList : blackAvailableList);

		int point;	// �� ��ġ���� �� �� �ִ� ���� ������ �����ϱ� ���� �ӽ� ����
		// �� ��ġ(availableList)������ �� �� �ִ� ���� ������ �����ϴ� ����Ʈ
		// pointList �� availableList �� ũ��� ���� �ȴ�.
		ArrayList <Integer> pointList = new ArrayList<Integer>();

		size = availableList.size();
		for (int i=0; i<size; i++)
		{
			point = 0;
			currentRow = availableList.get(i).getRow();
			currentCol = availableList.get(i).getCol();

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			// traversal �� �ش� (��, ��) ���� ����� ���� ���� ���� �̵��Ѵ�. (��ǻ� ��ǻ�Ͱ� �����ϱ� ������ �浹�� ���� �̵�)
			for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveUp());
			
			// ���� ���� ���� ���� ���� ���̶��, for ���� selectedPosition �� �̿��� �ش� (��, ��) �� traversal ���̿� �ִ� ����� ���� ������ ��
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveUp(); !selectedPosition.equals(traversal); selectedPosition.moveUp())
					point++;	// �� �������� �� �� �ִ� ���� ������ ����

			/*
				�Ʒ� �ڵ���� ���⸸ �ٸ��� �ϴ� �۾��� �����ϴ�.
			*/
			
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRightUp(); !selectedPosition.equals(traversal); selectedPosition.moveRightUp())
					point++;

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRight());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRight(); !selectedPosition.equals(traversal); selectedPosition.moveRight())
					point++;

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRightDown(); !selectedPosition.equals(traversal); selectedPosition.moveRightDown())
					point++;

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveDown(); !selectedPosition.equals(traversal); selectedPosition.moveDown())
					point++;

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeftDown(); !selectedPosition.equals(traversal); selectedPosition.moveLeftDown())
					point++;

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeft());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeft(); !selectedPosition.equals(traversal); selectedPosition.moveLeft())
					point++;

			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			
			for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeftUp(); !selectedPosition.equals(traversal); selectedPosition.moveLeftUp())
					point++;

			// �ش� (��, ��) ���� ����� ���� �� �� �ִ� ������ ����Ʈ�� �߰�
			pointList.add(point);
		}

		// ����� ���� ���� ���� ���� ������ ����
		int max = Collections.max(pointList);

		// ����� ���� ���� ���� ���� ��ġ���� �����ϱ� ���� ����Ʈ
		ArrayList <Position> maxPositionList = new ArrayList<Position>();

		size = availableList.size();
		for (int i=0; i<size; i++)
			if (pointList.get(i) == max)	// �ش� ��ġ���� ����� ���� ���� ���� ���� ���
				maxPositionList.add(availableList.get(i));	// maxPositionList �� ��ġ �߰�

		return maxPositionList;		// ����� ���� ���� ���� ���� ��ġ���� ��ȯ
	} // maxPositionListGenerator()
	
	// �ش� (��, ��) ���� 8���������� ��輱������ �ִܰŸ��� ��ȯ
	private int minDistanceToBound(Position rPosition)
	{
		int currentRow = rPosition.getRow();
		int currentCol = rPosition.getCol();

		// 8�������� �̵����� ���� ���� ����
		TraversingPosition traversal = new TraversingPosition();

		int distance;	// �� �������� �̵��������� ��輱������ �Ÿ��� �����ϴ� ����
		int minDistance;	// ���� (��, ��) ���� ��輱������ �ִܰŸ��� ����

		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		// ��輱�� ���������� distance �� ������Ű�� ���� �̵�
		for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveUp(), distance++);
		minDistance = distance;	// minDistance �ʱ�ȭ

		/*
			�Ʒ� �ڵ���� ���⸸ �ٸ��� �ϴ� �۾��� �����ϴ�.
		*/
		
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveRightUp(), distance++);
		if (distance < minDistance) { minDistance = distance; }	// minDistance ����

		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveRight(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveRightDown(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveDown(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveLeftDown(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveLeft(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveLeftUp(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		return minDistance;	// 8���⿡���� ��輱������ �ִܰŸ� ��ȯ
	} // minDistanceToBound()
	
	// ����Ʈ�� �����Ҷ�, �ߺ��� ���̱� ���� ������ ��ġ�� �ִ��� Ȯ���� �߰�
	private void pushElement(ArrayList <Position> list, Position element)
	{
		int size = list.size();
		for (int i=0; i<size; i++)
			if (list.get(i).equals(element))
				return;

		list.add(new Position(element.getRow(),element.getCol()));
	} // pushElement()
} // Reversi Class