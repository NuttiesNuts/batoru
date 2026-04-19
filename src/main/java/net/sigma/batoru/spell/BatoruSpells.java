package net.sigma.batoru.spell;

import net.sigma.batoru.spell.custom.ProjectileSpell;

public class BatoruSpells {
    public static void initialize(){
        SpellRegistry.register(new ProjectileSpell());
    }
}
