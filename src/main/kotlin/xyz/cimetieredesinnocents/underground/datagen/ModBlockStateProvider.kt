package xyz.cimetieredesinnocents.underground.datagen

import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import xyz.cimetieredesinnocents.cdilib.loaders.DataGenLoaderFactory
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.loaders.BlockLoader

class ModBlockStateProvider(context: DataGenLoaderFactory.Context) :
    BlockStateProvider(context.output, Underground.ID, context.efh) {
    override fun registerStatesAndModels() {
        simpleBlock(BlockLoader.GROUND_EXPLOSION)
    }
}