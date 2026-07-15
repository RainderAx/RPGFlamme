package Entity;

import java.util.Scanner;

public class Bob extends Entity {
    private boolean isTaunting = false;
    private Scanner scanner = new Scanner(System.in);
    protected int ultiTicks = 5;
    protected boolean isUltimateReady = false;
        
    public Bob(String name, int hp, int attackPoints, int defPoints) {
        super(name, hp, attackPoints, defPoints);
    }
    
    @Override
    public void performTurn(Entity target) {

        this.isTaunting = false; 

        System.out.println("\n--- Tour de " + getName() + " (PV: " + this.getHp() + ") ---");
        System.out.println("1. Coup du Marteau");
        System.out.println("2. Je suis pas ton pote ! (Provocation)");
        
        System.out.print("Votre choix : ");
        
        if (!scanner.hasNextInt()) {
            System.out.println("Entrée invalide !");
            scanner.next(); 
            return;
        }

        int choix = scanner.nextInt();
        if (choix == 2) {
            System.out.println(getName() + " hurle : 'Tapez-moi si vous l'osez !'");
            this.isTaunting = true;
        } else {
            this.coupDuMarteau(target);
        }
    }

    public boolean isTaunting() { 
        return isTaunting; 
    }
    
    public void coupDuMarteau(Entity target) {
        int damage = 50 + target.getDefPoints();
        System.out.println(getName() + " frappe lourdement " + target.getName() + " !");
        target.takeDamage(damage);
        this.isTaunting = false;
    }
    
    public void activerProvocation() {
        System.out.println(getName() + " hurle : 'Tapez-moi si vous l'osez !'");
        this.isTaunting = true; 
    }
    
    @Override
    public void takeDamage(int amount) {
  
        if (this.isTaunting) {

            int finalDamage = (amount / 2) - this.getDefPoints();

            if (finalDamage < 0) finalDamage = 0;
            
            this.setHp(this.getHp() - finalDamage);
            System.out.println(this.getName() + " encaisse ! Perd " + finalDamage + " PV. (Reste : " + this.getHp() + ")");
        } else {
            super.takeDamage(amount);
        }
        
    }
    
    public void décompteUlti() {
    	if (ultiTicks > 0) {
    		ultiTicks--;
    	}else {
    		System.out.println("Ultimate Disponible");
    		isUltimateReady = true;
    	}
    }
    
    public void ultim (Entity target) {
    	if (isUltimateReady == true) {
    		ultiTicks =  5;
    		isUltimateReady = false;
    	}
    }
    
    public void perceuse(Entity target) {
    	this.setDefPoints(getDefPoints() + 10);
    	int damage = 80 +target.getDefPoints() + this.getDefPoints();
    	
    	target.takeDamage(damage);
    }
    
    public void superProvoc() {
    	int boost = this.getDefPoints() / 4;
        System.out.println(getName() + " hurle : 'Tapez-moi si vous l'osez !'");
        this.setDefPoints(getDefPoints() + boost);
        this.isTaunting = true; 
    }
    
}