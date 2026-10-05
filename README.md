# Strata Basic

The layered world of Strata, and nothing else (NeoForge 1.21.1). Built from Strata by `tools/make_strata_basic.py` in the Strata project.

- **Layers** (top to bottom): stone (above Y 0), deepslate (0 to -100), lithoslate (-100 to -200), mantleslate (-200 to -300), ferrite
  (-300 to -400), and the Nether below -400: the Nether's terrain and biomes, in the bottom 128 blocks of the world (Y -528 to -400). The world is
  Y -528 to 256.
- **Stones:** lithoslate, mantleslate and ferrite are deepslate in other colours, with a variant of every ore and everything deepslate can be
  made into (cobbled, polished, bricks, cracked bricks, tiles, cracked tiles, chiseled, and their slabs, stairs and walls). Mining a stone
  drops its cobbled block.
- **Ores:** coal, iron, copper, gold, redstone, lapis, diamond and emerald generate in every layer, in the layer's own stone.
- **Creepers:** Deeper (deepslate), Lithoslate, Mantleslate and Void (ferrite), each with a bigger blast; each only spawns naturally in its own
  layer (an ordinary creeper only in the stone layer).
- **The Nether:** vanilla Nether fortresses and bastion remnants generate in it, and the Nether's own features (quartz, gold, debris...) at its height.

Everything else is vanilla.
