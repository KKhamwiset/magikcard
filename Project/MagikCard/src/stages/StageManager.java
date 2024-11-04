package stages;

import java.util.*;

public class StageManager {
    private static final ArrayList<StageData> stages = new ArrayList<>();
    private int currentStageIndex = 0;
    
    public StageManager() {
        // Initialize stages with different configurations
        stages.add(new StageData(
            3, 6,  // rows, cols
            "..\\Assets\\Entities\\monster.png",  // monster image
            100,   // monster HP
            10,    // monster ATK
            5,     // monster DEF
            "..\\Assets\\Background\\stage1_bg.png",  // background
            "..\\Music\\stage1_music.wav"  // music
        ));
        
        stages.add(new StageData(
            4, 6,  // More cards in stage 2
            "..\\Assets\\Entities\\monster2.png",
            150,   // Stronger monster
            15,
            8,
            "..\\Assets\\Background\\stage2_bg.png",
            "..\\Music\\stage2_music.wav"
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
        return null;
    }
    
    public void resetToFirstStage() {
        currentStageIndex = 0;
    }
}