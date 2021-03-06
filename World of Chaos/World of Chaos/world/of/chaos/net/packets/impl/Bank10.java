/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package world.of.chaos.net.packets.impl;

/**
 *
 * @author Loki
 */

import redone.game.content.random.PartyRoom;
import redone.game.content.skills.crafting.JewelryMaking;
import world.of.chaos.player.Client;
import world.of.chaos.net.packets.PacketType;

public class Bank10 implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		int interfaceId = player.getInStream().readUnsignedWordBigEndian();
		int removeId = player.getInStream().readUnsignedWordA();
		int removeSlot = player.getInStream().readUnsignedWordA();

		switch (interfaceId) {

		case 4233:
		case 4239:
		case 4245:
			JewelryMaking.mouldItem(player, removeId, 10);
			break;

		case 1688:
			player.getPlayerAssistant().useOperate(removeId);
			break;
		case 3900:
			player.getShopAssistant().buyItem(removeId, removeSlot, 5);
			break;

		case 3823:
			if(!player.getItemAssistant().playerHasItem(removeId)) {
				return;
			}
			player.getShopAssistant().sellItem(removeId, removeSlot, 5);
			break;

		case 7423:
			if (player.storing) {
				return;
			}
			player.getItemAssistant().bankItem(removeId, removeSlot, 10);
			player.getItemAssistant().resetItems(7423);
			break;

		case 5064:
			if(!player.getItemAssistant().playerHasItem(removeId)) {
				return;
			}
			if (player.inPartyRoom) {
				PartyRoom.depositItem(player, removeId, 10);
				break;
			}
			if (player.inTrade) {
				player.getActionSender().sendMessage("You can't store items while trading!");
				return;
			}
			player.getItemAssistant().bankItem(removeId, removeSlot, 10);
			break;

		case 5382:
			player.getItemAssistant().fromBank(removeId, removeSlot, 10);
			break;

		case 3322:
			if (player.duelStatus <= 0) {
				player.getTrading().tradeItem(removeId, removeSlot, 10);
			} else {
				player.getDueling().stakeItem(removeId, removeSlot, 10);
			}
			break;

		case 3415:
			if (player.duelStatus <= 0) {
				player.getTrading().fromTrade(removeId, removeSlot, 10);
			}
			break;

		case 6669:
			player.getDueling().fromDuel(removeId, removeSlot, 10);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			player.getSmithing().readInput(player.playerLevel[player.playerSmithing],
					Integer.toString(removeId), player, 10);
			break;

		}
	}

}

