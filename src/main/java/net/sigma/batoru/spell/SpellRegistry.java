package net.sigma.batoru.spell;

import net.minecraft.resources.Identifier;

import java.util.*;

public class SpellRegistry {

    private static final Map<Identifier, Spell> REGISTRY = new HashMap<>();

    private SpellRegistry() {}

    public static void register(Spell spell) {
        Identifier id = spell.id();
        REGISTRY.put(id, spell);
    }

    public static Optional<Spell> get(Identifier id) {
        return Optional.ofNullable(REGISTRY.get(id));
    }

    public static boolean contains(Identifier id) {
        return REGISTRY.containsKey(id);
    }

    public static Collection<Spell> getAll() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

}
