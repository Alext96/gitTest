package wildernesslooter;

import java.awt.*;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Entity;
import static org.osbot.rs07.script.MethodProvider.random;
import static org.osbot.rs07.script.MethodProvider.sleep;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;
 //yolo2
@ScriptManifest(author = "Alex", info = "Loots wilderness for items", name = "WildyLooter", version = 0, logo = "")

public class WildernessLooter extends Script {
 
    final String Bone_Name = "Bone";
    final Area BANK_AREA = new Area(3092,3489,3097,3498);
    final Area LOOT_AREA = new Area(3079,3523,3105,3556);
    
    private final Position[] path = {
      new Position(3103, 3508, 0),  
      new Position(3114, 3520, 0),
      new Position(3114, 3521, 0),
      new Position(3102, 3533, 0),
    };
    
    @Override
    public void onStart() {
        log("Wilderness looter");
        log("startar...");
    }

    private boolean walkTile(Position path)
    {
        getWalking().walkPath((java.util.List<Position>) path);
        
        return true;
}
    
    
    private enum State {
        LOOT, WALK_TO_BANK, WALK_TO_WILDERNESS, BANK
    };
    
    private State getState() 
    {
        if(inventory.isFull() && LOOT_AREA.contains(myPlayer()))
        {
            return State.WALK_TO_BANK;
        }
        {
            if(!inventory.isFull() && BANK_AREA.contains(myPlayer()))
            {
                return State.WALK_TO_WILDERNESS;
            }
            {
                if(inventory.isFull() && BANK_AREA.contains(myPlayer()))
                {
                    return State.BANK;
                    
                }
                    }
        }
        return State.LOOT;
    }
    
    private void traversePath(Position[] path, boolean reversed) throws InterruptedException {
	if (!reversed) {
		for (int i = 1; i < path.length; i++)
			if (!walkTile(path[i]))
				i--;
	} else {
		for (int i = path.length-2; i > 0; i--)
			if (!walkTile(path[i]))
				i++;
	}
} 
 
    @Override
    public int onLoop() throws InterruptedException 
    {
        switch (getState()) 
        {
            case LOOT:
                Entity loot = objects.closest("Bone");
                if(!inventory.isFull())
                {
                    loot.interact("Take");
                    sleep(random(100, 1000));
                    break;
                }
            case WALK_TO_BANK:
                traversePath(path, false);
                sleep(random(1500, 2500));
                break;
                
            case WALK_TO_WILDERNESS:
                traversePath(path, true);
                sleep(random(1500, 2500));
                break;
                
            case BANK:
            RS2Object bankBooth = objects.closest("Bank booth");
    if (bankBooth != null) {
        if (bankBooth.interact("Bank")) {
            while (!bank.isOpen())
                sleep(250);
            bank.depositAll();
            break;
        }
    }
        }
        
        return random(200, 1000);
    }
    
    @Override
    public void onExit() {
        //here i can log how many goblins i have killed, how long i ran script for, logging purposes
        log("Tack för att du testade WildyLoot!");
    }
 
    @Override
    public void onPaint(Graphics2D g) {
            //for displaying GFX/PAINT DISPLAY INFO ON SCREEN
    }
 
}
