<template>
  <component :is="templateComponent" :data="data" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import TplClassic from './TplClassic.vue'
import TplProfessional from './TplProfessional.vue'
import TplCreative from './TplCreative.vue'
import TplTech from './TplTech.vue'
import TplMinimal from './TplMinimal.vue'

const props = defineProps<{
  templateId: number | null
  data: Record<string, any>
}>()

const templateMap: Record<number, any> = {
  1: TplClassic,
  2: TplProfessional,
  3: TplCreative,
  4: TplTech,
  5: TplMinimal,
}

const templateComponent = computed(() => {
  return templateMap[props.templateId || 1] || TplClassic
})
</script>
