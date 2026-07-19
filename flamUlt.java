    @Override
    public void ultim (Entity target, List<Entity> allTargets) {
    	if (isUltimateReady == true) {
        	int planteFleche = this.getAttackPoints()*2;
        	target.setArrow(true);
        	target.takeDamage(planteFleche);
        	ultiTicks = 5;
        	isUltimateReady = false;
    	}

    }
    
    public void checkUlti(Entity target) {
        // Vérifie si le lanceur est en vie et si la cible a une flèche
        if (this.isAlive() && target.getArrow()) {
            
            //verifie si la cible a moins de 15% de sa vie max
            if (target.getHp() < 15 * target.getMaxHp() / 100) {
                System.out.println("OVERKILL !");
                target.takeDamage(target.getHp() + target.getDefPoints()); 
            } else {
               
                System.out.println("La flèche brûle" + getName());
                target.setBurn(target.getBurnTicks() + 1);
            }
            
            target.setArrow(false); 
        }