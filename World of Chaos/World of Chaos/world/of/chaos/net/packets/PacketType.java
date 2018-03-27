package world.of.chaos.net.packets;

/**
 *
 * @author Loki
 */

import world.of.chaos.player.Client;

public interface PacketType {

	public void processPacket(Client c, int packetType, int packetSize);
}
