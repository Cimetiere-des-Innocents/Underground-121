package xyz.cimetieredesinnocents.underground.loaders

import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.block.GroundExplosionBlock

object BlockLoader {
    class BlockAndItsItem<T : Block>(
        blockHolder: DeferredHolder<Block, T>,
        itemHolder: DeferredHolder<Item, BlockItem>
    ) {
        val block by blockHolder
        val item by itemHolder
    }

    val REGISTRY = DeferredRegister.create(Registries.BLOCK, Underground.ID)
    val BLOCKS_WITH_LOOT = arrayListOf<DeferredHolder<Block, out Block>>()

    private fun <T : Block> register(name: String, block: () -> T): BlockAndItsItem<T> {
        return register(name, 0, block)
    }

    private fun <T : Block> register(name: String, priority: Int, block: () -> T): BlockAndItsItem<T> {
        val registeredBlock = blockOnly(name, block)
        BLOCKS_WITH_LOOT.add(registeredBlock)
        val registeredItem = ItemLoader.register(name, priority) { BlockItem(registeredBlock.get(), Item.Properties()) }
        return BlockAndItsItem(registeredBlock, registeredItem)
    }

    private fun <T : Block> blockOnly(name: String, block: () -> T): DeferredHolder<Block, T> {
        val registeredBlock = REGISTRY.register(name, block)
        BLOCKS_WITH_LOOT.add(registeredBlock)
        return registeredBlock
    }

    val GROUND_EXPLOSION by blockOnly("ground_explosion", ::GroundExplosionBlock)
}