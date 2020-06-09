package it.polimi.ingsw.PSP14.server.model.gods;

import it.polimi.ingsw.PSP14.server.controller.ClientConnection;
import it.polimi.ingsw.PSP14.server.controller.MatchController;
import it.polimi.ingsw.PSP14.server.model.Match;

import java.io.IOException;

public class Poseidon extends God {
    public Poseidon(String owner) {
        super(owner);
    }

    @Override
    public void afterTurn(String player, int workerIndex, MatchController controller, Match model) throws IOException {
        if(!player.equals(getOwner())) return;

        int otherWorker = workerIndex == 1 ? 0 : 1;

        if(model.getBoard().getTowerSize(model.getPlayerByUsername(player).getWorkerPos(otherWorker)) == 0) {
            for(int i = 0; i < 3; ++i) {
                if(model.getBuildable(player, otherWorker).size() > 0) {
                    boolean choice = controller.askQuestion(player, "POSEIDON: Why don't you let the other guy build as well?");

                    if (!choice) break;

                    model.build(player, otherWorker);
                } else break;
            }
        }
    }
}
