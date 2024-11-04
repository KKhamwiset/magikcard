package stages;

import java.util.*;

public class StageManager {
    private ArrayList<StageData> stages = new ArrayList<>();
    private int currentStageIndex = 0;
    
    public StageManager() {
        stages.add(new StageData(
            3, 6, 
            "..\\Assets\\Entities\\monster.png",  
            100,
            10, 
            5,
            1,
            "..\\Assets\\Background\\2306.w063.n005.146B.p1.146.jpg", 
            "..\\Music\\Revived Witch OST  - 16 -  Seth Tsui - Frog Chevalier.wav"
        ));
        stages.add(new StageData(
            3, 6, 
            "..\\Assets\\Entities\\player.png",  
            100,
            10, 
            5,
            1,
            "..\\Assets\\Background\\2306.w063.n005.146B.p1.146.jpg", 
            "..\\Music\\Revived Witch OST  - 16 -  Seth Tsui - Frog Chevalier.wav"
        ));
    }
    
    public StageData getCurrentStage() {
        return stages.get(currentStageIndex);
    }
    
    public boolean hasNextStage() {
        return currentStageIndex < stages.size() - 1;
    }
    
    public StageData moveToNextStage() {
        if (hasNextStage()) {
            currentStageIndex++;
            return getCurrentStage();
        }
        System.out.println("No more stages available!");
        return null;
    }
    
    public void resetToFirstStage() {
        currentStageIndex = 0;
    }
}