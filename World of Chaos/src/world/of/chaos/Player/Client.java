package world.of.chaos.Player;

/**
 *
 * @author Loki
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Future;

import world.of.chaos.Stream;
import world.of.chaos.items.ItemAssistant;

public class Client extends Player {
    
    public byte buffer[] = null;
    public Stream inStream = null, outStream = null;
    private IoSession session;
    private final ItemAssistant itemAssistant = new ItemAssistant(this);
    private final ShopAssistant shopAssistant = new ShopAssistant(this);
    private final Trading trading = new Trading(this);
    private final Dueling duel = new Dueling(this);
    private final PlayerAssistant playerAssistant = new PlayerAssistant(this);
    private final CombatAssistant combatAssistant = new CombatAssistant(this);
    private final ObjectsActions actionHandler = new ObjectsActions(this);
    private final NpcActions npcs = new NpcActions(this);
    private final Queue<Packet> queuedPackets = new LinkedList<Packet>();
    private final EmoteHandler emoteHandler = new EmoteHandler(this);
    private final SkillInterfaces skillInterfaces = new SkillInterfaces(this);
    private final PlayerAction playeraction = new PlayerAction(this);
    public String creationAddress = "";
    private final HashMap<String, Object> temporary = new HashMap<String, Object>();
    private final BankPin bankPin = new BankPin(this);
    private final ActionSender actionSender = new ActionSender(this);
    private final DialogueHandler dialogues = new DialogueHandler(this);
    private final ObjectManager objectManager = new ObjectManager();
    private ChallengePlayer challengePlayer = new ChallengePlayer();
}
