package stages;

import java.util.*;

public class StageManager {

    private ArrayList<StageData> stages = new ArrayList<>();
    private int currentStageIndex = 0;

    public StageManager() {
        System.out.println("=== Initializing Stage Manager ===");

        stages.add(new StageData(
                3, 2,
                "..\\Assets\\Entities\\monster.png",
                500,
                100,
                10,
                0,
                "..\\Assets\\Background\\stage1.png",
                "..\\Music\\Revived Witch OST  - 16 -  Seth Tsui - Frog Chevalier.wav",
                false
        ));
        stages.add(new StageData(
                3, 4,
                "..\\Assets\\Entities\\monster2.png",
                1000,
                150,
                100,
                25,
                "..\\Assets\\Background\\stage2.png",
                "..\\Music\\Revived Witch OST  - 16 -  Seth Tsui - Frog Chevalier.wav",
                false
        ));

        stages.add(new StageData(
                3, 6,
                "..\\Assets\\Entities\\monster3.png",
                2000,
                200,
                200,
                75,
                "..\\Assets\\Background\\2306.w063.n005.146B.p1.146.jpg",
                "..\\Music\\Revived Witch OST  - 16 -  Seth Tsui - Frog Chevalier.wav",
                true
        ));
    }

    public StageData getCurrentStage() {
        StageData current = stages.get(currentStageIndex);
        System.out.println("Getting stage " + currentStageIndex
                + " - Monster HP: " + current.getMonsterHP());
        return current;
    }

    public boolean hasNextStage() {
        boolean hasNext = currentStageIndex < stages.size() - 1;
        System.out.println("Checking next stage - Current index: " + currentStageIndex
                + ", Has next: " + hasNext);
        return hasNext;
    }

    public StageData moveToNextStage() {
        if (hasNextStage()) {
            currentStageIndex++;
            StageData next = getCurrentStage();
            System.out.println("Moving to stage " + currentStageIndex
                    + " - Monster HP: " + next.getMonsterHP());
            return next;
        }
        System.out.println("No more stages available! Current index: " + currentStageIndex);
        return null;
    }

    public void resetToFirstStage() {
        System.out.println("Resetting to first stage from index " + currentStageIndex);
        currentStageIndex = 0;
    }
}
