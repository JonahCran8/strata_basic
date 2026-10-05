package com.jonah.stratabasic.registry;

import com.jonah.stratabasic.StrataBasic;
import com.jonah.stratabasic.entity.DeeperCreeper;
import com.jonah.stratabasic.entity.LithoslateCreeper;
import com.jonah.stratabasic.entity.MantleslateCreeper;
import com.jonah.stratabasic.entity.VoidCreeper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

// Attributes and spawn rules for creepers, each only spawns naturally in its own layer
@EventBusSubscriber(modid = StrataBasic.MODID)
public class ModEntityEvents {

    private static final int LITHOSLATE_TOP = -100;
    private static final int DEEPSLATE_TOP = 0;
    private static final int MANTLESLATE_TOP = -200;
    private static final int FERRITE_TOP = -300;
    private static final int NETHER_TOP = -400;
    // Void creeper doesn't spawn naturally with another this close
    private static final double VOID_CREEPER_SPACING = 128.0;

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DEEPER_CREEPER.get(), DeeperCreeper.createAttributes().build());
        event.put(ModEntities.LITHOSLATE_CREEPER.get(), LithoslateCreeper.createAttributes().build());
        event.put(ModEntities.MANTLESLATE_CREEPER.get(), MantleslateCreeper.createAttributes().build());
        event.put(ModEntities.VOID_CREEPER.get(), VoidCreeper.createAttributes().build());
    }

    // Whether creeper belongs in layer at that height
    private static boolean fitsLayer(EntityType<?> type, int y) {
        if (type == ModEntities.VOID_CREEPER.get()) {
            return y >= NETHER_TOP && y < FERRITE_TOP;
        }
        if (type == ModEntities.MANTLESLATE_CREEPER.get()) {
            return y >= FERRITE_TOP && y < MANTLESLATE_TOP;
        }
        if (type == ModEntities.LITHOSLATE_CREEPER.get()) {
            return y >= MANTLESLATE_TOP && y < LITHOSLATE_TOP;
        }
        if (type == ModEntities.DEEPER_CREEPER.get()) {
            return y >= LITHOSLATE_TOP && y < DEEPSLATE_TOP;
        }
        return y >= DEEPSLATE_TOP;
    }

    // Natural spawning only, spawners and eggs ignore layers
    private static boolean naturalSpawnFits(EntityType<?> type, MobSpawnType spawnType, int y) {
        return (spawnType != MobSpawnType.NATURAL && spawnType != MobSpawnType.CHUNK_GENERATION) || fitsLayer(type, y);
    }

    @SubscribeEvent
    public static void registerSpawns(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.DEEPER_CREEPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) -> naturalSpawnFits(type, spawnType, pos.getY())
                        && Monster.checkMonsterSpawnRules(type, level, spawnType, pos, random),
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.LITHOSLATE_CREEPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) -> naturalSpawnFits(type, spawnType, pos.getY())
                        && Monster.checkMonsterSpawnRules(type, level, spawnType, pos, random),
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.MANTLESLATE_CREEPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) -> naturalSpawnFits(type, spawnType, pos.getY())
                        && Monster.checkMonsterSpawnRules(type, level, spawnType, pos, random),
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // Never while another is near
        event.register(ModEntities.VOID_CREEPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) -> naturalSpawnFits(type, spawnType, pos.getY())
                        && spawnType != MobSpawnType.CHUNK_GENERATION      // (a chunk being made can't see its neighbours' creepers)
                        && (spawnType != MobSpawnType.NATURAL || level.getEntitiesOfClass(VoidCreeper.class, new AABB(pos).inflate(VOID_CREEPER_SPACING)).isEmpty())
                        && Monster.checkMonsterSpawnRules(type, level, spawnType, pos, random),
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // Normal creeper keeps its rules and is limited to stone layer
        SpawnPlacements.SpawnPredicate<Creeper> stoneOnly = (type, level, spawnType, pos, random) -> naturalSpawnFits(type, spawnType, pos.getY());
        event.register(EntityType.CREEPER, stoneOnly, RegisterSpawnPlacementsEvent.Operation.AND);
    }
}
