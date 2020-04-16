package gg.strix.strixgravestones.events;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.carrier.Chest;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class PlayerDeathEventHandling {

    @Listener
    public void onPlayerDeath(DestructEntityEvent.Death event){

        Living target = event.getTargetEntity();
        //System.out.println("I've gotten the Death event!");
        if(target instanceof Player){

            Player player = (Player)target;


            int lvl = player.get(Keys.TOTAL_EXPERIENCE).get();
            int bottles = lvl / 5; // About 5 is inbetween 3-11.

            Inventory inv = player.getInventory();

            Location<World> location = player.getLocation();
            Location<World> targetLoc = location;

            BlockState chestBlock = targetLoc.getBlock();
            targetLoc.setBlockType(BlockTypes.CHEST);


            Location<World> targetChest = location.getRelative(Direction.UP);
            Chest chest = (Chest)targetChest.getTileEntity().get();

            Location<World> targetSign = targetChest.getRelative(Direction.UP);
            targetSign.setBlockType(BlockTypes.STANDING_SIGN);

            Sign sign = (Sign)targetSign.getTileEntity().get();

            SignData data = sign.getOrCreate(SignData.class).get();

            data.set(data.lines().set(0, Text.of("RIP")));
            data.set(data.lines().set(1, Text.of("Here lies")));
            data.set(data.lines().set(2, Text.of(player.getDisplayNameData().displayName().get())));

            sign.offer(data);

            int count = 0; // Add one to allow for XP
            if(bottles > 0){
                count = (int)Math.ceil(bottles / 64);
            }

            for (Inventory slot : inv.slots()) {
                if(slot.peek().isPresent()){
                    ItemStack it = slot.peek().get();
                    if(it.getType() != ItemTypes.AIR){
                        count = count + 1;
                    }

                }
            }

            Inventory targetInv = null;
            targetInv = chest.getInventory();
            if(count > 27){
                targetLoc.getRelative(Direction.EAST).setBlockType(BlockTypes.CHEST);

                targetInv = chest.getDoubleChestInventory().get();
            }
            final Inventory targ = targetInv;
            inv.slots().forEach( slot ->{
                slot.peek().ifPresent( it ->{
                    targ.offer(it);
                    slot.set(ItemStack.builder().itemType(ItemTypes.AIR).build());
                });

            } );
            for (int i =0; i < bottles; i++){
                targ.offer(ItemStack.builder().itemType(ItemTypes.EXPERIENCE_BOTTLE).build());
            }
            player.offer(Keys.TOTAL_EXPERIENCE, 0);
        }


    }

}
