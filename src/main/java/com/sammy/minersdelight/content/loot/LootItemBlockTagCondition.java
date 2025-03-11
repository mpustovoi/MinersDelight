package com.sammy.minersdelight.content.loot;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import com.sammy.minersdelight.setup.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.*;

import java.util.*;

public final class LootItemBlockTagCondition implements LootItemCondition {
   public static final MapCodec<LootItemBlockTagCondition> CODEC = RecordCodecBuilder.mapCodec(
           obj -> obj.group(
                           ResourceLocation.CODEC.fieldOf("tag").forGetter(LootItemBlockTagCondition::tag)
                   )
                   .apply(obj, LootItemBlockTagCondition::new)
   );
   private final TagKey<Block> tag;


   public LootItemBlockTagCondition(ResourceLocation tag) {
      this.tag = TagKey.create(BuiltInRegistries.BLOCK.key(), tag);
   }

   @Override
   public LootItemConditionType getType() {
      return MDLootConditions.BLOCK_TAG_CONDITION.get();
   }

   @Override
   public Set<LootContextParam<?>> getReferencedContextParams() {
      return Set.of(LootContextParams.BLOCK_STATE);
   }

   public boolean test(LootContext context) {
      BlockState blockstate = context.getParamOrNull(LootContextParams.BLOCK_STATE);
      return blockstate != null && blockstate.is(tag);
   }

   public static Builder hasBlockStateProperties(TagKey<Block> tag) {
      return new Builder(tag);
   }

   public ResourceLocation tag() {
      return tag.location();
   }

   public static class Builder implements LootItemCondition.Builder {
      private final TagKey<Block> tag;

      public Builder(TagKey<Block> tag) {
         this.tag = tag;
      }

      @Override
      public LootItemCondition build() {
         return new LootItemBlockTagCondition(tag.location());
      }
   }
}