/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package world.of.chaos.npcs;

/**
 *
 * @author Loki
 */
public class NpcSize {

	/**
	 * Gets the size of the specified NPC.
	 * 
	 * @param npcType
	 *            The type of the NPC.
	 * @return The NPC size.
	 */
	public static int getNPCSize(int npcType) {
		int NPC_TYPE = 0;
		int NPC_SIZE = 1;
		for (int[] element : NPC_SIZES) {
			if (npcType == element[NPC_TYPE]) {
				return element[NPC_SIZE];
			}
		}
		return 1;
	}
        
        /**
	 * NPC Sizes. {NPC TYPE, SIZE}.
	 */
	private static final int[][] NPC_SIZES = { { 0, 0 }, { 0, 0 }, { 0, 0 },
			{ 0, 0 }, { 0, 0 }, { 0, 0}, { 0, 0}, { 0, 0 }, { 0, 0 },
			{ 0, 0}, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 },
			{ 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }};

}