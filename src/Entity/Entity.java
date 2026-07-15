package Entity;

import java.util.Random;

public abstract class Entity {
    private String name;
    private int hp;
    private int attackPoints;
    private int defPoints;
    protected int burnTicks = 0;
    protected static final Random RAND = new Random();
    protected boolean arrow = false;

    
    public Entity(String name, int hp, int attackPoints, int defPoints) {
        this.name = name;
        this.hp = hp;
        this.attackPoints = attackPoints;
        this.defPoints = defPoints;
    }
    
    public abstract void performTurn(Entity target);
    
    public void applyPostTurnEffects() {
    	if (burnTicks > 0) {
    		int burnDamage = (this.hp * (5+burnTicks))/100;
    		this.hp -= burnDamage;
    		burnTicks--;
    		System.out.println(this.name + " brûle et perd " + burnDamage + " PV !");
    		
    	}
    }
    
    public void setBurn(int ticks) { this.burnTicks = ticks; }
    
    public int attack(Entity target) {
    	Random rand = new Random();
    	
    	int crit = rand.nextInt(6) +1;
    	int degatsBruts = this.attackPoints * crit;
    	   	
        target.takeDamage(degatsBruts) ;

        return degatsBruts;

        
    }
    
    public void takeDamage(int amount) {
    	Random rand = new Random();
    	int finalDamage;
 
    	if (rand.nextBoolean()) {
    		System.out.println(this.name + " se met en garde !");
    		int superDefense = this.defPoints + ((this.attackPoints / 4)+this.defPoints/4);
    		finalDamage = amount - superDefense;
    	} else {
    		 // Défense normale
    		finalDamage = amount - this.defPoints;
    	}
    	
    	if (finalDamage < 0) {
    		finalDamage = 0;
    	}
        this.hp -= finalDamage;
        
        if (this.hp < 0) this.hp = 0;
        System.out.println(this.name + " perd " + finalDamage + " PV. (Reste : " + this.hp + ")");
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    // Getters et Setters
    public String getName() { return name; }
    
    public int getHp() { 
        return hp; 
    }

    public void setHp(int hp) { 
        this.hp = hp; 
        // Sécurité pour ne pas descendre sous 0
        if (this.hp < 0) this.hp = 0; 
    }

    public int getAttackPoints() { 
        return attackPoints; 
    }

    public void setAttackPoints(int attackPoints) { 
        this.attackPoints = attackPoints; 
    }

    public int getDefPoints() { 
        return defPoints; 
    }
    
    public void setDefPoints(int defPoints) { 
        this.defPoints = defPoints; 
    }

    public int getBurnTicks() { 
        return burnTicks; 
    }
    
    public boolean getArrow() {
    	return arrow;
    }
    
    public void setArrow(boolean state) {
    	this.arrow = state;
    }
}