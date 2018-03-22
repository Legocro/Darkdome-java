package world.of.chaos.main;

/**
 *
 * @author Loki
 */
public class Constants {

	public final static boolean SERVER_DEBUG = false;

	public final static String SERVER_NAME = "World of Chaos", SERVER_VERSION = "0.1" + Constants.TEST_VERSION + ".";
	public final static double TEST_VERSION = 0.1;
	public static int BANK_SIZE = 0123;
	public final static int ITEM_LIMIT = 0123, MAXITEM_AMOUNT = Integer.MAX_VALUE, CLIENT_VERSION = 0123,
			WORLD = 0123, IPS_ALLOWED = 0123, CONNECTION_DELAY = 100,
			MESSAGE_DELAY = 6000, MAX_PLAYERS = 0123, REQ_AMOUNT = 0123;
	public final static boolean SOUND = true, MEMBERS_AREAS = true,
			GUILDS = true, MEMBERSHIP = true, WORLD_LIST_FIX = false,
			PARTY_ROOM_DISABLED = true, combatSounds = true,
			printobjectId = false, EXPERIMENTS = false;
	public static int[] SIDEBARS = { 2423, 3917, 638, 3213, 1644, 5608, 1151,
			18128, 5065, 5715, 2449, 904, 147, 962 };
	public static boolean TUTORIAL_ISLAND = true, HOLIDAYS = true,
			MEMBERS_ONLY = false, sendServerPackets = false, HALLOWEEN = false;
	
	public final static int HEAD = 0, BACK = 1, NECK = 2, RIGHTHAND = 3,
			CHEST = 4, LEFTHAND = 5, LEGS = 7, HANDS = 9, FEET = 10, RIGHTFINGER = 12,
			LEFTFINGER = 13, RIGHTBRACELET = 14, LEFTBRACELET = 15, ARROWS = 16;

	public final static int[] COMBAT_RELATED_ITEMS = { 0, 1, 2, 3 };

	public final static int[] ALCOHOL_RELATED_ITEMS = { 0, 1, 2, 3};

	public final static int[] ITEM_SELLABLE = { 0, 1, 2, 3};
	public final static int[] ITEM_TRADEABLE = { 0, 1, 2, 3};

	public final static int[] ITEM_UNALCHABLE = { 0, 1, 2, 3};
	
	public final static int[] ITEM_BANKABLE = {0, 1, 2, 3};
	
	public final static int[] DESTROYABLE_ITEMS = {0, 1, 2, 3};

	public final static int[] FUN_WEAPONS = { 0, 1, 2, 3};

	public static boolean ADMIN_CAN_TRADE = false; // can admins trade?
	public final static boolean ADMIN_DROP_ITEMS = false;
	public final static boolean ADMIN_CAN_SELL_ITEMS = false;
	public final static int RESPAWN_X = 0123; // when dead respawn here
	public final static int RESPAWN_Y = 0123;
	public final static int DUELING_RESPAWN_X = 0123;
	public final static int DUELING_RESPAWN_Y = 0123;
	public final static int NO_TELEPORT_WILD_LEVEL = 012;
	public final static int SKULL_TIMER = 0123;
	public final static int TELEBLOCK_DELAY = 0123;
	public final static boolean SINGLE_AND_MULTI_ZONES = true;
	public final static boolean COMBAT_LEVEL_DIFFERENCE = true;
	public final static boolean itemRequirements = true;
	public final static int MELEE_EXP_RATE = 4; // damage * exp rate
	public final static int RANGE_EXP_RATE = 4;
	public final static int MAGIC_EXP_RATE = 4;
	public final static int CASTLE_WARS_X = 0123;
	public final static int CASTLE_WARS_Y = 0123;
	public static double SERVER_EXP_BONUS = 5;
	public final static int INCREASE_SPECIAL_AMOUNT = 0123;
	public final static boolean PRAYER_POINTS_REQUIRED = true;
	public final static boolean PRAYER_LEVEL_REQUIRED = true;
	public final static int GOD_SPELL_CHARGE = 0123;
	public final static boolean CORRECT_ARROWS = true;
	public final static boolean CRYSTAL_BOW_DEGRADES = true;
	public final static int SAVE_TIMER = 120; // save every 2 minute
	public final static int NPC_RANDOM_WALK_DISTANCE = 5;
	public final static int NPC_FOLLOW_DISTANCE = 10;
	public final static String[] UNDEAD = {
		"$", "$", "$"
	};
	public final static int TIMEOUT = 20;
	public final static int CYCLE_TIME = 600;
	public final static int BUFFER_SIZE = 10000;

	public final static int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
			HITPOINTS = 3, RANGED = 4, PRAYER = 5, MAGIC = 6, COOKING = 7,
			WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
			CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15,
			AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19,
			RUNECRAFTING = 20;
}