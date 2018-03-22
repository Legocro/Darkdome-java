package world.of.chaos.Player;

/**
 *
 * @author Loki
 */
import java.util.ArrayList;
public abstract class Player {
    public double weight = 0.0;

    public int getLocalX() {
		return getX() - 8 * getMapRegionX();
    }

    public int getLocalY() {
		return getY() - 8 * getMapRegionY();
    }  
    public Client asClient() {
       return (Client) this;
    }
   
    private Player player;
    public Player asPlayer() {
       return (Player) player;
       
    public int playerId = -1;
	public String playerName = null;
    
    public String playerPass = null;
    public int playerRights;
    public PlayerHandler handler = null;
    public int playerItems[] = new int[35];
    public int playerItemsN[] = new int[35];
    public int bankItems[] = new int[Constants.BANK_SIZE];
    public int bankItemsN[] = new int[Constants.BANK_SIZE];
    
    public int clickNpcType, clickObjectType, objectId, objectX,
	       objectY, objectXOffset, objectYOffset, objectDistance;
   
    public int playerHead = 0;
    public int playerBack = 1;
    public int playerNeck = 2;
    public int playerRightHand = 3;
    public int playerChest = 4;
    public int playerLeftHand = 5;
    public int playerLegs = 6;
    public int playerHands = 7;
    public int playerFeet = 8;
    public int playerRightFinger = 9;
    public int playerLeftFinger = 10;
    public int playerRightBracelet = 11;
    public int playerLeftBracelet = 12;
    public int playerArrows = 13;
        
    public int[] playerEquipment = new int[13];
        
    public Player(int _playerId) {
		playerId = _playerId;
		playerRights = 0;

		for (int i = 0; i < playerItems.length; i++) {
			playerItems[i] = 0;
                
                playerEquipment[playerHead] = -1;
		playerEquipment[playerBack] = -1;
		playerEquipment[playerNeck] = -1;
		playerEquipment[playerRightHand] = -1;
		playerEquipment[playerChest] = -1;
		playerEquipment[playerLeftHand] = -1;
                playerEquipment[playerLegs] = -1;
		playerEquipment[playerHands] = -1;
		playerEquipment[playerFeet] = -1;
		playerEquipment[playerRightFinger] = -1;
		playerEquipment[playerLeftFinger] = -1;
                playerEquipment[playerRightBracelet] = -1;
                playerEquipment[playerLeftBracelet] = -1;
		playerEquipment[playerArrows] = -1;
            
            heightLevel = 0;
            
            absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
                
        public boolean withinDistance(Player otherPlr) {
		if (heightLevel != otherPlr.heightLevel) {
			return false;
		}
		int deltaX = otherPlr.absX - absX, deltaY = otherPlr.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(Npc npc) {
		if (heightLevel != npc.heightLevel) {
			return false;
		}
		if (npc.needRespawn == true) {
			return false;
		}
		int deltaX = npc.absX - absX, deltaY = npc.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(int absX, int getY, int getHeightLevel) {
		if (heightLevel != getHeightLevel) {
			return false;
		}
		if (objectId == 2242) {
			System.out.println("not within distance");
			return false;
		}
		int deltaX = getX() - absX, deltaY = getY() - getY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public int distanceToPoint(int pointX, int pointY) {
		return (int) Math.sqrt(Math.pow(absX - pointX, 2)
				+ Math.pow(absY - pointY, 2));
	}

	public int mapRegionX, mapRegionY;
	public int absX;

	public int absY;
	public int currentX, currentY;

	public int heightLevel;
	public int playerSE = 0x328;
	public int playerSEW = 0x333;
	public int playerSER = 0x334;

	public boolean updateRequired = true;

	public final int walkingQueueSize = 50;
	public int walkingQueueX[] = new int[walkingQueueSize],
			walkingQueueY[] = new int[walkingQueueSize];
	public int wQueueReadPtr = 0;
	public int wQueueWritePtr = 0;
	public boolean isRunning = true;
	public int teleportToX = -1, teleportToY = -1;

	public void resetWalkingQueue() {
		wQueueReadPtr = wQueueWritePtr = 0;
		for (int i = 0; i < walkingQueueSize; i++) {
			walkingQueueX[i] = currentX;
			walkingQueueY[i] = currentY;
		}
	}

	public void addToWalkingQueue(int x, int y) {
		// if (VirtualWorld.I(heightLevel, absX, absY, x, y, 0)) {
		int next = (wQueueWritePtr + 1) % walkingQueueSize;
		if (next == wQueueWritePtr) {
			return;
		}
		walkingQueueX[wQueueWritePtr] = x;
		walkingQueueY[wQueueWritePtr] = y;
		wQueueWritePtr = next;
        }
        
        public boolean goodDistance(int objectX, int objectY, int playerX,
			int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if (objectId == 2282 || objectId == 10883 || objectId == 2322
						|| objectId == 4493 || objectId == 12164
						|| objectId == 1721 || objectId == 1722
						|| objectId == 4304 && playerX == 2619
						&& playerY == 3667) {
					return true;
				}
				if (objectX + i == playerX
						&& (objectY + j == playerY || objectY - j == playerY || objectY == playerY)) {
					return true;
				} else if (objectX - i == playerX
						&& (objectY + j == playerY || objectY - j == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& (objectY + j == playerY || objectY - j == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}
       
}
