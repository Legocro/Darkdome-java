package world.of.chaos.npcs;

import world.of.chaos.Player.Player;
import world.of.chaos.Player.Client;
/**
 *
 * @author Loki
 */
public class Npc {

	public int npcId;
	public int npcType;
	public int absX, absY;
	public int heightLevel;
	public static int lastX, lastY;
	public int makeX, makeY, maxHit, defence, attack, moveX, moveY, direction, walkingType, hitsToHeal;
	public int spawnX, spawnY;
	public int viewX, viewY;
	public boolean summoner;
	public int summonedBy, size;
	public int focusPointX, focusPointY, masterId;
	public boolean turnUpdateRequired;

	/**
	 * attackType: 0 = melee, 1 = range, 2 = mage
	 */
	public int attackType, projectileId, endGfx, spawnedBy, hitDelayTimer, HP,
			MaxHP, hitDiff, animNumber, actionTimer, enemyX, enemyY,
			combatLevel;
	public boolean applyDead, isDead, needRespawn, respawns, aggressive;
	public boolean walkingHome, underAttack;
	public int freezeTimer, attackTimer, killerId, killedBy, oldIndex,
			underAttackBy;
	public long lastDamageTaken;
	public boolean randomWalk;
	public boolean dirUpdateRequired;
	public boolean animUpdateRequired;
	public boolean hitUpdateRequired;
	public boolean updateRequired;
	public boolean forcedChatRequired;
	public boolean faceToUpdateRequired;
	public int firstAttacker;
	public String forcedText;
	public boolean transformUpdateRequired = false, isTransformed = false;
	public int transformId;
	
	public Npc(int _npcId, int _npcType) {
		npcId = _npcId;
		npcType = _npcType;
		direction = -1;
		isDead = false;
		applyDead = false;
		actionTimer = 0;
		randomWalk = true;
	}
	
	public void requestTransform(int id) {
    	transformId = id;
    	transformUpdateRequired = true;
    	updateRequired = true;
	}
	
	public void requestTransformTime(Client player, int itemId, int animation, final int currentId, final int newId, int transformTime) {
		if (!player.getItemAssistant().playerHasItem(itemId)) {
			player.getActionSender().sendMessage("You need " + ItemAssistant.getItemName(itemId).toLowerCase() + " to do that.");
			return;
		}
		if (NpcHandler.npcs[currentId].isTransformed == true) {
			player.getActionSender().sendMessage("This npc is already transformed.");
			return;
		}
		if (animation > 0) {
			player.startAnimation(animation);
		}
		NpcHandler.npcs[currentId].isTransformed = true;
		requestTransform(newId);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				requestTransform(currentId);
				container.stop();
			}

			@Override
			public void stop() {
				NpcHandler.npcs[currentId].isTransformed = false;
			}
		}, transformTime);
	}
	
	public void appendTransformUpdate(Stream str) {
    	str.writeWordBigEndianA(transformId);
	}
	

	public void updateNPCMovement(Stream str) {
		if (direction == -1) {

			if (updateRequired) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[direction]);
			if (updateRequired) {
				str.writeBits(1, 1);
			} else {
				str.writeBits(1, 0);
			}
		}
	}

	/**
	 * Text update
	 **/

	public void forceChat(String text) {
		forcedText = text;
		forcedChatRequired = true;
		updateRequired = true;
	}
        
        public void turnNpc(int i, int j) {
		FocusPointX = 2 * i + 1;
		FocusPointY = 2 * j + 1;
		updateRequired = true;
		turnUpdateRequired = true;
	}

	public int getNextWalkingDirection2() {
		int dir;
		dir = Misc.direction(absX, absY, absX + moveX, absY + moveY);
		dir >>= 1;
		absX += moveX;
		absY += moveY;
		return dir;
	}

	public void getRandomAndHomeNPCWalking(int i) {
		direction = -1;
		if (NpcHandler.npcs[i].freezeTimer == 0) {
			direction = getNextWalkingDirection2();
		}
	}

	public void appendFaceEntity(Stream str) {
		str.writeWord(face);
	}

	public void facePlayer(int player) {
		face = player + 32768;
		dirUpdateRequired = true;
		updateRequired = true;
	}

	public void appendFaceToUpdate(Stream str) {
		str.writeWordBigEndian(viewX);
		str.writeWordBigEndian(viewY);
	}
        
        public void clearUpdateFlags() {
		updateRequired = false;
		forcedChatRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		animUpdateRequired = false;
		dirUpdateRequired = false;
		transformUpdateRequired = false;
		forcedText = null;
		moveX = 0;
		moveY = 0;
		direction = -1;
		focusPointX = -1;
		focusPointY = -1;
		turnUpdateRequired = false;
	}
        
        public int getNextWalkingDirection() {
		int nextX = absX + moveX;
		int nextY = absY + moveY;
		int dir;
		dir = Misc.direction(absX, absY, absX + moveX, absY + moveY);
		for (Npc npc : NpcHandler.npcs) {
			if (npc == null) {
				continue;
			}
			if (npc.absX == nextX && npc.absY == nextY
					&& npc.heightLevel == heightLevel) {
				return -1;
			}
		}
		for (Player p : PlayerHandler.players) {
			if (p == null) {
				continue;
			}
			if (p.absX == nextX && p.absY == nextY
					&& p.heightLevel == heightLevel) {
				return -1;
			}
		}
		if (dir == -1) {
			return -1;
		}
		dir >>= 1;
		absX += moveX;
		absY += moveY;
		return dir;
	}

	public void getNextNPCMovement(int i) {
		direction = -1;
		if (NpcHandler.npcs[i].freezeTimer == 0) {
			direction = getNextWalkingDirection();
		}
	}

	public void appendHitUpdate(Stream str) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeByteC(hitDiff);
		if (hitDiff > 0) {
			str.writeByteS(1);
		} else {
			str.writeByteS(0);
		}
		str.writeByteS(HP);
		str.writeByteC(MaxHP);
	}
        
        public int hitDiff2 = 0;
	public boolean hitUpdateRequired2 = false;

	public void appendHitUpdate2(Stream str) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeByteA(hitDiff2);
		if (hitDiff2 > 0) {
			str.writeByteC(1);
		} else {
			str.writeByteC(0);
		}
		str.writeByteA(HP);
		str.writeByte(MaxHP);
	}

	public void handleHitMask(int damage) {
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;
		}
		updateRequired = true;
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public int getLastX() {
		return lastX;
	}

	public int getLastY() {
		return lastY;
	}

	public void setAbsX(int absX) {
		Npc.lastX = this.absX;
		this.absX = absX;
	}

	public void setAbsY(int absY) {
		Npc.lastY = this.absY;
		this.absY = absY;
	}

	public void deleteNPC(Npc npc) {
		setAbsX(0);
		setAbsY(0);
		npc = null;
	}
    
}
