package xyz.cimetieredesinnocents.underground.datagen

import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.loaders.BlockLoader

class ModBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, Underground.ID, exFileHelper) {
    override fun registerStatesAndModels() {
        simpleBlock(BlockLoader.GROUND_EXPLOSION)
    }
}