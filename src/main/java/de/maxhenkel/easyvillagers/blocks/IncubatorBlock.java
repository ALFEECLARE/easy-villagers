package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.block.IItemBlock;
import de.maxhenkel.corelib.blockentity.SimpleBlockEntityTicker;
import de.maxhenkel.corelib.client.CustomRendererBlockItem;
import de.maxhenkel.corelib.client.ItemRenderer;
import de.maxhenkel.easyvillagers.ItemTileEntityCache;
import de.maxhenkel.easyvillagers.blocks.tileentity.IncubatorTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.IncubatorContainer;
import de.maxhenkel.easyvillagers.items.render.IncubatorItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class IncubatorBlock extends VillagerBlockBase implements EntityBlock, IItemBlock {

    public IncubatorBlock() {
        super(Properties.of().mapColor(MapColor.METAL).strength(2.5F).sound(SoundType.METAL).noOcclusion());
    }

    @Override
    public Item toItem() {
        return new CustomRendererBlockItem(this, new Item.Properties()) {
            @OnlyIn(Dist.CLIENT)
            @Override
            public ItemRenderer createItemRenderer() {
                return new IncubatorItemRenderer();
            }
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter blockGetter, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, blockGetter, components, tooltipFlag);
        IncubatorTileentity trader = ItemTileEntityCache.getTileEntity(stack, () -> new IncubatorTileentity(BlockPos.ZERO, ModBlocks.TRADER.get().defaultBlockState()));
        EasyVillagerEntity villager = trader.getVillagerEntity();
        if (villager != null) {
            components.add(villager.getAdvancedName());
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (!(tileEntity instanceof IncubatorTileentity)) {
            return super.use(state, worldIn, pos, player, handIn, hit);
        }
        IncubatorTileentity incubator = (IncubatorTileentity) tileEntity;

        player.openMenu(new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable(state.getBlock().getDescriptionId());
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                return new IncubatorContainer(id, playerInventory, incubator.getInputInventory(), incubator.getOutputInventory());
            }
        });

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level1, BlockState state, BlockEntityType<T> type) {
        return new SimpleBlockEntityTicker<>();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new IncubatorTileentity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1F;
    }

}
